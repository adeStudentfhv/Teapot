package at.fhv.sysarch.lab3.pipeline.Filters.Push_Fil;

import at.fhv.sysarch.lab3.obj.ColoredFace;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Push_Int.AbstractPushFilter;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class WireframeRenderPushFilter extends AbstractPushFilter<ColoredFace, Void> {
    private final GraphicsContext graphicsContext;

    public WireframeRenderPushFilter(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    @Override
    public void push(ColoredFace coloredFace) {
        var face = coloredFace.getFace();
        Color color = coloredFace.getColor();

        graphicsContext.setStroke(color);

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

        graphicsContext.strokePolygon(xPoints, yPoints, 3);
    }
}