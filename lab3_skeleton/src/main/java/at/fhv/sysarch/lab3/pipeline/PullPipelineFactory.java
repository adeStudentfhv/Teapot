package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.Filters.Pull_Fil.*;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Pull_Int.PullFilter;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

public class PullPipelineFactory {

    public static AnimationTimer createPipeline(PipelineData pd) {
        GraphicsContext gc = pd.getGraphicsContext();
        Model model = pd.getModel();

        ModelSourceFilter source = new ModelSourceFilter();
        source.setModel(model);

        ModelViewTransformPullFilter mvTransform = new ModelViewTransformPullFilter(pd, 0f);
        mvTransform.setSource(source);

        BackfaceCullingPullFilter culling = new BackfaceCullingPullFilter();
        culling.setSource(mvTransform);

        PullFilter<?> colorStage;
        if (pd.isPerformLighting()) {
            FlatShadingPullFilter shading = new FlatShadingPullFilter(pd);
            shading.setSource(culling);
            colorStage = shading;
        } else {
            ColoringPullFilter coloring = new ColoringPullFilter(pd);
            coloring.setSource(culling);
            colorStage = coloring;
        }

        ProjectionTransformPullFilter projection = new ProjectionTransformPullFilter(pd);
        projection.setSource(colorStage);

        ScreenSpaceTransformPullFilter screen = new ScreenSpaceTransformPullFilter(pd);
        screen.setSource(projection);

        PullFilter<?> renderStage;
        switch (pd.getRenderingMode()) {
            case POINT -> {
                PointRenderPullFilter renderer = new PointRenderPullFilter(pd);
                renderer.setSource(screen);
                renderStage = renderer;
            }
            case WIREFRAME -> {
                WireframeRenderPullFilter renderer = new WireframeRenderPullFilter(pd);
                renderer.setSource(screen);
                renderStage = renderer;
            }
            case FILLED -> {
                if (pd.isPerformLighting()) {
                    ShadedRenderPullFilter renderer = new ShadedRenderPullFilter(pd);
                    renderer.setSource(screen);
                    renderStage = renderer;
                } else {
                    FilledRenderPullFilter renderer = new FilledRenderPullFilter(pd);
                    renderer.setSource(screen);
                    renderStage = renderer;
                }
            }
            default -> throw new IllegalStateException("Unsupported rendering mode: " + pd.getRenderingMode());
        }

        SinkPullFilter sink = new SinkPullFilter();
        sink.setSource(renderStage);

        return new AnimationRenderer(pd) {
            private float angle = 0f;

            @Override
            protected void render(float fraction, Model model) {
                gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
                angle += (fraction * Math.PI * 2) / 10;

                Mat4 rotation = Matrices.rotate(angle, pd.getModelRotAxis());
                Mat4 modelMatrix = rotation.multiply(pd.getModelTranslation());
                Mat4 modelView = pd.getViewTransform().multiply(modelMatrix);
                mvTransform.setModelViewMatrix(modelView);

                source.setModel(model);

                sink.pull();
            }
        };
    }
}
