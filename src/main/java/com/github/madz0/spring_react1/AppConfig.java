package com.github.madz0.spring_react1;

import java.util.concurrent.CountDownLatch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import reactor.Environment;
import reactor.bus.EventBus;

import static com.github.madz0.spring_react1.App.*;

/**
 * @author Mohamad Zeinali
 *
 * Feb 14, 2018
 */
@Configuration
@ComponentScan
public class AppConfig {

	@Bean
    Environment env() {
        return Environment.initializeIfEmpty()
                          .assignErrorJournal();
    }
	
	@Bean
    EventBus createEventBus(Environment env) {
	    return EventBus.create(env, Environment.THREAD_POOL);
    }

	@Bean
	public CountDownLatch latch() {
		
		return new CountDownLatch(NUMBER_OF_QUOTES);
	}
	
	@Bean
	public RestTemplate restTemplate() {
		
		return new RestTemplate();
	}
}
