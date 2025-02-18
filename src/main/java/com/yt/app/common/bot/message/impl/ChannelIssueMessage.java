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
import com.yt.app.api.v1.service.QrcodeaccountorderService;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.bot.message.UpdateChannelMessageService;
import com.yt.app.common.util.DateTimeUtil;

/**
 * 商户余额
 * 
 * @author zj
 *
 */
@Component
public class ChannelIssueMessage implements UpdateChannelMessageService {

	@Autowired
	private QrcodeaccountMapper qrcodeaccountmapper;

	@Autowired
	private ChannelMapper channelmapper;

	@Autowired
	private QrcodeaccountorderService qrcodeaccountorderservice;

	@Override
	public SendMessage getUpdate(Update update, Tgchannelgroup tcg) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId().toString());
		if (tcg.getChannelids() != null) {
			String username = update.getMessage().getFrom().getUserName();
			if (username.equals(tcg.getMangers())) {
				TenantIdContext.removeFlag();
				StringBuffer msg = new StringBuffer();
				String[] text = update.getMessage().getText().split(" ");
				if (text.length == 3) {
					String merchantname = text[1];
					for (Long cid : tcg.getChannelids()) {
						Channel c = channelmapper.get(cid);
						if (c.getName().equals(merchantname)) {
							qrcodeaccountorderservice.incomewithdrawTelegram(c, Double.valueOf(text[2]));
						}
						Qrcodeaccount qrcodeaccount = qrcodeaccountmapper.getByUserId(c.getUserid());
						msg.append("\r\n渠道：*" + c.getName() + "*\r\n\r\n今日收入：" + c.getTodaycount() + " \r\n总共下发：" + qrcodeaccount.getWithdrawamount() + "\r\n总共收入：" + qrcodeaccount.getTotalincome() + " \r\n可用余额：" + qrcodeaccount.getBalance()
								+ "\r\n  \r\n*" + DateTimeUtil.getDateTime() + "*");
					}
					sendMessage.setText(msg.toString());
					sendMessage.enableMarkdown(true);
				} else {
					sendMessage.setText("下发格式 下发 渠道名 金额");
				}
			} else {
				sendMessage.setText("你没有操作权限");
			}
		} else {
			sendMessage.setText("系统还没有绑定渠道");
		}
		return sendMessage;
	}

}
