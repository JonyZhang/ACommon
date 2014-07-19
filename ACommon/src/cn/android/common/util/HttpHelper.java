package cn.android.common.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import cn.android.common.entity.HttpResponseEntity;

/**
 * @author JonyZhang E-mail:xxpgd2@gmail.com
 * @version Create time：2014-6-6
 * @description 
 */
public class HttpHelper {
	
	public static HttpResponseEntity doGet(String url) throws MalformedURLException, IOException {
		HttpResponseEntity entity = HttpUtils.httpGet(url);
		return entity;
	}
	
	public static HttpResponseEntity doPost(String url, Map<String, String> map) throws ClientProtocolException, IOException {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		Set<Entry<String, String>> entrySet = map.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			NameValuePair nameValuePairs = new BasicNameValuePair(entry.getKey(), entry.getValue());
			LogUtils.d("key = " + entry.getKey() + " | value = " + entry.getValue());
			list.add(nameValuePairs);
		}
		return HttpUtils.httpPost(url, new UrlEncodedFormEntity(list, HTTP.UTF_8));
	}
}
