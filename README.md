# Modulo de paypal para spring boot
[![Join the chat at https://gitter.im/jledo/paypal-module](https://badges.gitter.im/jledo/paypal-module.svg)](https://gitter.im/jledo/paypal-module?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

### Introducción :
Hay dos formas básicas de usar este modulo la primera es incluir la 
dependencia es.paradigma:paypal-autoconfigure:1.0.0 y esperar a que 
el usuario incluya la dependencia de paypal o por el contrario añadir
la dependencia es.paradigma:paypal-starter:1.0.0 

### Uso:
---
	
	@autowire
	APIContext apiContext ;
	
	public void createCreditCar(){
	
		CreditCard creditCard = new CreditCard();
		creditCard.setExpireMonth(11);
		creditCard.setExpireYear(2018);
		creditCard.setNumber("4417119669820331");
		creditCard.setType("visa");
		
		try {
			CreditCard createdCreditCard = creditCard.create(apiContext);
			log.info("Credit Card Created With ID: "
				+ createdCreditCard.getId());
		} catch (PayPalRESTException e) {
		}
	}
---

### Version
1.0.0

License
----
GPL

Autor
----
Javier Ledo Vázquez, Email: <jledo@paradigmatecnologico.com>


