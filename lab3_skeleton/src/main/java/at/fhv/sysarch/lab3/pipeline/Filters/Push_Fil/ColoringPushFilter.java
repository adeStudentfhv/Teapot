package at.fhv.sysarch.lab3.pipeline.Filters.Push_Fil;

import at.fhv.sysarch.lab3.obj.ColoredFace;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Push_Int.AbstractPushFilter;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import javafx.scene.paint.Color;


public class ColoringPushFilter extends AbstractPushFilter<Face, ColoredFace> {
    private final Color color;

    public ColoringPushFilter(PipelineData pipelineData) {
        this.color = pipelineData.getModelColor();
    }

    @Override
    public void push(Face face) {
        ColoredFace colored = new ColoredFace(face, color);
        successor.push(colored);  // keine Nebenwirkungen!
    }
}
