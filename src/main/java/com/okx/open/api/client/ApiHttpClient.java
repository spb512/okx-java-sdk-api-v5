package com.okx.open.api.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.InvalidKeyException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.okx.open.api.config.ApiConfiguration;
import com.okx.open.api.constant.ApiConstants;
import com.okx.open.api.enums.ContentTypeEnum;
import com.okx.open.api.enums.HttpHeadersEnum;
import com.okx.open.api.exception.ApiException;
import com.okx.open.api.utils.DateUtils;
import com.okx.open.api.utils.HmacSha256Base64Utils;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * 
 * @author spb512
 * @date 2022年6月5日 下午5:02:59
 *
 */
public class ApiHttpClient {

	Logger logger = LoggerFactory.getLogger(getClass());

	private final ApiConfiguration config;
	private final ApiCredentials credentials;

	public ApiHttpClient(final ApiConfiguration config, final ApiCredentials credentials) {
		this.config = config;
		this.credentials = credentials;
	}

	/**
	 * Get a ok http 3 client object. <br/>
	 * Declare: <blockquote>
	 * 
	 * <pre>
	 *  1. Set default client args:
	 *         connectTimeout=30s
	 *         readTimeout=30s
	 *         writeTimeout=30s
	 *         retryOnConnectionFailure=true.
	 *  2. Set request headers:
	 *      Content-Type: application/json; charset=UTF-8  (default)
	 *      Cookie: locale=en_US        (English)
	 *      OK-ACCESS-KEY: (Your setting)
	 *      OK-ACCESS-SIGN: (Use your setting, auto sign and add)
	 *      OK-ACCESS-TIMESTAMP: (Auto add)
	 *      OK-ACCESS-PASSPHRASE: Your setting
	 *  3. Set default print api info: false.
	 * </pre>
	 * 
	 * </blockquote>
	 */
	public OkHttpClient client() {
		final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

		if (config.getProxyServerAddress() != null && config.getProxyServerPort() > 0) {
			// 使用代理
			clientBuilder.proxy(new Proxy(Proxy.Type.HTTP,
					new InetSocketAddress(config.getProxyServerAddress(), config.getProxyServerPort())));
		}
		clientBuilder.connectTimeout(this.config.getConnectTimeout(), TimeUnit.SECONDS);
		clientBuilder.readTimeout(this.config.getReadTimeout(), TimeUnit.SECONDS);
		clientBuilder.writeTimeout(this.config.getWriteTimeout(), TimeUnit.SECONDS);
		clientBuilder.retryOnConnectionFailure(this.config.isRetryOnConnectionFailure());
		clientBuilder.addInterceptor((Interceptor.Chain chain) -> {
			final Request.Builder requestBuilder = chain.request().newBuilder();
			final String timestamp = DateUtils.getUnixTime();
			// 打印首行时间戳
//            System.out.println("时间戳timestamp={" + timestamp + "}");
//              设置模拟盘请求头
//            String simulated = "1";
			requestBuilder.headers(this.headers(chain.request(), timestamp));
			final Request request = requestBuilder.build();
			if (this.config.isPrint()) {
				this.printRequest(request, timestamp);
			}
			return chain.proceed(request);
		});
		return clientBuilder.build();
	}

	/**
	 * String simulated
	 * 
	 * @param request
	 * @param timestamp
	 * @return
	 */
	private Headers headers(final Request request, final String timestamp) {
		final Headers.Builder builder = new Headers.Builder();
		builder.add(ApiConstants.ACCEPT, ContentTypeEnum.APPLICATION_JSON.contentType());
		builder.add(ApiConstants.CONTENT_TYPE, ContentTypeEnum.APPLICATION_JSON_UTF8.contentType());
		builder.add(ApiConstants.COOKIE, this.getCookie());

		if (StringUtils.isNotEmpty(this.credentials.getSecretKey())) {
			// 拼接上秘钥，密码，签名和时间戳
			builder.add(HttpHeadersEnum.OK_ACCESS_KEY.header(), this.credentials.getApiKey());
			builder.add(HttpHeadersEnum.OK_ACCESS_SIGN.header(), this.sign(request, timestamp));
			builder.add(HttpHeadersEnum.OK_ACCESS_TIMESTAMP.header(), timestamp);
			builder.add(HttpHeadersEnum.OK_ACCESS_PASSPHRASE.header(), this.credentials.getPassphrase());
		}
		if (this.config.getSimulated() == 1) {
			// 模拟环境标识
			builder.add("x-simulated-trading", "1");
		}

		return builder.build();
	}

