package org.paypal.client.example;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(App.class)
@WebIntegrationTest
public class PruebaTests {
	
	@Autowired
	ApplicationContext applicationContext;

	@Test
	public void test(){
		Assert.assertTrue(applicationContext.containsBean("paypalApiContextTest"));
		Assert.assertFalse(applicationContext.containsBean("paypalApiContextDefault"));

	}

}