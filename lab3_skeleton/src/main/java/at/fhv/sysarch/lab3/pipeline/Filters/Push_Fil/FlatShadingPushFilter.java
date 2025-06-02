package at.fhv.sysarch.lab3.pipeline.Filters.Push_Fil;

import at.fhv.sysarch.lab3.obj.ColoredFace;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Push_Int.AbstractPushFilter;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.utils.VectorUtils;
import com.hackoeur.jglm.Vec3;
import javafx.scene.paint.Color;

public class FlatShadingPushFilter extends AbstractPushFilter<ColoredFace, ColoredFace> {
    private final PipelineData pipelineData;

    public FlatShadingPushFilter(PipelineData pipelineData) {
        this.pipelineData = pipelineData;
    }
    @Override
    public void push(ColoredFace face) {
        Vec3 lightPos = pipelineData.getLightPos(); // Position der Lichtquelle im View Space
        Vec3 faceCenter = computeFaceCenter(face); // Mittelpunkt des Dreiecks
        Vec3 toLight = VectorUtils.normalize(lightPos.subtract(faceCenter)); // Richtung zum Licht

        Vec3 faceNormal = VectorUtils.normalize(face.getFace().getN1().toVec3());

        float brightness = faceNormal.dot(toLight);
        brightness = Math.max(brightness, 0f); // wenn negativ → 0

        Color original = face.getColor();

        Color shaded = Color.rgb(
                (int) (original.getRed() * 255 * brightness),
                (int) (original.getGreen() * 255 * brightness),
                (int) (original.getBlue() * 255 * brightness)
        );


        ColoredFace result = new ColoredFace(face.getFace(), shaded);
        successor.push(result);
    }

    private Vec3 computeFaceCenter(ColoredFace coloredFace) {
        Vec3 v1 = coloredFace.getFace().getV1().toVec3();
        Vec3 v2 = coloredFace.getFace().getV2().toVec3();
        Vec3 v3 = coloredFace.getFace().getV3().toVec3();

        return new Vec3(
                (v1.getX() + v2.getX() + v3.getX()) / 3f,
                (v1.getY() + v2.getY() + v3.getY()) / 3f,
                (v1.getZ() + v2.getZ() + v3.getZ()) / 3f
        );
    }

}
