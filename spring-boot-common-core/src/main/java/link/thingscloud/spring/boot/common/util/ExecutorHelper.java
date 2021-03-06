package link.thingscloud.spring.boot.common.util;

import cn.hutool.core.thread.NamedThreadFactory;

import java.util.concurrent.*;

/**
 * <p>ExecutorHelper class.</p>
 *
 * @author : zhouhailin
 * @version 1.0.0
 */
public class ExecutorHelper {

    private static final ScheduledExecutorService EXECUTOR_SERVICE = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2,
            new NamedThreadFactory("pool-helper-exec-", true));

    private ExecutorHelper() {
    }

    /**
     * <p>execute.</p>
     *
     * @param runnable a {@link java.lang.Runnable} object.
     */
    public static void execute(Runnable runnable) {
        EXECUTOR_SERVICE.execute(runnable);
    }

    /**
     * <p>submit.</p>
     *
     * @param runnable a {@link java.lang.Runnable} object.
     * @param result   a T object.
     * @param <T>      a T object.
     * @return a {@link java.util.concurrent.Future} object.
     */
    public static <T> Future<T> submit(Runnable runnable, T result) {
        return EXECUTOR_SERVICE.submit(runnable, result);
    }

    /**
     * <p>submit.</p>
     *
     * @param callable a {@link java.util.concurrent.Callable} object.
     * @param <T>      a T object.
     * @return a {@link java.util.concurrent.Future} object.
     */
    public static <T> Future<T> submit(Callable<T> callable) {
        return EXECUTOR_SERVICE.submit(callable);
    }

    /**
     * <p>schedule.</p>
     *
     * @param command a {@link java.lang.Runnable} object.
     * @param delay   a long.
     * @param unit    a {@link java.util.concurrent.TimeUnit} object.
     * @return a {@link java.util.concurrent.ScheduledFuture} object.
     */
    public static ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return EXECUTOR_SERVICE.schedule(command, delay, unit);
    }

    /**
     * <p>scheduleWithFixedDelay.</p>
     *
     * @param command      a {@link java.lang.Runnable} object.
     * @param initialDelay a long.
     * @param delay        a long.
     * @param unit         a {@link java.util.concurrent.TimeUnit} object.
     * @return a {@link java.util.concurrent.ScheduledFuture} object.
     */
    public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return EXECUTOR_SERVICE.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    /**
     * <p>scheduleAtFixedRate.</p>
     *
     * @param command      a {@link java.lang.Runnable} object.
     * @param initialDelay a long.
     * @param period       a long.
     * @param unit         a {@link java.util.concurrent.TimeUnit} object.
     * @return a {@link java.util.concurrent.ScheduledFuture} object.
     */
    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return EXECUTOR_SERVICE.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

}
