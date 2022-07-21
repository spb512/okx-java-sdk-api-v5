package com.okx.open.api.test.market.data;

import com.okx.open.api.config.ApiConfiguration;
import com.okx.open.api.enums.I18nEnum;
import com.okx.open.api.test.BaseTests;

/**
 * 
 * @author spb512
 * @date 2022年6月5日 下午5:02:59
 *
 */
public class MarketDataApiBaseTest extends BaseTests {
	public ApiConfiguration config() {
		ApiConfiguration config = new ApiConfiguration();

		config.setEndpoint("https://www.okx.com/");

		config.setApiKey("");
		config.setSecretKey("");
		config.setPassphrase("");

		config.setPrint(true);
		/* config.setI18n(I18nEnum.SIMPLIFIED_CHINESE); */
		config.setI18n(I18nEnum.ENGLISH);
		return config;
	}

}
