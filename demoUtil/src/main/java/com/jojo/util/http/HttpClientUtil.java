package com.jojo.util.http;

import static com.jojo.util.http.HttpConstant.OK;
import static com.jojo.util.http.HttpConstant.OTHER_IO_EXCEPTION;
import static com.jojo.util.http.HttpConstant.READ_TIMEOUT;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {

	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	/**
	 * 连接池管理器
	 */
	private static PoolingHttpClientConnectionManager clientConnectionManager;

	
	/********************************** 静态代码块，初始化区域 ***************************************/
	/**
	 * 初始化连接池管理器
	 */
	static {
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
		registryBuilder.register("http", PlainConnectionSocketFactory.getSocketFactory());
		registryBuilder.register("ssl", SSLConnectionSocketFactory.getSocketFactory());

		clientConnectionManager = new PoolingHttpClientConnectionManager(registryBuilder.build());
		// 最大连接数
		clientConnectionManager.setMaxTotal(200);
		// 每个路由的基础连接数
		clientConnectionManager.setDefaultMaxPerRoute(20);
	}
	
	
	
	
	
	
	/******************************** 方法区 ************************************/
			
	/**
	 * 获取HttpClient，由连接池管理器提供
	 * @param timeOut 
	 * 
	 * @return
	 */
	private static HttpClient getHttpClient(int timeOut) {
		HttpClientBuilder clientBuilder = HttpClients.custom();
		// 从连接池获取
		clientBuilder.setConnectionManager(clientConnectionManager);
		// 设置连接超时时间
		Builder configBuilder = RequestConfig.custom();
		configBuilder.setConnectionRequestTimeout(timeOut * 1000);
		configBuilder.setConnectTimeout(timeOut * 1000);
		configBuilder.setSocketTimeout(timeOut * 1000);
		clientBuilder.setDefaultRequestConfig(configBuilder.build());

		// 请求重试处理，重试3次
		HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				HttpClientContext clientContext = HttpClientContext.adapt(context);
				HttpRequest request = clientContext.getRequest();
				// 如果请求是幂等的，就再次尝试
				if (!(request instanceof HttpEntityEnclosingRequest)) { return true; }
				// 如果服务器丢掉了连接，那么就重试
				else if (exception instanceof NoHttpResponseException) { return true; }
				// 如果已经重试了3次，就放弃
				else if (executionCount >= 3) { return false; }
				// 不要重试SSL握手异常
				else if (exception instanceof SSLHandshakeException) { return false; }
				// 超时
				else if (exception instanceof InterruptedIOException) { return false; }
				// 目标服务器不可达
				else if (exception instanceof UnknownHostException) { return false; }
				// 连接被拒绝
				else if (exception instanceof ConnectTimeoutException) { return false; }
				// ssl握手异常
				else if (exception instanceof SSLException) { return false; }
				// 不属于以上情况，不重试，其实这个 return 写在那两个true后面就好了，留下些异常就当学习
				else { return false; }
			}
		};
		clientBuilder.setRetryHandler(httpRequestRetryHandler);
		return clientBuilder.build();
	}

	
	/**
	 * GET请求，将参数放入URL中
	 * @param paramMap
	 * @return
	 */
	private static String generateUrl(String url, Map<String, String> paramMap) {
		if(null == paramMap || paramMap.isEmpty()) { return url; }
		
		StringBuilder newUrl = new StringBuilder(url);
		try {
			for (Entry<String, String> param : paramMap.entrySet()) {
				// key
				newUrl.append("&").append(param.getKey());
				// value
				newUrl.append("=").append(URLEncoder.encode(param.getValue(), "UTF-8"));
			}
			// 这个catch也就意思意思……
		} catch (UnsupportedEncodingException e) {
			logger.error("生成 GET 请求 URL 时发生异常", e);
		}
		return newUrl.toString().replaceFirst("&", "?");
	}
	
	/**
	 * 设置请求header
	 * @param headerMap
	 * @param httpGet
	 * @return
	 */
	private static void setRequestHeader(Map<String, String> headerMap, HttpMessage httpMessage) {
		if(CollectionUtils.sizeIsEmpty(headerMap)) { return ; }
		
		for(Entry<String, String> header : headerMap.entrySet()) {
			httpMessage.setHeader(header.getKey(), header.getValue());
		}
	}
	
	/**
	 * 设置返回时的header
	 * @param response
	 * @param result
	 */
	private static void setResponseHeader(HttpResponse response, HttpResult result) {
		if(ArrayUtils.isEmpty(response.getAllHeaders())) {
			return ;
		}
		Map<String, String> headerMap = new HashMap<String, String>();
		for (Header header : response.getAllHeaders()) {
			headerMap.put(header.getName(), header.getValue());
		}
		result.setHeaderMap(headerMap);
	}
	
	
	/********************************************* GET *********************************************/
	/**
	 * 根据给定的 url 做GET请求，不带参数，超时时间默认10s
	 * 
	 * @param url
	 *            内部日志会打印该url
	 * @return
	 */
	public static HttpResult doGet(String url) {
		return doGet(url, 10, null, null);
	}
	
	/**
	 * 带参数的GET请求，超时时间默认10s
	 * 
	 * @param url
	 *            内部日志会打印该url
	 * @param paramMap
	 * @return
	 */
	public static HttpResult doGet(String url, Map<String, String> paramMap) {
		return doGet(url, 10, paramMap, null);
	}
	
	/**
	 * 带参数，但无header的GET请求，需手动指定超时时间
	 * 
	 * @param url
	 *            内部日志会打印该url
	 * @param timeOut
	 * @param paramMap
	 * @return
	 */
	public static HttpResult doGet(String url, int timeOut, Map<String, String> paramMap) {
		return doGet(url, timeOut, paramMap, null);
	}
	
	
	public static HttpResult doGet(String url, Map<String, String> headerMap, int timeOut) {
		return doGet(url, timeOut, null, headerMap);
	}
	
	/**
	 * 基础的执行方法，get 请求最后都走这个，如果发生异常则返回null
	 * 
	 * @param url
	 *            内部日志会打印该url
	 * @param timeOut
	 * @param paramMap
	 * @param headerMap
	 * @return
	 */
	public static HttpResult doGet(String url, int timeOut, Map<String, String> paramMap,
			Map<String, String> headerMap) {
		HttpResult result = new HttpResult();
		// 生成新的URL
		url = generateUrl(url, paramMap);
		logger.error("GET请求的Url为：{}", url);

		HttpGet httpGet = new HttpGet(url);

		// 设置Http请求头
		setRequestHeader(headerMap, httpGet);
		// 获取连接
		HttpClient httpClient = getHttpClient(timeOut);
		try {
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity resEntity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			result.setCode(statusCode);
			if (resEntity != null) {
				result.setBody(EntityUtils.toString(resEntity));
				logger.error("GET请求的返回值：{}",result.getBody());
			}

			// 这里其实可以看情况做些东西 
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpGet.releaseConnection();
		}
		return result;
	}



	
	

	
	/******************************************* POST *********************************************/
	public static HttpResult doPostContent(String url, String content, String contentType, int timeOut) {
		HttpResult httpResult = new HttpResult();
		String result = null;
		HttpPost post = new HttpPost(url);
		try {

			if (StringUtils.isNotEmpty(content)) {
				post.setEntity(new StringEntity(content, "UTF-8"));
			}

			// 设置Header
			if (StringUtils.isNotEmpty(contentType)) {
				post.setHeader("Content-Type", contentType);
			}
			
			HttpResponse response = getHttpClient(timeOut).execute(post);

			HttpEntity resEntity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			httpResult.setCode(statusCode);

			setResponseHeader(response, httpResult);

			if (statusCode != OK) {
				logger.error("+++++==>statusCode:[" + statusCode + "],url:" + url + " <==+++++");
				post.abort();
				return httpResult;
			}
			if (resEntity != null) {
				String respBody = EntityUtils.toString(resEntity);
				try {
					result = respBody;
				} catch (Exception e) {
					logger.error("+++++==> respBody:" + respBody + " <==+++++", e);
				}
			}
		} catch (SocketTimeoutException e) {
			httpResult.setCode(READ_TIMEOUT);
			return httpResult;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("+++++==> doPost:" + url + " <==+++++", e);
			httpResult.setCode(OTHER_IO_EXCEPTION);
			return httpResult;
		} finally {
			post.releaseConnection();
		}
		httpResult.setBody(result);
		return httpResult;
	}

	


	public static HttpResult doPostCharSet(String url, Map<String, String> paramsMap, String charSet, int timeout) {
		return doPostCharSet(url, paramsMap, null, charSet, timeout);
	}

	/**
	 * 根据URL发送post请求获取数据,支持传字符集
	 * 
	 * @param url
	 * @param paramsMap
	 * @return
	 */
	public static HttpResult doPostCharSet(String url, Map<String, String> paramsMap, Map<String, String> headerMap,
			String charSet, int timeout) {
		HttpResult httpResult = new HttpResult();
		String result = null;
		HttpPost post = new HttpPost(url);
		try {

			charSet = charSet == null || charSet.trim().equals("") ? "UTF-8" : charSet;
			if (paramsMap != null && paramsMap.size() > 0) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> m : paramsMap.entrySet()) {
					params.add(new BasicNameValuePair(m.getKey(), m.getValue()));
				}
				UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(params, charSet);
				post.setEntity(reqEntity);
			}

			// 设置Header
			if (headerMap != null && !headerMap.isEmpty()) {
				for (Map.Entry<String, String> entry : headerMap.entrySet()) {
					post.setHeader(entry.getKey(), entry.getValue());
				}
			}

			HttpResponse response = getHttpClient(timeout).execute(post);

			HttpEntity resEntity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			httpResult.setCode(statusCode);
			if (resEntity != null) {
				try {
					result = EntityUtils.toString(resEntity);
				} catch (Exception e) {
					logger.error("+++++==> EntityUtils.toString <==+++++", e);
				}
				httpResult.setBody(result);
			}
			if (statusCode != OK) {
				logger.error("+++++==>statusCode:[" + statusCode + "],url:" + url + " <==+++++");
				post.abort();
				return httpResult;
			}
		} catch (SocketTimeoutException e) {
			httpResult.setCode(READ_TIMEOUT);
			return httpResult;
		} catch (IOException e) {
			logger.error("+++++==> doPost:" + url + " <==+++++", e);
			httpResult.setCode(OTHER_IO_EXCEPTION);
			return httpResult;
		} finally {
			post.releaseConnection();
		}
		return httpResult;
	}

	public static HttpResult doPost(String url, Map<String, String> paramsMap, int timeout) {
		return doPost(url, paramsMap, null, timeout);
	}

	/**
	 * 根据URL发送post请求获取数据
	 *
	 * @param url
	 * @param paramsMap
	 * @return
	 */
	public static HttpResult doPost(String url, Map<String, String> paramsMap, Map<String, String> headerMap,
			int timeout) {
		HttpResult httpResult = new HttpResult();
		String result = null;
		HttpPost post = new HttpPost(url);
		try {

			// post.addHeader("Cookie", "JSESSIONID=DCD7BFE23126E4B69ABCC415A6D688AF;
			// Path=/");

			if (paramsMap != null && paramsMap.size() > 0) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> m : paramsMap.entrySet()) {
					params.add(new BasicNameValuePair(m.getKey(), m.getValue()));
				}
				UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(params, "UTF-8");
				post.setEntity(reqEntity);
			}

			// 设置Header
			if (headerMap != null && !headerMap.isEmpty()) {
				for (Map.Entry<String, String> entry : headerMap.entrySet()) {
					post.setHeader(entry.getKey(), entry.getValue());
				}
			}

			HttpResponse response = getHttpClient(timeout).execute(post);

			HttpEntity resEntity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			httpResult.setCode(statusCode);
			if (resEntity != null) {
				try {
					result = EntityUtils.toString(resEntity);
				} catch (Exception e) {
					logger.error("+++++==> EntityUtils.toString <==+++++", e);
				}
				httpResult.setBody(result);
			}
			if (statusCode != OK) {
				logger.error("+++++==>statusCode:[" + statusCode + "],url:" + url + " <==+++++");
				post.abort();
				return httpResult;
			}
		} catch (SocketTimeoutException e) {
			httpResult.setCode(READ_TIMEOUT);
			return httpResult;
		} catch (IOException e) {
			logger.error("+++++==> doPost:" + url + " <==+++++", e);
			httpResult.setCode(OTHER_IO_EXCEPTION);
			return httpResult;
		} finally {
			post.releaseConnection();
		}
		return httpResult;
	}

	/**
	 * 根据URL发送post请求获取数据
	 * 
	 * @param url
	 * @param paramsMap
	 * @return
	 */
	public static HttpResult doPostGetCookie(String url, Map<String, String> paramsMap, int timeout) {
		HttpResult httpResult = new HttpResult();
		String result = null;
		HttpPost post = new HttpPost(url);
		try {
			if (paramsMap != null && paramsMap.size() > 0) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> m : paramsMap.entrySet()) {
					params.add(new BasicNameValuePair(m.getKey(), m.getValue()));
				}
				UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(params, "UTF-8");
				post.setEntity(reqEntity);
			}
			HttpResponse response = getHttpClient(timeout).execute(post);
			Header[] cookie = response.getHeaders("Set-Cookie");

			int statusCode = response.getStatusLine().getStatusCode();
			httpResult.setCode(statusCode);
			if (statusCode != OK) {
				logger.error("+++++==>statusCode:[" + statusCode + "],url:" + url + " <==+++++");
				post.abort();
				return httpResult;
			}
			if (cookie != null) {
				result = cookie[0].toString();
				result = result.split("Set-Cookie: ")[1];
			}

		} catch (SocketTimeoutException e) {
			httpResult.setCode(READ_TIMEOUT);
			return httpResult;
		} catch (IOException e) {
			logger.error("+++++==> doPost:" + url + " <==+++++", e);
			httpResult.setCode(OTHER_IO_EXCEPTION);
			return httpResult;
		} finally {
			post.releaseConnection();
		}
		httpResult.setBody(result);
		return httpResult;
	}

	public static HttpResult doPostUseCookie(String url, Map<String, String> paramsMap, String cookie, int timeout) {
		HttpResult httpResult = new HttpResult();
		String result = null;
		HttpPost post = new HttpPost(url);
		try {
			post.addHeader("Cookie", cookie);
			if (paramsMap != null && paramsMap.size() > 0) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> m : paramsMap.entrySet()) {
					params.add(new BasicNameValuePair(m.getKey(), m.getValue()));
				}
				UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(params, "UTF-8");
				post.setEntity(reqEntity);
			}
			HttpResponse response = getHttpClient(timeout).execute(post);

			HttpEntity resEntity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			httpResult.setCode(statusCode);
			if (statusCode != OK) {
				logger.error("+++++==>statusCode:[" + statusCode + "],url:" + url + " <==+++++");
				post.abort();
				return httpResult;
			}
			if (resEntity != null) {
				String respBody = EntityUtils.toString(resEntity);
				try {
					result = respBody;
				} catch (Exception e) {
					logger.error("+++++==> respBody:" + respBody + " <==+++++", e);
				}
			}
		} catch (SocketTimeoutException e) {
			httpResult.setCode(READ_TIMEOUT);
			return httpResult;
		} catch (IOException e) {
			logger.error("+++++==> doPost:" + url + " <==+++++", e);
			httpResult.setCode(OTHER_IO_EXCEPTION);
			return httpResult;
		} finally {
			post.releaseConnection();
		}
		httpResult.setBody(result);
		return httpResult;
	}

	/**
	 * 访问服务
	 * 
	 * @param url
	 *            地址
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static HttpResult doPostXml(String url, String xml, int timeout) {
		return doPostXml(url, "UTF-8", xml, null, timeout);
	}

	public static HttpResult doPostXml(String url, String xml, Map<String, String> headerMap, int timeout) {
		return doPostXml(url, "UTF-8", xml, headerMap, timeout);
	}

	public static HttpResult doPostXml(String url, String charSet, String xml, int timeout) {
		return doPostXml(url, charSet, xml, null, timeout);
	}

	public static HttpResult doPostXml(String url, String charSet, String xml, Map<String, String> headerMap,
			int timeout) {
		HttpResult httpResult = new HttpResult();
		String result = null;
		HttpPost post = new HttpPost(url);
		// 然后把Soap请求数据添加到PostMethod中
		byte[] b = null;
		InputStream is = null;
		HttpResponse response = null;
		HttpClient httpClient = null;
		try {
			if (headerMap != null && !headerMap.isEmpty()) {
				for (Map.Entry<String, String> entry : headerMap.entrySet()) {
					post.setHeader(entry.getKey(), entry.getValue());
				}
			}
			charSet = charSet == null ? "UTF-8" : charSet;
			b = xml.getBytes(charSet);
			is = new ByteArrayInputStream(b, 0, b.length);
			HttpEntity reqEntity = new InputStreamEntity(is, b.length,
					ContentType.create(ContentType.TEXT_XML.getMimeType(), Charset.forName(charSet)));
			post.setEntity(reqEntity);
			httpClient = getHttpClient(timeout);
			response = httpClient.execute(post);
			HttpEntity resEntity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			httpResult.setCode(statusCode);
			if (resEntity != null) {
				try {
					result = EntityUtils.toString(resEntity);
					httpResult.setBody(result);
				} catch (Exception e) {
					logger.error("+++++==> EntityUtils.toString error <==+++++", e);
				}
			}
			if (statusCode != OK) {
				logger.error("+++++==>statusCode:[" + statusCode + "],url:" + url + " <==+++++");
				post.abort();
				return httpResult;
			}

		} catch (SocketTimeoutException e) {
			httpResult.setCode(READ_TIMEOUT);
			return httpResult;
		} catch (Exception e) {
			logger.error("+++++==> doPostXml:" + url + " <==+++++", e);
			httpResult.setCode(OTHER_IO_EXCEPTION);
			return httpResult;
		} finally {
			post.releaseConnection();
			response = null;
			httpClient = null;
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error("HttpClientUtil doPostXml error", e);
				}
			}
		}

		return httpResult;
	}

	/**
	 * 根据URL发送post请求获取数据 Rest
	 * 
	 * @param url
	 * @return
	 */
	public static HttpResult doPostJson(String url, String json, int timeout) {
		HttpResult httpResult = new HttpResult();
		String result = null;
		HttpPost post = new HttpPost(url);
		try {
			post.addHeader("content-type", "application/json");
			if (StringUtils.isNotEmpty(json)) {
				StringEntity myEntity = new StringEntity(json, "UTF-8");
				post.setEntity(myEntity);
			}
			HttpResponse response = getHttpClient(timeout).execute(post);

			HttpEntity resEntity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			httpResult.setCode(statusCode);
			Map<String, String> headerMap = new HashMap<String, String>();
			for (Header header : response.getAllHeaders()) {
				headerMap.put(header.getName(), header.getValue());
			}
			httpResult.setHeaderMap(headerMap);
			if (resEntity != null) {
				try {
					result = EntityUtils.toString(resEntity);
				} catch (Exception e) {
					logger.error("+++++==> EntityUtils.toString <==+++++", e);
				}
				httpResult.setBody(result);
			}

			if (statusCode != OK) {
				logger.error("+++++==>statusCode:[" + statusCode + "],url:" + url + " <==+++++");
				post.abort();
				return httpResult;
			}

		} catch (SocketTimeoutException e) {
			httpResult.setCode(READ_TIMEOUT);
			return httpResult;
		} catch (IOException e) {
			logger.error("+++++==> doPostJson:" + url + " <==+++++", e);
			httpResult.setCode(OTHER_IO_EXCEPTION);
			return httpResult;
		} finally {
			post.releaseConnection();
		}
		return httpResult;
	}

	public static HttpResult doPostJson(String url, String json, String charset, int timeout) {
		HttpResult httpResult = new HttpResult();
		String result = null;
		HttpPost post = new HttpPost(url);
		try {
			post.addHeader("content-type", "application/json");
			if (StringUtils.isNotEmpty(json)) {
				StringEntity myEntity = new StringEntity(json, charset);
				post.setEntity(myEntity);
			}
			HttpResponse response = getHttpClient(timeout).execute(post);

			HttpEntity resEntity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			httpResult.setCode(statusCode);
			Map<String, String> headerMap = new HashMap<String, String>();
			for (Header header : response.getAllHeaders()) {
				headerMap.put(header.getName(), header.getValue());
			}
			httpResult.setHeaderMap(headerMap);
			if (resEntity != null) {
				try {
					result = EntityUtils.toString(resEntity);
				} catch (Exception e) {
					logger.error("+++++==> EntityUtils.toString <==+++++", e);
				}
				httpResult.setBody(result);
			}

			if (statusCode != OK) {
				logger.error("+++++==>statusCode:[" + statusCode + "],url:" + url + " <==+++++");
				post.abort();
				return httpResult;
			}

		} catch (SocketTimeoutException e) {
			httpResult.setCode(READ_TIMEOUT);
			return httpResult;
		} catch (IOException e) {
			logger.error("+++++==> doPostJson:" + url + " <==+++++", e);
			httpResult.setCode(OTHER_IO_EXCEPTION);
			return httpResult;
		} finally {
			post.releaseConnection();
		}
		return httpResult;
	}

	

	/**
	 * 发送文件到文件服务器
	 * 
	 * @param filetype
	 *            文件类型 1图片 2音频 3视频 4安装文件 5以上自定义
	 * @param data
	 * @param typelimit
	 *            文件类型 填写允许的文件类型后缀,多个则逗号隔开
	 * @param sizelimit
	 *            文件大小 单位byte,默认无限制
	 * @return
	 */
	public static HttpResult uploadToFileStore(String url, byte[] data, String fileName, String filetype,
			String typelimit, String sizelimit, int timeout) {
		HttpResult httpResult = new HttpResult();
		String result = null;
		if (data == null || data.length <= 0) {
			return null;
		}
		File tmpFile = null;
		HttpPost post = new HttpPost(url);
		try {
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null,
					Charset.forName("UTF-8"));
			// 1图片 2音频 3视频 4安装文件 5以上自定义
			if (StringUtils.isNotEmpty(filetype)) {
				reqEntity.addPart("filetype", new StringBody(filetype));
			}
			// 填写允许的文件类型后缀,多个则逗号隔开
			if (StringUtils.isNotEmpty(typelimit)) {
				reqEntity.addPart("typelimit", new StringBody(typelimit));
			}
			// 单位byte,默认无限制
			if (StringUtils.isNotEmpty(sizelimit)) {
				reqEntity.addPart("sizelimit", new StringBody(sizelimit));
			}
			if (StringUtils.isEmpty(fileName)) {
				fileName = "tmp.jpeg";
			}
			tmpFile = getFileFromBytes(data, "/tmp/" + fileName);
			if (tmpFile != null && tmpFile.length() > 0) {
				reqEntity.addPart("file", new FileBody(tmpFile));
			}

			post.setEntity(reqEntity);

			HttpResponse response = getHttpClient(timeout).execute(post);
			int statusCode = response.getStatusLine().getStatusCode();
			httpResult.setCode(statusCode);
			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				String respBody = EntityUtils.toString(resEntity);
				try {
					result = respBody;
				} catch (Exception e) {
					logger.error("+++++==> respBody:" + respBody + " <==+++++", e);
				}
			}
		} catch (SocketTimeoutException e) {
			httpResult.setCode(READ_TIMEOUT);
			return httpResult;
		} catch (IOException e) {
			logger.error("+++++==> uploadToFileStore:" + url + " <==+++++", e);
			httpResult.setCode(OTHER_IO_EXCEPTION);
			return httpResult;
		} finally {
			post.releaseConnection();
			if (tmpFile != null) {
				tmpFile.delete();
			}
		}
		httpResult.setBody(result);
		return httpResult;
	}

	/**
	 * 流转文件
	 * 
	 * @param b
	 * @param outputFile
	 * @return
	 */
	private static File getFileFromBytes(byte[] b, String outputFile) {
		File ret = null;
		if (null == b || StringUtils.isEmpty(outputFile)) {
			return null;
		}

		BufferedOutputStream stream = null;
		try {
			ret = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(ret);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			logger.error("~~~", e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					logger.error("~~~~", e);
				}
			}
		}
		return ret;
	}
	
}
