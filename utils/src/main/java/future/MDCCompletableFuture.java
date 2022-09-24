package future;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * @author Ali Mojahed on 9/3/2022
 * @project mdcconcurrency
 **/
public class MDCCompletableFuture<T> extends CompletableFuture<T> {
    public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        Map<String, String> mdc = MDC.getCopyOfContextMap();
        return CompletableFuture.supplyAsync(() -> {
            try {
                MDC.setContextMap(mdc);
                return supplier.get();

            } finally {
                MDC.clear();
            }
        });
    }

    public static CompletableFuture<Void> runAsync(Runnable command) {
        Map<String, String> mdc = MDC.getCopyOfContextMap();
        return CompletableFuture.runAsync(() -> {
            try {
                MDC.setContextMap(mdc);
                command.run();

            } finally {
                MDC.clear();
            }
        });
    }


}
