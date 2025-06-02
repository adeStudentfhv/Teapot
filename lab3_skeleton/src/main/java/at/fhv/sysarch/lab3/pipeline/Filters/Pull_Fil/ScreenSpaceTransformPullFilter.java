package at.fhv.sysarch.lab3.pipeline.Filters.Pull_Fil;

import at.fhv.sysarch.lab3.obj.ColoredFace;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.FaceTransformer;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Pull_Int.AbstractPullFilter;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import com.hackoeur.jglm.Mat4;

public class ScreenSpaceTransformPullFilter extends AbstractPullFilter<ColoredFace, ColoredFace> {

    private final PipelineData pipelineData;

    public ScreenSpaceTransformPullFilter(PipelineData pipelineData) {
        this.pipelineData = pipelineData;
    }

    @Override
    public ColoredFace pull() {
        ColoredFace coloredFace = source.pull();
        if (coloredFace == null) return null;

        Face face = coloredFace.getFace();
        Mat4 viewport = pipelineData.getViewportTransform();

        // Wende perspektivische Division + Viewport-Transformation an
        Face transformed = FaceTransformer.perspectiveDivideAndViewport(face, viewport);

        // Behalte die ursprüngliche Farbe
        return new ColoredFace(transformed, coloredFace.getColor());
    }
}
