package com.yt.app.common.util;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FootBallUtil {

	// 通银代付申请回单
	public static String SendFootBall() {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Accept", "*/*");
			headers.add("Accept-Encoding", "gzip, deflate, br, zstd");
			headers.add("Accept-Language", "zh-CN,zh;q=0.9");
			headers.add("Connection", "keep-alive");
			headers.add("Content-Length", "139");
			headers.add("Content-Type", "application/x-www-form-urlencoded");
			headers.add("Host", "ag.hga027.com");
			headers.add("Cookie",
					"protocolstr=https; ag_userC=asd4447772; LPVID=FjOWY5NjAwY2E5NjBlZDll; LPSID-9137304=O77ar5LsQECjP2QenXehag; isclick=mem_user=cxcxs41f6o*ag_user=xykto408*; ag_12057338_v=%DAem%1D%22b%22o%EB%FB3%95%26%17%A7J%2AB5%CE%3Cr%9C%26bJra%8F%9E%2A%B6");
			headers.add("Origin", "https://ag.hga027.com");
			headers.add("Referer", "https://ag.hga027.com/app/wmc/index.php");
			headers.add("Sec-Ch-Ua", "Not)A;Brand';v='8', 'Chromium';v='138', 'Google Chrome';v='138'");
			headers.add("Sec-Ch-Ua-Mobile", "?0");
			headers.add("Sec-Ch-Ua-Platform", "'Windows'");
			headers.add("Sec-Fetch-Dest", "empty");
			headers.add("Sec-Fetch-Mode", "cors");
			headers.add("Sec-Fetch-Site", "same-origin");
			headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36");
			//

			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("login_layer", "ag");
			map.add("uid", "23e14f36m12057338l766946267xw");
			map.add("langx", "zh-cn");
			map.add("ver", "version-08-07");
			map.add("p", "get_wmc_list_bet");
			map.add("totalBets", "wmc");
			map.add("gtype", "ALL");
			map.add("sel_maxid", "1053236");

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();

			ResponseEntity<String> str = resttemplate.exchange("https://ag.hga027.com/transform.php?ver=version-08-07", HttpMethod.POST, httpEntity, String.class);

			log.info(" 返回消息：" + str);
		} catch (Exception e) {
			log.info("错误返回消息：" + e.getMessage());
		}
		return null;
	}

	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");
		SendFootBall();
	}
}
