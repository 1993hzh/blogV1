package edu.cuit.hzhspace.util;

import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * 系统配置文件
 * @author huitang
 */
public class SystemConf {
	private static Map<String, String> confMap = new HashMap<String, String>();
	public static boolean isWindos = "windows".equals(System.getProperties().get("sun.desktop"));
	public static String confFileDir;//配置文件加载目录
	static {
		String filePath = Thread.currentThread().getContextClassLoader().getResource("configuration.properties")
				.getPath();
		filePath = filePath.replaceAll("%20", " ");
		Properties prop = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);// 属性文件输入流

			prop.load(fis);// 将属性文件流装载到Properties对象中
			fis.close();// 关闭流
		} catch (Exception e) {
			//e.printStackTrace();
		}
		Enumeration<?> enumeration = prop.propertyNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			confMap.put(name, prop.getProperty(name).trim());
		}
		confFileDir = filePath.substring(0, filePath.lastIndexOf('/') + 1);
	}

	/**
	 * 根据操作系统类型得到相应的配置值
	 * @param key
	 * @return
	 */
	public static String getConfBySystem(String key) {
		if (isWindos) {
			key += "Windows";
		} else {
			key += "Linux";
		}
		String value = confMap.get(key);
		return value;
	}

	public static String getConf(String key) {
		String value = confMap.get(key);
		return value;
	}

	public static void main(String[] args) {
	}
}
