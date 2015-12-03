package org.paypal.autoconfigure;

import java.util.Properties;

import javax.management.RuntimeErrorException;

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
	
	
	@ConditionalOnClass(Test.class)
	@ConditionalOnMissingBean
	@Bean
	public APIContext paypalApiContextTest(){
		Properties properties =new Properties();
		
		PayPalResource.initConfig(properties);
		
	    String accessToken = getAccessToken();

	    APIContext apiContext = new APIContext(accessToken);
		return apiContext;
	}
	
	@ConditionalOnMissingClass("junit.framework.Test")
	@ConditionalOnMissingBean
	@ConditionalOnProperty(matchIfMissing=true, prefix="paypal")
	@Bean
	public APIContext paypalApiContextLocal() {
		Properties properties =new Properties();
		configurePropertiesLocal(properties);
		
		PayPalResource.initConfig(properties);
		
	    String accessToken = getAccessToken();

	    APIContext apiContext = new APIContext(accessToken);
		return apiContext;
	}
	
	@ConditionalOnMissingClass("junit.framework.Test")
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value={"paypal.clientId", "paypal.clientSecret"})
	@Bean
	public APIContext paypalApiContextPro(){
		Properties properties =new Properties();
		configurePropertiesPro(properties);
		
		PayPalResource.initConfig(properties);
		
	    String accessToken = getAccessToken();

	    APIContext apiContext = new APIContext(accessToken);
		return apiContext;
	}
	
	
	private void configurePropertiesPro(Properties properties) {
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
		
		properties.put("service.EndPoint", "https://api.paypal.com");

		properties.put("clientId", payPalProperties.getClientId());
		properties.put("clientSecret", payPalProperties.getClientSecret());

	}

	private void configurePropertiesLocal(Properties properties) {
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
		
		properties.put("service.EndPoint", "https://api.sandbox.paypal.com");

		properties.put("clientId", "AYSq3RDGsmBLJE-otTkBtM-jBRd1TCQwFf9RGfwddNXWz0uFU9ztymylOhRS");
		properties.put("clientSecret", "EGnHDxD_qRPdaLdZz8iCr8N7_MzF-YHPTkjs6NKYQvQSBngp4PTTVWkPZRbL");

	}

	public String getAccessToken()  {
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
}
