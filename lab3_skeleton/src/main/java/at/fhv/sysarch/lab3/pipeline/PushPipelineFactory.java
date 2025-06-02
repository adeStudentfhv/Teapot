package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.Filters.Push_Fil.*;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Push_Int.PushFilter;
import at.fhv.sysarch.lab3.rendering.RenderingMode;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import javafx.animation.AnimationTimer;

import static at.fhv.sysarch.lab3.rendering.RenderingMode.*;

public class PushPipelineFactory {

    public static AnimationTimer createPipeline(PipelineData pd) {
        // TODO: push from the source (model)
        var model = pd.getModel();
        var gc = pd.getGraphicsContext();

        ModelViewTransformFilter mvFilter = new ModelViewTransformFilter(pd, 0f);

        // TODO 1. perform model-view transformation from model to VIEW SPACE coordinates
        PushFilter<?> current = mvFilter;

        // TODO 2. perform backface culling in VIEW SPACE
        BackfaceCullingFilter cullingFilter = new BackfaceCullingFilter();
        mvFilter.connectTo(cullingFilter);
        current = cullingFilter;

        // TODO 3. perform depth sorting in VIEW SPACE

        // TODO 4. add coloring (space unimportant)
        ColoringFilter coloringFilter = new ColoringFilter(pd);
        current.connectTo(coloringFilter);
        current = coloringFilter;

        // lighting can be switched on/off
        if (pd.isPerformLighting()) {
            // 4a. TODO perform lighting in VIEW SPACE
            FlatShadingFilter shadingFilter = new FlatShadingFilter(pd);
            coloringFilter.connectTo(shadingFilter);
            current = shadingFilter;
            
            // 5. TODO perform projection transformation on VIEW SPACE coordinates
            ProjectionTransformFilter projFilter = new ProjectionTransformFilter(pd);
            current.connectTo(projFilter);
            current = projFilter;
        } else {
            // 5. TODO perform projection transformation
            ProjectionTransformFilter projFilter = new ProjectionTransformFilter(pd);
            coloringFilter.connectTo(projFilter);
            current = projFilter;
        }

        // TODO 6. perform perspective division to screen coordinates
        ScreenSpaceTransformFilter screenFilter = new ScreenSpaceTransformFilter(pd);
        current.connectTo(screenFilter);
        current = screenFilter;

        // TODO 7. feed into the sink (renderer)
        switch (pd.getRenderingMode()) {
            case POINT:
                PointRenderFilter pointRender = new PointRenderFilter(gc);
                current.connectTo(pointRender);
                break;
            case WIREFRAME:
                WireframeRenderFilter wireframeRender = new WireframeRenderFilter(gc);
                current.connectTo(wireframeRender);
                break;
            case FILLED:
                if (pd.isPerformLighting()) {
                    current.connectTo(new ShadedRenderFilter(gc));
                } else {
                    current.connectTo(new FilledRenderFilter(gc));
                }
                break;
        }

        // returning an animation renderer which handles clearing of the
        // viewport and computation of the praction

        return new AnimationRenderer(pd) {
            private float angle = 0f;
            // TODO rotation variable goes in here

            /** This method is called for every frame from the JavaFX Animation
             * system (using an AnimationTimer, see AnimationRenderer). 
             * @param fraction the time which has passed since the last render call in a fraction of a second
             * @param model    the model to render 
             */
            @Override
            protected void render(float fraction, Model model) {

                // TODO compute rotation in radians
                angle += fraction * Math.PI * 2;

                // TODO create new model rotation matrix using pd.modelRotAxis
                Mat4 rotation = Matrices.rotate(angle, pd.getModelRotAxis());

                // TODO compute updated model-view tranformation
                Mat4 modelView = pd.getViewTransform().multiply(rotation.multiply(pd.getModelTranslation()));

                // TODO update model-view filter
                mvFilter.setTransform(modelView);

                // TODO trigger rendering of the pipeline
                for (Face face : model.getFaces()) {
                    mvFilter.push(face);
                }
            }
        };
    }
}