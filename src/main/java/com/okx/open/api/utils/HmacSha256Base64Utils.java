package com.okx.open.api.utils;

import com.okx.open.api.constant.ApiConstants;
import com.okx.open.api.enums.AlgorithmEnum;
import com.okx.open.api.enums.CharsetEnum;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.management.RuntimeErrorException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 
 * @author spb512
 * @date 2022年6月5日 下午5:02:59
 *
 */
public class HmacSha256Base64Utils {

	/**
	 * Signing a Message.<br/>
	 * <p>
	 * using: Hmac SHA256 + base64
	 *
	 * @param timestamp   the number of seconds since Unix Epoch in UTC. Decimal
	 *                    values are allowed. eg: 2018-03-08T10:59:25.789Z
	 * @param method      eg: POST
	 * @param requestPath eg: /orders
	 * @param queryString eg: before=2&limit=30
	 * @param body        json string, eg:
	 *                    {"product_id":"BTC-USD-0309","order_id":"377454671037440"}
	 * @param secretKey   user's secret key eg: E65791902180E9EF4510DB6A77F6EBAE
	 * @return signed string eg: TO6uwdqz+31SIPkd4I+9NiZGmVH74dXi+Fd5X0EzzSQ=
	 * @throws CloneNotSupportedException
	 * @throws InvalidKeyException
	 * @throws UnsupportedEncodingException
	 */
	public static String sign(String timestamp, String method, String requestPath, String queryString, String body,
			String secretKey) throws CloneNotSupportedException, InvalidKeyException, UnsupportedEncodingException {
		if (StringUtils.isEmpty(secretKey) || StringUtils.isEmpty(method)) {
			return ApiConstants.EMPTY;
		}

		String preHash = preHash(timestamp, method, requestPath, queryString, body);
		byte[] secretKeyBytes = secretKey.getBytes(CharsetEnum.UTF_8.charset());
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, AlgorithmEnum.HMAC_SHA256.algorithm());
		Mac mac = (Mac) MAC.clone();
		mac.init(secretKeySpec);
		return Base64.getEncoder().encodeToString(mac.doFinal(preHash.getBytes(CharsetEnum.UTF_8.charset())));
	}

	/**
	 * the prehash string = timestamp + method + requestPath + body .<br/>
	 *
	 * @param timestamp   the number of seconds since Unix Epoch in UTC. Decimal
	 *                    values are allowed. eg: 2018-03-08T10:59:25.789Z
	 * @param method      eg: POST
	 * @param requestPath eg: /orders
	 * @param queryString eg: before=2&limit=30
	 * @param body        json string, eg:
	 *                    {"product_id":"BTC-USD-0309","order_id":"377454671037440"}
	 * @return prehash string eg:
	 *         2018-03-08T10:59:25.789ZPOST/orders?before=2&limit=30{"product_id":"BTC-USD-0309",
	 *         "order_id":"377454671037440"}
	 */
	public static String preHash(String timestamp, String method, String requestPath, String queryString, String body) {
		StringBuilder preHash = new StringBuilder();
		preHash.append(timestamp);
		preHash.append(method.toUpperCase());
		preHash.append(requestPath);
		// get方法
		if (StringUtils.isNotEmpty(queryString)) {
			// 在queryString前面拼接上？
			preHash.append(ApiConstants.QUESTION).append(queryString);
			// 改动了
			// preHash.append(queryString);
		}
		// post方法
		if (StringUtils.isNotEmpty(body)) {
			preHash.append(body);
		}
		return preHash.toString();
	}

	public static Mac MAC;

	static {
		try {
			MAC = Mac.getInstance(AlgorithmEnum.HMAC_SHA256.algorithm());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeErrorException(new Error("Can't get Mac's instance."));
		}
	}
}
