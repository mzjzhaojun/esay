package com.yt.app.common.runnable;

import java.util.HashMap;
import java.util.List;

import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Qrcode;
import com.yt.app.api.v1.entity.Systemaccount;
import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.QrcodeMapper;
import com.yt.app.api.v1.mapper.SystemaccountMapper;
import com.yt.app.api.v1.service.ChannelService;
import com.yt.app.api.v1.service.MerchantService;
import com.yt.app.api.v1.service.QrcodeService;
import com.yt.app.api.v1.service.SystemstatisticalreportsService;
import com.yt.app.common.base.context.BeanContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.resource.DictionaryResource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StatisticsThread implements Runnable {

	private String dateval;

	public StatisticsThread(String _dateval) {
		dateval = _dateval;
	}

	@Override
	public void run() {

		try {
			log.info("start Statistics");
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}

		TenantIdContext.removeFlag();
		SystemstatisticalreportsService systemstatisticalreportsservice = BeanContext.getApplicationContext().getBean(SystemstatisticalreportsService.class);
		MerchantService merchantservice = BeanContext.getApplicationContext().getBean(MerchantService.class);
		MerchantMapper merchantmapper = BeanContext.getApplicationContext().getBean(MerchantMapper.class);
		SystemaccountMapper systemaccountmapper = BeanContext.getApplicationContext().getBean(SystemaccountMapper.class);
		ChannelMapper channelmapper = BeanContext.getApplicationContext().getBean(ChannelMapper.class);
		QrcodeMapper qrcodemapper = BeanContext.getApplicationContext().getBean(QrcodeMapper.class);
		ChannelService channelservice = BeanContext.getApplicationContext().getBean(ChannelService.class);
		QrcodeService qrcodeservice = BeanContext.getApplicationContext().getBean(QrcodeService.class);
		// 系统
		List<Systemaccount> listsa = systemaccountmapper.list(new HashMap<String, Object>());
		listsa.forEach(sa -> {
			// 单日数据
			systemstatisticalreportsservice.updateDayValue(dateval, sa);
		});

		// 商户
		List<Merchant> listm = merchantmapper.list(new HashMap<String, Object>());
		listm.forEach(m -> {
			// 单日数据
			if (m.getTodaycount() > 1) {
				//代收
				if (m.getType() == DictionaryResource.MERCHANT_TYPE_IN)
					merchantservice.updateIncome(m, dateval);
				//代付
				else if (m.getType() == DictionaryResource.MERCHANT_TYPE_OUT)
					merchantservice.updatePayout(m, dateval);
			}
		});
		// 渠道
		List<Channel> listc = channelmapper.list(new HashMap<String, Object>());
		listc.forEach(c -> {
			// 单日数据
			if (c.getTodaycount() > 1)
				channelservice.updateDayValue(c, dateval);
		});
		// 码商
		List<Qrcode> listq = qrcodemapper.list(new HashMap<String, Object>());
		listq.forEach(q -> {
			// 单日数据
			if (q.getTodayincome() > 1)
				qrcodeservice.updateDayValue(q, dateval);
		});
	}

}
