package at.fhv.sysarch.lab3.pipeline.Interfaces.Push_Int;


@SuppressWarnings("unchecked")
public abstract class AbstractPushFilter<I, O> implements PushFilter<I> {
    protected PushFilter<O> successor;

    @Override
    public void connectTo(PushFilter<?> successor) {
        this.successor = (PushFilter<O>) successor;
    }
}

