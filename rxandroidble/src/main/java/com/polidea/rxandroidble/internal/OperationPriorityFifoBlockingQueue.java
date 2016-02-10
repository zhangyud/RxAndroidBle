package com.polidea.rxandroidble.internal;

import android.support.annotation.NonNull;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class OperationPriorityFifoBlockingQueue {

    private final PriorityBlockingQueue<FIFOEntry> q = new PriorityBlockingQueue<>();

    public void add(RxBleRadioOperation object) {
        q.add(new FIFOEntry(object));
    }

    public RxBleRadioOperation take() throws InterruptedException {
        return q.take().getEntry();
    }

    public boolean isEmpty() {
        return q.isEmpty();
    }

    private static class FIFOEntry implements Comparable<FIFOEntry> {

        static final AtomicLong seq = new AtomicLong(0);

        final long seqNum;

        final RxBleRadioOperation entry;

        public FIFOEntry(RxBleRadioOperation entry) {
            seqNum = seq.getAndIncrement();
            this.entry = entry;
        }

        public RxBleRadioOperation getEntry() {
            return entry;
        }

        public int compareTo(@NonNull FIFOEntry other) {
            int res = entry.compareTo(other.entry);
            if (res == 0 && other.entry != this.entry) {
                res = (seqNum < other.seqNum ? -1 : 1);
            }
            return res;
        }
    }
}
