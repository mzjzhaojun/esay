package com.yt.app.common.bot.message.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Tgchannelgroup;
import com.yt.app.api.v1.entity.Tgmerchantgroup;
import com.yt.app.api.v1.mapper.TgchannelgroupMapper;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.bot.message.UpdateMerchantMessageService;
import com.yt.app.common.util.DateTimeUtil;

/**
 * 商户余额
 * 
 * @author zj
 *
 */
@Component
public class ChannelBalanceMessage implements UpdateMerchantMessageService {

	@Autowired
	private TgchannelgroupMapper tgchannelgroupmapper;

	@Override
	public SendMessage getUpdate(Update update, Tgmerchantgroup tmg) {
		SendMessage sendMessage = new SendMessage();
		return sendMessage;
	}

	public SendMessage getUpdate(Channel c) {
		SendMessage sendMessage = new SendMessage();
		TenantIdContext.removeFlag();
		Tgchannelgroup tmg = tgchannelgroupmapper.getByChannelId(c.getId());
		if (tmg != null) {
			sendMessage.setChatId(tmg.getTgid());
			sendMessage.setText("渠道:*" + c.getName() + "*\r\n\r\n今日支付：" + c.getTodayincomecount() + " \r\n今日收入：" + c.getTodaycount()+ " \r\n今日结算：" + (c.getTodayincomecount()-c.getTodaycount()) + "\r\n总共支付：" + c.getIncomecount() + "\r\n\r\n*" + DateTimeUtil.getDateTime() + "*");
			sendMessage.enableMarkdown(true);
		} else {
			return null;
		}
		return sendMessage;
	}
}
