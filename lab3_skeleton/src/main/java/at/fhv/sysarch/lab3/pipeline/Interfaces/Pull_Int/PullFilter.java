package at.fhv.sysarch.lab3.pipeline.Interfaces.Pull_Int;

public interface PullFilter<T> {
    T pull();
    void setSource(PullFilter<?> source);
}
