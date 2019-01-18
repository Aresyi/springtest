package com.ydj.springtest.utils;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.spi.MessageTree;
import com.ydj.test.cat.integration.my.MyCatConstants;
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
import java.util.*;
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

	private static final ThreadLocal<Cat.Context> CAT_CONTEXT = new ThreadLocal<>();


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
		String html =  getHtmlContent(url, 5000, 5000, "GBK");
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

		Transaction transaction = Cat.newTransaction(MyCatConstants.CROSS_CONSUMER, url);

		try {
			HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();

			HttpURLConnection.setFollowRedirects(true);
			urlConnection.setConnectTimeout(connectTimeout);
			urlConnection.setReadTimeout(readTimeout);

			urlConnection.setRequestProperty("Connection", "keep-alive");
			urlConnection.setRequestProperty("User-Agent", userAgent);
			urlConnection.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			urlConnection.setRequestProperty("Cookie", cookie);

            addRequestHeadAndCreateConsumerCross(urlConnection);

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), charset));

			String str;
			while ((str = in.readLine()) != null) {
				inputLine.append(str).append("\r\n");
			}

			in.close();
			transaction.setStatus(Transaction.SUCCESS);
		} catch (Exception e) {
			transaction.setStatus(e);
		}finally {
			transaction.complete();
		}

		return inputLine.toString();
	}


	private static class HTTPCatContext implements Cat.Context{

		private Map<String,String> properties = new HashMap<>();

		@Override
		public void addProperty(String key, String value) {
			properties.put(key,value);
		}

		@Override
		public String getProperty(String key) {
			return properties.get(key);
		}
	}


	private static Cat.Context getContext(String root,String parent,String child){
		Cat.Context context = CAT_CONTEXT.get();
		if(context == null){
			context = new HTTPCatContext();
			context.addProperty(Cat.Context.ROOT,root);
			context.addProperty(Cat.Context.PARENT,parent);
			context.addProperty(Cat.Context.CHILD,child);
			CAT_CONTEXT.set(context);
		}
		return context;
	}

	private static void addRequestHeadAndCreateConsumerCross(HttpURLConnection urlConnection){

        MessageTree threadLocalMessageTree = Cat.getManager().getThreadLocalMessageTree();
        String root = threadLocalMessageTree.getRootMessageId();
        String parent =  threadLocalMessageTree.getParentMessageId();
        String child = Cat.getCurrentMessageId();

        System.out.println(String.format("rootId=%s parentId=%s childId=%s",root,parent,child));

        if (StringUtils.isEmpty(root)){
            root = child;
        }

        if (StringUtils.isEmpty(parent)){
            parent = child;
        }
        System.out.println(String.format("rootId=%s parentId=%s childId=%s",root,parent,child));

        urlConnection.setRequestProperty(MyCatConstants.CAT_HTTP_HEADER_ROOT_MESSAGE_ID,root);
        urlConnection.setRequestProperty(MyCatConstants.CAT_HTTP_HEADER_PARENT_MESSAGE_ID,parent);
        urlConnection.setRequestProperty(MyCatConstants.CAT_HTTP_HEADER_CHILD_MESSAGE_ID,child);
        urlConnection.setRequestProperty(MyCatConstants.CAT_HTTP_HEADER_TRACE_MODE,"true");
        urlConnection.setRequestProperty(MyCatConstants.APPLICATION_KEY,SysProperties.getAppName());

        String url = urlConnection.getURL().toString();
        Cat.logEvent(MyCatConstants.CONSUMER_CALL_APP,getSecDomain(url));
        Cat.logEvent(MyCatConstants.CONSUMER_CALL_SERVER,getSecDomain(url));
        Cat.logEvent(MyCatConstants.CONSUMER_CALL_PORT,getPort(url));

        Cat.Context context = getContext(root, parent, child);
        String domain = Cat.getManager().getDomain();

        Cat.logRemoteCallClient(context,domain);
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



    // 二级域名提取
    private static final String RE_TOP = "([a-z0-9][a-z0-9\\-]*?\\.(?:com|cn|net|org|gov|info|la|cc|co)(?:\\.(?:cn|jp))?)";

    public static String getSecDomain(String url) {
        try {
            String s = url.replace("https://","").replace("http://","").replace("www.","");
            s = s.substring(0,s.indexOf("."));
            return  s;
        } catch (Exception e) {
        }
        return "www";
    }

    public static String getPort(String url) {
        try {
            String s = url.substring(url.indexOf("://")+3);
            s = s.substring(s.indexOf(":")+1,s.indexOf("/"));
            return Integer.valueOf(s)+"";
        } catch (Exception e) {
        }
        return "80";
    }



    public static void main(String[] args) {
        String url = "https://www.xyz.qidianla.com/";
        System.out.println(getPort(url));
        System.out.println(getSecDomain(url));

        String sql ="select id from news_basic where uid=? limit 1";
        System.out.println(sql.substring(0,Math.min(25,sql.length())));
    }

}