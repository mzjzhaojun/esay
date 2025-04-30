package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.SysconfigMapper;
import com.yt.app.api.v1.mapper.SysokxMapper;
import com.yt.app.api.v1.service.SysconfigService;
import com.yt.app.api.v1.vo.SysOxxVo;

import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Sysconfig;
import com.yt.app.api.v1.entity.Sysokx;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.util.RedisUtil;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

	@Autowired
	private SysokxMapper sysokxmapper;

	@Override
	@Transactional
	public Integer post(Sysconfig t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Sysconfig> page(Map<String, Object> param) {
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
	@Transactional
	public void initSystemData() {
		getUsdtPrice();
	}

	private void getUsdtPrice() {
		Long time = System.currentTimeMillis();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("accept", "application/json");
		headers.put("accept-charset", "UTF-8");
		headers.put("accept-encoding", "gzip, deflate, br, zstd");
		headers.put("accept-language", "zh-CN,zh;q=0.9");
		headers.put("app-type", "web");
		headers.put("cookie",
				"devId=fae86fb7-537f-4282-9828-692e98f04bba; ok_site_info===QfxojI5RXa05WZiwiIMFkQPx0Rfh1SPJiOiUGZvNmIsICSLJiOi42bpdWZyJye; locale=zh_CN; ok_prefer_udColor=0; ok_prefer_udTimeZone=0; okg.currentMedia=xl; intercom-id-ny9cf50h=caae9c37-e7fe-4e4c-b7bc-840b31a29399; intercom-device-id-ny9cf50h=6d13a014-49cd-47f2-bd83-11adfa02578c; _ym_uid=1742714694954280392; _ym_d=1742714694; first_ref=https%3A%2F%2Fwww.okx.com%2Fzh-hans%2Fp2p-markets%2Fcny%2Fbuy-usdt; ok-exp-time=1745406087314; __cf_bm=R3qL8LLwF3s5rMV4iyk4F4DwIqW6QDawHD4owXr5swo-1745406087-1.0.1.1-eT1l0FFSN9x5DAIw_Leg6Y6DTriAroOAdM8fEveM7p91Dz7uoHItXF0g50N4EAaaKMziLt.DSFdSPXkhz71kLN0fJGNoEHqEYT_xYZFefm4; fingerprint_id=e47f443f-e1b2-4c5f-bb59-c5280f9f51fa; _gid=GA1.2.1008823734.1745406088; tmx_session_id=jt60ejlk2ge_1745406090942; fp_s=0; intercom-session-ny9cf50h=; _ym_isad=2; _ym_visorc=b; ok_prefer_currency=%7B%22currencyId%22%3A0%2C%22isDefault%22%3A1%2C%22isPremium%22%3Afalse%2C%22isoCode%22%3A%22USD%22%2C%22precision%22%3A2%2C%22symbol%22%3A%22%24%22%2C%22usdToThisRate%22%3A1%2C%22usdToThisRatePremium%22%3A1%2C%22displayName%22%3A%22%E7%BE%8E%E5%85%83%22%7D; _gat_UA-35324627-3=1; traceId=2130554063215500006; _ga=GA1.1.467806720.1742714688; _ga_G0EKWWQGTZ=GS1.1.1745406088.4.1.1745406321.57.0.0; _monitor_extras={\"deviceId\":\"G8Ypj07bTQ1vNX4MDSyOAc\",\"eventId\":62,\"sequenceNumber\":62}; ok-ses-id=vN4go68GDpsi61TDO6oudMO2VIez8D810XcOrbg2iWbM9/71LJEUIh/eQmcF3LU1VuyQYTmbvfjo+WrdfO5imfWKXfjKUkSo+mimyN5RZqzZFCvgmIHuTh7u0S8tDzWM");
		headers.put("devid", "fae86fb7-537f-4282-9828-692e98f04bba");
		headers.put("priority", "u=1, i");
		headers.put("referer", "https://www.okx.com/zh-hans/p2p-markets/cny/buy-usdt");
		headers.put("sec-ch-ua", "'Google Chrome';v='135', 'Not-A.Brand';v='8', 'Chromium';v='135'");
		headers.put("sec-ch-ua-mobile", "?0");
		headers.put("sec-ch-ua-platform", "'macOS'");
		headers.put("sec-fetch-dest", "empty");
		headers.put("sec-fetch-mode", "cors");
		headers.put("sec-fetch-site", "same-origin");
		headers.put("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36");
		headers.put("x-cdn", "https://www.okx.com");
		headers.put("x-fptoken",
				"eyJraWQiOiIxNjgzMzgiLCJhbGciOiJFUzI1NiJ9.eyJpYXQiOjE3NDU0MDYwOTEsImVmcCI6InJiNDNlMEx6WjBmdDEyZlgyYTBPVTNKamw2YVZwN2lSRjZHako0bE5wU1RMVWVKZ1lHUWIwbi9rbGVwMHU5QzciLCJkaWQiOiIiLCJjcGsiOiJNRmt3RXdZSEtvWkl6ajBDQVFZSUtvWkl6ajBEQVFjRFFnQUVUSHhJbTI5aEVRMTZCRi8wM01WRTROV1RJanphanRKak95VEJXbzhTOU5UMHVEUkgzdVlFeWp2aWpFTzRyZjFoRWZ0UnRLS1Jzd1J0U0owVlBudHM5QT09In0.6ROjseit0iY9P3SGkq_tyA1OP0YW5LGpWqUDxVTxEnQbHPaXqHzHy6tvzXMYhjICioAp8c2vNlBqv2GaBT04_A");
		headers.put("x-fptoken-signature", "{P1363}hvziU/oWch7/f35Xjj0ixBBL0Lk5Ekdd4y9g9x5XRZsheVkegy7gB73IyX1MQYptvPqA2h8vxRG4z95Rd0qsdA==");
		headers.put("x-id-group", "2140359440211530002-c-13");
		headers.put("x-locale", "zh_CN");
		headers.put("x-request-timestamp", time + "");
		headers.put("x-simulated-trading", "undefined");
		headers.put("x-site-info", "==QfxojI5RXa05WZiwiIMFkQPx0Rfh1SPJiOiUGZvNmIsICSLJiOi42bpdWZyJye");
		headers.put("x-utc", "8");
		headers.put("x-zkdex-env", "0");
		String url = "https://www.okx.com/v3/c2c/tradingOrders/books?quoteCurrency=CNY&baseCurrency=USDT&side=sell&paymentMethod=all&userType=all&receivingAds=false&t=" + time;
		String str = HttpRequest.get(url).addHeaders(headers).execute().body();
		SysOxxVo data = JSONUtil.toBean(str, SysOxxVo.class);
		List<Object> array = data.getData().getSell();
		Double price = Double.valueOf(BeanUtil.beanToMap(array.get(0)).get("price").toString());
		mapper.putUsdtToTrxExchange(price);
		RedisUtil.set(SystemConstant.CACHE_SYS_EXCHANGE + ServiceConstant.SYSTEM_PAYCONFIG_USDTOTEXCHANGE, price.toString());
		sysokxmapper.deleteAll();
		for (int i = 0; i < 20; i++) {
			Sysokx tt = new Sysokx();
			tt.setName(BeanUtil.beanToMap(array.get(i)).get("nickName").toString());
			tt.setPrice(Double.valueOf(BeanUtil.beanToMap(array.get(0)).get("price").toString()));
			tt.setType(BeanUtil.beanToMap(array.get(i)).get("paymentMethods").toString());
			sysokxmapper.post(tt);
		}
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Sysconfig getUsdtExchangeData() {
		return mapper.getByName(ServiceConstant.SYSTEM_PAYCONFIG_USDTEXCHANGE);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Sysconfig getUsdtToTrxExchangeData() {
		return mapper.getByName(ServiceConstant.SYSTEM_PAYCONFIG_USDTOTEXCHANGE);
	}

	@Override
	public List<Sysconfig> getDataTop() {
		Long time = System.currentTimeMillis() / 1000;
		String str = HttpUtil.get("https://www.okx.com/v3/c2c/tradingOrders/books?quoteCurrency=CNY&baseCurrency=USDT&side=sell&paymentMethod=all&userType=all&receivingAds=false&t=" + time);
		SysOxxVo data = JSONUtil.toBean(str, SysOxxVo.class);
		List<Object> list = data.getData().getSell();
		List<Sysconfig> listpc = new ArrayList<Sysconfig>();
		for (Integer i = 0; i < 10; i++) {
			Sysconfig e = new Sysconfig();
			e.setName(BeanUtil.beanToMap(list.get(i)).get("nickName").toString());
			e.setExchange(Double.valueOf(BeanUtil.beanToMap(list.get(i)).get("price").toString()));
			listpc.add(e);
		}
		return listpc;
	}

	@Override
	public List<Sysconfig> getAliPayDataTop() {
		Long time = System.currentTimeMillis() / 1000;
		String str = HttpUtil.get("https://www.okx.com/v3/c2c/tradingOrders/books?quoteCurrency=CNY&baseCurrency=USDT&side=sell&paymentMethod=all&userType=all&receivingAds=false&t=" + time);
		SysOxxVo data = JSONUtil.toBean(str, SysOxxVo.class);
		List<Object> list = data.getData().getSell();
		List<Sysconfig> listpc = new ArrayList<Sysconfig>();
		for (Integer i = 0; i < 10; i++) {
			Sysconfig e = new Sysconfig();
			e.setName(BeanUtil.beanToMap(list.get(i)).get("nickName").toString());
			e.setExchange(Double.valueOf(BeanUtil.beanToMap(list.get(i)).get("price").toString()));
			listpc.add(e);
		}
		return listpc;
	}

}