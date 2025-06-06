package at.fhv.sysarch.lab3.pipeline.Filters.Push_Fil;

import at.fhv.sysarch.lab3.obj.ColoredFace;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Push_Int.AbstractPushFilter;
import com.hackoeur.jglm.Vec4;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PointRenderPushFilter extends AbstractPushFilter<ColoredFace, Void> {

    private final GraphicsContext graphicsContext;

    public PointRenderPushFilter(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    @Override
    public void push(ColoredFace coloredFace) {
        var face = coloredFace.getFace();
        Color color = coloredFace.getColor();

        graphicsContext.setStroke(color);

        for (Vec4 v : new Vec4[] {
                face.getV1(),
                face.getV2(),
                face.getV3()
        }) {
            double x = v.getX();
            double y = v.getY();
            graphicsContext.strokeLine(x, y, x + 1, y + 1);
        }

    }
}