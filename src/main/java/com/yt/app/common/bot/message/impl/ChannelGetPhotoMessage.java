package com.yt.app.common.bot.message.impl;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.entity.Tgchannelgroup;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.api.v1.mapper.TgchannelgroupMapper;
import com.yt.app.api.v1.service.FileService;
import com.yt.app.common.bot.ChannelBot;
import com.yt.app.common.bot.message.UpdateChannelMessageService;

import cn.hutool.http.HttpRequest;

/**
 * 商户余额
 * 
 * @author zj
 *
 */
@Component
public class ChannelGetPhotoMessage implements UpdateChannelMessageService {

	@Autowired
	private ChannelBot channelbot;

	@Autowired
	private FileService fileservice;

	@Autowired
	private PayoutMapper payoutmapper;

	@Autowired
	private TgchannelgroupMapper tgchannelgroupmapper;

	@Override
	public SendMessage getUpdate(Update update, Tgchannelgroup tcg) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(tcg.getTgid());
		return sendMessage;
	}

	public SendMessage getUpdate(Long cid, String orderNum) {
		SendMessage sendMessage = new SendMessage();
		Tgchannelgroup tcg = tgchannelgroupmapper.getByChannelId(cid);
		if (tcg != null) {
			sendMessage.setChatId(tcg.getTgid());
			sendMessage.setText("代付回单" + orderNum);
			return sendMessage;
		}
		{
			return null;
		}
	}

	public void getUpdateSendPhoto(Update update) {
		// 查询订单
		String text = update.getMessage().getReplyToMessage().getText();
		String ordernum = text.split(" ")[1];
		Payout pout = payoutmapper.getByOrdernum(ordernum);
		try {
			PhotoSize photo = update.getMessage().getReplyToMessage().getPhoto().get(update.getMessage().getReplyToMessage().getPhoto().size() - 1);
			GetFile getFile = new GetFile();
			getFile.setFileId(photo.getFileId());
			String filePath = channelbot.execute(getFile).getFileUrl(channelbot.getBotToken());
			InputStream is = HttpRequest.get(filePath).execute().bodyStream();
			String url = fileservice.addInputStream(is);
			pout.setImgurl(url);
			payoutmapper.put(pout);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

}
