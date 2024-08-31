package com.yt.app.api.v1.service.impl;

import org.eclipse.jetty.util.resource.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.SysconfigMapper;
import com.yt.app.api.v1.service.SysconfigService;
import com.yt.app.api.v1.vo.SysOxxVo;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Sysconfig;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.util.RedisUtil;

import cn.hutool.core.bean.BeanUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 18:42:54
 */

@Service
public class SysconfigServiceImpl extends YtBaseServiceImpl<Sysconfig, Long> implements SysconfigService {
	@Autowired
	private SysconfigMapper mapper;

	@Override
	@Transactional
	public Integer post(Sysconfig t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Sysconfig> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Sysconfig>(Collections.emptyList());
			}
		}
		List<Sysconfig> list = mapper.list(param);
		return new YtPageBean<Sysconfig>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Sysconfig get(Long id) {
		Sysconfig t = mapper.get(id);
		return t;
	}

	@Override
	public void initExchangeData() {
		// test
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
		Double exchange = Double.valueOf(BeanUtil.beanToMap(list.get(0)).get("price").toString());
		mapper.putExchange(exchange);
		RedisUtil.set(SystemConstant.CACHE_SYS_EXCHANGE, exchange.toString());
	}

	@Override
	public Sysconfig getData() {
		return mapper.getByName(ServiceConstant.SYSTEM_PAYCONFIG_EXCHANGE);
	}

	@Override
	public List<Sysconfig> getDataTop() {
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
		List<Sysconfig> listpc = new ArrayList<Sysconfig>();
		for (Integer i = 0; i < 5; i++) {
			Sysconfig e = new Sysconfig();
			e.setName(BeanUtil.beanToMap(list.get(i)).get("nickName").toString());
			e.setExchange(Double.valueOf(BeanUtil.beanToMap(list.get(i)).get("price").toString()));
			listpc.add(e);
		}
		return listpc;
	}

	@Override
	public List<Sysconfig> getAliPayDataTop() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		HttpEntity<Resource> httpEntity = new HttpEntity<Resource>(headers);
		RestTemplate resttemplate = new RestTemplate();
		ResponseEntity<SysOxxVo> sov = resttemplate.exchange(
				"https://www.okx.com/v3/c2c/tradingOrders/books?quoteCurrency=CNY&baseCurrency=USDT&side=sell&paymentMethod=aliPay&userType=all",
				HttpMethod.GET, httpEntity, SysOxxVo.class);
		SysOxxVo data = sov.getBody();
		List<Object> list = data.getData().getSell();
		List<Sysconfig> listpc = new ArrayList<Sysconfig>();
		for (Integer i = 0; i < 5; i++) {
			Sysconfig e = new Sysconfig();
			e.setName(BeanUtil.beanToMap(list.get(i)).get("nickName").toString());
			e.setExchange(Double.valueOf(BeanUtil.beanToMap(list.get(i)).get("price").toString()));
			listpc.add(e);
		}
		return listpc;
	}
}