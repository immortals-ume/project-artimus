package com.immortals.LLD.parkinglot.infra;

import java.util.concurrent.atomic.AtomicLong;

public interface IdGenerator {
    static IdGenerator sequential(String prefix) {
        AtomicLong counter = new AtomicLong();
        return () -> prefix + "-" + counter.incrementAndGet();
    }

    String next();
}