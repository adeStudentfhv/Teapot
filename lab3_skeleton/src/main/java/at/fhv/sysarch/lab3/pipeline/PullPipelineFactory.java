package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.Filters.Pull_Fil.*;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Pull_Int.PullFilter;
import at.fhv.sysarch.lab3.rendering.RenderingMode;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import javafx.animation.AnimationTimer;


public class PullPipelineFactory {
    public static AnimationTimer createPipeline(PipelineData pd) {
        var model = pd.getModel();
        var gc = pd.getGraphicsContext();

        // TODO: pull from the source (model)
        ModelSourceFilter source = new ModelSourceFilter(model);


        // TODO 1. perform model-view transformation from model to VIEW SPACE coordinates
        ModelViewTransformationPullFilter mvFilter = new ModelViewTransformationPullFilter(pd, 0f);
        mvFilter.setSource(source);
        PullFilter<?> current = mvFilter;

        // TODO 2. perform backface culling in VIEW SPACE
        BackfaceCullingPullFilter cullingPullFilter = new BackfaceCullingPullFilter();
        cullingPullFilter.setSource(current);
        current = cullingPullFilter;

        // TODO 3. perform depth sorting in VIEW SPACE

        // TODO 4. add coloring (space unimportant)
        ColoringPullFilter coloringPullFilter = new ColoringPullFilter(pd);
        coloringPullFilter.setSource(current);
        current = coloringPullFilter;

        // lighting can be switched on/off
        if (pd.isPerformLighting()) {
            FlatShadingPullFilter shadingFilter = new FlatShadingPullFilter(pd);
            shadingFilter.setSource(current);
            current = shadingFilter;
        }

        ProjectionTransformPullFilter projectionFilter = new ProjectionTransformPullFilter(pd);
        projectionFilter.setSource(current);
        current = projectionFilter;

        ScreenSpaceTransformPullFilter screenFilter = new ScreenSpaceTransformPullFilter(pd);
        screenFilter.setSource(current);
        current = screenFilter;

        // TODO 6. perform perspective division to screen coordinates

        // TODO 7. feed into the sink (renderer)
        PullFilter<?> finalFilter;
        RenderingMode mode = pd.getRenderingMode();
        switch (mode) {
            case POINT -> {
                PointRenderPullFilter pointRender = new PointRenderPullFilter(gc);
                pointRender.setSource(current);
                finalFilter = pointRender;
            }
            case WIREFRAME -> {
                WireframeRenderPullFilter wireframeRender = new WireframeRenderPullFilter(gc);
                wireframeRender.setSource(current);
                finalFilter = wireframeRender;
            }
            case FILLED -> {
                if (pd.isPerformLighting()) {
                    ShadedRenderPullFilter shadedRender = new ShadedRenderPullFilter(gc);
                    shadedRender.setSource(current);
                    finalFilter = shadedRender;
                } else {
                    FilledRenderPullFilter filledRender = new FilledRenderPullFilter(gc);
                    filledRender.setSource(current);
                    finalFilter = filledRender;
                }
            }
            default -> throw new IllegalStateException("Unsupported rendering mode: " + mode);
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
                angle += fraction * Math.PI * 2;

                Mat4 rotation = Matrices.rotate(angle, pd.getModelRotAxis());
                Mat4 modelView = pd.getViewTransform().multiply(rotation.multiply(pd.getModelTranslation()));

                mvFilter.setModelViewMatrix(modelView);

                while (finalFilter.pull() != null) {
                    // rendering geschieht im Sink-Filter
                }
            }
        };
    }
}