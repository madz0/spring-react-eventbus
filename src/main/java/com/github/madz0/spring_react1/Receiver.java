package com.github.madz0.spring_react1;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.github.madz0.spring_react1.model.QuoteResource;

import lombok.extern.slf4j.Slf4j;
import reactor.bus.Event;
import reactor.fn.Consumer;

/**
 * @author Mohamad Zeinali
 *
 * Jan 24, 2018
 */
@Slf4j
@Service
public class Receiver implements Consumer<Event<Integer>> {

	@Autowired
	private CountDownLatch latch;

	@Autowired
	private RestTemplate restTemplate;
	
	/* (non-Javadoc)
	 * @see reactor.fn.Consumer#accept(java.lang.Object)
	 */
	public void accept(Event<Integer> ev) {
		
		QuoteResource quoteResource =
				restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", QuoteResource.class);
	
		log.info("Quote {}: {} =============> {}", ev.getData(), quoteResource, quoteResource.getValue().getQuote());
		latch.countDown();
	}

}
