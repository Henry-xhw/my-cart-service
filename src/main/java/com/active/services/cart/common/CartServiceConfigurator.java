package com.active.services.cart.common;

import com.active.platform.concurrent.ExecutorServiceTaskRunner;
import com.active.platform.concurrent.TaskRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.EventBus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class CartServiceConfigurator {

    @Autowired
    public void configureObjectMapper(ObjectMapper objectMapper) {
        Event.setObjectMapper(objectMapper);
    }

    @Bean
    public EventBus eventBus() {
        return new EventBus("cart-service-event-bus");
    }

    @Bean
    public TaskRunner taskRunner() {
        return new ExecutorServiceTaskRunner(new ThreadPoolExecutor(5, 100,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new NamedThreadFactory("cart-backend-compute-thread-")));
    }

    private class NamedThreadFactory implements ThreadFactory {
        private final String threadBaseName;

        private AtomicLong counter = new AtomicLong(1L);

        public NamedThreadFactory(String baseName) {
            this.threadBaseName = baseName;
        }

        public Thread newThread(Runnable r) {
            return new Thread(r, threadBaseName + counter.getAndIncrement());
        }
    }
}
