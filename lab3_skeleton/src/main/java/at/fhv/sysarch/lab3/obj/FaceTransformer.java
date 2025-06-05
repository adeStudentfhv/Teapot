package at.fhv.sysarch.lab3.obj;

import at.fhv.sysarch.lab3.utils.VectorUtils;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;

public class FaceTransformer {
    public static Face transform(Face face, Mat4 transformation) {
        Vec4 v1 = transformation.multiply(face.getV1());
        Vec4 v2 = transformation.multiply(face.getV2());
        Vec4 v3 = transformation.multiply(face.getV3());

        Vec4 n1 = transformation.multiply(face.getN1());
        Vec4 n2 = transformation.multiply(face.getN2());
        Vec4 n3 = transformation.multiply(face.getN3());

        return new Face(v1, v2, v3, n1, n2, n3);
    }

    public static Face perspectiveDivideAndViewport(Face face, Mat4 viewport) {
        Vec4 v1 = viewport.multiply(VectorUtils.perspectiveDivide(face.getV1()));
        Vec4 v2 = viewport.multiply(VectorUtils.perspectiveDivide(face.getV2()));
        Vec4 v3 = viewport.multiply(VectorUtils.perspectiveDivide(face.getV3()));

        return new Face(v1, v2, v3, face.getN1(), face.getN2(), face.getN3());
    }

}