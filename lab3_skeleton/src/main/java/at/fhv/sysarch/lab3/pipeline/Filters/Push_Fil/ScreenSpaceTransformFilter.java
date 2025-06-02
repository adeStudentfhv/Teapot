package at.fhv.sysarch.lab3.pipeline.Filters.Push_Fil;

import at.fhv.sysarch.lab3.obj.ColoredFace;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.FaceTransformer;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Push_Int.AbstractPushFilter;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import com.hackoeur.jglm.Mat4;

public class ScreenSpaceTransformFilter extends AbstractPushFilter<ColoredFace, ColoredFace> {

    private final PipelineData pipelineData;

    public ScreenSpaceTransformFilter(PipelineData pipelineData) {
        this.pipelineData = pipelineData;
    }

    @Override
    public void push(ColoredFace coloredFace) {
        Face face = coloredFace.getFace();
        Mat4 viewport = pipelineData.getViewportTransform();

        // Wende perspektivische Division + Viewport-Transformation an
        Face transformed = FaceTransformer.perspectiveDivideAndViewport(face, viewport);

        // Behalte die ursprüngliche Farbe
        ColoredFace result = new ColoredFace(transformed, coloredFace.getColor());

        successor.push(result);
    }
}
