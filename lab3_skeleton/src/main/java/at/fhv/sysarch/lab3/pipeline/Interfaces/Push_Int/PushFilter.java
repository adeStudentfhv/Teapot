package at.fhv.sysarch.lab3.pipeline.Interfaces.Push_Int;

public interface PushFilter<T> {
    void push(T data);

    void connectTo(PushFilter<?> successor);
}