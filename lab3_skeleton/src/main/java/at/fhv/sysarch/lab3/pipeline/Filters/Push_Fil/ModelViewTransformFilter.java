package at.fhv.sysarch.lab3.pipeline.Filters.Push_Fil;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.FaceTransformer;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Push_Int.AbstractPushFilter;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;

public class ModelViewTransformFilter extends AbstractPushFilter<Face, Face> {
    private final PipelineData pipelineData;
    private Mat4 transform;

    public ModelViewTransformFilter(PipelineData pipelineData, float initialAngle) {
        this.pipelineData = pipelineData;
        // setze initiale Transformationsmatrix
        Mat4 rotation = Matrices.rotate(initialAngle, pipelineData.getModelRotAxis());
        this.transform = pipelineData.getViewTransform()
                .multiply(rotation.multiply(pipelineData.getModelTranslation()));
    }

    @Override
    public void push(Face face) {
        Face transformedFace = FaceTransformer.transform(face, transform);
        successor.push(transformedFace);
    }

    public void setTransform(Mat4 transform) {
        this.transform = transform;
    }
}
