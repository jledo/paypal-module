package org.paypal.autoconfigure;

import java.util.Properties;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.paypal.base.ConfigManager;
import com.paypal.base.Constants;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.base.rest.PayPalResource;

import junit.framework.Test;

@Configuration
@ConditionalOnClass(PayPalResource.class)
@EnableConfigurationProperties(PayPalProperties.class)
public class PayPalConfiguration {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PayPalProperties payPalProperties;
	
	@ConditionalOnMissingBean
	@ConditionalOnProperty(matchIfMissing=true, prefix="paypal")
	@Bean
	public APIContext paypalApiContextTest(){
		boolean production = false;
		boolean useProperties = false;
		Properties properties =	configurePropertiesPro(production, useProperties);
		return initApi(properties);
	}
	
	@ConditionalOnClass(Test.class)
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value={"paypal.clientId", "paypal.clientSecret"})
	@Bean
	public APIContext paypalApiContextTestProperties(){
		boolean production = false;
		boolean useProperties = true;
		Properties properties =	configurePropertiesPro(production, useProperties);
		return initApi(properties);
	}
	
	@ConditionalOnMissingClass("junit.framework.Test")
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value={"paypal.clientId", "paypal.clientSecret"})
	@Bean
	public APIContext paypalApiContextPro(){
		boolean production = true;
		boolean useProperties = true;
		Properties properties =	configurePropertiesPro(production, useProperties);
		return initApi(properties);
	}
	

	private APIContext initApi(Properties properties) {
		PayPalResource.initConfig(properties);
		return new APIContext(getAccessToken());
	}
	
	private String getAccessToken()  {
		String clientID = ConfigManager.getInstance().getConfigurationMap().get(Constants.CLIENT_ID);
		String clientSecret = ConfigManager.getInstance().getConfigurationMap().get(
				Constants.CLIENT_SECRET);
		try {
			return new OAuthTokenCredential(clientID, clientSecret)
					.getAccessToken();
		} catch (PayPalRESTException e) {
			logger.error("Error recuperando el token del api paypal",e);
			throw new RuntimeException("No se puede configurar el token para el acceso al api de paypal", e);
		}
	}
	private Properties configurePropertiesPro(boolean production, boolean useProperties) {
		Properties properties = new Properties();
		properties.put("http.ConnectionTimeOut", 5000);
		properties.put("http.Retry", 1);
		properties.put("http.ReadTimeOut", 30000);
		properties.put("http.MaxConnection", 100);
		
		properties.put("http.ProxyPort", 8080);
		properties.put("http.ProxyHost", "127.0.0.1");
		properties.put("http.UseProxy", false);
		properties.put("http.ProxyUserName", null);
		properties.put("http.ProxyPassword", null);

		properties.put("http.GoogleAppEngine", false);
		
		if(production){
			properties.put("service.EndPoint", "https://api.paypal.com");
		} else {
			properties.put("service.EndPoint", "https://api.sandbox.paypal.com");
		}

		if(useProperties){
			properties.put("clientId", payPalProperties.getClientId());
			properties.put("clientSecret", payPalProperties.getClientSecret());
		} else {
			properties.put("clientId", "AYSq3RDGsmBLJE-otTkBtM-jBRd1TCQwFf9RGfwddNXWz0uFU9ztymylOhRS");
			properties.put("clientSecret", "EGnHDxD_qRPdaLdZz8iCr8N7_MzF-YHPTkjs6NKYQvQSBngp4PTTVWkPZRbL");
		}

		return properties;

	}




}
