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
        // TODO: push from the source (model)
        var model = pd.getModel();
        var gc = pd.getGraphicsContext();

        ModelViewTransformPushFilter mvFilter = new ModelViewTransformPushFilter(pd, 0f);

        // TODO 1. perform model-view transformation from model to VIEW SPACE coordinates
        PushFilter<?> current = mvFilter;

        // TODO 2. perform backface culling in VIEW SPACE
        BackfaceCullingPushFilter cullingFilter = new BackfaceCullingPushFilter();
        mvFilter.connectTo(cullingFilter);
        current = cullingFilter;

        // TODO 3. perform depth sorting in VIEW SPACE
        DepthSortingFilter depthFilter = new DepthSortingFilter();
        cullingFilter.connectTo(depthFilter);
        current = depthFilter;


        // TODO 4. add coloring (space unimportant)
        ColoringPushFilter coloringPushFilter = new ColoringPushFilter(pd);
        current.connectTo(coloringPushFilter);
        current = coloringPushFilter;

        // lighting can be switched on/off
        if (pd.isPerformLighting()) {
            // 4a. TODO perform lighting in VIEW SPACE
            FlatShadingPushFilter shadingFilter = new FlatShadingPushFilter(pd);
            coloringPushFilter.connectTo(shadingFilter);
            current = shadingFilter;
            
            // 5. TODO perform projection transformation on VIEW SPACE coordinates
            ProjectionTransformPushFilter projFilter = new ProjectionTransformPushFilter(pd);
            current.connectTo(projFilter);
            current = projFilter;
        } else {
            // 5. TODO perform projection transformation
            ProjectionTransformPushFilter projFilter = new ProjectionTransformPushFilter(pd);
            coloringPushFilter.connectTo(projFilter);
            current = projFilter;
        }

        // TODO 6. perform perspective division to screen coordinates
        ScreenSpaceTransformPushFilter screenFilter = new ScreenSpaceTransformPushFilter(pd);
        current.connectTo(screenFilter);
        current = screenFilter;

        // TODO 7. feed into the sink (renderer)
        switch (pd.getRenderingMode()) {
            case POINT:
                PointRenderPushFilter pointRender = new PointRenderPushFilter(gc);
                current.connectTo(pointRender);
                break;
            case WIREFRAME:
                WireframeRenderPushFilter wireframeRender = new WireframeRenderPushFilter(gc);
                current.connectTo(wireframeRender);
                break;
            case FILLED:
                if (pd.isPerformLighting()) {
                    current.connectTo(new ShadedRenderPushFilter(gc));
                } else {
                    current.connectTo(new FilledRenderPushFilter(gc));
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

                depthFilter.flush(); // sortiert + rendert danach

            }
        };
    }
}