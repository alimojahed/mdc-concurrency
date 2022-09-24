package executor;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.*;

/**
 * A SLF4J MDC-compatible {@link ThreadPoolExecutor}.
 * <p/>
 * In general, MDC is used to store diagnostic information (e.g. a user's session id) in per-thread variables, to facilitate
 * logging. However, although MDC data is passed to thread children, this doesn't work when threads are reused in a
 * thread pool. This is a drop-in replacement for {@link ThreadPoolExecutor} sets MDC data before each task appropriately.
 * <p/>
 * Created by jlevy.
 * Date: 6/14/13
 */
public class MDCThreadPoolExecutor extends ThreadPoolExecutor {

    final private boolean useFixedContext;
    final private Map<String, String> fixedContext;

    public MDCThreadPoolExecutor(Map<String, String> fixedContext, int corePoolSize, int maximumPoolSize,
                                 long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.fixedContext = fixedContext;
        useFixedContext = (fixedContext != null);
    }

    public MDCThreadPoolExecutor(int corePoolSize,
                                 int maximumPoolSize,
                                 long keepAliveTime,
                                 TimeUnit unit,
                                 BlockingQueue<Runnable> workQueue,
                                 ThreadFactory threadFactory) {

        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.fixedContext = null;
        useFixedContext = false;
    }

    /**
     * Pool where task threads take MDC from the submitting thread.
     */
    public MDCThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
                                 long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue ) {
        this(null, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);

    }


    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return new Runnable() {
            @Override
            public void run() {
                Map<String, String> previous = MDC.getCopyOfContextMap();
                if (context == null) {
                    MDC.clear();
                } else {
                    MDC.setContextMap(context);
                }
                try {
                    runnable.run();
                } finally {
                    if (previous == null) {
                        MDC.clear();
                    } else {
                        MDC.setContextMap(previous);
                    }
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getContextForTask() {
        return useFixedContext ? fixedContext : MDC.getCopyOfContextMap();
    }

    /**
     * All executions will have MDC injected. {@code ThreadPoolExecutor}'s submission methods ({@code submit()} etc.)
     * all delegate to this.
     */
    @Override
    public void execute(Runnable command) {
        super.execute(wrap(command, getContextForTask()));
    }
}