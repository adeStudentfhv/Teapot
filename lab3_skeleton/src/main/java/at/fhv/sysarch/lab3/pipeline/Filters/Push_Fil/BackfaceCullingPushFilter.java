package at.fhv.sysarch.lab3.pipeline.Filters.Push_Fil;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Push_Int.AbstractPushFilter;
import at.fhv.sysarch.lab3.utils.VectorUtils;
import com.hackoeur.jglm.Vec3;

public class BackfaceCullingPushFilter extends AbstractPushFilter<Face, Face> {
    @Override
    public void push(Face face) {
        Vec3 v1 = face.getV1().toVec3();
        Vec3 v2 = face.getV2().toVec3();
        Vec3 v3 = face.getV3().toVec3();

        Vec3 normal = VectorUtils.normalize(face.getN1().toVec3());

        Vec3 viewVector = VectorUtils.normalize(v1.getNegated());

        float dot = normal.dot(viewVector);

        if (dot > 0f) {
            successor.push(face);
        }
    }
}