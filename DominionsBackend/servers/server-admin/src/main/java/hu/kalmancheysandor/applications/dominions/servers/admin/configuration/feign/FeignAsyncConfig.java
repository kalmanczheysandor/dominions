package hu.kalmancheysandor.applications.dominions.servers.admin.configuration.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Configuration for async execution.
 * <p>
 * When using @Async, Spring runs code in another thread.
 * In that case the HTTP request data (like session) is lost.
 * <p>
 * This config helps to keep that data available.
 */
@Configuration
public class FeignAsyncConfig {
    /**
     * Copies the current request data to the new async thread.
     * <p>
     * This makes sure that session and security information
     * are still available inside @Async methods.
     */
    @Bean
    public TaskDecorator contextCopyingDecorator() {
        return runnable -> {
            RequestAttributes context = RequestContextHolder.getRequestAttributes();
            return () -> {
                try {
                    RequestContextHolder.setRequestAttributes(context);
                    runnable.run();
                } finally {
                    RequestContextHolder.resetRequestAttributes();
                }
            };
        };
    }

    /**
     * Custom executor for @Async methods.
     * <p>
     * It uses the TaskDecorator to make sure the request
     * data is passed to async threads.
     */
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setTaskDecorator(contextCopyingDecorator());
        executor.initialize();
        return executor;
    }
}
