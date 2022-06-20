package ru.otus.protobuf;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.StreamValueServiceOuterClass;

import java.util.concurrent.CountDownLatch;

public class ClientStreamObserver implements StreamObserver<StreamValueServiceOuterClass.ResponseValue> {

    private int currentValue = 0;
    private final CountDownLatch latch;

    public ClientStreamObserver(CountDownLatch latch) {
        this.latch = latch;
    }

    public synchronized int getValueFromServer() {
        int prevValue = currentValue;
        currentValue = 0;
        return prevValue;
    }


    private synchronized void addValue(int value) {
        currentValue = value;
    }

    @Override
    public void onNext(StreamValueServiceOuterClass.ResponseValue value) {
        System.out.println("new value: " + value.getDigit());
        addValue(value.getDigit());
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("Something went wrong...");
    }

    @Override
    public void onCompleted() {
        System.out.println("Streaming has ended.");
        latch.countDown();
    }

}
