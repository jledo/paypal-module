package org.paypal.autoconfigure;

import java.util.HashMap;
import java.util.Map;
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

import com.paypal.base.ConfigManager;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.base.rest.PayPalResource;

@Configuration
@ConditionalOnClass(PayPalResource.class)
@EnableConfigurationProperties(PayPalProperties.class)
public class PayPalConfiguration {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PayPalProperties payPalProperties;
	

	@ConditionalOnClass(name="junit.framework.Test")
	@ConditionalOnMissingBean
	@Bean
	public APIContext paypalApiContextTest() {
		boolean production = false;
		boolean useProperties = false;
		Properties properties = generateProperties(production, useProperties);
		return initApi(properties);
	}
	
	@ConditionalOnMissingClass("junit.framework.Test")
	@ConditionalOnMissingBean
	@ConditionalOnProperty(matchIfMissing = true, value= { "paypal.clientId", "paypal.clientSecret" })
	@Bean
	public APIContext paypalApiContextDefault() {
		boolean production = false;
		boolean useProperties = false;
		Properties properties = generateProperties(production, useProperties);
		return initApi(properties);
	}


	@ConditionalOnMissingClass("junit.framework.Test")
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value = { "paypal.clientId", "paypal.clientSecret" })
	@Bean
	public APIContext paypalApiContextProperties() {
		boolean production = false;
		boolean useProperties = true;
		Properties properties = generateProperties(production, useProperties);
		return initApi(properties);
	}
	
	@ConditionalOnMissingClass("junit.framework.Test")
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value = { "paypal.clientId", "paypal.clientSecret" , "paypal.production"})
	@Bean
	public APIContext paypalApiContextPro() {
		boolean production = true;
		boolean useProperties = payPalProperties.getProduction();
		Properties properties = generateProperties(production, useProperties);
		return initApi(properties);
	}

	private APIContext initApi(Properties properties) {
		ConfigManager.combineDefaultProperties(properties);
		String token = getAccessToken(properties);
		PayPalResource.initConfig(properties);
		return new APIContext(token);
	}

	private String getAccessToken(Properties properties) {
		String clientID = properties.getProperty("clientId");
		String clientSecret =properties.getProperty("clientSecret");
		Map<String, String> map = new HashMap<String, String>();
		for (final String name: properties.stringPropertyNames()){
			map.put(name, properties.getProperty(name));
	}
		try {
			return new OAuthTokenCredential(clientID, clientSecret,map).getAccessToken();
		} catch (PayPalRESTException e) {
			logger.error("Error recuperando el token del api paypal", e);
			throw new RuntimeException("No se puede configurar el token para el acceso al api de paypal", e);
		}
	}

	private Properties generateProperties(boolean production, boolean useProperties) {
		Properties properties = new Properties();
		properties.put("http.ConnectionTimeOut", "5000");
		properties.put("http.Retry", "1");
		properties.put("http.ReadTimeOut", "30000");
		properties.put("http.MaxConnection", "100");

		properties.put("http.ProxyPort", "8080");
		properties.put("http.ProxyHost", "127.0.0.1");
		properties.put("http.UseProxy", "false");


		properties.put("http.GoogleAppEngine", "false");

		if (production) {
			properties.put("service.EndPoint", "https://api.paypal.com");
		} else {
			properties.put("service.EndPoint", "https://api.sandbox.paypal.com");
		}

		if (useProperties) {
			properties.put("clientId", payPalProperties.getClientId());
			properties.put("clientSecret", payPalProperties.getClientSecret());
		} else {
			properties.put("clientId", "AYSq3RDGsmBLJE-otTkBtM-jBRd1TCQwFf9RGfwddNXWz0uFU9ztymylOhRS");
			properties.put("clientSecret", "EGnHDxD_qRPdaLdZz8iCr8N7_MzF-YHPTkjs6NKYQvQSBngp4PTTVWkPZRbL");
		}

		return properties;

	}

}
