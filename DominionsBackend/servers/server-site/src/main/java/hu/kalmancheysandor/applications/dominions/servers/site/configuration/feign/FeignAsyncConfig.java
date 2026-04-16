package hu.kalmancheysandor.applications.dominions.servers.site.configuration.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Configuration
public class FeignAsyncConfig {

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

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setTaskDecorator(contextCopyingDecorator());
        executor.initialize();
        return executor;
    }
}
