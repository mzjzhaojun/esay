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

import com.yt.app.api.v1.entity.Tgmessagegroup;
import com.yt.app.common.bot.MessageBot;
import com.yt.app.common.bot.message.UpdateMessageService;

import cn.hutool.http.HttpRequest;

/**
 * 商户余额
 * 
 * @author zj
 *
 */
@Component
public class M2CMessage implements UpdateMessageService {

	@Autowired
	private MessageBot messagebot;

	@Override
	public SendMessage getUpdate(Update update) {
		SendMessage sendmessage = new SendMessage();
		return sendmessage;
	}

	public SendMessage getReplyUpdate(Update update, Tgmessagegroup tmg) {
		SendMessage sendmessage = new SendMessage();
		String message = update.getMessage().getReplyToMessage().getText();
		Long chatid = update.getMessage().getChatId();
		String username = update.getMessage().getFrom().getUserName();
		if (username.equals(tmg.getAdminmangers()) || username.equals(tmg.getMangers())) {
			if (chatid.longValue() == tmg.getTgmid()) {
				sendmessage.setChatId(tmg.getTgcid());
			} else {
				sendmessage.setChatId(tmg.getTgmid());
			}
			if (message == null)
				sendmessage.setText("p");
			else
				sendmessage.setText(message);
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
			String filePath = messagebot.execute(getFile).getFileUrl(messagebot.getBotToken());
			InputFile infile = new InputFile();
			InputStream is = HttpRequest.get(filePath).execute().bodyStream();
			infile.setMedia(is, "jpg");
			sendphoto.setPhoto(infile);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
		return sendphoto;
	}
}
