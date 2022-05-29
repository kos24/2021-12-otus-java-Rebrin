package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Этот класс нужно реализовать
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final BlockingQueue<SensorData> dataBuffer;
    private final List<SensorData> bufferedData = new ArrayList<>();
    private final Lock locker = new ReentrantLock(true);

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.dataBuffer = new PriorityBlockingQueue<>(
                bufferSize, Comparator.comparing(SensorData::getMeasurementTime));
    }

    @Override
    public void process(SensorData data) {

        Thread.currentThread().setPriority(10);
        Thread.yield();
        locker.lock();
        try {
            if (dataBuffer.size() >= bufferSize) {
                flush();
            }
            dataBuffer.add(data);
        } finally {
            locker.unlock();
        }
    }

    public void flush() {
        Thread.currentThread().setPriority(1);
        locker.lock();
        try {
            if (!dataBuffer.isEmpty()) {
                bufferedData.clear();
                dataBuffer.drainTo(bufferedData);
                writer.writeBufferedData(bufferedData);
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        } finally {
            if (!dataBuffer.isEmpty()) {
                flush();
            }
            locker.unlock();
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
