package edu.cuit.hzhspace.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Zhonghua Hu
 *
 */
public class AclHttpConnection {

	/**
	 * @param optUrl
	 * @return
	 * @throws IOException
	 */
	public static String httpPost(String optUrl) throws IOException {
		URL url = new URL(optUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		InputStream inputStream = connection.getInputStream();
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		StringBuffer sb = new StringBuffer();
		try {
			connection.connect();
			//对应的字符编码转换  
			BufferedReader bufferedReader = new BufferedReader(reader);
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				sb.append(str);
			}
		} finally {
			reader.close();
			connection.disconnect();
		}
		return sb.toString();
	}

	public static void main(String[] args) {

	}
}
