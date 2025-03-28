package com.yt.app.common.bot.message.impl;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.yt.app.api.v1.entity.Tgmerchantgroup;
import com.yt.app.api.v1.mapper.TgmerchantgroupMapper;
import com.yt.app.common.bot.message.UpdateMerchantMessageService;

import cn.hutool.http.HttpRequest;

/**
 * 商户余额
 * 
 * @author zj
 *
 */
@Component
public class MerchantGetPhotoMessage implements UpdateMerchantMessageService {

	@Autowired
	private TgmerchantgroupMapper tgmerchantgroupmapper;

	@Override
	public SendMessage getUpdate(Update update, Tgmerchantgroup tcg) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(tcg.getTgid());
		return sendMessage;
	}

	public SendMessage getUpdate(Long cid, String orderNum) {
		SendMessage sendMessage = new SendMessage();
		Tgmerchantgroup tcg = tgmerchantgroupmapper.getByMerchantId(cid);
		if (tcg != null) {
			sendMessage.setChatId(tcg.getTgid());
			sendMessage.setText("代付回单 " + orderNum);
			return sendMessage;
		} else {
			return null;
		}
	}

	public SendPhoto getUpdateSendPhoto(Long mid, String imgurl) {
		Tgmerchantgroup tcg = tgmerchantgroupmapper.getByMerchantId(mid);
		if (tcg != null) {
			SendPhoto sendphoto = new SendPhoto();
			sendphoto.setChatId(tcg.getTgid());
			InputFile infile = new InputFile();
			InputStream is = HttpRequest.get(imgurl).execute().bodyStream();
			infile.setMedia(is, "jpg");
			sendphoto.setPhoto(infile);
			return sendphoto;
		} else {
			return null;
		}

	}

}
