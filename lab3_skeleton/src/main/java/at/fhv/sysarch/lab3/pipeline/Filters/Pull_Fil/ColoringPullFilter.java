package at.fhv.sysarch.lab3.pipeline.Filters.Pull_Fil;

import at.fhv.sysarch.lab3.obj.ColoredFace;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Pull_Int.AbstractPullFilter;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import javafx.scene.paint.Color;

public class ColoringPullFilter extends AbstractPullFilter<Face, ColoredFace> {
    private final PipelineData pipelineData;

    public ColoringPullFilter(PipelineData pipelineData) {
        this.pipelineData = pipelineData;
    }

    @Override
    public ColoredFace pull() {
        System.out.println(">> [FilterName] pulling");
        Face face = source.pull();
        if (face == null) return null;

        Color color = pipelineData.getModelColor();
        return new ColoredFace(face, color);
    }
}
