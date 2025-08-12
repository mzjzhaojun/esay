package com.yt.app.common.util;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;

public class JTutil {
//	//初始化需要用到的参数
//	private static final String CLIENT_TYPE="1";
//	private static final String CLIENT_VERSION="1.0.0";
//	private static final String API_SECRET="376d044008c3b4f4849f8ef29bba4444";
//	private static final String API_KEY="jo_HmRmNiQVPfOEbqap4br8q82Cy2222";
//	private static final String STRING_KEY="open_api_vf12kwQ75v7xd333${clientType}${clientVersion}${apiKey}${apiSecret}${timestamp}";
//	private static final String URL="https://pay.new.jieoukeji.cn/jo-pay-open-api";
//	private static final String PATH="/v1/bank/info";//示例接口：获取银行卡信息
//	private static final Pattern VALUE_PATTERN = Pattern.compile("\\$\\{([^}]*)\\}");
//
//	    long timestamp = System.currentTimeMillis();
//	    Map<String,String> headers = new HashMap<>(6);
//	    headers.put("client-type",CLIENT_TYPE);
//	    headers.put("client-version",CLIENT_VERSION);
//	    headers.put("api-secret",API_SECRET);
//	    headers.put("api-key",API_KEY);
//	    headers.put("access-timestamp",String.valueOf(timestamp));
//	    headers.put("Content-Type","application/json");
//	    //拼接字符串,签名开头保留，${} 全替换为header参数
//	    headers.put("access-sign",getSign(STRING_KEY,timestamp));
//	    //开始请求API接口
//	    Map<String, String> param = new HashMap<>();
//	    param.put("cardNo", "123546345674573457");
//	    try {
//	        String results = get(URL+PATH,headers, param);
//	        //返回结果JSON字符串
//	        System.out.println(results);
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	    /**
//	 * 获取签名前准备参数
//	 * @param format
//	 * @param timestamp
//	 * @return
//	 */
//	    public  String getSign(String format,long timestamp) {
//	    JSONObject dataSign=new JSONObject();
//	    dataSign.put("clientType",CLIENT_TYPE);
//	    dataSign.put("clientVersion",CLIENT_VERSION);
//	    dataSign.put("apiSecret",API_SECRET);
//	    dataSign.put("apiKey",API_KEY);
//	    dataSign.put("timestamp",timestamp);
//	    String formatData = getFormatData(dataSign, format);
//	    return md5(formatData);
//	}
//	/**
//	 * 拼接替换生成签名前字符串
//	 * @param format
//	 * @param timestamp
//	 * @return
//	 */
//	public  String getFormatData(JSONObject data, String format) {
//	    Matcher matcher = VALUE_PATTERN.matcher(format);
//	    while(true) {
//	        while(matcher.find()) {
//	            String sign = matcher.group();
//	            String key = matcher.group(1);
//	            if (ObjectUtils.isNotEmpty(key)) {
//	                format = format.replace(sign, data.containsKey(key) && ObjectUtils.isNotEmpty(data.getString(key)) && !"null".equalsIgnoreCase(data.getString(key)) ? data.getString(key) : "");
//	            } else {
//	                format = format.replace(sign, "");
//	            }
//	        }
//	        return format;
//	    }
//	}
//	/**
//	 * 生成md5,小写
//	 * @param message
//	 * @return
//	 */
//	public static String md5(String message) {
//	    try {
//	        // 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
//	        MessageDigest md = MessageDigest.getInstance("MD5");
//
//	        // 2 将消息变成byte数组
//	        byte[] input = message.getBytes();
//
//	        // 3 计算后获得字节数组,这就是那128位了
//	        byte[] buff = md.digest(input);
//
//	        // 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
//	        return byte2hex(buff);
//	    } catch (Exception e) {
//	        throw new RuntimeException(e);
//	    }
//	}
//
//	/**
//	 * 二进制转十六进制字符串
//	 * @param bytes
//	 * @return
//	 */
//	private static String byte2hex(byte[] bytes) {
//	    StringBuilder sign = new StringBuilder();
//	    for (int i = 0; i < bytes.length; i++) {
//	        String hex = Integer.toHexString(bytes[i] & 0xFF);
//	        if (hex.length() == 1) {
//	            sign.append("0");
//	        }
//	        sign.append(hex.toLowerCase());
//	    }
//	    return sign.toString();
//	}
//	/**
//	 * 调用apiPost方法
//	 * @param format
//	 * @param timestamp
//	 * @return
//	 */
//	public static HttpResponse doPost(String host, String path, String method,
//	                                  Map<String, String> headers,
//	                                  Map<String, String> querys,
//	                                  String body)
//	        throws Exception {
//	    HttpClient httpClient = wrapClient(host);
//
//	    HttpPost request = new HttpPost(buildUrl(host, path, querys));
//	    for (Map.Entry<String, String> e : headers.entrySet()) {
//	        request.addHeader(e.getKey(), e.getValue());
//	    }
//
//	    if (StringUtils.isNotBlank(body)) {
//	        request.setEntity(new StringEntity(body, "utf-8"));
//	    }
//	    return httpClient.execute(request);
//	}
//
//	public static HttpClient wrapClient(String host) {
//	    HttpClient httpClient = new DefaultHttpClient();
//	    if (host.startsWith("https://")) {
//	        sslClient(httpClient);
//	    }
//
//	    return httpClient;
//	}
//
//	public static String buildUrl(String host, String path, Map<String, String> querys) throws UnsupportedEncodingException {
//	    StringBuilder sbUrl = new StringBuilder();
//	    sbUrl.append(host);
//	    if (!StringUtils.isBlank(path)) {
//	        sbUrl.append(path);
//	    }
//	    if (null != querys) {
//	        StringBuilder sbQuery = new StringBuilder();
//	        for (Map.Entry<String, String> query : querys.entrySet()) {
//	            if (0 < sbQuery.length()) {
//	                sbQuery.append("&");
//	            }
//	            if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
//	                sbQuery.append(query.getValue());
//	            }
//	            if (!StringUtils.isBlank(query.getKey())) {
//	                sbQuery.append(query.getKey());
//	                if (!StringUtils.isBlank(query.getValue())) {
//	                    sbQuery.append("=");
//	                    sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
//	                }
//	            }
//	        }
//	        if (0 < sbQuery.length()) {
//	            sbUrl.append("?").append(sbQuery);
//	        }
//	    }
//
//	    return sbUrl.toString();
//	}
//
//	private static void sslClient(HttpClient httpClient) {
//	    try {
//	        SSLContext ctx = SSLContext.getInstance("TLS");
//	        X509TrustManager tm = new X509TrustManager() {
//	            @Override
//	            public X509Certificate[] getAcceptedIssuers() {
//	                return null;
//	            }
//	            @Override
//	            public void checkClientTrusted(X509Certificate[] xcs, String str) {
//
//	            }
//	            @Override
//	            public void checkServerTrusted(X509Certificate[] xcs, String str) {
//
//	            }
//	        };
//	        ctx.init(null, new TrustManager[] { tm }, null);
//	        SSLSocketFactory ssf = new SSLSocketFactory(ctx);
//	        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//	        ClientConnectionManager ccm = httpClient.getConnectionManager();
//	        SchemeRegistry registry = ccm.getSchemeRegistry();
//	        registry.register(new Scheme("https", 443, ssf));
//	    } catch (KeyManagementException ex) {
//	        throw new RuntimeException(ex);
//	    } catch (NoSuchAlgorithmException ex) {
//	        throw new RuntimeException(ex);
//	    }
//	}

}
