package com.yt.app.common.bot.message.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yt.app.common.bot.message.UpdateService;
import com.yt.app.common.bot.message.Keyboard.InlineKeyboard;

@Component
public class EnergyFlashRentMessage implements UpdateService {

	@Override
	public SendMessage getUpdate(Update update) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId().toString());
		sendMessage.setText("【🔋1小时闪租】\r\n" + "🔶转账  3 TRX=  1 笔能量\r\n" + "🔶转账  6 TRX=  2 笔能量\r\n"
				+ "1.向无U地址转账，需要双倍能量。\r\n" + "2.请在1小时内转账，否则过期回收。\r\n" + "\r\n" + "🌈转账到下面地址6秒自动到账能量:\r\n"
				+ "`TUrntwm5t9umKhC7jv89RXGo33qcTFAAAA` \r\n" + "（点击地址自动复制）\r\n" + "注意:\r\n" + "⚠️（不要使用交易所转账）\r\n"
				+ "⚠️按照指定金额转账，否则租用失败。");
		sendMessage.enableMarkdown(true);
		sendMessage.setReplyMarkup(InlineKeyboard.getInlineKeyboardMarkup());
		return sendMessage;
	}
}
