package cn.android.common.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;
import cn.android.common.constant.HttpConstants;
import cn.android.common.entity.HttpRequestEntity;
import cn.android.common.entity.HttpResponseEntity;

/**
 * HttpUtils
 * <ul>
 * <strong>Http get</strong>
 * <li>{@link #httpGet(HttpRequestEntity)}</li>
 * <li>{@link #httpGet(String)}</li>
 * <li>{@link #httpGetString(String)}</li>
 * </ul>
 * <ul>
 * <strong>Http post</strong>
 * <li>{@link #httpPost(HttpRequestEntity)}</li>
 * <li>{@link #httpPost(String)}</li>
 * <li>{@link #httpPostString(String)}</li>
 * <li>{@link #httpPostString(String, Map)}</li>
 * </ul>
 * <ul>
 * <strong>Http params</strong>
 * <li>{@link #getUrlWithParas(String, Map)}</li>
 * <li>{@link #getUrlWithValueEncodeParas(String, Map)}</li>
 * <li>{@link #joinParas(Map)}</li>
 * <li>{@link #joinParasWithEncodedValue(Map)}</li>
 * <li>{@link #appendParaToUrl(String, String, String)}</li>
 * <li>{@link #parseGmtTime(String)}</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-12
 */
public class HttpUtils {
	public static final String TAG = "HttpUtils";

	private static final SimpleDateFormat GMT_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
    /** url and para separator **/
    public static final String URL_AND_PARA_SEPARATOR = "?";
    /** parameters separator **/
    public static final String PARAMETERS_SEPARATOR   = "&";
    /** paths separator **/
    public static final String PATHS_SEPARATOR        = "/";
    /** equal sign **/
    public static final String EQUAL_SIGN             = "=";

    /**
     * http get
     * <ul>
     * <li>use gzip compression default</li>
     * <li>use bufferedReader to improve the reading speed</li>
     * </ul>
     * 
     * @param request
     * @return the response of the url, if null represents http error
     * @throws MalformedURLException, IOException 
     */
    public static HttpResponseEntity httpGet(HttpRequestEntity request) throws MalformedURLException, IOException {
        if (request == null) {
            return null;
        }

		BufferedReader input = null;
		HttpURLConnection con = null;
		URL url = new URL(request.getUrl());
		HttpResponseEntity response = new HttpResponseEntity(request.getUrl());
		// default gzip encode
		con = (HttpURLConnection) url.openConnection();
		setURLConnection(request, con);
		input = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String s;
		while ((s = input.readLine()) != null) {
			sb.append(s).append("\n");
		}
		response.setResponseBody(sb.toString());
		setHttpResponse(con, response);
		return response;
    }

    /**
     * http get
     * 
     * @param httpUrl
     * @return the response of the url, if null represents http error
     * @throws IOException 
     * @throws MalformedURLException 
     * @see HttpUtils#httpGet(HttpRequestEntity)
     */
    public static HttpResponseEntity httpGet(String httpUrl) throws MalformedURLException, IOException {
        return httpGet(new HttpRequestEntity(httpUrl));
    }

    /**
     * http get
     * 
     * @param httpUrl
     * @return the content of the url, if null represents http error
     * @throws IOException 
     * @throws MalformedURLException 
     * @see HttpUtils#httpGet(HttpRequestEntity)
     */
    public static String httpGetString(String httpUrl) throws MalformedURLException, IOException {
        HttpResponseEntity response = httpGet(new HttpRequestEntity(httpUrl));
        return response == null ? null : response.getResponseBody();
    }

    /**
     * http post
     * <ul>
     * <li>use gzip compression default</li>
     * <li>use bufferedReader to improve the reading speed</li>
     * </ul>
     * 
     * @param httpUrl
     * @param paras
     * @return the response of the url, if null represents http error
     * @throws ProtocolException 
     * @throws MalformedURLException 
     */
    public static HttpResponseEntity httpPost(HttpRequestEntity request) throws ProtocolException, MalformedURLException, IOException {
        if (request == null) {
            return null;
        }

		BufferedReader input = null;
		HttpURLConnection con = null;
		URL url = new URL(request.getUrl());
		HttpResponseEntity response = new HttpResponseEntity(request.getUrl());
		con = (HttpURLConnection) url.openConnection();
		setURLConnection(request, con);
		con.setConnectTimeout(20000);
		con.setReadTimeout(20000);
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		String paras = request.getParas();
		if (!StringUtils.isEmpty(paras)) {
			con.getOutputStream().write(paras.getBytes());
		}
		input = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String s;
		while ((s = input.readLine()) != null) {
			sb.append(s).append("\n");
		}
		LogUtils.d(TAG, "http post response = " + sb.toString());
		response.setResponseBody(sb.toString());
		setHttpResponse(con, response);
		return response;
    }

