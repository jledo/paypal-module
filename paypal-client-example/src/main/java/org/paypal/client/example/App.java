package org.paypal.client.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App implements CommandLineRunner{
	private Logger logger = LoggerFactory.getLogger(App.class);
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private boolean paypalApiContextProperties;
	private boolean paypalApiContextTest;
	private boolean paypalApiContextPro;
	private boolean paypalApiContextDefault;

	
	public static void main(String[] args) {
		new SpringApplication(App.class).run(args);
	}

	public void run(String... args) throws Exception {
		paypalApiContextDefault = applicationContext.containsBean("paypalApiContextDefault");
		if(paypalApiContextDefault){
			logger.info("Iniciando paypal con la configuración por defecto");
			logger.info("Definido el bean: paypalApiContextDefault");
		}
		
		paypalApiContextProperties = applicationContext.containsBean("paypalApiContextProperties");
		if(paypalApiContextProperties){
			logger.info("Iniciando paypal con un archivo de properties");
			logger.info("Definido el bean: paypalApiContextProperties");
		}
		
		paypalApiContextTest = applicationContext.containsBean("paypalApiContextTest");
		if(paypalApiContextTest){
			logger.info("Iniciando paypal en modo test");
			logger.info("Definido el bean: paypalApiContextTest");
		}
		
		paypalApiContextPro = applicationContext.containsBean("paypalApiContextPro");
		if(paypalApiContextPro){
			logger.info("Iniciando paypal en modo producción");
			logger.info("Definido el bean: paypalApiContextPro");
		}
		
		
	}
}
