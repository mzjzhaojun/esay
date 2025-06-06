package com.yt.app.common.bot.message.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import com.yt.app.api.v1.entity.Tronmember;
import com.yt.app.api.v1.entity.Tronmemberorder;
import com.yt.app.api.v1.mapper.TronmemberorderMapper;
import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.bot.message.UpdateMessageService;
import com.yt.app.common.bot.message.Keyboard.ButtonResource;
import com.yt.app.common.bot.message.Keyboard.InlineKeyboard;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.DateTimeUtil;
import com.yt.app.common.util.NumberUtil;
import com.yt.app.common.util.RedisUtil;
import com.yt.app.common.util.StringUtil;

@Component
public class TrxFlashRentMessage implements UpdateMessageService {

	@Autowired
	private TronmemberorderMapper tronmemberordermapper;

	private static String owneraddress = "TPP1KaeDnFBs5ATXadVVc8PhaBXqzJJJJJ";

	@Override
	public SendMessage getUpdate(Update update) {
		Double price = Double.valueOf(RedisUtil.get(SystemConstant.CACHE_SYS_EXCHANGE + ServiceConstant.SYSTEM_PAYCONFIG_USDTEXCHANGE));
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId().toString());
		sendMessage.setText("*实时汇率*\r\n" + "1 USDT = " + (price - 0.64) + " TRX  \r\n" + "\r\n" + "转账即兑，全自动返，等值 10U 起换\r\n" + "\r\n" + "选择兑换数量\r\n" + "例如: “10U” 可实时计算10U可兑换的TRX数量=" + (10 * (price - 0.64)) + "\r\n");
		sendMessage.enableMarkdown(true);
		sendMessage.setReplyMarkup(InlineKeyboard.getInlineKeyboardMarkup());
		return sendMessage;
	}

	public EditMessageText excuteExchange(Update update, Tronmember tronmember) {
		Double price = Double.valueOf(RedisUtil.get(SystemConstant.CACHE_SYS_EXCHANGE + ServiceConstant.SYSTEM_PAYCONFIG_USDTEXCHANGE));
		String callbackdata = update.getCallbackQuery().getData();
		Double amount = Double.valueOf(callbackdata.replace(ButtonResource.EXCHANGE, ""));
		User sender = update.getCallbackQuery().getFrom();
		MaybeInaccessibleMessage originalMessage = update.getCallbackQuery().getMessage();
		Double fewamount = NumberUtil.getExchangeFewAmount();
		Integer i = 0;
		Tronmemberorder tronmemberorder = new Tronmemberorder();
		if (fewamount < 3) {
			tronmemberorder.setAmount(amount);
			tronmemberorder.setChatid(sender.getId());
			tronmemberorder.setMessageid(originalMessage.getMessageId());
			tronmemberorder.setTrxamount(Integer.parseInt(String.format("%.0f", (amount * (price - 0.64)))));
			tronmemberorder.setGoodsname("购买 " + tronmemberorder.getTrxamount() + " TRX");
			tronmemberorder.setUsdtamount(amount);
			tronmemberorder.setFewamount(fewamount);
			tronmemberorder.setRealamount(amount - fewamount);
			tronmemberorder.setTgid(tronmember.getTgid());
			tronmemberorder.setOrdernum("E" + StringUtil.getOrderNum());
			tronmemberorder.setType(DictionaryResource.EXCHANGE_TYPE_601);
			tronmemberorder.setStatus(DictionaryResource.ORDERSTATUS_50);
			tronmemberorder.setIncomeaddress(owneraddress);
			tronmemberorder.setExpireddate(DateTimeUtil.addMinute(5));
			i = tronmemberordermapper.post(tronmemberorder);
		}
		EditMessageText editmessagetext = new EditMessageText();
		editmessagetext.setChatId(String.valueOf(sender.getId()));
		editmessagetext.setMessageId(originalMessage.getMessageId());
		if (i > 0) {
			editmessagetext.setText("*订单信息*\r\n" + "*单号*： " + tronmemberorder.getOrdernum() + " \r\n" + "*商品*： " + tronmemberorder.getGoodsname() + " \r\n" + "*支付*：` " + tronmemberorder.getRealamount() + "`  U  (点击金额自动复制)\r\n" + "\r\n"
					+ "*支付地址*:\r\n" + "`" + owneraddress + "` (点击地址自动复制)\r\n" + "\r\n" + "请务必按照订单显示金额支付‼️\r\n" + "\r\n" + "转账即兑，全自动返\r\n" + "\r\n" + "请在 *5分钟* 内完成支付\r\n");
		} else {
			editmessagetext.setText("当前服务器繁忙请稍后再试");
		}
		editmessagetext.enableMarkdown(true);
		return editmessagetext;
	}
}
