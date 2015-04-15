package edu.cuit.hzhspace.util;

public class IPSeekerFactory {
	//纯真IP数据库名  
	public static String IP_FILE = "QQWry.dat";
	//保存的文件夹  
	public static String INSTALL_DIR = getPath("QQWry.dat");

	private static class ResourceHolder {
		public static IPSeeker resource = new IPSeeker(IP_FILE, INSTALL_DIR);
	}

	public static IPSeeker getResource() {
		return IPSeekerFactory.ResourceHolder.resource;
	}

	private static String getPath(String name) {
		String folderPath = Thread.currentThread().getContextClassLoader().getResource(name).getPath();
		folderPath = folderPath.replaceAll("%20", " ").replaceAll(name, "");
		return folderPath;
	}

	public static void main(String[] args) {
		String ip = "222.18.167.201";
		System.out.println(getResource().getArea(ip));
		System.out.println(getResource().getCountry(ip));
	}
}
