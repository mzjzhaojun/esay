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
import com.yt.app.api.v1.service.IncomemerchantaccountorderService;
import com.yt.app.common.bot.message.UpdateMerchantMessageService;
import com.yt.app.common.util.DateTimeUtil;

/**
 * 商户余额
 * 
 * @author zj
 *
 */
@Component
public class MerchantIssueMessage implements UpdateMerchantMessageService {

	@Autowired
	private IncomemerchantaccountMapper IncomemerchantaccountMapper;

	@Autowired
	private MerchantMapper merchantmapper;

	@Autowired
	private IncomemerchantaccountorderService incomemerchantaccountorderservice;

	@Override
	public SendMessage getUpdate(Update update, Tgmerchantgroup tmg) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(update.getMessage().getChatId().toString());
		if (tmg.getMerchantids() != null) {
			String username = update.getMessage().getFrom().getUserName();
			if (username.equals(tmg.getMangers())) {
				StringBuffer msg = new StringBuffer();
				String[] text = update.getMessage().getText().split(" ");
				if (text.length == 3) {
					String merchantname = text[1];
					for (Long mid : tmg.getMerchantids()) {
						Merchant m = merchantmapper.get(mid);
						if (m.getName().equals(merchantname)) {
							incomemerchantaccountorderservice.incomewithdrawTelegram(m, Double.valueOf(text[2]));
						}
						Incomemerchantaccount merchantaccount = IncomemerchantaccountMapper.getByMerchantId(mid);
						msg.append("\r\n商户：*" + m.getName() + "*\r\n\r\n今日收入：" + m.getTodaycount() + " \r\n总共下发：" + merchantaccount.getWithdrawamount() + "\r\n总共收入：" + merchantaccount.getTotalincome() + " \r\n可用余额：" + merchantaccount.getBalance()
								+ "\r\n  \r\n*" + DateTimeUtil.getDateTime() + "*");
					}
					sendMessage.setText(msg.toString());
					sendMessage.enableMarkdown(true);
				}else {
					sendMessage.setText("下发格式 下发 商户名 金额");
				}
			} else {
				sendMessage.setText("你没有操作权限");
			}
		} else {
			sendMessage.setText("系统还没有绑定商户");
		}
		return sendMessage;
	}

}
