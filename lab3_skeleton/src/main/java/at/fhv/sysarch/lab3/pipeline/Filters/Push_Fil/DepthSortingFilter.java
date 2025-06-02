package at.fhv.sysarch.lab3.pipeline.Filters.Push_Fil;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Push_Int.AbstractPushFilter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DepthSortingFilter extends AbstractPushFilter<Face, Face> {
    private final List<Face> buffer = new ArrayList<>();

    @Override
    public void push(Face face) {
        buffer.add(face);
    }

    public void flush() {
        buffer.sort(Comparator.comparingDouble(this::avgZ).reversed());
        for (Face face : buffer) {
            successor.push(face);
        }
        buffer.clear();
    }

    private float avgZ(Face face) {
        return (face.getV1().getZ() + face.getV2().getZ() + face.getV3().getZ()) / 3f;
    }
}
