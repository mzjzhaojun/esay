package com.yt.app.common.bot.message.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.bot.message.UpdateMessageService;
import com.yt.app.common.bot.message.Keyboard.InlineKeyboard;
import com.yt.app.common.util.RedisUtil;

@Component
public class MemberFlashExchangeMessage implements UpdateMessageService {

	@Override
	public SendMessage getUpdate(Update update) {
		Double price = Double.valueOf(RedisUtil.get(SystemConstant.CACHE_SYS_EXCHANGE +ServiceConstant.SYSTEM_PAYCONFIG_USDTEXCHANGE));
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId().toString());
		sendMessage.setText("*实时汇率*\r\n" + "1 USDT = " + (price) + " 人民币  \r\n" + "\r\n" + "自动兑换地址:\r\n" + "`TWXQjegKptQkfaGXA3m7V5A2AnMGT88888` (点击地址自动复制)\r\n" + "\r\n" + "\r\n" + "转账即兑，全自动返，等值 20u 起换\r\n" + "\r\n");
		sendMessage.enableMarkdown(true);
		sendMessage.setReplyMarkup(InlineKeyboard.getUpInlineKeyboardMarkup(update.getMessage().getChatId()));
		return sendMessage;
	}

}
