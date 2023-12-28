package com.yt.app.common.bot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.yt.app.api.v1.entity.Payconfig;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.entity.Tgchannelgroup;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.api.v1.mapper.TgchannelgroupMapper;
import com.yt.app.api.v1.service.PayconfigService;
import com.yt.app.common.base.context.TenantIdContext;

@Component
public class Cbot extends TelegramLongPollingBot {

	@Autowired
	private TgchannelgroupMapper tgchannelgroupmapper;

	@Autowired
	private PayconfigService payconfigservice;

	@Autowired
	private PayoutMapper payoutmapper;

	@Override
	public String getBotUsername() {
		return "rabbityyds_bot";
	}

	@Override
	public String getBotToken() {
		return "6649299837:AAGhyh19Q9KrDFmvZqopJ5t6kwKLuaANCds";
	}

	@Override
	public void onUpdateReceived(Update update) {
		Long chatid = update.getMessage().getChat().getId();
		TenantIdContext.removeFlag();
		Tgchannelgroup tmg = tgchannelgroupmapper.getByTgGroupId(chatid);
		String message = update.getMessage().getText();
		Integer replyid = update.getMessage().getMessageId();
		System.out.println("cccccccc" + update.toString());
		if (tmg == null) {
			tmg = tgchannelgroupmapper.getByTgGroupName(update.getMessage().getChat().getTitle());
			if (tmg != null) {
				tmg.setTgid(chatid);
				tgchannelgroupmapper.put(tmg);
				handlemessage(message, chatid, replyid);
			}
		} else {
			handlemessage(message, chatid, replyid);
		}
	}

	private void handlemessage(String message, Long chatid, Integer replyid) {
		if (message.equals("lx")) {// 汇率
			List<Payconfig> list = payconfigservice.getDatas();
			StringBuffer sb = new StringBuffer();
			Integer i = 1;
			for (Payconfig pc : list) {
				sb.append(i + "" + pc.getName() + "，价格:" + pc.getExchange() + "\n");
				i++;
			}
			sendText(chatid, sb.toString());
		} else if (message.equals("cz")) {// 充值
		} else if (message.indexOf("cd=") >= 0) {// 催单
			String orderno = message.substring(message.indexOf("="));
			Payout po = payoutmapper.getByOrdernum(orderno);
			String msg = "订单号：" + orderno + "\n  名字:" + po.getAccname() + "\n 卡号:" + po.getAccnumer()
					+ "\n 已经联系通道部加急处理，稍后查看回复你结果。";
			sendReplyText(chatid, replyid, msg);
		}
	}

	// 发送消息
	public void sendText(Long who, String what) {
		SendMessage sm = SendMessage.builder().chatId(who.toString()).text(what).build();
		try {
			execute(sm);
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

	// 发送消息
	public void sendReplyText(Long who, Integer replyid, String what) {
		SendMessage sm = SendMessage.builder().chatId(who.toString()).text(what).replyToMessageId(replyid).build();
		try {
			execute(sm);
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

	// 发送消息
	public void sendAtText(Long who, String what) {
		SendMessage sm = SendMessage.builder().chatId(who.toString()).text(what).build();
		try {
			execute(sm);
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

	// 复制消息
	public void copyMessage(Long who, Integer msgId) {
		CopyMessage cm = CopyMessage.builder().fromChatId(who.toString()).chatId(who.toString()).messageId(msgId)
				.build();
		try {
			execute(cm);
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

	// 转发
	public void forwardMessage(Long who, Integer msgId) {
		ForwardMessage cm = ForwardMessage.builder().fromChatId(who.toString()).chatId(who.toString()).messageId(msgId)
				.build();
		try {
			execute(cm);
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

	public void sendMenu(Long who, String txt, InlineKeyboardMarkup kb) {
		SendMessage sm = SendMessage.builder().chatId(who.toString()).parseMode("HTML").text(txt).replyMarkup(kb)
				.build();

		try {
			execute(sm);
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

}
