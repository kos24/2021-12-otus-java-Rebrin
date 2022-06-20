package ru.otus.protobuf.service;


import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.StreamValueServiceGrpc;
import ru.otus.protobuf.generated.StreamValueServiceOuterClass;

public class StreamValueServiceImpl extends StreamValueServiceGrpc.StreamValueServiceImplBase {

    @Override
    public void requestValue(StreamValueServiceOuterClass.ValueLimit request, StreamObserver<StreamValueServiceOuterClass.ResponseValue> responseObserver) {
        StreamValueServiceOuterClass.ResponseValue response;
        for (int i = request.getLowerLimit(); i < request.getUpperLimit(); i++) {
            response = StreamValueServiceOuterClass.ResponseValue
                    .newBuilder()
                    .setDigit(i)
                    .build();
            responseObserver.onNext(response);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
        responseObserver.onCompleted();
    }

}
