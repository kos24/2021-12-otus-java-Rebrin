package ru.otus.protobuf;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ru.otus.protobuf.generated.StreamValueServiceGrpc;
import ru.otus.protobuf.generated.StreamValueServiceOuterClass;

import java.util.concurrent.CountDownLatch;

public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8081;
    private static final int LOWER_LIMIT = 0;
    private static final int UPPER_LIMIT = 30;

    private static int value;

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        StreamValueServiceOuterClass.ValueLimit request = StreamValueServiceOuterClass.ValueLimit
                .newBuilder()
                .setLowerLimit(LOWER_LIMIT)
                .setUpperLimit(UPPER_LIMIT)
                .build();
        var latch = new CountDownLatch(1);
        StreamValueServiceGrpc.StreamValueServiceStub stub = StreamValueServiceGrpc.newStub(channel);
        var observer = new ClientStreamObserver(latch);
        stub.requestValue(request, observer);
        System.out.println("Client is starting...");

        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    for (int i = 0; i < 50; i++) {
                        System.out.println(processValue(observer));
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Client has stopped.");
                }
            }
        });
        thread.start();
        latch.await();
        thread.interrupt();
        channel.shutdown();
    }

    private static int processValue(ClientStreamObserver observer) {
        value = value + observer.getValueFromServer() + 1;
        return value;
    }

}
