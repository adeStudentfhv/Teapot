package at.fhv.sysarch.lab3.pipeline.Interfaces.Push_Int;

public interface PushFilter<T> {
    void push(T data);

    // Einheitliche Signatur zur Vermeidung von Generics-Erasure-Konflikten
    void connectTo(PushFilter<?> successor);
}
