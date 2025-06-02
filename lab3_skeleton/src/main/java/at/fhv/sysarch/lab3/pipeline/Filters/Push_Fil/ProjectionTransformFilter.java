package at.fhv.sysarch.lab3.pipeline.Filters.Push_Fil;

import at.fhv.sysarch.lab3.obj.ColoredFace;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.FaceTransformer;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Push_Int.AbstractPushFilter;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import com.hackoeur.jglm.Mat4;

public class ProjectionTransformFilter extends AbstractPushFilter<ColoredFace, ColoredFace> {
    private final PipelineData pipelineData;

    public ProjectionTransformFilter(PipelineData pipelineData) {
        this.pipelineData = pipelineData;
    }

    @Override
    public void push(ColoredFace coloredFace) {
        Face original = coloredFace.getFace();
        Mat4 proj = pipelineData.getProjTransform();

        Face projected = FaceTransformer.transform(original, proj);
        ColoredFace result = new ColoredFace(projected, coloredFace.getColor());

        successor.push(result);
    }
}
