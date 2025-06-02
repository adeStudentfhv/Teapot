package at.fhv.sysarch.lab3.pipeline.Filters.Pull_Fil;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.FaceTransformer;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Pull_Int.AbstractPullFilter;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import com.hackoeur.jglm.Mat4;

public class ModelViewTransformationPullFilter extends AbstractPullFilter<Face, Face> {
    private Mat4 modelViewMatrix;

    public ModelViewTransformationPullFilter(PipelineData pipelineData, float initialAngle) {
        // Optional: Initialwert setzen
        this.modelViewMatrix = pipelineData.getViewTransform()
                .multiply(pipelineData.getModelTranslation()); // oder leer lassen
    }

    public void setModelViewMatrix(Mat4 modelViewMatrix) {
        this.modelViewMatrix = modelViewMatrix;
    }

    @Override
    public Face pull() {
        Face face = source.pull();
        if (face == null) return null;
        return FaceTransformer.transform(face, modelViewMatrix);
    }
}
