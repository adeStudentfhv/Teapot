package at.fhv.sysarch.lab3.pipeline.Filters.Push_Fil;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Push_Int.AbstractPushFilter;
import com.hackoeur.jglm.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DepthSortingPushFilter extends AbstractPushFilter<Face, Face> {

    private final List<Face> buffer = new ArrayList<>();
    private final Vec3 viewPos;

    public DepthSortingPushFilter(Vec3 viewPos) {
        this.viewPos = viewPos;
    }

    @Override
    public void push(Face face) {
        buffer.add(face);
    }

    public void flush() {
        buffer.sort(Comparator.comparingDouble(this::avgDistance).reversed());
        for (Face face : buffer) {
            successor.push(face);
        }
        buffer.clear();
    }

    private double avgDistance(Face face) {
        var v1 = face.getV1().toVec3();
        var v2 = face.getV2().toVec3();
        var v3 = face.getV3().toVec3();

        double d1 = v1.subtract(viewPos).getLength();
        double d2 = v2.subtract(viewPos).getLength();
        double d3 = v3.subtract(viewPos).getLength();

        return (d1 + d2 + d3) / 3.0;
    }
}
