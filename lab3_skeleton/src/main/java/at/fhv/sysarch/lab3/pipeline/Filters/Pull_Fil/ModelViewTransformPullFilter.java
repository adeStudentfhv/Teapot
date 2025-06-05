package at.fhv.sysarch.lab3.pipeline.Filters.Pull_Fil;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.FaceTransformer;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Pull_Int.AbstractPullFilter;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Matrices;

public class ModelViewTransformPullFilter extends AbstractPullFilter<Face, Face> {
    private Mat4 modelViewMatrix;
    public ModelViewTransformPullFilter(PipelineData pipelineData, float initialAngle) {
        Mat4 modelRotation = Matrices.rotate(initialAngle, pipelineData.getModelRotAxis());
        Mat4 modelTranslation = pipelineData.getModelTranslation();
        Mat4 modelMatrix = modelRotation.multiply(modelTranslation);
        this.modelViewMatrix = pipelineData.getViewTransform().multiply(modelMatrix);
    }

    public void setModelViewMatrix(Mat4 modelViewMatrix) {
        this.modelViewMatrix = modelViewMatrix;
    }
    @Override
    public Face pull() {
        if (modelViewMatrix == null) {
            throw new IllegalStateException("Model-View-Matrix wurde nicht gesetzt.");
        }

        Face face = source.pull();
        if (face == null) return null;

        return FaceTransformer.transform(face, modelViewMatrix);
    }
}
