package org.paypal.client.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppTest {
	public static void main(String[] args) {
		new SpringApplication(App.class).run(args);
	}
}