    /**
     * http post
     * 
     * @param httpUrl
     * @return the response of the url, if null represents http error
     * @throws IOException 
     * @throws MalformedURLException 
     * @throws ProtocolException 
     * @see HttpUtils#httpPost(HttpRequestEntity)
     */
    public static HttpResponseEntity httpPost(String httpUrl) throws ProtocolException, MalformedURLException, IOException {
        return httpPost(new HttpRequestEntity(httpUrl));
    }
    
	public static HttpResponseEntity httpPost(String url, HttpEntity entity) throws ClientProtocolException, IOException {

		HttpResponseEntity response = new HttpResponseEntity();
		response.setUrl(url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(entity);

		org.apache.http.HttpResponse resp = httpClient.execute(httpPost);
		int statusCode = resp.getStatusLine().getStatusCode();
		response.setResponseCode(statusCode);
		LogUtils.d(TAG, "statusCode = " + statusCode);
		if (statusCode == HttpURLConnection.HTTP_OK) {
			String temp = EntityUtils.toString(resp.getEntity(), HTTP.UTF_8);
			LogUtils.d(TAG, "http post response = " + temp);
			response.setResponseBody(temp);
		}
		return response;
	}

	public static HttpResponseEntity httpPost(String url, List<NameValuePair> nameValuePairs) throws ClientProtocolException, IOException {

		HttpResponseEntity response = new HttpResponseEntity();
		response.setUrl(url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));

		org.apache.http.HttpResponse resp = httpClient.execute(httpPost);
		int statusCode = resp.getStatusLine().getStatusCode();
		response.setResponseCode(statusCode);
		LogUtils.d(TAG, "statusCode = " + statusCode);
		if (statusCode == HttpURLConnection.HTTP_OK) {
			String temp = EntityUtils.toString(resp.getEntity(), HTTP.UTF_8);
			LogUtils.d(TAG, "http post response = " + temp);
			response.setResponseBody(temp);
		}
		return response;
	}

    /**
     * http post
     * 
     * @param httpUrl
     * @return the content of the url, if null represents http error
     * @throws IOException 
     * @throws MalformedURLException 
     * @throws ProtocolException 
     * @see HttpUtils#httpPost(HttpRequestEntity)
     */
    public static String httpPostString(String httpUrl) throws ProtocolException, MalformedURLException, IOException {
        HttpResponseEntity response = httpPost(new HttpRequestEntity(httpUrl));
        return response == null ? null : response.getResponseBody();
    }

    /**
     * http post
     * 
     * @param httpUrl
     * @param parasMap paras map, key is para name, value is para value. will be transfrom to String by
     * {@link HttpUtils#joinParas(Map)}
     * @return the content of the url, if null represents http error
     * @throws IOException 
     * @throws MalformedURLException 
     * @throws ProtocolException 
     * @see HttpUtils#httpPost(HttpRequestEntity)
     */
    public static String httpPostString(String httpUrl, Map<String, String> parasMap) throws ProtocolException, MalformedURLException, IOException {
        HttpResponseEntity response = httpPost(new HttpRequestEntity(httpUrl, parasMap));
        return response == null ? null : response.getResponseBody();
    }

    /**
     * join url and paras
     * 
     * <pre>
     * getUrlWithParas(null, {(a, b)})                        =   "?a=b";
     * getUrlWithParas("baidu.com", {})                       =   "baidu.com";
     * getUrlWithParas("baidu.com", {(a, b), (i, j)})         =   "baidu.com?a=b&i=j";
     * getUrlWithParas("baidu.com", {(a, b), (i, j), (c, d)}) =   "baidu.com?a=b&i=j&c=d";
     * </pre>
     * 
     * @param url url
     * @param parasMap paras map, key is para name, value is para value
     * @return if url is null, process it as empty string
     */
    public static String getUrlWithParas(String url, Map<String, String> parasMap) {
        StringBuilder urlWithParas = new StringBuilder(StringUtils.isEmpty(url) ? "" : url);
        String paras = joinParas(parasMap);
        if (!StringUtils.isEmpty(paras)) {
            urlWithParas.append(URL_AND_PARA_SEPARATOR).append(paras);
        }
        return urlWithParas.toString();
    }

