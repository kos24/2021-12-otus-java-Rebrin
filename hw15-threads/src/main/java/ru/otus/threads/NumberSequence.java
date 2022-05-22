package ru.otus.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberSequence {

    private static final Logger log = LoggerFactory.getLogger(NumberSequence.class);
    public static final int UPPER_LIMIT = 10;
    public static final int LOWER_LIMIT = 1;

    private int number;
    private boolean allow = true;
    private boolean switcher = true;

    public static void main(String[] args) throws InterruptedException {
        var sequenceNumber = new NumberSequence();
        sequenceNumber.start();
    }

    private void start() throws InterruptedException {
        var thread1 = new Thread(this::sequenceGenerator);
        var thread2 = new Thread(this::sequenceReader);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    private synchronized void sequenceGenerator() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    while (!allow) {
                        wait();
                    }
                    if (number == UPPER_LIMIT) {
                        switcher = false;
                    }
                    if (number == LOWER_LIMIT) {
                        switcher = true;
                    }
                    inc(switcher);
                    log.info(String.valueOf(number));
                    sleep(1);
                    allow = false;
                    notifyAll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
    }

    private synchronized void sequenceReader() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    while (allow) {
                        wait();
                    }
                    log.info(String.valueOf(number));
                    sleep(1);
                    allow = true;
                    notifyAll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
    }

    private void sleep(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void inc(boolean switcher) {
        if (switcher) {
            number++;
        } else {
            number--;
        }
    }
}
