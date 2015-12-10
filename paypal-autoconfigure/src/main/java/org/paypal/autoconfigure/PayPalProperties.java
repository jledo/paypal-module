package org.paypal.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "paypal")
public class PayPalProperties {
	private String clientId;
	private String clientSecret;
	private Boolean production;
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public Boolean getProduction() {
		return production;
	}
	public void setProduction(Boolean production) {
		this.production = production;
	}
	
	
}
