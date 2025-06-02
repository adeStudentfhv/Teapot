package at.fhv.sysarch.lab3.pipeline.Filters.Pull_Fil;

import at.fhv.sysarch.lab3.obj.ColoredFace;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Pull_Int.AbstractPullFilter;
import com.hackoeur.jglm.Vec4;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PointRenderPullFilter extends AbstractPullFilter<ColoredFace, Void> {

    private final GraphicsContext graphicsContext;

    public PointRenderPullFilter(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    @Override
    public Void pull() {
        ColoredFace coloredFace = source.pull();
        if (coloredFace == null) return null;

        var face = coloredFace.getFace();
        Color color = coloredFace.getColor();
        graphicsContext.setStroke(color);

        for (Vec4 v : new Vec4[]{face.getV1(), face.getV2(), face.getV3()}) {
            double x = v.getX();
            double y = v.getY();
            graphicsContext.strokeLine(x, y, x + 1, y + 1);
        }

        return null;
    }
}
