package com.yt.app.common.bot.message.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.yt.app.api.v1.entity.Tgmessagegrouprecord;
import com.yt.app.api.v1.mapper.TgmessagegrouprecordMapper;
import com.yt.app.common.bot.MessageBot;
import com.yt.app.common.bot.message.UpdateMessageService;
import com.yt.app.common.util.DateTimeUtil;

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
	@Autowired
	private TgmessagegrouprecordMapper tgmessagegrouprecordmapper;

	@Override
	public SendMessage getUpdate(Update update) {
		SendMessage sendmessage = new SendMessage();
		return sendmessage;
	}

	public SendMessage getReplyChannelUpdate(Update update, Tgmessagegroup tmg) {
		SendMessage sendmessage = new SendMessage();
		Long chatid = update.getMessage().getChatId();
		String username = update.getMessage().getFrom().getUserName();
		if (tmg.getAdminmangers().indexOf(username) != -1 || tmg.getCustomermangers().indexOf(username) != -1) {
			if (chatid.longValue() == tmg.getTgmid()) {
				sendmessage.setChatId(tmg.getTgcid());
			} else {
				sendmessage.setChatId(tmg.getTgmid());
			}
			if (update.getMessage().getText() != null) {
				sendmessage.setText(update.getMessage().getText());
			} else {
				sendmessage.setText("c");
			}
			if (update.getMessage().getText() != null && update.getMessage().getText().startsWith("+")) {
				sendmessage.setText(update.getMessage().getText());
			}
		}
		return sendmessage;
	}

	public SendMessage getReplyUpdate(Update update, Tgmessagegroup tmg) {
		SendMessage sendmessage = new SendMessage();
		Long chatid = update.getMessage().getChatId();
		String username = update.getMessage().getFrom().getUserName();
		if (tmg.getAdminmangers().indexOf(username) != -1 || tmg.getMangers().indexOf(username) != -1) {
			if (chatid.longValue() == tmg.getTgmid()) {
				sendmessage.setChatId(tmg.getTgcid());
			} else {
				sendmessage.setChatId(tmg.getTgmid());
			}
			if (update.getMessage().getText() != null) {
				sendmessage.setText(update.getMessage().getText());
			} else {
				sendmessage.setText("c");
			}
			if (update.getMessage().getText() != null && update.getMessage().getText().startsWith("+")) {
				sendmessage.setText(update.getMessage().getText());
			}
		}
		return sendmessage;
	}

	public void getReplyRecordUpdate(Update update, Tgmessagegroup tmg) {
		Long chatid = update.getMessage().getChatId();
		String username = update.getMessage().getFrom().getUserName();
		if (tmg.getAdminmangers().indexOf(username) != -1 || tmg.getMangers().indexOf(username) != -1) {
			String reecaption = update.getMessage().getReplyToMessage().getCaption();
			Tgmessagegrouprecord tmgr = new Tgmessagegrouprecord();
			tmgr.setMid(tmg.getTgmid());
			tmgr.setCid(tmg.getTgcid());
			tmgr.setChatid(chatid);
			tmgr.setMreplyid(update.getMessage().getReplyToMessage().getMessageId());
			String name = reecaption.split(" ")[1];
			if (name == null || name.equals("")) {
				name = reecaption.substring(reecaption.indexOf(" "));
			}
			tmgr.setName(name.trim());
			tmgr.setAmount(Double.valueOf(reecaption.split(" ")[0]));
			tmgr.setStatus(false);
			tmgr.setOptionname(username);
			tmgr.setRemark(reecaption);
			tgmessagegrouprecordmapper.post(tmgr);
		}
	}

	public SendMessage getReplyRecordSuccessUpdate(Update update, Tgmessagegroup tmg) {
		SendMessage sendmessage = new SendMessage();
		String username = update.getMessage().getFrom().getUserName();
		sendmessage.setChatId(tmg.getTgmid());
		if (tmg.getAdminmangers().indexOf(username) != -1 || tmg.getMangers().indexOf(username) != -1) {
			Tgmessagegrouprecord tgmessagegrouprecord = tgmessagegrouprecordmapper.getMidReplyid(tmg.getTgmid().toString(), update.getMessage().getReplyToMessage().getMessageId());
			if (tgmessagegrouprecord != null) {
				tgmessagegrouprecord.setStatus(true);
				tgmessagegrouprecord.setConfirmname(username);
				tgmessagegrouprecordmapper.put(tgmessagegrouprecord);
				sendmessage.setText("姓名:" + tgmessagegrouprecord.getName() + " 金额:" + tgmessagegrouprecord.getAmount() + " 入款成功!");
			}
		} else {
			sendmessage.setText("没有权限，或者没找到！");
		}
		return sendmessage;
	}

	public SendMessage getReplyRecordSuccessListUpdate(Update update, Tgmessagegroup tmg) {
		SendMessage sendmessage = new SendMessage();
		String username = update.getMessage().getFrom().getUserName();
		sendmessage.setChatId(tmg.getTgmid());
		if (tmg.getAdminmangers().indexOf(username) != -1 || tmg.getMangers().indexOf(username) != -1) {
			String name = update.getMessage().getText().split(" ")[1];
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("name", name);
			List<Tgmessagegrouprecord> list = tgmessagegrouprecordmapper.list(param);
			StringBuffer msg = new StringBuffer();
			msg.append("\r\n姓名：" + name + "\r\n订单数：" + list.size() + " \r\n");
			for (Tgmessagegrouprecord tmgr : list) {
				String statuss = tmgr.getStatus() ? "成功" : "失败";
				String datetime = DateTimeUtil.getDateTime(tmgr.getCreate_time(), DateTimeUtil.DEFAULT_DATETIME_FORMAT);
				msg.append(" \r\n金额：" + tmgr.getAmount() + "\r\n状态：" + statuss + "\r\n日期：" + datetime + "\r\n");
			}
			sendmessage.setText(msg.toString());
		} else {
			sendmessage.setText("没有权限");
		}
		return sendmessage;
	}

	public SendMessage getReplyRecordSuccessAddUpdate(Update update, Tgmessagegroup tmg) {
		SendMessage sendmessage = new SendMessage();
		Long chatid = update.getMessage().getChatId();
		sendmessage.setChatId(chatid);
		String username = update.getMessage().getFrom().getUserName();
		if (tmg.getAdminmangers().indexOf(username) != -1 || tmg.getMangers().indexOf(username) != -1) {
			String reecaption = update.getMessage().getText();
			Tgmessagegrouprecord tmgr = new Tgmessagegrouprecord();
			tmgr.setMid(tmg.getTgmid());
			tmgr.setCid(tmg.getTgcid());
			tmgr.setChatid(chatid);
			String name = reecaption.split(" ")[2];
			if (name == null || name.equals("")) {
				name = reecaption.substring(reecaption.indexOf(" "));
			}
			tmgr.setName(name.trim());
			tmgr.setAmount(Double.valueOf(reecaption.split(" ")[1]));
			tmgr.setStatus(true);
			tmgr.setOptionname(username);
			tmgr.setConfirmname(username);
			tmgr.setRemark(reecaption);
			tgmessagegrouprecordmapper.post(tmgr);
			sendmessage.setText(tmgr.getName() + " 添加成功!");
		} else {
			sendmessage.setText("没有权限");
		}
		return sendmessage;
	}

	public SendPhoto getUpdateSendRplPhoto(Update update, String chatid) {
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

	public SendPhoto getUpdateSendPhoto(Update update, String chatid) {
		SendPhoto sendphoto = new SendPhoto();
		sendphoto.setChatId(chatid);
		try {
			PhotoSize photo = update.getMessage().getPhoto().get(update.getMessage().getPhoto().size() - 1);
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
