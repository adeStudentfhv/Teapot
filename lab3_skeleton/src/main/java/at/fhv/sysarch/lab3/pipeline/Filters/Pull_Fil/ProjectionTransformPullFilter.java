package at.fhv.sysarch.lab3.pipeline.Filters.Pull_Fil;

import at.fhv.sysarch.lab3.obj.ColoredFace;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.FaceTransformer;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Pull_Int.AbstractPullFilter;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import com.hackoeur.jglm.Mat4;

public class ProjectionTransformPullFilter extends AbstractPullFilter<ColoredFace, ColoredFace> {
    private final PipelineData pipelineData;

    public ProjectionTransformPullFilter(PipelineData pipelineData) {
        this.pipelineData = pipelineData;
    }

    @Override
    public ColoredFace pull() {
        ColoredFace coloredFace = source.pull();
        if (coloredFace == null) return null;

        Face original = coloredFace.getFace();
        Mat4 proj = pipelineData.getProjTransform();

        Face projected = FaceTransformer.transform(original, proj);
        return new ColoredFace(projected, coloredFace.getColor());
    }
}
