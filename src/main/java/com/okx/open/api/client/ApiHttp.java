package com.okx.open.api.client;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.okx.open.api.bean.funding.HttpResult;
import com.okx.open.api.config.ApiConfiguration;
import com.okx.open.api.constant.ApiConstants;
import com.okx.open.api.exception.ApiException;
import com.okx.open.api.utils.DateUtils;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 
 * @author spb512
 * @date 2022年6月5日 下午5:02:59
 *
 */
public class ApiHttp {

	Logger logger = LoggerFactory.getLogger(getClass());

	private OkHttpClient client;
	private ApiConfiguration config;

	static final MediaType MEDIA_TYPE_JSON = MediaType.Companion.parse("application/json;charset=utf-8");

	public ApiHttp(ApiConfiguration config, OkHttpClient client) {
		this.config = config;
		this.client = client;
	}

	public String get(String url) {
		Request request = new Request.Builder().url(url(url)).build();
		return execute(request);
	}

	public String post(String url, JSONObject params) {
		String body = params.toJSONString();
		RequestBody requestBody = RequestBody.create(body, MEDIA_TYPE_JSON);
		Request request = new Request.Builder().url(url(url)).post(requestBody).build();
		return execute(request);
	}

	public String delete(String url, JSONObject params) {
		String body = params.toJSONString();
		RequestBody requestBody = RequestBody.create(body, MEDIA_TYPE_JSON);
		Request request = new Request.Builder().url(url(url)).delete(requestBody).build();
		return execute(request);
	}

	public String execute(Request request) {
		try {
			Response response = this.client.newCall(request).execute();
			// System.out.println("Response::::::::"+response);
			int status = response.code();
			String bodyString = response.body().string();
			boolean responseIsNotNull = response != null;
			if (this.config.isPrint()) {
				printResponse(status, response.message(), bodyString, responseIsNotNull);
			}
			String message = new StringBuilder().append(response.code()).append(" / ").append(response.message())
					.toString();
			if (response.isSuccessful()) {
				return bodyString;
			} else if (ApiConstants.RESULT_STATUS_ARRAY.contains(status)) {
				HttpResult result = JSON.parseObject(bodyString, HttpResult.class);
				throw new ApiException(result.getCode(), result.getMessage());
			} else {
				throw new ApiException(message);
			}
		} catch (IOException e) {
			throw new ApiException("ApiClient executeSync exception.", e);
		}
	}

	private void printResponse(int status, String message, String body, boolean responseIsNotNull) {
		StringBuilder responseInfo = new StringBuilder();
		responseInfo.append("\n\tResponse").append("(").append(DateUtils.timeToString(null, 4)).append("):");
		if (responseIsNotNull) {
			responseInfo.append("\n\t\t").append("Status: ").append(status);
			responseInfo.append("\n\t\t").append("Message: ").append(message);
			responseInfo.append("\n\t\t").append("Response Body: ").append(body);
		} else {
			responseInfo.append("\n\t\t").append("\n\tRequest Error: response is null");
		}
		logger.info(responseInfo.toString());
	}

	public String url(String url) {
		return new StringBuilder(this.config.getEndpoint()).append(url).toString();
	}
}
