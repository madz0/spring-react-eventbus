package com.github.madz0.spring_react1;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.bus.Event;
import reactor.bus.EventBus;

/**
 * @author Mohamad Zeinali
 *
 * Jan 24, 2018
 */
@Service
public class Publisher {

	@Autowired
	EventBus eventBus;
	
	@Autowired
	CountDownLatch latch;
	
	public void publishQuotes(int numberOfQuotes) throws InterruptedException {
		long start = System.currentTimeMillis();

		AtomicInteger counter = new AtomicInteger(1);

		for (int i = 0; i < numberOfQuotes; i++) {
			eventBus.notify("quotes", Event.wrap(counter.getAndIncrement()));
		}

		latch.await();

		long elapsed = System.currentTimeMillis() - start;

		System.out.println("Elapsed time: " + elapsed + "ms");
		System.out.println("Average time per quote: " + elapsed / numberOfQuotes + "ms");
	}
}
