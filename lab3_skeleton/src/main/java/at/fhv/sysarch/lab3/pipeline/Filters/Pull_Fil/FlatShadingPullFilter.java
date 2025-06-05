package at.fhv.sysarch.lab3.pipeline.Filters.Pull_Fil;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.ColoredFace;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Pull_Int.AbstractPullFilter;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.utils.VectorUtils;
import com.hackoeur.jglm.Vec3;
import javafx.scene.paint.Color;

public class FlatShadingPullFilter extends AbstractPullFilter<Face, ColoredFace> {
    private final Vec3 lightPos;
    private final Color baseColor;

    public FlatShadingPullFilter(PipelineData pipelineData) {
        this.lightPos = pipelineData.getLightPos();
        this.baseColor = pipelineData.getModelColor();
    }

    @Override
    public ColoredFace pull() {
        Face face = source.pull();
        if (face == null) return null;

        Vec3 faceCenter = computeFaceCenter(face);
        Vec3 toLight = VectorUtils.normalize(lightPos.subtract(faceCenter));
        Vec3 normal = VectorUtils.normalize(face.getN1().toVec3());

        float brightness = Math.max(0.1f, normal.dot(toLight));

        int red   = clamp((int) (baseColor.getRed() * 255 * brightness), 0, 255);
        int green = clamp((int) (baseColor.getGreen() * 255 * brightness), 0, 255);
        int blue  = clamp((int) (baseColor.getBlue() * 255 * brightness), 0, 255);

        Color shaded = Color.rgb(red, green, blue);

        return new ColoredFace(face, shaded);
    }

    private Vec3 computeFaceCenter(Face face) {
        Vec3 v1 = face.getV1().toVec3();
        Vec3 v2 = face.getV2().toVec3();
        Vec3 v3 = face.getV3().toVec3();

        return new Vec3(
                (v1.getX() + v2.getX() + v3.getX()) / 3f,
                (v1.getY() + v2.getY() + v3.getY()) / 3f,
                (v1.getZ() + v2.getZ() + v3.getZ()) / 3f
        );
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
