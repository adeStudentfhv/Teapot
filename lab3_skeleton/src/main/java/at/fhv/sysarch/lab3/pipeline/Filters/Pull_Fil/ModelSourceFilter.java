package at.fhv.sysarch.lab3.pipeline.Filters.Pull_Fil;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Pull_Int.PullFilter;

import java.util.Iterator;

public class ModelSourceFilter implements PullFilter<Face> {
    private Iterator<Face> faceIterator;

    public void setModel(Model model) {
        this.faceIterator = model.getFaces().iterator();
    }

    @Override
    public Face pull() {
        if (faceIterator != null && faceIterator.hasNext()) {
            return faceIterator.next();
        }
        return null;
    }

    @Override
    public void setSource(PullFilter<?> source) {
        // not needed for source
    }
}
