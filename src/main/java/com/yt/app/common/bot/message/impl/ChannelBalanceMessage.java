package com.yt.app.common.bot.message.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Qrcodeaccount;
import com.yt.app.api.v1.entity.Tgchannelgroup;
import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.QrcodeaccountMapper;
import com.yt.app.api.v1.mapper.TgchannelgroupMapper;
import com.yt.app.common.bot.message.UpdateChannelMessageService;
import com.yt.app.common.util.DateTimeUtil;

/**
 * 商户余额
 * 
 * @author zj
 *
 */
@Component
public class ChannelBalanceMessage implements UpdateChannelMessageService {

	@Autowired
	private TgchannelgroupMapper tgchannelgroupmapper;

	@Autowired
	private ChannelMapper channelmapper;

	@Autowired
	private QrcodeaccountMapper qrcodeaccountmapper;

	@Override
	public SendMessage getUpdate(Update update, Tgchannelgroup tcg) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId().toString());
		if (tcg.getChannelids() != null) {
			StringBuffer msg = new StringBuffer();
			for (Long mid : tcg.getChannelids()) {
				Channel c = channelmapper.get(mid);
				Qrcodeaccount qrcodeaccount = qrcodeaccountmapper.getByUserId(c.getUserid());
				msg.append("\r\n渠道：*" + c.getName() + "*\r\n\r\n今日利润：" + c.getTodaycount() + " \r\n今日收入：" + c.getTodayincomecount()+ " \r\n总共下发：" + qrcodeaccount.getWithdrawamount() + "\r\n总共收入：" + qrcodeaccount.getTotalincome() + " \r\n可用余额：" + qrcodeaccount.getBalance() + "\r\n  \r\n*"
						+ DateTimeUtil.getDateTime() + "*");
			}
			sendMessage.setText(msg.toString());
			sendMessage.enableMarkdown(true);
		} else {
			sendMessage.setText("系统还没有绑定商户");
		}
		return sendMessage;
	}

	public SendMessage getUpdate(Channel c) {
		SendMessage sendMessage = new SendMessage();
		Tgchannelgroup tmg = tgchannelgroupmapper.getByChannelId(c.getId());
		if (tmg != null) {
			sendMessage.setChatId(tmg.getTgid());
			sendMessage.setText("渠道:*" + c.getName() + "标签:" + c.getNkname() + "*\r\n\r\n今日收入：" + c.getTodayincomecount() + " \r\n今日利润：" + c.getTodaycount() + " \r\n今日结算：" + (c.getTodayincomecount() - c.getTodaycount()) + "\r\n总共支付："
					+ c.getIncomecount() + "\r\n\r\n*" + DateTimeUtil.getDateTime() + "*");
			sendMessage.enableMarkdown(true);
		} else {
			return null;
		}
		return sendMessage;
	}
}
