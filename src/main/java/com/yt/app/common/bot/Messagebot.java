package com.yt.app.common.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.yt.app.api.v1.entity.Tgbotgroup;
import com.yt.app.api.v1.mapper.TgbotgroupMapper;
import com.yt.app.common.base.context.BeanContext;
import com.yt.app.common.base.context.TenantIdContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Messagebot extends TelegramLongPollingBot {

	private TgbotgroupMapper tgbotgroupmapper;

	private String name;
	private String token;

	public Messagebot() {
	}

	public Messagebot(String _name, String _token) {
		tgbotgroupmapper = BeanContext.getApplicationContext().getBean(TgbotgroupMapper.class);
		name = _name;
		token = _token;
	}

	@Override
	public String getBotUsername() {
		return name;
	}

	@Override
	public String getBotToken() {
		return token;
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update != null) {
			Long chatid = update.getMessage().getChat().getId();
			if (update.getMessage() != null) {
				TenantIdContext.setTenantId(1720395906240614400L);
				String msg = update.getMessage().getText();
				log.info("message:" + msg);
				Tgbotgroup tmg = tgbotgroupmapper.getByTgGroupId(chatid);
				// 第一次加入
				if (tmg == null) {
					Tgbotgroup t = new Tgbotgroup();
					t.setTgid(chatid);
					t.setStatus(true);
					t.setTgname(update.getMessage().getChat().getTitle());
					tgbotgroupmapper.post(t);
					sendText(chatid, "谢谢您把我加入群，请把我设置成管理员权限！");
				}
				// 回复的消息体
				Message replymsg = update.getMessage().getReplyToMessage();
				log.info(replymsg.getText());
				///////////////////////////////////////////////////////////////////////////////////////////////// 常用指令///////////////////////////
				if (msg.indexOf("+") == 0) {
					String str = msg.substring(1);
					log.info("str:" + str);
					if (str.matches("-?\\d+(\\.\\d+)?")) {
						Double amout = Double.parseDouble(str);
						sendText(chatid, "入款：" + amout + "");
					} else if (str.indexOf("/") != -1) {
						Double amout = Double.parseDouble(str.substring(0, str.indexOf("/")));
						Double exchange = Double.parseDouble(str.substring(str.indexOf("/") + 1));
						sendText(chatid, "入款：" + amout + ",汇率：" + exchange);
					}
				} else if (msg.indexOf("-") == 0) {
					String str = msg.substring(1);
					log.info("str:" + str);
					if (str.matches("-?\\d+(\\.\\d+)?")) {
						Double amout = Double.parseDouble(str);
						sendText(chatid, "出款：" + amout + "");
					} else if (str.indexOf("/") != -1) {
						Double amout = Double.parseDouble(str.substring(0, str.indexOf("/")));
						Double exchange = Double.parseDouble(str.substring(str.indexOf("/") + 1));
						sendText(chatid, "出款：" + amout + ",汇率：" + exchange);
					}
				} else if (msg.indexOf("回") == 0) {
					String str = msg.substring(1);
					log.info("str:" + str);
					if (str.matches("-?\\d+(\\.\\d+)?")) {
						Double amout = Double.parseDouble(str);
						sendText(chatid, "下发：" + amout + "");
					}
				} else if (msg.indexOf("下发") == 0) {
					String str = msg.substring(2);
					log.info("str:" + str);
					if (str.matches("-?\\d+(\\.\\d+)?")) {
						Double amout = Double.parseDouble(str);
						sendText(chatid, "下发：" + amout + "");
					}
				} else if (msg.equals("上课")) {
					sendText(chatid, "开始上课");

				} else if (msg.equals("下课")) {
					sendText(chatid, "下课结束");

				} else if (msg.equals("账单")) {
					sendText(chatid, "显示账单");

				} else if (msg.equals("我的账单")) {
					sendText(chatid, "仅显示我的账单");

				} else if (msg.equals("他的账单")) {
					sendText(chatid, "仅显示他的账单");

				} else if (msg.equals("统计")) {
					sendText(chatid, "显示统计账单 ");

				} else if (msg.equals("按用戶统计")) {
					sendText(chatid, "显示用戶统计账单 ");

				} else if (msg.equals("按汇率统计")) {
					sendText(chatid, "显示汇率统计账单 ");

				} else if (msg.indexOf("设置费率") == 0) {///////////////////////////////////////////////////////////////////////////////////////////// 设置指令////////////////////////////////////////////////
					String str = msg.substring(msg.indexOf("率") + 1, msg.indexOf("%"));
					log.info("str:" + str);
					if (str.matches("-?\\d+(\\.\\d+)?")) {
						Integer amout = Integer.parseInt(str);
						sendText(chatid, "费率：" + amout + "%,设置成功。");
					}
				} else if (msg.indexOf("设置汇率") == 0) {
					String str = msg.substring(msg.indexOf("率") + 1);
					log.info("str:" + str);
					if (str.matches("-?\\d+(\\.\\d+)?")) {
						Double amout = Double.parseDouble(str);
						sendText(chatid, "汇率：" + amout + ",设置成功。");
					}
				} else if (msg.equals("设置实时汇率")) {
					sendText(chatid, "实时汇率设置成功。");

				} else if (msg.indexOf("设置操作人") == 0) {
					String str = msg.substring(msg.indexOf("人") + 2);
					log.info("str:" + str);
					if (str.indexOf("@") == 0) {
						sendText(chatid, "操作人：" + str + ",设置成功。");
					}
				} else if (msg.indexOf("删除操作人") == 0) {
					String str = msg.substring(msg.indexOf("人") + 2);
					log.info("str:" + str);
					if (str.indexOf("@") == 0) {
						sendText(chatid, "操作人：" + str + ",刪除成功。");
					}
				} else if (msg.equals("清空操作人")) {
					sendText(chatid, "清空操作人成功。");

				} else if (msg.equals("显示操作人")) {
					sendText(chatid, "显示操作人成功。");

				} else if (msg.indexOf("设置全局操作人") == 0) {
					String str = msg.substring(msg.indexOf("人") + 2);
					log.info("str:" + str);
					if (str.indexOf("@") == 0) {
						sendText(chatid, "全局操作人：" + str + ",设置成功。");
					}
				} else if (msg.indexOf("删除全局操作人") == 0) {
					String str = msg.substring(msg.indexOf("人") + 2);
					log.info("str:" + str);
					if (str.indexOf("@") == 0) {
						sendText(chatid, "全局操作人：" + str + ",刪除成功。");
					}
				} else if (msg.equals("清空全局操作人")) {
					sendText(chatid, "清空全局操作人成功。");

				} else if (msg.equals("显示全局操作人")) {
					sendText(chatid, "显示全局操作人成功。");

				}

				TenantIdContext.remove();
			}
		}
	}

	// 发送消息
	public Message sendText(Long who, String what) {
		SendMessage sm = SendMessage.builder().chatId(who.toString()).text(what).build();
		try {
			Message msg = execute(sm);
			return msg;
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

	// 发送回复消息
	public void sendReplyText(Long who, Integer replyid, String what) {
		SendMessage sm = SendMessage.builder().chatId(who.toString()).text(what).replyToMessageId(replyid).build();
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
