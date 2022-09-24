package stream;

import org.slf4j.MDC;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Ali Mojahed on 9/3/2022
 * @project mdcconcurrency
 **/
public class ParallelStreamWrapper {
    public static <T> void forEach(Collection<T> collection, Consumer<T> action) {
        final Map<String, String> context = MDC.getCopyOfContextMap();

        collection
                .parallelStream()
                .forEach(t -> {
                    try {
                        MDC.setContextMap(context);
                        action.accept(t);
                    }finally {
                        MDC.clear();
                    }
                });

        MDC.setContextMap(context);
    }

    public static <T> boolean anyMatch(Collection<T> collection, Predicate<T> predicate) {
        final Map<String, String> context = MDC.getCopyOfContextMap();
        boolean result = collection.parallelStream().anyMatch(t -> {
            try {
                MDC.setContextMap(context);
                return predicate.test(t);
            }finally {
                MDC.clear();
            }
        });

        MDC.setContextMap(context);

        return result;
    }



}
