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
import com.yt.app.api.v1.entity.Tgconfig;
import com.yt.app.api.v1.entity.Tgmerchantgroup;
import com.yt.app.api.v1.mapper.TgconfigMapper;
import com.yt.app.api.v1.mapper.TgmerchantgroupMapper;
import com.yt.app.api.v1.service.PayconfigService;
import com.yt.app.common.base.context.TenantIdContext;

@Component
public class Tbot extends TelegramLongPollingBot {

	@Autowired
	private TgmerchantgroupMapper tgmerchantgroupmapper;

	@Autowired
	private TgconfigMapper tgconfigmapper;

	@Autowired
	private PayconfigService payconfigservice;

	@Override
	public String getBotUsername() {
		return "payyds_bot";
	}

	@Override
	public String getBotToken() {
		return "6432595042:AAFsycyy8Yjc32RFaCFA6r6aL4aiajoFgzk";
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update.getMessage().getChat().isSuperGroupChat()) {
			Long chatid = update.getMessage().getChat().getId();
			TenantIdContext.removeFlag();
			Tgmerchantgroup tmg = tgmerchantgroupmapper.getByTgGroupName(update.getMessage().getChat().getTitle());
			// 根据tgid查询
			if (tmg == null) {
				tmg = tgmerchantgroupmapper.getByTgGroupId(chatid);
			}
			if (tmg != null && tmg.getTgid() == null) {
				tmg.setTgid(chatid);
				tgmerchantgroupmapper.put(tmg);

			}
			String message = update.getMessage().getText();
			switch (message) {
			case "lx":
				List<Payconfig> list = payconfigservice.getDatas();
				StringBuffer sb = new StringBuffer();
				list.forEach(pc -> {
					sb.append("商戶：" + pc.getName() + "，价格：" + pc.getExchange()+"\n");
				});

				sendText(tmg.getTgid(), sb.toString());
				break;
			case "充值":
				Tgconfig tgc = tgconfigmapper.getByNmae(message);
				if (tgc != null) {
					sendText(tmg.getTgid(), tgc.getSetvalues());
				} else {
					sendText(tmg.getTgid(), "系统出错了！");
				}
				break;
			case "催单":
				sendText(tmg.getTgid(), "返回代付列表id");
				break;
			case "cid":
				sendText(tmg.getTgid(), "已经在催促处理了，请老板稍后查看结果！");
				break;
			case "充值完成":
				sendText(tmg.getTgid(), "后台正在审核充值状态，请稍等...");
				break;
			default:
				if (message.indexOf("cs") != -1) {
					sendText(tmg.getTgid(), "当前订单【" + message + "】正在代付中，已经联系通道部门加速处理！");
				} else {
					sendText(tmg.getTgid(), "不能识别的指令！");
				}
				break;
			}
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
