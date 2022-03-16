package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.ZoneId;

public class EvenSecondsExceptionProcessor implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public EvenSecondsExceptionProcessor(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {

        if (dateTimeProvider.getTime().atZone(ZoneId.systemDefault()).toEpochSecond() % 2 == 0) {
            throw new RuntimeException(dateTimeProvider.getTime().toString());
        }
        return message;
    }
}
