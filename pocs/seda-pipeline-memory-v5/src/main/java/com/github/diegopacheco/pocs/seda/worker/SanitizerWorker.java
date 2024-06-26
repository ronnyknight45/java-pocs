package com.github.diegopacheco.pocs.seda.worker;

import com.github.diegopacheco.pocs.seda.event.Event;
import com.github.diegopacheco.pocs.seda.ff.FeatureFlagManager;
import com.github.diegopacheco.pocs.seda.metrics.MetricsManager;
import com.github.diegopacheco.pocs.seda.seda.SEDAManager;
import com.github.diegopacheco.pocs.seda.seda.Queues;
import com.github.diegopacheco.pocs.seda.thread.SilentThread;

public class SanitizerWorker implements Worker {

    private SEDAManager sedaManager;
    private Queues next;

    private Event<String> event;

    public SanitizerWorker(SEDAManager queueManager, Queues next, Event<String> event) {
        this.sedaManager = queueManager;
        this.next = next;
        this.event = event;
    }

    @Override
    public void run() {
        if (null != event) {
            try {
                Event<String> sanitizedEvent = sanitize(event);
                System.out.println(" >> " + sanitizedEvent);
                sedaManager.publish(next, sanitizedEvent);

                MetricsManager.ok(Queues.SANITIZER_QUEUE.name());
            } catch (Exception e) {
                MetricsManager.error(Queues.SANITIZER_QUEUE.name());
            }
        }
        System.out.println("Worker[" + this.getClass().getSimpleName() +
                "~" + Thread.currentThread().getName() +
                "] completed. ");
    }

    private Event<String> sanitize(Event<String> event) {
        SilentThread.sleep(FeatureFlagManager.get(FeatureFlagManager.QUEUE_SANITIZER_TIME_BACKPRESSURE_MS));
        return new Event(event.getContent().trim().toLowerCase()).addStage("SANITIZER");
    }

}
