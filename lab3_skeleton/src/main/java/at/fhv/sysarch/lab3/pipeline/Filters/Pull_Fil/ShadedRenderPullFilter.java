package at.fhv.sysarch.lab3.pipeline.Filters.Pull_Fil;

import at.fhv.sysarch.lab3.obj.ColoredFace;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Pull_Int.AbstractPullFilter;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ShadedRenderPullFilter extends AbstractPullFilter<ColoredFace, Void> {
    private final GraphicsContext graphicsContext;

    public ShadedRenderPullFilter(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }
    @Override
    public Void pull() {
        ColoredFace coloredFace = source.pull();
        if (coloredFace == null) return null;

        var face = coloredFace.getFace();
        Color color = coloredFace.getColor();

        graphicsContext.setFill(color);

        double[] xPoints = new double[] {
                face.getV1().getX(),
                face.getV2().getX(),
                face.getV3().getX()
        };
        double[] yPoints = new double[] {
                face.getV1().getY(),
                face.getV2().getY(),
                face.getV3().getY()
        };

        graphicsContext.fillPolygon(xPoints, yPoints, 3);

        return null;
    }
}
