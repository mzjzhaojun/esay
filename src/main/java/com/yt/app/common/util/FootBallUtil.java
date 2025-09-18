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

	public static ResponseEntity<String> LoginFootBall(String url, String username, String password, String ver, String origin) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Accept", "*/*");
			headers.add("Accept-Language", "zh-CN,zh;q=0.9");
			headers.add("Connection", "keep-alive");
			headers.add("Content-Length", "139");
			headers.add("Content-Type", "application/x-www-form-urlencoded");
			headers.add("Host", "ag.hga027.com");
			headers.add("Cookie",
					"LPVID=FjOWY5NjAwY2E5NjBlZDll; isclick=mem_user=cxcx1122a*ag_user=xykto408*mem_user=chggzq2*mem_user=chggzq3*ag_user=hhg12345*; protocolstr=https; LPVID=FjOWY5NjAwY2E5NjBlZDll; ag_12057338_v=%85%9As%8B%B4%D7k%95%86%90H%FD%16c%81%DB%09%0A%C8p%3B%D5%A5%FE%D1G%A3Q%9CK%CCq; LPSID-9137304=gUOHYR0TTr6SzvhY3rEp0g");
			headers.add("Origin", origin);
			headers.add("Referer", origin + "/");
			headers.add("Sec-Ch-Ua", "Not)A;Brand';v='8', 'Chromium';v='138', 'Google Chrome';v='138'");
			headers.add("Sec-Ch-Ua-Mobile", "?0");
			headers.add("Sec-Ch-Ua-Platform", "'Windows'");
			headers.add("Sec-Fetch-Dest", "empty");
			headers.add("Sec-Fetch-Mode", "cors");
			headers.add("Sec-Fetch-Site", "same-origin");
			headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36");
			//

			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("p", "login_chk");
			map.add("ver", ver);
			map.add("login_layer", "ag");
			map.add("username", username);
			map.add("pwd", password);
			map.add("pwd_safe", "none");
			map.add("uid", "");
			map.add("langx", "zh-cn");
			map.add("auto", "GEACDI");
			map.add("blackbox", "");

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();

			ResponseEntity<String> str = resttemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
			log.info("正确返回消息：" + str.getBody());
			return str;
		} catch (Exception e) {
			log.info("错误返回消息：" + e.getMessage());
		}
		return null;
	}

	public static ResponseEntity<String> SendFootBall(String url, String uid, String maxid, String cookie) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Accept", "*/*");
			headers.add("Accept-Language", "zh-CN,zh;q=0.9");
			headers.add("Connection", "keep-alive");
			headers.add("Content-Length", "139");
			headers.add("Content-Type", "application/x-www-form-urlencoded");
			headers.add("Host", "ag.hga027.com");
			headers.add("Cookie", cookie);
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
			map.add("uid", uid);
			map.add("langx", "zh-cn");
			map.add("ver", "version-08-07");
			map.add("p", "get_wmc_list_bet");
			map.add("totalBets", "wmc");
			map.add("gtype", "ALL");
			map.add("sel_maxid", maxid);

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
			RestTemplate resttemplate = new RestTemplate();

			ResponseEntity<String> str = resttemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);

			log.info("正确返回消息：" + str.getBody());
			return str;
		} catch (Exception e) {
			log.info("错误返回消息：" + e.getMessage());
		}
		return null;
	}

	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");
//		LoginFootBall("https://ag.hga027.com/transform.php?ver=version-08-07", "hhg12345a", "Aabb1188", "version-08-07");
//		LoginFootBall("https://ag.hga027.com/transform.php?ver=version-08-07", "asd4447772", "Qwer1234", "version-08-07", "https://ag.hga027.com");
		SendFootBall("https://ag.hga027.com/transform.php?ver=version-08-07", "649c4c66m12057338l767540488xw", "400063",
				"LPVID=FjOWY5NjAwY2E5NjBlZDll; isclick=mem_user=cxcx1122a*ag_user=xykto408*mem_user=chggzq2*mem_user=chggzq3*ag_user=hhg12345*; protocolstr=https; LPVID=FjOWY5NjAwY2E5NjBlZDll; ag_12057338_v=eE%F9%12%40%F2%2B%95q%DD%DA%B4%FB%10%7B%D3%88%10%CAN%A5%1A3%95%9A%00%C0%D5%04%94%28%7E; LPSID-9137304=FYa2we2DTi-dILJUKDZj9w");
	}
}
