package ru.otus.exceptions;

public class ContainerInstantiationException extends RuntimeException {
    public ContainerInstantiationException(String message) {
        super(message);
    }

    public ContainerInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
