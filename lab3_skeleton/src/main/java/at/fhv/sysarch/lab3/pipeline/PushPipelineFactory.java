package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.Filters.Push_Fil.*;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Push_Int.PushFilter;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import javafx.animation.AnimationTimer;

public class PushPipelineFactory {

    public static AnimationTimer createPipeline(PipelineData pd) {
        var model = pd.getModel();
        var gc = pd.getGraphicsContext();

        // 1. Model-View Transformation
        ModelViewTransformPushFilter mvFilter = new ModelViewTransformPushFilter(pd, 0f);

        // 2. Backface Culling
        BackfaceCullingPushFilter cullingFilter = new BackfaceCullingPushFilter();
        mvFilter.connectTo(cullingFilter);

        // 3. Depth Sorting (jetzt VOR Coloring!)
        DepthSortingPushFilter sortingFilter = new DepthSortingPushFilter(pd.getViewingEye());
        cullingFilter.connectTo(sortingFilter);

        // 4. Coloring (Face → ColoredFace)
        ColoringPushFilter coloringFilter = new ColoringPushFilter(pd);
        sortingFilter.connectTo(coloringFilter);

        // 5. Optional Lighting (FlatShading im View Space)
        PushFilter<?> current;
        if (pd.isPerformLighting()) {
            FlatShadingPushFilter lightingFilter = new FlatShadingPushFilter(pd);
            coloringFilter.connectTo(lightingFilter);
            current = lightingFilter;
        } else {
            current = coloringFilter;
        }

        // 6. Projection
        ProjectionTransformPushFilter projFilter = new ProjectionTransformPushFilter(pd);
        current.connectTo(projFilter);

        // 7. Screen Space Transformation
        ScreenSpaceTransformPushFilter screenFilter = new ScreenSpaceTransformPushFilter(pd);
        projFilter.connectTo(screenFilter);

        // 8. Rendering (Sink)
        switch (pd.getRenderingMode()) {
            case POINT -> {
                PointRenderPushFilter renderer = new PointRenderPushFilter(gc);
                screenFilter.connectTo(renderer);
            }
            case WIREFRAME -> {
                WireframeRenderPushFilter renderer = new WireframeRenderPushFilter(gc);
                screenFilter.connectTo(renderer);
            }
            case FILLED -> {
                if (pd.isPerformLighting()) {
                    screenFilter.connectTo(new ShadedRenderPushFilter(gc));
                } else {
                    screenFilter.connectTo(new FilledRenderPushFilter(gc));
                }
            }
        }

        // 9. Rückgabe des AnimationTimer mit Rotation und Flush
        return new AnimationRenderer(pd) {
            private float angle = 0f;

            @Override
            protected void render(float fraction, Model model) {
                gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

                angle += (fraction * Math.PI * 2) / 10;
                Mat4 rotation = Matrices.rotate(angle, pd.getModelRotAxis());
                Mat4 modelView = pd.getViewTransform().multiply(rotation.multiply(pd.getModelTranslation()));
                mvFilter.setTransform(modelView);

                for (Face face : model.getFaces()) {
                    mvFilter.push(face);
                }

                sortingFilter.flush(); // Wichtig: Sortierung ausführen!
            }
        };
    }

}