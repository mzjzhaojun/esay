package com.yt.app.common.runnable;

import java.util.HashMap;
import java.util.List;

import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.service.ChannelService;
import com.yt.app.api.v1.service.MerchantService;
import com.yt.app.api.v1.service.SystemstatisticalreportsService;
import com.yt.app.common.base.context.BeanContext;

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
			Thread.sleep(60000 * 15);
		} catch (InterruptedException e) {
		}

		SystemstatisticalreportsService systemstatisticalreportsservice = BeanContext.getApplicationContext().getBean(SystemstatisticalreportsService.class);
		MerchantService merchantservice = BeanContext.getApplicationContext().getBean(MerchantService.class);
		MerchantMapper merchantmapper = BeanContext.getApplicationContext().getBean(MerchantMapper.class);
		ChannelMapper channelmapper = BeanContext.getApplicationContext().getBean(ChannelMapper.class);
		ChannelService channelservice = BeanContext.getApplicationContext().getBean(ChannelService.class);
		// 系统
		systemstatisticalreportsservice.updateDayValue(dateval);
		// 商户
		List<Merchant> listm = merchantmapper.list(new HashMap<String, Object>());
		listm.forEach(m -> {
			// 单日数据
			merchantservice.updateDayValue(m, dateval);
		});
		// 渠道
		List<Channel> listc = channelmapper.list(new HashMap<String, Object>());
		listc.forEach(c -> {
			// 单日数据
			channelservice.updateDayValue(c, dateval);
		});
	}

}
