package com.github.madz0.spring_react1;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import reactor.Environment;
import reactor.bus.EventBus;

import static reactor.bus.selector.Selectors.$;

/**
 * Hello world!
 *
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@Profile("!test")
public class App implements CommandLineRunner {
	
	private static final int NUMBER_OF_QUOTES = 10;
	
	@Bean
    Environment env() {
        return Environment.initializeIfEmpty()
                          .assignErrorJournal();
    }
	
	@Bean
    EventBus createEventBus(Environment env) {
	    return EventBus.create(env, Environment.THREAD_POOL);
    }
	
	@Autowired
	private EventBus eventBus;

	@Autowired
	private Receiver receiver;

	@Autowired
	private Publisher publisher;

	@Bean
	public CountDownLatch latch() {
		
		return new CountDownLatch(NUMBER_OF_QUOTES);
	}
	
	@Bean
	public RestTemplate restTemplate() {
		
		return new RestTemplate();
	}
	
    public static void main(String[] args) throws BeansException, InterruptedException {
    	
    	ApplicationContext app = SpringApplication.run(App.class, args);
    	
    	app.getBean(CountDownLatch.class).await(1, TimeUnit.SECONDS);
		app.getBean(Environment.class).shutdown();
    }

	/* (non-Javadoc)
	 * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
	 */
	public void run(String... arg0) throws Exception {
		
		eventBus.on($("quotes"), receiver);
		publisher.publishQuotes(NUMBER_OF_QUOTES);
	}
}
