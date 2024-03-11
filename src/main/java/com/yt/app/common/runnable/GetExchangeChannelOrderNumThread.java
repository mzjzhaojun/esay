package com.yt.app.common.runnable;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Exchange;
import com.yt.app.api.v1.entity.Tgchannelgroup;
import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.ExchangeMapper;
import com.yt.app.api.v1.mapper.TgchannelgroupMapper;
import com.yt.app.common.base.context.BeanContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.bot.Channelbot;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.DateTimeUtil;
import com.yt.app.common.util.PayUtil;
import com.yt.app.common.util.StringUtil;

public class GetExchangeChannelOrderNumThread implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(GetPayoutChannelOrderNumThread.class);

	private Long id;

	public GetExchangeChannelOrderNumThread(Long _id) {
		id = _id;
	}

	@Override
	public void run() {
		TenantIdContext.removeFlag();
		ExchangeMapper mapper = BeanContext.getApplicationContext().getBean(ExchangeMapper.class);
		ChannelMapper channelmapper = BeanContext.getApplicationContext().getBean(ChannelMapper.class);
		TgchannelgroupMapper tgchannelgroupmapper = BeanContext.getApplicationContext()
				.getBean(TgchannelgroupMapper.class);
		Channelbot cbot = BeanContext.getApplicationContext().getBean(Channelbot.class);
		Exchange exchange = mapper.get(id);
		Channel channel = channelmapper.get(exchange.getChannelid());
		Random rd = new Random();
		logger.info("换汇获取渠道单号 start---------------------商户单号：" + exchange.getMerchantordernum());
		int i = 1;
		while (true) {
			try {
				String channelordernum = "EC" + StringUtil.getOrderNum();
				if (channel.getIfordernum()) {
					channelordernum = PayUtil.SendTySubmit(exchange, channel);
				}
				// 获取到单号
				exchange.setChannelordernum(channelordernum);
				exchange.setStatus(DictionaryResource.PAYOUTSTATUS_51);
				int j = mapper.put(exchange);
				if (j > 0) {
					Tgchannelgroup tgchannelgroup = tgchannelgroupmapper.getByChannelId(exchange.getChannelid());
					StringBuffer what = new StringBuffer();
					what.append("状态：新增代付\n");
					what.append("单号：" + exchange.getChannelordernum() + "\n");
					what.append("姓名：" + exchange.getAccname() + "\n");
					what.append("卡号：" + exchange.getAccnumer() + "\n");
					what.append("金额：" + exchange.getAmount() + "\n");
					what.append("发起时间：" + DateTimeUtil.getDateTime() + "\n");
					what.append("客户请你们尽快处理\n");
					if (tgchannelgroup != null) {
						cbot.sendText(tgchannelgroup.getTgid(), what.toString());
					}
				}
				break;
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					Thread.sleep(1000 * rd.nextInt(60));
					i++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (i > 3) {
				exchange.setStatus(DictionaryResource.PAYOUTSTATUS_54);
				mapper.put(exchange);
				break;
			}
		}
		logger.info("获取 end---------------------");
	}

}
