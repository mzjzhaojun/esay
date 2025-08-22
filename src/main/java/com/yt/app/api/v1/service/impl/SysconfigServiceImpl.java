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
import cn.hutool.json.JSONUtil;

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
	public List<Sysokx> getDataTop() {
		List<Sysokx> listpc = sysokxmapper.listTop("");
		return listpc;
	}

	@Override
	public List<Sysokx> getAliPayDataTop() {
		List<Sysokx> listpc = sysokxmapper.listTop("aliPay");
		return listpc;
	}

	@Override
	public Sysokx getDataTopOne() {
		List<Sysokx> listpc = sysokxmapper.listTop("");
		return listpc.get(0);
	}

	@Override
	@Transactional
	public void initSystemData() {
		getUsdtPrice();
	}

	private void getUsdtPrice() {
		Long time = System.currentTimeMillis();
		Map<String, String> headers = new HashMap<String, String>();
//		headers.put(":authority", "www.okx.com");
//		headers.put(":method", "GET");
//		headers.put(":path", "/v3/c2c/tradingOrders/books?quoteCurrency=CNY&baseCurrency=USDT&side=sell&paymentMethod=all&userType=all&receivingAds=false&t=1749446813166");
//		headers.put(":scheme", "https");
		headers.put("accept-encoding", "gzip, deflate, br, zstd");
		headers.put("accept-language", "en-US,en;q=0.9");
		headers.put("app-type", "web");
		headers.put("cookie",
				"devId=8ade397c-c980-4ef1-8acb-9704b22368ad; ok_site_info===QfxojI5RXa05WZiwiIMFkQPx0Rfh1SPJiOiUGZvNmIsICSLJiOi42bpdWZyJye; locale=en_US; preferLocale=en_US; ok_prefer_udColor=0; ok_prefer_udTimeZone=0; AMP_MKTG_56bf9d43d5=JTdCJTIycmVmZXJyZXIlMjIlM0ElMjJodHRwcyUzQSUyRiUyRnd3dy5nb29nbGUuY29tJTJGJTIyJTJDJTIycmVmZXJyaW5nX2RvbWFpbiUyMiUzQSUyMnd3dy5nb29nbGUuY29tJTIyJTdE; intercom-id-ny9cf50h=4c49a7df-c82b-4ff6-9b09-54740015b1fc; intercom-device-id-ny9cf50h=470e6739-1af8-4ef0-87e4-6327c8e10753; _ym_uid=1735479862720207978; _ym_d=1735479862; AMP_56bf9d43d5=JTdCJTIyZGV2aWNlSWQlMjIlM0ElMjI4YWRlMzk3Yy1jOTgwLTRlZjEtOGFjYi05NzA0YjIyMzY4YWQlMjIlMkMlMjJ1c2VySWQlMjIlM0ElMjIlMjIlMkMlMjJzZXNzaW9uSWQlMjIlM0ExNzM1NDc5ODQ5NDA0JTJDJTIyb3B0T3V0JTIyJTNBZmFsc2UlMkMlMjJsYXN0RXZlbnRUaW1lJTIyJTNBMTczNTQ3OTg2NjU5NSUyQyUyMmxhc3RFdmVudElkJTIyJTNBMjAlMkMlMjJwYWdlQ291bnRlciUyMiUzQTAlN0Q=; ok-exp-time=1749446698855; ok_prefer_currency=0%7C1%7Cfalse%7CUSD%7C2%7C%24%7C1%7C1%7CUSD; __cf_bm=luPKEKpUTYSh.KLFDJhUZKPURZL8W8tFE8JOEVLxCOk-1749446698-1.0.1.1-ln8FAQ4JZEWywZAuPLdPInsMUyOgHteSDVKiUKbCubftMedFgrgOvXlxoVlYLVJA6e9u7VRkbGp5KRXa5q12_wVkC6r_SKQIhfn.zHSkArM; fingerprint_id=8ade397c-c980-4ef1-8acb-9704b22368ad; _gid=GA1.2.748480325.1749446706; tmx_session_id=xzm71m1d9u_1749446707456; first_ref=https%3A%2F%2Fwww.okx.com%2Fp2p-markets%2Fcny%2Fbuy-usdt; _ga=GA1.1.372117000.1735479850; fp_s=0; intercom-session-ny9cf50h=; traceId=2121194467091840004; _ym_isad=2; _ym_visorc=b; okg.currentMedia=xl; ok-ses-id=7Cqw1UlAjSuRRB8vMuCDskE39yMD5kgNfF3fOxTjzC6poqaNY+7KVxhohFOoPZVaU/avZCnFjklbhIHZ4Q5onoKQMvP1TxFMcpz+mYSvCAP2BIbl/x0NYaZ+LuY3V4rB; _monitor_extras={\"deviceId\":\"5bg0ayLsNEQbGwbU8imL5n\",\"eventId\":78,\"sequenceNumber\":78}; _ga_G0EKWWQGTZ=GS2.1.s1749446706$o6$g1$t1749446778$j60$l0$h0");
		headers.put("devid", "8ade397c-c980-4ef1-8acb-9704b22368ad");
		headers.put("priority", "u=1, i");
		headers.put("referer", "https://www.okx.com/zh-hans/p2p-markets/cny/buy-usdt");
		headers.put("sec-ch-ua", "'Google Chrome';v='137, 'Chromium';v='137'', 'Not-A.Brand';v='24'");
		headers.put("sec-ch-ua-mobile", "?0");
		headers.put("sec-ch-ua-platform", "'Windows'");
		headers.put("sec-fetch-dest", "empty");
		headers.put("sec-fetch-mode", "cors");
		headers.put("sec-fetch-site", "same-origin");
		headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36");
		headers.put("x-cdn", "https://www.okx.com");
		headers.put("x-fptoken",
				"eyJraWQiOiIxNjgzMzgiLCJhbGciOiJFUzI1NiJ9.eyJpYXQiOjE3NDk0NDY3MDcsImVmcCI6ImgwbWd6dHNsTU5IQnBoY3Zlam5BTEh3a1ljbVh1aWRESFY0SFRabno3WmNFN2JhUzZ4Y05xSVVZYnEvZ1ROenEiLCJkaWQiOiIiLCJjcGsiOiJNRmt3RXdZSEtvWkl6ajBDQVFZSUtvWkl6ajBEQVFjRFFnQUVPaDBCMWFiWW0xUWtnYkl5OVJvZGZUUlQ5NW1SK3NMenI0NlNEcGJqa283bU54L1VvM3RSeU9KQmFKc01neXFsVk51MktQWFBkTzdiK0NXS3dtMVV0QT09In0.yHGAq6inmWCSOLQOWiqw1V4AfnF-W_A1pnOl1mVNrjnb8lCCwWzdQ6dexoLRHI_rZL0quTYq7317_-umNe6A1Q");
		headers.put("x-fptoken-signature", "{P1363}2txSZMDtdWuu3kpTkUjCa2iqyQ2RfTqDkJR5jHhiTAPynN7ABcrMNhfuHEOpLFEdaFwEXZkRC+3QMFFhySvvxg==");
		headers.put("x-id-group", "4100394467026090004-c-31");
		headers.put("x-locale", "en_US");
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
		mapper.putUsdtExchange(price);
		RedisUtil.set(SystemConstant.CACHE_SYS_EXCHANGE + ServiceConstant.SYSTEM_PAYCONFIG_USDTEXCHANGE, price.toString());
		sysokxmapper.deleteAll();
		for (int i = 0; i < 70; i++) {
			Sysokx tt = new Sysokx();
			tt.setName(BeanUtil.beanToMap(array.get(i)).get("nickName").toString());
			tt.setPrice(Double.valueOf(BeanUtil.beanToMap(array.get(i)).get("price").toString()));
			tt.setType(BeanUtil.beanToMap(array.get(i)).get("paymentMethods").toString());
			sysokxmapper.post(tt);
		}
	}

}