package com.yt.app.common.bot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.bot.message.impl.ExchangeMessage;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.DateTimeUtil;

@SuppressWarnings("deprecation")
@Component
public class BookAccountBot extends TelegramLongPollingBot {

	@Autowired
	private TgbotgroupMapper tgbotgroupmapper;
	@Autowired
	private TgbotgrouprecordMapper tgbotgrouprecordmapper;
	@Autowired
	private ExchangeMessage exchangemessage;

	@Override
	public String getBotUsername() {
		return " @feitujzbot";
	}

	@Override
	public String getBotToken() {
		return "7669769378:AAHuq1hNImYQQT6oxQCB5edTOx-YKmxBYKY";
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update != null) {
			Long chatid = update.getMessage().getChat().getId();
			if (update.getMessage().hasText()) {
				try {
					TenantIdContext.removeFlag();
					String msg = update.getMessage().getText().replaceAll(" ", "");
					String from = update.getMessage().getFrom().getFirstName();
					String replyname = from;
					Tgbotgroup tmg = tgbotgroupmapper.getByTgGroupId(chatid);
					if (tmg == null) {
						Tgbotgroup t = new Tgbotgroup();
						t.setTgid(chatid);
						t.setStatus(false);
						t.setTgname(update.getMessage().getChat().getTitle());
						tgbotgroupmapper.post(t);
						sendText(chatid, "飞兔机器人当前群Tgid:" + update.getMessage().getChatId() + "请給我管理员权限。");
					}
					// 回复的消息体
					Message replymsg = update.getMessage().getReplyToMessage();
					if (replymsg != null) {
						replyname = replymsg.getFrom().getFirstName();
					}
					if (tmg.getStatus()) {
						if (msg.startsWith("+")) {
							String str = msg.substring(1);
							if (str.matches("-?\\d+(\\.\\d+)?")) {
								Double amount = Double.parseDouble(str);
								income(tmg, amount, from, replyname);
								sendText(chatid, getOrder(tmg));
							} else if (str.indexOf("/") != -1) {
								Double amount = Double.parseDouble(str.substring(0, str.indexOf("/")));
								income(tmg, amount, from, replyname);
								sendText(chatid, getOrder(tmg));
							}
						} else if (msg.startsWith("-")) {
							String str = msg.substring(1);
							if (str.matches("-?\\d+(\\.\\d+)?")) {
								Double amount = Double.parseDouble(str);
								withdraw(tmg, amount, from, replyname);
								sendText(chatid, getOrder(tmg));
							} else if (str.indexOf("/") != -1) {
								Double amount = Double.parseDouble(str.substring(0, str.indexOf("/")));
								withdraw(tmg, amount, from, replyname);
								sendText(chatid, getOrder(tmg));
							}
						} else if (msg.startsWith("回")) {
							String str = msg.substring(1);
							if (str.matches("-?\\d+(\\.\\d+)?")) {
								Double amount = Double.parseDouble(str);
								outusdt(tmg, amount, from, replyname);
								sendText(chatid, getOrder(tmg));
							}
						} else if (msg.startsWith("下发")) {
							String str = msg.substring(2);
							if (str.matches("-?\\d+(\\.\\d+)?")) {
								Double amount = Double.parseDouble(str);
								outusdt(tmg, amount, from, replyname);
								sendText(chatid, getOrder(tmg));
							}
						}
					}
					if (msg.equals("uj")) {
						execute(exchangemessage.getUpdate(update));
					} else if (msg.equals("ua")) {
						execute(exchangemessage.getAliUpdate(update));
					} else if (msg.equals("上课")) {
						tmg.setStatus(true);
						if (tgbotgroupmapper.put(tmg) > 0)
							sendText(chatid, "上课开始。记账开始。");
					} else if (msg.equals("下课")) {
						tmg.setStatus(false);
						if (tgbotgroupmapper.put(tmg) > 0)
							sendText(chatid, "本群已经下课");
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
					} else if (msg.startsWith("设置费率")) {
						String str = msg.substring(msg.indexOf("率") + 1, msg.indexOf("%"));
						if (str.matches("-?\\d+(\\.\\d+)?")) {
							Double cost = Double.valueOf(str);
							tmg.setCost(cost);
							if (tgbotgroupmapper.put(tmg) > 0)
								sendText(chatid, "费率：" + cost + "%,设置成功。");
						}
					} else if (msg.startsWith("设置汇率")) {
						String str = msg.substring(msg.indexOf("率") + 1);
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
					} else if (msg.startsWith("设置操作人")) {
						String str = msg.substring(msg.indexOf("人") + 2);
						if (str.indexOf("@") == 0) {
							tmg.setGmanger(str);
							if (tgbotgroupmapper.put(tmg) > 0)
								sendText(chatid, "操作人：" + str + ",设置成功。");
						}
					} else if (msg.startsWith("删除操作人")) {
						String str = msg.substring(msg.indexOf("人") + 2);
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

					} else if (msg.startsWith("设置全局操作人")) {
						String str = msg.substring(msg.indexOf("人") + 2);
						if (str.indexOf("@") == 0) {
							tmg.setXmanger(str);
							if (tgbotgroupmapper.put(tmg) > 0)
								sendText(chatid, "全局操作人：" + str + ",设置成功。");
						}
					} else if (msg.startsWith("删除全局操作人")) {
						String str = msg.substring(msg.indexOf("人") + 2);
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
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (TelegramApiException e) {
					e.printStackTrace();
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
		Double cost = 0.00;
		if (tbg.getCost() > 0)
			cost = amount * (tgr.getCost() / 100);
		tgr.setWithdrawusdt((amount - cost) / tgr.getExchange());
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
		Double cost = 0.00;
		if (tbg.getCost() > 0)
			cost = amount * (tgr.getCost() / 100);
		tgr.setWithdrawusdt((amount - cost) / tgr.getExchange());
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
		List<Tgbotgrouprecord> listincome = tgbotgrouprecordmapper.listByType(tbg.getTgid(), DictionaryResource.TGBOTGROUPRECORD_TYPE_INCOME);
		StringBuffer sb = new StringBuffer();
		Integer i = 1;
		double countincome = 0.00;
		double countusdt = 0.00;
		sb.append("*入款*:" + listincome.size() + " 笔\n");
		for (Tgbotgrouprecord tbgr : listincome) {
			Double cost = 0.00;
			if (tbgr.getCost() > 0)
				cost = tbgr.getAmount() * (tbgr.getCost() / 100);
			sb.append(DateTimeUtil.getDateTime(tbgr.getCreate_time(), DateTimeUtil.DEFAULT_TIME_FORMAT) + " " + tbgr.getAmount() + "-" + cost + "/" + tbgr.getExchange() + "=" + tbgr.getWithdrawusdt() + " " + "\n");
			countincome = countincome + tbgr.getAmount();
			countusdt = countusdt + tbgr.getWithdrawusdt();
			i++;
		}
		sb.append("\n");
		List<Tgbotgrouprecord> liswithdraw = tgbotgrouprecordmapper.listByType(tbg.getTgid(), DictionaryResource.TGBOTGROUPRECORD_TYPE_WITHDRAW);
		i = 1;
		double counwithdeaw = 0.00;
		double outusdt = 0.00;
		sb.append("*减款*:" + liswithdraw.size() + " 笔\n");
		for (Tgbotgrouprecord tbgr : liswithdraw) {
			Double cost = 0.00;
			if (tbgr.getCost() > 0)
				cost = tbgr.getAmount() * (tbgr.getCost() / 100);
			sb.append(DateTimeUtil.getDateTime(tbgr.getCreate_time(), DateTimeUtil.DEFAULT_TIME_FORMAT) + " " + tbgr.getAmount() + "-" + cost + "/" + tbgr.getExchange() + "=" + tbgr.getWithdrawusdt() + " " + "\n");
			counwithdeaw = counwithdeaw + tbgr.getAmount();
			outusdt = outusdt + tbgr.getWithdrawusdt();
			i++;
		}
		sb.append("\n");
		List<Tgbotgrouprecord> listusdt = tgbotgrouprecordmapper.listByType(tbg.getTgid(), DictionaryResource.TGBOTGROUPRECORD_TYPE_USDT);
		i = 1;
		double usdt = 0.00;
		sb.append("*下发*:" + listusdt.size() + " 笔\n");
		for (Tgbotgrouprecord tbgr : listusdt) {
			sb.append(i + "：" + DateTimeUtil.getDateTime(tbgr.getCreate_time(), DateTimeUtil.DEFAULT_TIME_FORMAT) + "    " + tbgr.getWithdrawusdt() + " U" + "\n");
			usdt = usdt + tbgr.getWithdrawusdt();
			i++;
		}
		sb.append("\n");
		sb.append("*费率*：" + tbg.getCost() + "\n");
		sb.append("*汇率*：" + tbg.getExchange() + "\n");
		sb.append("\n");
		sb.append("*总入款*：" + countincome + "\n");
		sb.append("*总减款*：" + counwithdeaw + "\n");
		sb.append("\n");
		sb.append("*应下发*：" + String.format("%.2f", (countusdt - outusdt)) + "\n");
		sb.append("*已下发*：" + String.format("%.2f", usdt) + "\n");
		sb.append("*未下发*：" + String.format("%.2f", (countusdt - outusdt - usdt)) + "\n");
		return sb.toString();
	}

	// 发送消息
	public Message sendText(Long who, String what) {
		SendMessage sm = SendMessage.builder().chatId(who.toString()).text(what).build();
		sm.enableMarkdown(true);
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
		CopyMessage cm = CopyMessage.builder().fromChatId(who.toString()).chatId(who.toString()).messageId(msgId).build();
		try {
			execute(cm);
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

	// 转发
	public void forwardMessage(Long who, Integer msgId) {
		ForwardMessage cm = ForwardMessage.builder().fromChatId(who.toString()).chatId(who.toString()).messageId(msgId).build();
		try {
			execute(cm);
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

	public void sendMenu(Long who, String txt, InlineKeyboardMarkup kb) {
		SendMessage sm = SendMessage.builder().chatId(who.toString()).parseMode("HTML").text(txt).replyMarkup(kb).build();

		try {
			execute(sm);
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

}
