package at.fhv.sysarch.lab3.pipeline.Filters.Pull_Fil;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Pull_Int.AbstractPullFilter;
import at.fhv.sysarch.lab3.utils.VectorUtils;
import com.hackoeur.jglm.Vec3;

public class BackfaceCullingPullFilter extends AbstractPullFilter<Face, Face> {
    @Override
    public Face pull() {
        while (true) {
            Face face = source.pull();
            if (face == null) return null;

            Vec3 v1 = face.getV1().toVec3();
            Vec3 normal = VectorUtils.normalize(face.getN1().toVec3());
            Vec3 viewVector = VectorUtils.normalize(v1.getNegated());

            float dot = normal.dot(viewVector);

            if (dot > 0f) {
                return face; // Sichtbare Fläche → weiterverarbeiten
            }
        }
    }
}
