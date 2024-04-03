package com.yt.app.common.bot;

import java.util.List;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.yt.app.api.v1.entity.Tgbotgroup;
import com.yt.app.api.v1.entity.Tgbotgrouprecord;
import com.yt.app.api.v1.mapper.TgbotgroupMapper;
import com.yt.app.api.v1.mapper.TgbotgrouprecordMapper;
import com.yt.app.common.base.context.BeanContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.DateTimeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Messagebot extends TelegramLongPollingBot {

	private TgbotgroupMapper tgbotgroupmapper;
	private TgbotgrouprecordMapper tgbotgrouprecordmapper;

	private String name;
	private String token;

	public Messagebot() {
	}

	public Messagebot(String _name, String _token) {
		tgbotgroupmapper = BeanContext.getApplicationContext().getBean(TgbotgroupMapper.class);
		tgbotgrouprecordmapper = BeanContext.getApplicationContext().getBean(TgbotgrouprecordMapper.class);
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
		log.info("messageupdate:" + update);
		if (update != null) {
			Long chatid = update.getMessage().getChat().getId();
			if (update.getMessage() != null) {
				TenantIdContext.setTenantId(1720395906240614400L);
				String msg = update.getMessage().getText();
				String from = update.getMessage().getFrom().getFirstName();
				String replyname = from;
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
				if (replymsg != null) {
					replyname = replymsg.getFrom().getFirstName();
				}
				///////////////////////////////////////////////////////////////////////////////////////////////// 常用指令///////////////////////////
				if (msg.indexOf("+") == 0) {
					String str = msg.substring(1);
					log.info("str:" + str);
					if (str.matches("-?\\d+(\\.\\d+)?")) {
						Double amount = Double.parseDouble(str);
						income(tmg, amount, from, replyname);
						sendText(chatid, getOrder(tmg));
					} else if (str.indexOf("/") != -1) {
						Double amount = Double.parseDouble(str.substring(0, str.indexOf("/")));
						// Double exchange = Double.parseDouble(str.substring(str.indexOf("/") + 1));
						income(tmg, amount, from, replyname);
						sendText(chatid, getOrder(tmg));
					}
				} else if (msg.indexOf("-") == 0) {
					String str = msg.substring(1);
					log.info("str:" + str);
					if (str.matches("-?\\d+(\\.\\d+)?")) {
						Double amount = Double.parseDouble(str);
						withdraw(tmg, amount, from, replyname);
						sendText(chatid, getOrder(tmg));
					} else if (str.indexOf("/") != -1) {
						Double amount = Double.parseDouble(str.substring(0, str.indexOf("/")));
						// Double exchange = Double.parseDouble(str.substring(str.indexOf("/") + 1));
						withdraw(tmg, amount, from, replyname);
						sendText(chatid, getOrder(tmg));
					}
				} else if (msg.indexOf("回") == 0) {
					String str = msg.substring(1);
					log.info("str:" + str);
					if (str.matches("-?\\d+(\\.\\d+)?")) {
						Double amount = Double.parseDouble(str);
						outusdt(tmg, amount, from, replyname);
						sendText(chatid, getOrder(tmg));
					}
				} else if (msg.indexOf("下发") == 0) {
					String str = msg.substring(2);
					log.info("str:" + str);
					if (str.matches("-?\\d+(\\.\\d+)?")) {
						Double amount = Double.parseDouble(str);
						outusdt(tmg, amount, from, replyname);
						sendText(chatid, getOrder(tmg));
					}
				} else if (msg.equals("上课")) {
					sendText(chatid, "开始上课");

				} else if (msg.equals("下课")) {
					sendText(chatid, "下课结束");

				} else if (msg.equals("账单")) {
					sendText(chatid, getOrder(tmg));

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
						Integer cost = Integer.parseInt(str);
						tmg.setCost(cost);
						if (tgbotgroupmapper.put(tmg) > 0)
							sendText(chatid, "费率：" + cost + "%,设置成功。");
					}
				} else if (msg.indexOf("设置汇率") == 0) {
					String str = msg.substring(msg.indexOf("率") + 1);
					log.info("str:" + str);
					if (str.matches("-?\\d+(\\.\\d+)?")) {
						Double exchange = Double.parseDouble(str);
						tmg.setExchange(exchange);
						tmg.setTmexchange(false);
						if (tgbotgroupmapper.put(tmg) > 0)
							sendText(chatid, "汇率：" + exchange + ",设置成功。");
					}
				} else if (msg.equals("设置实时汇率")) {
					tmg.setTmexchange(true);
					if (tgbotgroupmapper.put(tmg) > 0)
						sendText(chatid, "实时汇率设置成功。");

				} else if (msg.indexOf("设置操作人") == 0) {
					String str = msg.substring(msg.indexOf("人") + 2);
					log.info("str:" + str);
					if (str.indexOf("@") == 0) {
						tmg.setGmanger(str);
						if (tgbotgroupmapper.put(tmg) > 0)
							sendText(chatid, "操作人：" + str + ",设置成功。");
					}
				} else if (msg.indexOf("删除操作人") == 0) {
					String str = msg.substring(msg.indexOf("人") + 2);
					log.info("str:" + str);
					if (str.indexOf("@") == 0) {
						tmg.setGmanger("");
						if (tgbotgroupmapper.put(tmg) > 0)
							sendText(chatid, "操作人：" + str + ",刪除成功。");
					}
				} else if (msg.equals("清空操作人")) {
					tmg.setGmanger("");
					if (tgbotgroupmapper.put(tmg) > 0)
						sendText(chatid, "清空操作人成功。");

				} else if (msg.equals("显示操作人")) {
					sendText(chatid, "显示操作人：" + tmg.getGmanger());

				} else if (msg.indexOf("设置全局操作人") == 0) {
					String str = msg.substring(msg.indexOf("人") + 2);
					log.info("str:" + str);
					if (str.indexOf("@") == 0) {
						tmg.setXmanger(str);
						if (tgbotgroupmapper.put(tmg) > 0)
							sendText(chatid, "全局操作人：" + str + ",设置成功。");
					}
				} else if (msg.indexOf("删除全局操作人") == 0) {
					String str = msg.substring(msg.indexOf("人") + 2);
					log.info("str:" + str);
					if (str.indexOf("@") == 0) {
						tmg.setXmanger("");
						if (tgbotgroupmapper.put(tmg) > 0)
							sendText(chatid, "全局操作人：" + str + ",刪除成功。");
					}
				} else if (msg.equals("清空全局操作人")) {
					tmg.setGmanger("");
					if (tgbotgroupmapper.put(tmg) > 0)
						sendText(chatid, "清空全局操作人成功。");

				} else if (msg.equals("显示全局操作人")) {
					sendText(chatid, "显示全局操作人：" + tmg.getXmanger());

				}

				TenantIdContext.remove();
			}
		}
	}

	public Integer income(Tgbotgroup tbg, Double amount, String gmanger, String xmanger) {
		Tgbotgrouprecord tgr = new Tgbotgrouprecord();
		tgr.setAmount(amount);
		tgr.setTgid(tbg.getTgid());
		tgr.setExchange(tbg.getExchange());
		tgr.setCost(tbg.getCost());
		tgr.setType(DictionaryResource.TGBOTGROUPRECORD_TYPE_INCOME);
		tgr.setWithdrawusdt(amount / tgr.getExchange());
		tgr.setRemark("入款：" + amount + ",usdt:" + tgr.getWithdrawusdt());
		tgr.setXmanger(xmanger);
		tgr.setGmanger(gmanger);
		tgr.setStatus(true);
		tgr.setTmexchange(tbg.getTmexchange());
		return tgbotgrouprecordmapper.post(tgr);
	}

	public Integer withdraw(Tgbotgroup tbg, Double amount, String gmanger, String xmanger) {
		Tgbotgrouprecord tgr = new Tgbotgrouprecord();
		tgr.setAmount(amount);
		tgr.setTgid(tbg.getTgid());
		tgr.setExchange(tbg.getExchange());
		tgr.setCost(tbg.getCost());
		tgr.setType(DictionaryResource.TGBOTGROUPRECORD_TYPE_WITHDRAW);
		tgr.setWithdrawusdt(amount / tgr.getExchange());
		tgr.setRemark("减款：" + amount + ",usdt:" + tgr.getWithdrawusdt());
		tgr.setXmanger(xmanger);
		tgr.setGmanger(gmanger);
		tgr.setStatus(true);
		tgr.setTmexchange(tbg.getTmexchange());
		return tgbotgrouprecordmapper.post(tgr);
	}

	public Integer outusdt(Tgbotgroup tbg, Double amount, String gmanger, String xmanger) {
		Tgbotgrouprecord tgr = new Tgbotgrouprecord();
		tgr.setAmount(amount);
		tgr.setTgid(tbg.getTgid());
		tgr.setExchange(tbg.getExchange());
		tgr.setCost(tbg.getCost());
		tgr.setType(DictionaryResource.TGBOTGROUPRECORD_TYPE_USDT);
		tgr.setWithdrawusdt(amount);
		tgr.setRemark("下发：" + amount);
		tgr.setXmanger(xmanger);
		tgr.setGmanger(gmanger);
		tgr.setStatus(true);
		tgr.setTmexchange(tbg.getTmexchange());
		return tgbotgrouprecordmapper.post(tgr);
	}

	public String getOrder(Tgbotgroup tbg) {
		List<Tgbotgrouprecord> listincome = tgbotgrouprecordmapper.listByType(tbg.getTgid(),
				DictionaryResource.TGBOTGROUPRECORD_TYPE_INCOME);
		StringBuffer sb = new StringBuffer();
		Integer i = 1;
		double countincome = 0.00;
		double countusdt = 0.00;
		sb.append("入款：" + listincome.size() + " 笔\n");
		for (Tgbotgrouprecord tbgr : listincome) {
			sb.append(i + ":  " + DateTimeUtil.getDateTime(tbgr.getCreate_time(), DateTimeUtil.DEFAULT_TIME_FORMAT)
					+ "   " + tbgr.getAmount() + "/" + tbgr.getExchange() + "=" + tbgr.getWithdrawusdt() + "   >"
					+ tbgr.getGmanger() + "\n");
			countincome = countincome + tbgr.getAmount();
			countusdt = countusdt + tbgr.getWithdrawusdt();
			i++;
		}
		sb.append("\n");
		List<Tgbotgrouprecord> liswithdraw = tgbotgrouprecordmapper.listByType(tbg.getTgid(),
				DictionaryResource.TGBOTGROUPRECORD_TYPE_WITHDRAW);
		i = 1;
		double counwithdeaw = 0.00;
		double outusdt = 0.00;
		sb.append("减款：" + liswithdraw.size() + " 笔\n");
		for (Tgbotgrouprecord tbgr : liswithdraw) {
			sb.append(i + ":  " + DateTimeUtil.getDateTime(tbgr.getCreate_time(), DateTimeUtil.DEFAULT_TIME_FORMAT)
					+ "   " + tbgr.getAmount() + "/" + tbgr.getExchange() + "=" + tbgr.getWithdrawusdt() + "  >"
					+ tbgr.getGmanger() + "\n");
			counwithdeaw = counwithdeaw + tbgr.getAmount();
			outusdt = outusdt + tbgr.getWithdrawusdt();
			i++;
		}
		sb.append("\n");
		List<Tgbotgrouprecord> listusdt = tgbotgrouprecordmapper.listByType(tbg.getTgid(),
				DictionaryResource.TGBOTGROUPRECORD_TYPE_USDT);
		i = 1;
		double usdt = 0.00;
		sb.append("下发：" + listusdt.size() + " 笔\n");
		for (Tgbotgrouprecord tbgr : listusdt) {
			sb.append(i + ":   " + DateTimeUtil.getDateTime(tbgr.getCreate_time(), DateTimeUtil.DEFAULT_TIME_FORMAT)
					+ "    " + tbgr.getWithdrawusdt() + "  >" + tbgr.getGmanger() + "\n");
			usdt = usdt + tbgr.getWithdrawusdt();
			i++;
		}
		sb.append("\n");
		sb.append("费率：" + tbg.getCost() + "%\n");
		sb.append("汇率：" + tbg.getExchange() + "\n");
		sb.append("总入款：" + countincome + "\n");
		sb.append("总减款：" + counwithdeaw + "\n");
		sb.append("应下发：" + (countusdt - outusdt) + "\n");
		sb.append("已下发：" + usdt + "\n");
		sb.append("未下发：" + (countusdt - outusdt - usdt) + "\n");
		return sb.toString();
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
