package com.yt.app.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	public static String md5(String data) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] md5 = md.digest(data.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte b : md5) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void main(String args[]) {
		String signContent = "accountAttr=0&accountName=吴惠蔺&accountNo=6230522100008206470&amount=11200&bankCode=ABC&bankName=农业银行&mchId=6288900231&mchOrderNo=out2503311539489992&notifyUrl=http://103.151.116.145:18800/esay/rest/v1/order/xrcallback&remark=代付112.00&reqTime=20250331153949&keySign=AP9VHWR03PWVVD17ZOJPEMGOJ4EGIKWGFFX4HB1HHUAV0ISWVQZGI7BDJZYIS5EXLOYHCYGEPQAIVNYWUQGHTZWJZ8EVDMVJMUSGZEJYJJNDNIVSQKVEVNJALG7YOG6QApm";
		
		String sign = md5(signContent);
		
		System.out.println(sign);
	}
}
