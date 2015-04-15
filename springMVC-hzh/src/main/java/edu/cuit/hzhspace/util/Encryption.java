package edu.cuit.hzhspace.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

/**
 * 加密工具类，返字符串通过加密后的base64编码
 * 
 */
public class Encryption {
	/**
	 * 使用MD5进行加密
	 * 
	 * @param str待加密的字符串
	 * @return 加密后的字符串
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static String encoderByMd5(String str) {
		if (null == str) {
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5"); // 确定计算方法
			return Base64.encodeBase64String(md.digest(str.getBytes("utf-8"))).trim();// 返回加密后的字符串
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 使用SHA1进行加密
	 * 
	 * @param str待加密的字符串
	 * @return 加密后的字符串
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static String encoderBySHA1(String str) {
		if (null == str) {
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1"); // 确定计算方法
			return Base64.encodeBase64String(md.digest(str.getBytes("utf-8"))).trim();// 返回加密后的字符串
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 对于对一个比特数组进行加密
	 */
	public static byte[] encoder(byte[] bytes) {
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) ((bytes[i] ^ (i + 1)) + (i + 1) * 13);
		}
		return bytes;
	}

	/*
	 * 对于对一个比特数组进行解密
	 */
	public static byte[] decoder(byte[] bytes) {
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) ((bytes[i] - (i + 1) * 13) ^ (i + 1));
		}
		return bytes;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		System.out.println("md5:" + Encryption.encoderBySHA1("HZH823287929"));
		System.out.println("md5:" + Encryption.encoderByMd5("Admin123"));
		// System.out.println(Encryption.encoderByMd5("123").length());
		System.out.println("sha-1:" + Encryption.encoderBySHA1("Admin123"));
	}
}
