package org.rlms.common.util;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import org.rlms.common.exception.BaseException;

public class AsyncUtil {

    private AsyncUtil() {
    }

    public static void async(Runnable async) {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        try {
            CompletableFuture.runAsync(async, pool);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new BaseException(e);
        } finally{
            pool.shutdown();
        }
    }

    public static <V> V await(Supplier<V> await) {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        try {
            CompletableFuture<V> future = CompletableFuture.supplyAsync(await, pool);
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new BaseException(e);
        } finally{
            pool.shutdown();
        }
    }

    public static void asyncAll(List<Runnable> asyncJobs) {
        ExecutorService pool = Executors.newCachedThreadPool();
        try {
            StreamUtil.ofNullable(asyncJobs).forEach(async -> CompletableFuture.runAsync(async, pool));
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new BaseException(e);
        } finally{
            pool.shutdown();
        }
    }

    public static Object awaitAll(List<Supplier<?>> awaitJobs, boolean isIfAny) {
        ExecutorService pool = Executors.newCachedThreadPool();
        try {
            CompletableFuture<?>[] futures = StreamUtil.ofNullable(awaitJobs)
                    .map(await -> CompletableFuture.supplyAsync(await, pool))
                    .toArray(CompletableFuture<?>[]::new);

            CompletableFuture<?> result = isIfAny ? CompletableFuture.anyOf(futures) : CompletableFuture.allOf(futures);
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new BaseException(e);
        } finally{
            pool.shutdown();
        }
    }

}
