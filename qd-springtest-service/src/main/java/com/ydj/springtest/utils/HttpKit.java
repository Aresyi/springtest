package com.ydj.springtest.utils;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 *
 * @author : Ares.yi
 * @createTime : 2014-11-10 上午11:13:42
 * @version : 1.0
 * @description :
 *
 */
public class HttpKit {
	
	
	/**
	 * 根据网址url获取网页内容
	 * @param url
	 * @param connectTimeout
	 * @param readTimeout
	 * @param charset
	 * 
	 * @return
	 *
	 * @author : Ares.yi
	 * @createTime : 2017年4月27日 下午2:15:48
	 */
	public static String getHtmlContent(String url, int connectTimeout,
			int readTimeout, String charset) {
		return getHtmlContent(url, connectTimeout, readTimeout, charset,
				"", "");
	}

	/**
	 * 根据网址url获取网页内容
	 * @param url
	 * 
	 * @return
	 *
	 * @author : Ares.yi
	 * @createTime : 2017年4月27日 下午2:15:48
	 */
	public static String getHtmlContent(String url,String invoker) {

		Transaction t = Cat.newTransaction("RPC", "getHtmlContent");
		Cat.logEvent("RPC.Type", "Http", Message.SUCCESS, url);
		Cat.logEvent("RPC.Invoker", invoker);


		String html = null;
		try {
			html = getHtmlContent(url, 5000, 5000, "GBK");
			t.setStatus(Transaction.SUCCESS);
		} catch (Exception e) {
			Cat.logError(e);
		} finally {
			t.complete();
		}

		return html;
	}

	
	/**
	 * 根据网址url获取网页内容
	 * @param url
	 * @param connectTimeout
	 * @param readTimeout
	 * @param charset
	 * @param userAgent
	 * @param cookie
	 * 
	 * @return
	 *
	 * @author : Ares.yi
	 * @createTime : 2017年4月27日 下午2:15:48
	 */
	public static String getHtmlContent(String url, int connectTimeout,
			int readTimeout, String charset, String userAgent, String cookie) {

		if (url == null
				|| (!url.startsWith("https://") && !url.startsWith("http://"))) {
			return "";
		}

		StringBuffer inputLine = new StringBuffer();

		try {
			HttpURLConnection urlConnection = (HttpURLConnection) new URL(url)
					.openConnection();

			HttpURLConnection.setFollowRedirects(true);
			urlConnection.setConnectTimeout(connectTimeout);
			urlConnection.setReadTimeout(readTimeout);

			urlConnection.setRequestProperty("Connection", "keep-alive");
			urlConnection.setRequestProperty("User-Agent", userAgent);
			urlConnection
					.setRequestProperty("Accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			urlConnection.setRequestProperty("Cookie", cookie);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream(), charset));

			String str;
			while ((str = in.readLine()) != null)
				inputLine.append(str).append("\r\n");

			in.close();
		} catch (Exception e) {
			// e.printStackTrace();
			// System.err.println(url);
		}

		return inputLine.toString();

	}

	/**
	 * POST请求
	 * <p>
	 * 注意：若url后有参数，且含特殊字符，需进行URLEncoder
	 * @param url
	 * @param parameterMap
	 * @param charSet
	 * @return
	 *
	 * @author : Ares.yi
	 * @createTime : 2017年4月27日 下午2:15:48
	 */
	public static String postRequest(String url,
			Map<String, String> parameterMap, String charSet)
			throws UnsupportedEncodingException {
		CloseableHttpClient client = HttpClients.createDefault();

		HttpPost httpPost = new HttpPost(url);

		UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(getParam(parameterMap), charSet);
		httpPost.setEntity(postEntity);

		httpPost.addHeader("HOST", "sec.1688.com");
		httpPost.addHeader("User-Agent", "");
		httpPost.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpPost.addHeader("Cookie", "");

		MyLog.logInfo("request line:" + httpPost.getRequestLine());

		try {
			// 执行post请求
			HttpResponse httpResponse = client.execute(httpPost);

			Header header = httpResponse.getFirstHeader("Location");

			if (header != null && StringUtils.isNotEmpty(header.getValue())) {
				MyLog.logInfo("location:" + header.getValue());
				return "SUCCESS";
			} else {
				String html = printResponse(httpResponse);

				return html;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
			}
		}

		return "";
	}

	/**
	 * 包装参数
	 * @param parameterMap
	 * @return
	 *
	 * @author : Ares.yi
	 * @createTime : 2017年4月27日 下午2:15:48
	 */
	@SuppressWarnings("rawtypes")
	public static List<NameValuePair> getParam(Map parameterMap) {
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		Iterator it = parameterMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry parmEntry = (Entry) it.next();
			param.add(new BasicNameValuePair((String) parmEntry.getKey(),
					(String) parmEntry.getValue()));
		}
		return param;
	}

	/**
	 * 打印请求响应信息
	 * 
	 * @param httpResponse
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 *
	 * @author : Ares.yi
	 * @createTime : 2017年4月27日 下午2:15:15
	 */
	public static String printResponse(HttpResponse httpResponse)
			throws ParseException, IOException {
		// 获取响应消息实体
		HttpEntity entity = httpResponse.getEntity();
		// 响应状态
		MyLog.logInfo("status:" + httpResponse.getStatusLine());
		MyLog.logInfo("headers:");
		HeaderIterator iterator = httpResponse.headerIterator();
		while (iterator.hasNext()) {
			MyLog.logInfo("\t" + iterator.next());
		}
		// 判断响应实体是否为空
		if (entity != null) {
			String responseString = EntityUtils.toString(entity);
			MyLog.logInfo("response length:" + responseString.length());
			MyLog.logInfo("response content:"+ responseString.replace("\r\n", ""));
			return responseString;
		}

		return "";
	}

}