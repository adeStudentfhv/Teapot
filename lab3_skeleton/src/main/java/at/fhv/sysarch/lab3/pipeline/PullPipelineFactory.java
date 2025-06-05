package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.Filters.Pull_Fil.*;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Pull_Int.PullFilter;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import javafx.animation.AnimationTimer;

public class PullPipelineFactory {
    public static AnimationTimer createPipeline(PipelineData pd) {
        var model = pd.getModel();
        var gc = pd.getGraphicsContext();

        // 0. Source
        ModelSourceFilter source = new ModelSourceFilter();
        source.setModel(model);

        // 1. Model-View-Transformation
        ModelViewTransformationPullFilter mvTransform = new ModelViewTransformationPullFilter(pd, 0f);
        mvTransform.setSource(source);

        // 2. Backface Culling
        BackfaceCullingPullFilter culling = new BackfaceCullingPullFilter();
        culling.setSource(mvTransform);


        // 4. Coloring
        ColoringPullFilter coloring = new ColoringPullFilter(pd);
        coloring.setSource(culling);


        // 4a. Lighting optional
        FlatShadingPullFilter lighting = null;
        if (pd.isPerformLighting()) {
            lighting = new FlatShadingPullFilter(pd);
            lighting.setSource(coloring);
        }

        // 5. Projection
        ProjectionTransformPullFilter projection = new ProjectionTransformPullFilter(pd);
        projection.setSource(pd.isPerformLighting() ? lighting : coloring);

        // 6. Screen Space
        ScreenSpaceTransformPullFilter screen = new ScreenSpaceTransformPullFilter(pd);
        screen.setSource(projection);

        // 7. Renderer
        PullFilter<?> finalFilter;
        switch (pd.getRenderingMode()) {
            case POINT -> {
                PointRenderPullFilter renderer = new PointRenderPullFilter(gc);
                renderer.setSource(screen);
                finalFilter = renderer;
            }
            case WIREFRAME -> {
                WireframeRenderPullFilter renderer = new WireframeRenderPullFilter(gc);
                renderer.setSource(screen);
                finalFilter = renderer;
            }
            case FILLED -> {
                if (pd.isPerformLighting()) {
                    ShadedRenderPullFilter renderer = new ShadedRenderPullFilter(gc);
                    renderer.setSource(screen);
                    finalFilter = renderer;
                } else {
                    FilledRenderPullFilter renderer = new FilledRenderPullFilter(gc);
                    renderer.setSource(screen);
                    finalFilter = renderer;
                }
            }
            default -> throw new IllegalStateException("Unsupported rendering mode: " + pd.getRenderingMode());
        }

        // AnimationRenderer mit Rotation und Render-Trigger
        return new AnimationRenderer(pd) {

            private float angle = 0f;

            @Override
            protected void render(float fraction, Model model) {
                gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
                angle += fraction * Math.PI * 2;

                Mat4 rotation = Matrices.rotate(angle, pd.getModelRotAxis());
                Mat4 modelView = pd.getViewTransform().multiply(rotation.multiply(pd.getModelTranslation()));
                mvTransform.setModelViewMatrix(modelView);

                System.out.println(">>> RENDER startet");

                source.setModel(model);
                System.out.println("Model gesetzt mit " + model.getFaces().size() + " Faces");

                while (finalFilter.pull() != null) {
                    // Rendering passiert im letzten Filter
                }
            }


        };
    }
}
