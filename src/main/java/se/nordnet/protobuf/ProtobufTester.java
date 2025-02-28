package se.nordnet.protobuf;

import com.google.devtools.cloudtrace.v2.Span;
import com.google.pubsub.v1.PubsubMessage;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class ProtobufTester {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("STARTING");

        try (var e = Executors.newFixedThreadPool(2)) {
            CountDownLatch latch = new CountDownLatch(1);
            CountDownLatch doneLatch = new CountDownLatch(2);

            // We've observed concurrent initialization, in different threads, of Span & PubsubMessage protobuf
            // Hence these in the example
            e.submit(() -> runAfterLatch(latch, doneLatch, Span::getDescriptor));
            e.submit(() -> runAfterLatch(latch, doneLatch, PubsubMessage::getDescriptor));
            latch.countDown();
            doneLatch.await();
        }

        System.out.println("DONE");
    }

    private static void runAfterLatch(CountDownLatch latch, CountDownLatch doneLatch, Supplier<Object> trigger) {
        try {
            latch.await();
            trigger.get();
        } catch (Throwable ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        doneLatch.countDown();
    }
}
