package io.twitter.reporter.commons.retry;

import io.twitter.reporter.config.RetryConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RetryContext {

    private final RetryConfiguration retryConfiguration;

    @Bean
    public RetryTemplate retryTemplate() {
        final RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(createBackoffPolicy());
        retryTemplate.setRetryPolicy(createRetryPolicy());

        return retryTemplate;
    }

    private BackOffPolicy createBackoffPolicy() {
        final ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(retryConfiguration.getInitialIntervalInMs());
        backOffPolicy.setMaxInterval(retryConfiguration.getMaxIntervalInMs());
        backOffPolicy.setMultiplier(retryConfiguration.getMultiplier());
        return backOffPolicy;
    }

    private RetryPolicy createRetryPolicy() {
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(retryConfiguration.getMaxAttempts());

        return simpleRetryPolicy;
    }

}
