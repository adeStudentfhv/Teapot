package at.fhv.sysarch.lab3.pipeline.Filters.Pull_Fil;

import at.fhv.sysarch.lab3.obj.ColoredFace;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Pull_Int.AbstractPullFilter;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import com.hackoeur.jglm.Vec2;
import javafx.scene.canvas.GraphicsContext;

public class ShadedRenderPullFilter extends AbstractPullFilter<ColoredFace, ColoredFace> {
    private final GraphicsContext gc;

    public ShadedRenderPullFilter(PipelineData pipelineData) {
        this.gc = pipelineData.getGraphicsContext();
    }

    @Override
    public ColoredFace pull() {
        ColoredFace coloredFace = source.pull();
        if (coloredFace == null) return null;

        gc.setFill(coloredFace.getColor());

        Vec2 v1 = coloredFace.getFace().getV1().toScreen();
        Vec2 v2 = coloredFace.getFace().getV2().toScreen();
        Vec2 v3 = coloredFace.getFace().getV3().toScreen();

        gc.fillPolygon(
                new double[]{v1.getX(), v2.getX(), v3.getX()},
                new double[]{v1.getY(), v2.getY(), v3.getY()},
                3
        );

        return coloredFace;
    }
}
