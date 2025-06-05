package at.fhv.sysarch.lab3.pipeline.Filters.Pull_Fil;

import at.fhv.sysarch.lab3.obj.ColoredFace;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Pull_Int.AbstractPullFilter;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import com.hackoeur.jglm.Vec2;

public class PointRenderPullFilter extends AbstractPullFilter<ColoredFace, ColoredFace> {
    private final GraphicsContext gc;

    public PointRenderPullFilter(PipelineData pipelineData) {
        this.gc = pipelineData.getGraphicsContext();
    }

    @Override
    public ColoredFace pull() {
        ColoredFace coloredFace = source.pull();
        if (coloredFace == null) return null;

        gc.setStroke(coloredFace.getColor());

        Vec2 v1 = coloredFace.getFace().getV1().toScreen();
        Vec2 v2 = coloredFace.getFace().getV2().toScreen();
        Vec2 v3 = coloredFace.getFace().getV3().toScreen();

        drawPoint(v1);
        drawPoint(v2);
        drawPoint(v3);

        return coloredFace;
    }

    private void drawPoint(Vec2 v) {
        gc.strokeLine(v.getX(), v.getY(), v.getX(), v.getY());
    }
}
