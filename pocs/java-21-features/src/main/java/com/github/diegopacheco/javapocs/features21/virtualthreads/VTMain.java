package com.github.diegopacheco.javapocs.features21.virtualthreads;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class VTMain {
    public static void main(String[] args) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10_000).forEach(i -> {
                executor.submit(() -> {
                    Thread.sleep(Duration.ofSeconds(1));
                    System.out.println(i);
                    return i;
                });
            });
        }  // executor.close() is called implicitly, and waits
    }
}
