package com.okx.open.api.client;

import com.okx.open.api.config.ApiConfiguration;

/**
 * 
 * @author spb512
 * @date 2022年6月5日 下午5:02:59
 *
 */
public class ApiCredentials {
	/**
	 * The user's secret key provided by OKx.
	 */
	private String apiKey;
	/**
	 * The private key used to sign your request data.
	 */
	private String secretKey;
	/**
	 * The Passphrase will be provided by you to further secure your Api access.
	 */
	private String passphrase;

	public ApiCredentials(ApiConfiguration config) {
		super();
		this.apiKey = config.getApiKey();
		this.secretKey = config.getSecretKey();
		this.passphrase = config.getPassphrase();
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getPassphrase() {
		return passphrase;
	}

	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}
}
