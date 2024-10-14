package com.yt.app.common.bot.message.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.bot.message.UpdateService;
import com.yt.app.common.bot.message.Keyboard.InlineKeyboard;
import com.yt.app.common.util.RedisUtil;

@Component
public class MemberFlashExchangeMessage implements UpdateService {

	@Override
	public SendMessage getUpdate(Update update) {
		Double price = Double.valueOf(RedisUtil.get(SystemConstant.CACHE_SYS_EXCHANGE + ServiceConstant.SYSTEM_PAYCONFIG_USDTOTEXCHANGE));
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId().toString());
		sendMessage.setText("*实时汇率*\r\n" + "1 USDT = " + (price - 0.65) + " TRX  \r\n" + "100 TRX = " + 100 / (price + 0.65) + " USDT   \r\n" + "\r\n" + "自动兑换地址:\r\n" + "`TUrntwm5t9umKhC7jv89RXGo33qcTFAAAA` (点击地址自动复制)\r\n" + "\r\n"
				+ "请不要使用交易所转账‼️\r\n" + "切记切记，否则丢失自负‼️\r\n" + "\r\n" + "转账即兑，全自动返，等值 1u 起换\r\n" + "\r\n" + "输入兑换数量\r\n" + "例如: “10U” 可实时计算10U可兑换的TRX数量; \r\n" + "例如: “100TRX” 可实时计算100TRX可兑换的U数量;");
		sendMessage.enableMarkdown(true);
		sendMessage.setReplyMarkup(InlineKeyboard.getInlineKeyboardMarkup());
		return sendMessage;
	}

}
