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

import com.yt.app.api.v1.entity.Merchantaccount;
import com.yt.app.api.v1.entity.Payconfig;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.entity.Tgchannelgroup;
import com.yt.app.api.v1.entity.Tgmerchantchannelmsg;
import com.yt.app.api.v1.entity.Tgmerchantgroup;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.MerchantaccountMapper;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.api.v1.mapper.TgchannelgroupMapper;
import com.yt.app.api.v1.mapper.TgmerchantchannelmsgMapper;
import com.yt.app.api.v1.mapper.TgmerchantgroupMapper;
import com.yt.app.api.v1.service.PayconfigService;

@Component
public class Merchantbot extends TelegramLongPollingBot {

	@Autowired
	private TgmerchantgroupMapper tgmerchantgroupmapper;

	@Autowired
	private TgchannelgroupMapper tgchannelgroupmapper;

	@Autowired
	private PayconfigService payconfigservice;

	@Autowired
	private PayoutMapper payoutmapper;

	@Autowired
	private TgmerchantchannelmsgMapper tgmerchantchannelmsgmapper;

	@Autowired
	private Channelbot cbot;

	@Autowired
	private MerchantaccountMapper merchantaccountmapper;

	@Autowired
	private MerchantMapper merchantmapper;

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
		Long chatid = update.getMessage().getChat().getId();
		String message = update.getMessage().getText();
		if (message != null) {
			Tgmerchantgroup tmg = tgmerchantgroupmapper.getByTgGroupId(chatid);
			Integer replyid = update.getMessage().getMessageId();
			System.out.println("mmmmmm" + update.toString());
			if (tmg == null) {
				tmg = tgmerchantgroupmapper.getByTgGroupName(update.getMessage().getChat().getTitle());
				if (tmg != null) {
					tmg.setTgid(chatid);
					tgmerchantgroupmapper.put(tmg);
					handlemessage(message, chatid, replyid, tmg);
				} else {
					Tgmerchantgroup t = new Tgmerchantgroup();
					t.setTgid(chatid);
					t.setStatus(true);
					t.setTggroupname(update.getMessage().getChat().getTitle());
					tgmerchantgroupmapper.post(t);
				}
			} else {
				handlemessage(message, chatid, replyid, tmg);
			}
		}

	}

	private void handlemessage(String message, Long chatid, Integer replyid, Tgmerchantgroup tmg) {
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
			Payconfig pc = payconfigservice.getData();
			if (pc != null) {
				String msg = "USDT地址：" + pc.getUsdt() + "\n实时汇率：" + pc.getExchange();
				sendReplyText(chatid, replyid, msg);
			} else {
				sendText(chatid, "正在更新地址，请稍后再试！");
			}
		} else if (message.equals("bc")) {// 余额
			Merchantaccount merchantaccount = merchantaccountmapper
					.getByUserId(merchantmapper.get(tmg.getMerchantid()).getUserid());
			if (merchantaccount != null) {
				String msg = "账户可用余额：" + merchantaccount.getBalance() + "\n总充值金额：" + merchantaccount.getTotalincome()
						+ "\n总支出金额：" + merchantaccount.getWithdrawamount();
				sendReplyText(chatid, replyid, msg);
			} else {
				sendText(chatid, "没有查询到账户信息！");
			}
		} else if (message.indexOf("cd#") >= 0) {// 催单
			String orderno = message.substring(message.indexOf("#") + 1);
			Payout po = payoutmapper.getByOrdernum(orderno);
			if (po == null) {
				String msg = "订单号：" + orderno + "\n没有查询到此订单，请核实订单号在发送。";
				sendReplyText(chatid, replyid, msg);
			} else {
				String msg = "订单号：" + orderno + "\n名字:" + po.getAccname() + "\n卡号:" + po.getAccnumer() + "\n金额:"
						+ po.getAmount() + "\n已经联系通道部加急处理，稍后查看回复你结果。";
				sendReplyText(chatid, replyid, msg);

				// 查询渠道
				Tgchannelgroup tcg = tgchannelgroupmapper.getByChannelId(po.getChannelid());
				String cmsg = "订单号：" + po.getChannelordernum() + "\n名字:" + po.getAccname() + "\n卡号:" + po.getAccnumer()
						+ "\n金额:" + po.getAmount() + "\n客户加急催单。请回复！";
				Message messag = cbot.sendText(tcg.getTgid(), cmsg);
				System.out.println(messag.toString());
				Tgmerchantchannelmsg t = tgmerchantchannelmsgmapper.getOrderNum(orderno);
				if (t == null) {
					// 插入关联关系
					t = new Tgmerchantchannelmsg();
					t.setMid(chatid);
					t.setCid(tcg.getTgid());
					t.setMmanger(tmg.getMangers());
					t.setCmanger(tcg.getMangers());
					t.setOrdernum(orderno);
					t.setMreplyid(replyid);
					t.setCreplyid(messag.getMessageId());
					tgmerchantchannelmsgmapper.post(t);
				}
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
