package com.github.madz0.spring_react1;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.github.madz0.spring_react1.model.Quote;
import com.github.madz0.spring_react1.model.QuoteResource;

import lombok.extern.slf4j.Slf4j;
import reactor.bus.Event;
import reactor.bus.EventBus;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static reactor.bus.selector.Selectors.$;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Mohamad Zeinali
 *
 * Jan 30, 2018
 */
@Slf4j
@SpringBootTest(classes={AppConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class ReceiverTest {

	@MockBean
	private RestTemplate restTemplate;
	
	@MockBean
	private Receiver receiver;
	
	@Autowired
	private EventBus eventBus;

	@Autowired
	private Publisher publisher;

	@Autowired
	private CountDownLatch latch;
	
	@Before
	public void before() {

		when(restTemplate.getForObject(eq("http://gturnquist-quoters.cfapps.io/api/random"), Matchers.<Class<QuoteResource>>any())).thenReturn(new QuoteResource() {{
			
			setType("success");
			setValue(new Quote() {{
				
				setId(new Random().nextLong());
				setQuote(String.valueOf(new Random().nextInt()));
			}});
		}});
		
		doAnswer(new Answer<Void>() {
			
			/* (non-Javadoc)
			 * @see org.mockito.stubbing.Answer#answer(org.mockito.invocation.InvocationOnMock)
			 */
			@SuppressWarnings("unchecked")
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				
				QuoteResource quoteResource =
						restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", QuoteResource.class);
			
				log.info("Quote {}: {} +++++++++++++> {}", ((Event<Integer>)invocation.getArguments()[0]).getData(), 
						quoteResource, quoteResource!= null && quoteResource.getValue()!= null? 
								quoteResource.getValue().getQuote():null);
				
				assertNotNull(quoteResource);
				
				latch.countDown();
				
				return null;
			}
		}).when(receiver).accept(any());
	}
	
	@Test
	public void test1() {
		
		QuoteResource quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", QuoteResource.class);
		log.info(":::::::::::::::::::::::{}", quote);
		
		assertNotNull(quote);
	}
	
	@Test
	public void test2() {
		
		eventBus.on($("quotes"), receiver);
		try {
			publisher.publishQuotes(10);
			latch.await(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
