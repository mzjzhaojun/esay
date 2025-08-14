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

	public static String LoginFootBall(String url, String username, String password, String ver) {
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
					"LPVID=FjOWY5NjAwY2E5NjBlZDll; isclick=mem_user=chggzq2*mem_user=chggzq3*ag_user=hhg12345*; ag_12115260_v=I%16%DBO%FFy%CA%81%FF%D5y%18%3A%23%CEQ%E5%BA%F7%0E%87s%11-%16lw%F7%BE%2AI%9C; protocolstr=https; LPVID=FjOWY5NjAwY2E5NjBlZDll; LPSID-9137304=zj2v-MZUQnKA3tZj_tt5iA; ag_12057338_v=Y%98%21%B8%90N%17%DE-f%3B%F2%81v%7C%943G%84%8E%FA%D3%CE5R%E1O%9D%81%86W%C0");
			headers.add("Origin", "https://ag.hga027.com");
			headers.add("Referer", "https://ag.hga027.com/");
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

			ResponseEntity<String> str = resttemplate.exchange("https://ag.hga027.com/transform.php?ver=version-08-07", HttpMethod.POST, httpEntity, String.class);

			log.info(str.getBody());
		} catch (Exception e) {
			log.info("错误返回消息：" + e.getMessage());
		}
		return null;
	}

	public static ResponseEntity<String> SendFootBall(String url, String uid, String maxid, String cookie) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Accept", "*/*");
			headers.add("Accept-Encoding", "gzip, deflate, br, zstd");
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

			log.info(" 返回消息：" + str.getBody());

			return str;
		} catch (Exception e) {
			log.info("错误返回消息：" + e.getMessage());
		}
		return null;
	}

	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");
		LoginFootBall("https://ag.hga027.com/transform.php?ver=version-08-07", "hhg12345a", "Aabb1188", "version-08-07");
//		SendFootBall("https://ag.hga027.com/transform.php?ver=version-08-07", "4effbf0bm12057338l767205309xw", "1054831",
//				"LPVID=FjOWY5NjAwY2E5NjBlZDll; isclick=mem_user=chggzq2*mem_user=chggzq3*ag_user=hhg12345*; ag_12115260_v=I%16%DBO%FFy%CA%81%FF%D5y%18%3A%23%CEQ%E5%BA%F7%0E%87s%11-%16lw%F7%BE%2AI%9C; protocolstr=https; ag_12057338_v=%F5yw%EA%D2%D3%DD%9Bq%B5%3A%C7%17%8C%00e%BD%95%8A%89o%01%BE%82p%84%BCm%09%EF%D72; LPVID=FjOWY5NjAwY2E5NjBlZDll; LPSID-9137304=Zyn0aM4ERqWxUdcs1E26kA");
	}
}
