package com.yt.app.common.bot.message.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.bot.message.UpdateService;
import com.yt.app.common.bot.message.Keyboard.InlineKeyboard;
import com.yt.app.common.util.RedisUtil;

@Component
public class TrxFlashRentMessage implements UpdateService {

	@Override
	public SendMessage getUpdate(Update update) {
		Double price = Double.valueOf(RedisUtil.get(SystemConstant.CACHE_SYS_EXCHANGE + ServiceConstant.SYSTEM_PAYCONFIG_USDTOTEXCHANGE));
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId().toString());
		sendMessage.setText("*实时汇率*\r\n" + "1 USDT = " + (price - 0.65) + " TRX  \r\n" + "\r\n" + "自动兑换地址:\r\n" + "`TUrntwm5t9umKhC7jv89RXGo33qcTFAAAA` (点击地址自动复制)\r\n" + "\r\n" + "请不要使用交易所转账‼️\r\n" + "切记切记，否则丢失自负‼️\r\n" + "\r\n"
				+ "转账即兑，全自动返，等值 10u 起换\r\n" + "\r\n" + "选择兑换数量\r\n" + "例如: “10U” 可实时计算10U可兑换的TRX数量=" + (10 * (price - 0.65)) + "\r\n");
		sendMessage.enableMarkdown(true);
		sendMessage.setReplyMarkup(InlineKeyboard.getInlineKeyboardMarkup());
		return sendMessage;
	}

	public EditMessageText excuteEditMessage(Update update) {

		MaybeInaccessibleMessage originalMessage = update.getCallbackQuery().getMessage();
		User sender = update.getCallbackQuery().getFrom();

		EditMessageText editmessagetext = new EditMessageText();
		editmessagetext.setChatId(String.valueOf(sender.getId()));
		editmessagetext.setMessageId(originalMessage.getMessageId());
		editmessagetext.setText("12333333333333333333333");
		editmessagetext.setReplyMarkup(InlineKeyboard.getInlineKeyboardMarkup2());
		return editmessagetext;
	}

	public EditMessageReplyMarkup excuteExchange(Update update) {
		MaybeInaccessibleMessage originalMessage = update.getCallbackQuery().getMessage();
		User sender = update.getCallbackQuery().getFrom();

		EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
		editMessageReplyMarkup.setChatId(String.valueOf(sender.getId()));
		editMessageReplyMarkup.setMessageId(originalMessage.getMessageId());
		editMessageReplyMarkup.setReplyMarkup(InlineKeyboard.getInlineKeyboardMarkup2());

		return editMessageReplyMarkup;
	}
}
