package com.seven.userse.util;

import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.util.function.Supplier;
import io.github.resilience4j.core.IntervalFunction;

public class RetryUtils {

    public static <T> T executeWithRetry(Supplier<T> supplier, String retryName) {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(5)
                .waitDuration(Duration.ofMillis(500)) // Initial wait duration
                .intervalFunction(IntervalFunction.ofExponentialBackoff(500, 2.0)) // Exponential backoff with multiplier
                .build();

        Retry retry = Retry.of(retryName, config);

        Supplier<T> retryableSupplier = Retry.decorateSupplier(retry, supplier);
        return retryableSupplier.get();
    }
}
