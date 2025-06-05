package at.fhv.sysarch.lab3.obj;
import javafx.scene.paint.Color;

public class ColoredFace {
    private final Face face;
    private final Color color;

    public ColoredFace(Face face, Color color) {
        this.face = face;
        this.color = color;
    }

    public Face getFace() {
        return face;
    }

    public Color getColor() {
        return color;
    }
}