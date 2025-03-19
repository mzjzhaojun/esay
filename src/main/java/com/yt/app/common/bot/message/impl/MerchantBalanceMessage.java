package com.yt.app.common.bot.message.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yt.app.api.v1.entity.Incomemerchantaccount;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Tgmerchantgroup;
import com.yt.app.api.v1.mapper.IncomemerchantaccountMapper;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.TgmerchantgroupMapper;
import com.yt.app.common.bot.message.UpdateMerchantMessageService;
import com.yt.app.common.util.DateTimeUtil;

/**
 * 商户余额
 * 
 * @author zj
 *
 */
@Component
public class MerchantBalanceMessage implements UpdateMerchantMessageService {

	@Autowired
	private IncomemerchantaccountMapper IncomemerchantaccountMapper;

	@Autowired
	private MerchantMapper merchantmapper;

	@Autowired
	private TgmerchantgroupMapper tgmerchantgroupmapper;

	@Override
	public SendMessage getUpdate(Update update, Tgmerchantgroup tmg) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId().toString());
		if (tmg.getMerchantids() != null) {
			StringBuffer msg = new StringBuffer();
			for (Long mid : tmg.getMerchantids()) {
				Merchant m = merchantmapper.get(mid);
				Incomemerchantaccount merchantaccount = IncomemerchantaccountMapper.getByMerchantId(mid);
				msg.append("\r\n商户：*" + m.getName() + "*\r\n\r\n今日入款：" + m.getTodaycount() + " \r\n用户支付：" + m.getTodayincomecount() + " \r\n可用余额：" + merchantaccount.getBalance() + " \r\n总共下发：" + merchantaccount.getWithdrawamount() + "\r\n总共入款："
						+ merchantaccount.getTotalincome() + "\r\n\r\n*" + DateTimeUtil.getDateTime() + "*");
			}
			sendMessage.setText(msg.toString());
			sendMessage.enableMarkdown(true);
		} else {
			sendMessage.setText("系统还没有绑定商户");
		}
		return sendMessage;
	}

	public SendMessage getUpdate(Merchant m) {
		SendMessage sendMessage = new SendMessage();
		Tgmerchantgroup tmg = tgmerchantgroupmapper.getByMerchantId(m.getId());
		if (tmg != null) {
			sendMessage.setChatId(tmg.getTgid());
			Incomemerchantaccount merchantaccount = IncomemerchantaccountMapper.getByMerchantId(m.getId());
			sendMessage.setText("商户：*" + m.getName() + "*\r\n\r\n今日入款：" + m.getTodaycount() + " \r\n用户支付：" + m.getTodayincomecount() + "\r\n可用余额：" + merchantaccount.getBalance() + " \r\n总共下发：" + merchantaccount.getWithdrawamount() + "\r\n总共入款："
					+ merchantaccount.getTotalincome() + "\r\n\r\n*" + DateTimeUtil.getDateTime() + "*");
			sendMessage.enableMarkdown(true);
		} else {
			return null;
		}
		return sendMessage;
	}
}
