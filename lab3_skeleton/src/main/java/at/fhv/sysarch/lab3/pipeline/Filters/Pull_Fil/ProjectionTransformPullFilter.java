package at.fhv.sysarch.lab3.pipeline.Filters.Pull_Fil;

import at.fhv.sysarch.lab3.obj.ColoredFace;
import at.fhv.sysarch.lab3.obj.FaceTransformer;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Pull_Int.AbstractPullFilter;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import com.hackoeur.jglm.Mat4;

public class ProjectionTransformPullFilter extends AbstractPullFilter<ColoredFace, ColoredFace> {
    private final Mat4 projectionMatrix;

    public ProjectionTransformPullFilter(PipelineData pipelineData) {
        this.projectionMatrix = pipelineData.getProjTransform();
    }

    @Override
    public ColoredFace pull() {
        ColoredFace face = source.pull();
        if (face == null) return null;

        return new ColoredFace(
                FaceTransformer.transform(face.getFace(), projectionMatrix),
                face.getColor()
        );
    }
}
