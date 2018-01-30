package com.github.madz0.spring_react1;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.github.madz0.spring_react1.App;
import com.github.madz0.spring_react1.model.Quote;
import com.github.madz0.spring_react1.model.QuoteResource;

import lombok.extern.slf4j.Slf4j;

import static org.mockito.Mockito.*;

import java.util.Random;

/**
 * @author Mohamad Zeinali
 *
 * Jan 30, 2018
 */
@Slf4j
@SpringBootTest(classes= {App.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ReceiverTest {

	@MockBean
	private RestTemplate restTemplate;
	
	@Before
	public void before() {

		when(restTemplate.getForObject(eq("http://gturnquist-quoters.cfapps.io/api/random"), Matchers.<Class<QuoteResource>>any())).thenReturn(new QuoteResource() {{
			
			setType("success");
			setValue(new Quote() {{
				
				setId(1L);
				setQuote(String.valueOf(new Random().nextInt()));
			}});
		}});
	}
	
	@Test
	public void test1() {
		
		QuoteResource quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", QuoteResource.class);
		
		log.info(":::::::::::::::::::::::{}", quote);
	}
}
