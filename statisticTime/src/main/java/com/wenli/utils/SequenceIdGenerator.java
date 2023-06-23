package com.wenli.utils;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class SequenceIdGenerator {
    private static final AtomicInteger id = new AtomicInteger(0);

    public static int nextId() {
        id.compareAndSet(Integer.MAX_VALUE-256, 0);
        return id.incrementAndGet();
    }

}