    /**
     * join url and encoded paras
     * 
     * @param url
     * @param parasMap
     * @return
     * @see #getUrlWithParas(String, Map)
     * @see StringUtils#utf8Encode(String)
     */
    public static String getUrlWithValueEncodeParas(String url, Map<String, String> parasMap) {
        StringBuilder urlWithParas = new StringBuilder(StringUtils.isEmpty(url) ? "" : url);
        String paras = joinParasWithEncodedValue(parasMap);
        if (!StringUtils.isEmpty(paras)) {
            urlWithParas.append(URL_AND_PARA_SEPARATOR).append(paras);
        }
        return urlWithParas.toString();
    }

    /**
     * join paras
     * 
     * @param parasMap paras map, key is para name, value is para value
     * @return join key and value with {@link #EQUAL_SIGN}, join keys with {@link #PARAMETERS_SEPARATOR}
     */
    public static String joinParas(Map<String, String> parasMap) {
        if (parasMap == null || parasMap.size() == 0) {
            return null;
        }

        StringBuilder paras = new StringBuilder();
        Iterator<Map.Entry<String, String>> ite = parasMap.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>)ite.next();
            paras.append(entry.getKey()).append(EQUAL_SIGN).append(entry.getValue());
            if (ite.hasNext()) {
                paras.append(PARAMETERS_SEPARATOR);
            }
        }
        return paras.toString();
    }

    /**
     * join paras with encoded value
     * 
     * @param parasMap
     * @return
     * @see #joinParas(Map)
     * @see StringUtils#utf8Encode(String)
     */
    public static String joinParasWithEncodedValue(Map<String, String> parasMap) {
        StringBuilder paras = new StringBuilder("");
        if (parasMap != null && parasMap.size() > 0) {
            Iterator<Map.Entry<String, String>> ite = parasMap.entrySet().iterator();
            try {
                while (ite.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>)ite.next();
                    paras.append(entry.getKey()).append(EQUAL_SIGN).append(StringUtils.utf8Encode(entry.getValue()));
                    if (ite.hasNext()) {
                        paras.append(PARAMETERS_SEPARATOR);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return paras.toString();
    }

    /**
     * append a key and value pair to url
     * 
     * @param url
     * @param paraKey
     * @param paraValue
     * @return
     */
    public static String appendParaToUrl(String url, String paraKey, String paraValue) {
        if (StringUtils.isEmpty(url)) {
            return url;
        }

        StringBuilder sb = new StringBuilder(url);
        if (!url.contains(URL_AND_PARA_SEPARATOR)) {
            sb.append(URL_AND_PARA_SEPARATOR);
        } else {
            sb.append(PARAMETERS_SEPARATOR);
        }
        return sb.append(paraKey).append(EQUAL_SIGN).append(paraValue).toString();
    }

    /**
     * parse gmt time to long
     * 
     * @param gmtTime likes Thu, 11 Apr 2013 10:20:30 GMT
     * @return -1 represents exception otherwise time in  milliseconds
     */
    public static long parseGmtTime(String gmtTime) {
        try {
            return GMT_FORMAT.parse(gmtTime).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * set HttpRequest to HttpURLConnection
     * 
     * @param request source request
     * @param urlConnection destin url connection
     */
    private static void setURLConnection(HttpRequestEntity request, HttpURLConnection urlConnection) {
        if (request == null || urlConnection == null) {
            return;
        }

        Set<Entry<String, String>> proSet = request.getRequestPropertys().entrySet();
        for (Map.Entry<String, String> entry : proSet) {
            urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
        }
        if (request.getConnectTimeout() >= 0) {
            urlConnection.setConnectTimeout(request.getConnectTimeout());
        }
        if (request.getReadTimeout() >= 0) {
            urlConnection.setReadTimeout(request.getReadTimeout());
        }
    }

    /**
     * set HttpURLConnection to HttpResponse
     * 
     * @param urlConnection source url connection
     * @param response destin response
     */
    private static void setHttpResponse(HttpURLConnection urlConnection, HttpResponseEntity response) {
        if (response == null || urlConnection == null) {
            return;
        }
        try {
            response.setResponseCode(urlConnection.getResponseCode());
        } catch (IOException e) {
            response.setResponseCode(-1);
        }
        response.setResponseHeader(HttpConstants.EXPIRES, urlConnection.getHeaderField("Expires"));
        response.setResponseHeader(HttpConstants.CACHE_CONTROL, urlConnection.getHeaderField("Cache-Control"));
    }
    
    /**
	 * http post params and image
	 * @param url
	 * @param imageKey
	 * @param photoData
	 * @return
	 * @throws MalformedURLException
	 * @throws SocketTimeoutException
	 * @throws IOException
	 */
	public static String HttpPostParamsAndImage(String url, String imageKey, Map<String, String> photoData) throws MalformedURLException, SocketTimeoutException, IOException {
		HttpURLConnection conn = null;
		URL myUrl = new URL(url);
		conn = (HttpURLConnection) myUrl.openConnection();
		conn.setConnectTimeout(20000);
		conn.setReadTimeout(20000);
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Charset", "UTF-8");
		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + HttpPostImageHeader.BOUNDARY);

		DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
		if (photoData != null && photoData.size() > 0) {
			Set<Map.Entry<String, String>> entrySet = photoData.entrySet();
			Iterator<Map.Entry<String, String>> it = entrySet.iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = it.next();
				if (entry.getKey().equals(imageKey)) {
					if (entry.getValue() != null) {
						uploadImage(dos, entry);
					}
				} else {
//					LogUtils.d(TAG, "key = " + entry.getKey() + " | value = " + entry.getValue());
					writeParams(dos, entry);
				}
			}
			dos.writeBytes(HttpPostImageHeader.genEndLine());
			dos.flush();
		}
		BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String s;
		while ((s = input.readLine()) != null) {
			sb.append(s).append("\n");
		}
		dos.close();
		LogUtils.d(TAG, "http post params and image, response data = " + sb.toString());
		return sb.toString();
	}

	/**
	 * Write the params to server
	 * @param dos
	 * @param entry
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	private static void writeParams(DataOutputStream dos, Entry<String, String> entry)
			throws IOException, UnsupportedEncodingException {
		dos.writeBytes(HttpPostImageHeader.genHyphensLine());
		dos.writeBytes("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + HttpPostImageHeader.END);
		dos.writeBytes(HttpPostImageHeader.END);
		dos.write(((String)entry.getValue()).getBytes("UTF-8"));
		dos.writeBytes(HttpPostImageHeader.END);
	}

	/**
	 * Upload image to server
	 * @param dos
	 * @param entry
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static void uploadImage(DataOutputStream dos, Entry<String, String> entry)
			throws IOException, FileNotFoundException {
		dos.writeBytes(HttpPostImageHeader.genHyphensLine());
		dos.writeBytes("Content-Disposition: form-data; name=\""+ entry.getKey() +"\"; filename=\"" + entry.getValue().substring(entry.getValue().lastIndexOf("/") + 1)+ "\"" + HttpPostImageHeader.END);
		dos.writeBytes("Content-Type: image/jpeg" + HttpPostImageHeader.END);
		dos.writeBytes(HttpPostImageHeader.END);
		FileInputStream fis = new FileInputStream(entry.getValue());
		byte[] buffer = new byte[8192]; // 8k
		int count = 0;
		while ((count = fis.read(buffer)) != -1) {
			dos.write(buffer, 0, count);
		}
		dos.writeBytes(HttpPostImageHeader.END);
		fis.close();
	}
	
	public static class HttpPostImageHeader {
		public static final String END = "\r\n";
		public static final String TWO_HYPHENS = "--";
		public static final String BOUNDARY = "----" + UUID.randomUUID().toString();
		
		public static final String genHyphensLine() {
			return TWO_HYPHENS + BOUNDARY + END;
		}
		
		public static final String genEndLine() {
			return TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + END;
		}
	}
}
