package com.yt.app.common.bot.message.impl;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Tgchannelgroup;
import com.yt.app.api.v1.entity.Tgmerchantgroup;
import com.yt.app.api.v1.mapper.IncomeMapper;
import com.yt.app.api.v1.mapper.TgchannelgroupMapper;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.bot.MerchantBot;
import com.yt.app.common.bot.message.UpdateMerchantMessageService;

import cn.hutool.http.HttpRequest;

/**
 * 商户余额
 * 
 * @author zj
 *
 */
@Component
public class MerchantOrderMessage implements UpdateMerchantMessageService {

	@Autowired
	private IncomeMapper incomemapper;

	@Autowired
	private TgchannelgroupMapper tgchannelgroupmapper;

	@Autowired
	private MerchantBot merchantbot;

	@Override
	public SendMessage getUpdate(Update update, Tgmerchantgroup tmg) {
		SendMessage sendmessage = new SendMessage();
		String ordernum = update.getMessage().getText();
		if (ordernum.matches(".*\\d+.*")) {
			TenantIdContext.removeFlag();
			Income income = incomemapper.getByMerchantOrderNum(ordernum);
			if (income != null) {
				Tgchannelgroup tgchannelgroup = tgchannelgroupmapper.getByChannelId(income.getQrcodeid());
				if (tgchannelgroup != null) {
					sendmessage.setChatId(tgchannelgroup.getTgid());
					sendmessage.setText("订单: " + income.getOrdernum() + " \r\n 已支付，未回调。请你们速度处理!");
				}
			}
		}
		return sendmessage;
	}

	public SendPhoto getUpdateSendPhoto(Update update, String chatid) {
		SendPhoto sendphoto = new SendPhoto();
		sendphoto.setChatId(chatid);
		try {
			PhotoSize photo = update.getMessage().getReplyToMessage().getPhoto().get(update.getMessage().getReplyToMessage().getPhoto().size() - 1);
			GetFile getFile = new GetFile();
			getFile.setFileId(photo.getFileId());
			String filePath = merchantbot.execute(getFile).getFileUrl(merchantbot.getBotToken());
			InputFile infile = new InputFile();
			InputStream is = HttpRequest.get(filePath).execute().bodyStream();
			infile.setMedia(is, "jpg");
			sendphoto.setPhoto(infile);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
		return sendphoto;
	}

	public SendMessage getUpdateNotFind(Update update) {
		SendMessage sendmessage = new SendMessage();
		String ordernum = update.getMessage().getText();
		sendmessage.setChatId(update.getMessage().getChatId());
		sendmessage.setText("订单: " + ordernum + " \r\n没找到，请你重新处理!");
		return sendmessage;
	}

	public SendMessage getUpdateHandler(Update update) {
		SendMessage sendmessage = new SendMessage();
		String ordernum = update.getMessage().getText();
		sendmessage.setChatId(update.getMessage().getChatId());
		sendmessage.setReplyToMessageId(update.getMessage().getReplyToMessage().getMessageId());
		sendmessage.setText("订单: " + ordernum + " \r\n已传达，请稍后!");
		return sendmessage;
	}
}
