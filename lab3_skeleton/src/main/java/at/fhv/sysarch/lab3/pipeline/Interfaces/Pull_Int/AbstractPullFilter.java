package at.fhv.sysarch.lab3.pipeline.Interfaces.Pull_Int;

@SuppressWarnings("unchecked")
public abstract class AbstractPullFilter<I, O> implements PullFilter<O> {
    protected PullFilter<I> source;

    @Override
    public void setSource(PullFilter<?> source) {
        this.source = (PullFilter<I>) source;
    }
}

