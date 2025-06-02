package at.fhv.sysarch.lab3.pipeline.Filters.Pull_Fil;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.Interfaces.Pull_Int.PullFilter;

import java.util.Iterator;

public class ModelSourceFilter implements PullFilter<Face> {
    private final Iterator<Face> faceIterator;

    public ModelSourceFilter(Model model) {
        this.faceIterator = model.getFaces().iterator();
    }
    @Override
    public Face pull() {
        if (faceIterator.hasNext()) {
            return faceIterator.next();
        }
        return null;
    }

    @Override
    public void setSource(PullFilter<?> source) {

    }
}
