package at.fhv.sysarch.lab3.pipeline.Filters.Pull_Fil;

import at.fhv.sysarch.lab3.obj.ColoredFace;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Pull_Int.AbstractPullFilter;

public class SinkPullFilter extends AbstractPullFilter<ColoredFace, Void> {

    @Override
    public Void pull() {
        while (true) {
            ColoredFace face = source.pull();
            if (face == null) break;
        }
        return null;
    }
}
