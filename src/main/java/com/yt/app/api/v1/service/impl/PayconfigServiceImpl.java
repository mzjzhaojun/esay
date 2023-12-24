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
import com.yt.app.api.v1.mapper.PayconfigMapper;
import com.yt.app.api.v1.service.PayconfigService;
import com.yt.app.api.v1.vo.SysOxxVo;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Payconfig;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;

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
public class PayconfigServiceImpl extends YtBaseServiceImpl<Payconfig, Long> implements PayconfigService {
	@Autowired
	private PayconfigMapper mapper;

	@Override
	@Transactional
	public Integer post(Payconfig t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Payconfig> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Payconfig>(Collections.emptyList());
			}
		}
		List<Payconfig> list = mapper.list(param);
		return new YtPageBean<Payconfig>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Payconfig get(Long id) {
		Payconfig t = mapper.get(id);
		return t;
	}

	@Override
	public Payconfig getData() {
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
		mapper.putExchange(Double.valueOf(BeanUtil.beanToMap(list.get(0)).get("price").toString()));
		return mapper.get(ServiceConstant.SYSTEM_PAYCONFIG_EXCHANGE);
	}

	@Override
	public List<Payconfig> getDatas() {
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
		List<Payconfig> listpc = new ArrayList<Payconfig>();
		for (Integer i = 0; i < 5; i++) {
			Payconfig e = new Payconfig();
			e.setName(BeanUtil.beanToMap(list.get(i)).get("nickName").toString());
			e.setExchange(Double.valueOf(BeanUtil.beanToMap(list.get(i)).get("price").toString()));
			listpc.add(e);
		}
		return listpc;
	}
}