	private String getCookie() {
		final StringBuilder cookie = new StringBuilder();
		cookie.append(ApiConstants.LOCALE).append(this.config.getI18n().i18n());
		return cookie.toString();
	}

	private String sign(final Request request, final String timestamp) {
		final String sign;

		try {
			sign = HmacSha256Base64Utils.sign(timestamp, this.method(request), this.requestPath(request),
					this.queryString(request), this.body(request), this.credentials.getSecretKey());
			// System.out.println("签名字符串："+timestamp+this.method(request)+this.requestPath(request)+this.queryString(request)+this.body(request));
		} catch (final IOException e) {
			throw new ApiException("Request get body io exception.", e);
		} catch (final CloneNotSupportedException e) {
			throw new ApiException("Hmac SHA256 Base64 Signature clone not supported exception.", e);
		} catch (final InvalidKeyException e) {
			throw new ApiException("Hmac SHA256 Base64 Signature invalid key exception.", e);
		}
		return sign;
	}

	/**
	 * 返回请求路径url
	 * 
	 * @param request
	 * @return
	 */
	private String url(final Request request) {
		return request.url().toString();
	}

	/**
	 * 将请求方法转变为大写，并返回
	 * 
	 * @param request
	 * @return
	 */
	private String method(final Request request) {
		return request.method().toUpperCase();
	}

	/**
	 * 返回请求路径
	 * 
	 * @param request
	 * @return
	 */
	private String requestPath(final Request request) {
		String url = this.url(request);
		url = url.replace(this.config.getEndpoint(), ApiConstants.EMPTY);
		String requestPath = url;
		if (requestPath.contains(ApiConstants.QUESTION)) {
			requestPath = requestPath.substring(0, url.lastIndexOf(ApiConstants.QUESTION));
		}
		if (this.config.getEndpoint().endsWith(ApiConstants.SLASH)) {
			requestPath = ApiConstants.SLASH + requestPath;
		}
		return requestPath;
	}

	private String queryString(final Request request) {
		final String url = this.url(request);
		request.body();
		// 请求参数为空字符串
		String queryString = ApiConstants.EMPTY;
		// 如果URL中包含？即存在参数的拼接
		if (url.contains(ApiConstants.QUESTION)) {
			queryString = url.substring(url.lastIndexOf(ApiConstants.QUESTION) + 1);
		}
		return queryString;
	}

	private String body(final Request request) throws IOException {
		final RequestBody requestBody = request.body();
		String body = ApiConstants.EMPTY;
		if (requestBody != null) {
			final Buffer buffer = new Buffer();
			requestBody.writeTo(buffer);
			body = buffer.readString(ApiConstants.UTF_8);
		}
		return body;
	}

	private void printRequest(final Request request, final String timestamp) {
		final String method = this.method(request);
		final String requestPath = this.requestPath(request);

		final String queryString = this.queryString(request);

		final String body;
		try {
			body = this.body(request);
		} catch (final IOException e) {
			throw new ApiException("Request get body io exception.", e);
		}
		final StringBuilder requestInfo = new StringBuilder();

		requestInfo.append("\n\tRequest").append("(").append(DateUtils.timeToString(null, 4)).append("):");
		// 拼接Url
		requestInfo.append("\n\t\t").append("Url: ").append(this.url(request));
		requestInfo.append("\n\t\t").append("Method: ").append(method);
		requestInfo.append("\n\t\t").append("Headers: ");
		final Headers headers = request.headers();
		if (headers != null && headers.size() > 0) {
			for (final String name : headers.names()) {
				requestInfo.append("\n\t\t\t").append(name).append(": ").append(headers.get(name));
			}
		}
		requestInfo.append("\n\t\t").append("request body: ").append(body);
		final String preHash = HmacSha256Base64Utils.preHash(timestamp, method, requestPath, queryString, body);
		requestInfo.append("\n\t\t").append("preHash: ").append(preHash);
		logger.info(requestInfo.toString());
	}
}
