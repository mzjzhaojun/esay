package com.yt.app.common.runtask;

import java.util.List;

import org.eclipse.jetty.util.resource.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.yt.app.api.v1.mapper.PayconfigMapper;
import com.yt.app.api.v1.vo.SysOxxVo;

import cn.hutool.core.bean.BeanUtil;

@Component
public class TaskExchange {

	@Autowired
	private PayconfigMapper payconfigmapper;

	@Scheduled(cron = "0/30 * * * * ?")
	public void exchange() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		HttpEntity<Resource> httpEntity = new HttpEntity<Resource>(headers);
		RestTemplate resttemplate = new RestTemplate();
		ResponseEntity<SysOxxVo> sov = resttemplate.exchange(
				"https://www.okx.com/v3/c2c/tradingOrders/books?quoteCurrency=CNY&baseCurrency=USDT&side=sell&paymentMethod=all&userType=all",
				HttpMethod.GET, httpEntity, SysOxxVo.class);
		SysOxxVo data = sov.getBody();
		List<Object> list = data.getData().getSell();
		payconfigmapper.putExchange(Double.valueOf(BeanUtil.beanToMap(list.get(0)).get("price").toString()));
	}
}
