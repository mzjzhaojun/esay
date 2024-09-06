package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TronMapper;
import com.yt.app.api.v1.service.TronService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tron;
import com.yt.app.api.v1.vo.TronVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.enums.YtDataSourceEnum;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-06 15:25:57
 */
@Slf4j
@Service
public class TronServiceImpl extends YtBaseServiceImpl<Tron, Long> implements TronService {
	@Autowired
	private TronMapper mapper;

	@Override
	@Transactional
	public Integer post(Tron t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Tron> list(Map<String, Object> param) {
		List<Tron> list = mapper.list(param);
		return new YtPageBean<Tron>(list);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Tron get(Long id) {
		Tron t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<TronVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<TronVO>(Collections.emptyList());
		}
		List<TronVO> list = mapper.page(param);
		return new YtPageBean<TronVO>(param, list, count);
	}

	@Override
	public boolean validateaddress(String address) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/validateaddress",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);
		return false;
	}

	@Override
	public void createaccount(String owneraddress, String address) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/createaccount",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void getaccount(String address) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/getaccount",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void updateaccount(String name, String address) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/updateaccount",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void getaccountbalance(String address, Integer block) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/getaccountbalance",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void setaccountid(String address, Long accountid) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/setaccountid",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void getaccountbyid(Long accountid) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/getaccountbyid",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void createtransaction(String toaddress, String owneraddress) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/createtransaction",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void broadcasttransaction(String signature, String txid) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/broadcasttransaction",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void broadcasthex(String transaction) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/broadcasthex",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void getsignweight(String signature, String txid) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/getsignweight",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void getapprovedlist(String signature, String txid) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/getapprovedlist",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void getaccountresource(String address) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/getaccountresource",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void getaccountnet(String address) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/getaccountnet",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void freezebalancev2(String owneraddress, Integer frozenbalance, String resource) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/freezebalancev2",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void getnowblock() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/getnowblock",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void getblock(String idornum) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/getblock", HttpMethod.POST,
				httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void getblockbynum(Integer num) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/getblockbynum",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void getblockbyid(String value) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/getblockbyid",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void getblockbylatestnum(Integer num) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/getblockbylatestnum",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void getblockbalance(String hash, Integer number) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/getblockbalance",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void gettransactionbyid(String txid) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/gettransactionbyid",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void gettransactioninfobyid(String txid) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/gettransactioninfobyid",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void gettransactioncountbyblocknum(Integer number) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange(
				"https://nile.trongrid.io/wallet/gettransactioncountbyblocknum", HttpMethod.POST, httpEntity,
				Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void gettransactioninfobyblocknum(Integer number) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange(
				"https://nile.trongrid.io/wallet/gettransactioninfobyblocknum", HttpMethod.POST, httpEntity,
				Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}

	@Override
	public void getnodeinfo() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("address", "418b0F566C0a7940362979a634B0fBD79ce95273FF");

		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
		RestTemplate resttemplate = new RestTemplate();
		// https://nile.trongrid.io http://192.168.110.129:8090
		ResponseEntity<Object> sov = resttemplate.exchange("https://nile.trongrid.io/wallet/getnodeinfo",
				HttpMethod.POST, httpEntity, Object.class);
		String data = sov.getBody().toString();
		log.info("tron：" + data);

	}
}