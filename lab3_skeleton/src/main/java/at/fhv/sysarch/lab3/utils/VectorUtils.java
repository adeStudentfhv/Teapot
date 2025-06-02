package at.fhv.sysarch.lab3.utils;

import com.hackoeur.jglm.Vec3;
import com.hackoeur.jglm.Vec4;

public class VectorUtils {
    public static Vec3 normalize(Vec3 v) {
        float length = (float) Math.sqrt(v.getX() * v.getX() + v.getY() * v.getY() + v.getZ() * v.getZ());
        if (length == 0f) return v;
        return new Vec3(v.getX() / length, v.getY() / length, v.getZ() / length);
    }

    public static Vec4 perspectiveDivide(Vec4 vec) {
        float w = vec.getW();
        if (w == 0f) return vec;  // Sicherheitscheck

        return new Vec4(
                vec.getX() / w,
                vec.getY() / w,
                vec.getZ() / w,
                1f
        );
    }

}
