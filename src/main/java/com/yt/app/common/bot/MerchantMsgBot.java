package com.yt.app.common.bot;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.yt.app.api.v1.dbo.PaySubmitDTO;
import com.yt.app.api.v1.entity.Exchange;
import com.yt.app.api.v1.entity.PayoutMerchantaccount;
import com.yt.app.api.v1.entity.Sysconfig;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.entity.Tgchannelgroup;
import com.yt.app.api.v1.entity.Tgmerchantchannelmsg;
import com.yt.app.api.v1.entity.Tgmerchantgroup;
import com.yt.app.api.v1.entity.YtFile;
import com.yt.app.api.v1.mapper.FileMapper;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.PayoutMerchantaccountMapper;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.api.v1.mapper.TgchannelgroupMapper;
import com.yt.app.api.v1.mapper.TgmerchantchannelmsgMapper;
import com.yt.app.api.v1.mapper.TgmerchantgroupMapper;
import com.yt.app.api.v1.service.ExchangeService;
import com.yt.app.api.v1.service.SysconfigService;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.config.YtConfig;
import com.yt.app.common.util.FileUtil;
import com.yt.app.common.util.RedisUtil;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("deprecation")
@Slf4j
@Component
public class MerchantMsgBot extends TelegramLongPollingBot {

	@Autowired
	private TgmerchantgroupMapper tgmerchantgroupmapper;

	@Autowired
	private TgchannelgroupMapper tgchannelgroupmapper;

	@Autowired
	private SysconfigService payconfigservice;

	@Autowired
	private PayoutMapper payoutmapper;

	@Autowired
	private TgmerchantchannelmsgMapper tgmerchantchannelmsgmapper;

	@Autowired
	private ChannelMsgBot cbot;

	@Autowired
	private PayoutMerchantaccountMapper merchantaccountmapper;

	@Autowired
	private MerchantMapper merchantmapper;

	@Autowired
	private YtConfig appConfig;

	@Autowired
	private ExchangeService exchangeservice;

	@Autowired
	private FileMapper filemapper;

	@Override
	public String getBotUsername() {
		return "飞兔商户";
	}

	@Override
	public String getBotToken() {
		return "7472319600:AAGY998sxdVqHNiOVdW3OE6mnEj3KczMxto";
	}

	@Override
	public void onUpdateReceived(Update update) {
		// 获取书记表情，转发消息
		log.info(update.toString());
		String message = update.getMessage().getText();
		if (message != null) {
			TenantIdContext.removeFlag();
			Long chatid = update.getMessage().getChat().getId();
			Tgmerchantgroup tmg = tgmerchantgroupmapper.getByTgGroupId(chatid);
			if (tmg != null) {
				handlemessage(message, chatid, tmg, update);
			} else {
				Tgmerchantgroup t = new Tgmerchantgroup();
				t.setTgid(chatid);
				t.setStatus(true);
				t.setCost(0.0);
				t.setCostcount(0.0);
				t.setUsdcount(0.0);
				t.setCount(0.0);
				t.setCountorder(0);
				t.setTodaycount(0.0);
				t.setTodaycountorder(0);
				t.setTodayusdcount(0.0);
				t.setTggroupname(update.getMessage().getChat().getTitle());
				tgmerchantgroupmapper.post(t);
				sendText(chatid, "请配置后台开始工作：#h 查汇率  ，#d 查USDT充值地址，#y 查余额，#z 查账单");
			}
		}
		TenantIdContext.remove();
	}

	private void handlemessage(String message, Long chatid, Tgmerchantgroup tmg, Update update) {
		Integer messageid = update.getMessage().getMessageId();
		String username = update.getMessage().getFrom().getUserName();
		if (message.equals("#h")) {
			// 汇率
			List<Sysconfig> list = payconfigservice.getDataTop();
			StringBuffer sb = new StringBuffer();
			Integer i = 1;
			for (Sysconfig pc : list) {
				sb.append(i + "" + pc.getName() + "，价格:" + pc.getExchange() + "\n");
				i++;
			}
			sendText(chatid, sb.toString());
		} else if (message.equals("#d")) {
			// 充值地址
			Sysconfig pc = payconfigservice.getUsdtExchangeData();
			if (pc != null) {
				String msg = "USDT地址：" + pc.getUsdt() + "";
				sendReplyText(chatid, messageid, msg);
			} else {
				sendText(chatid, "正在更新地址，请稍后再试！");
			}
		} else if (message.equals("#y")) {
			// 余额
			PayoutMerchantaccount merchantaccount = merchantaccountmapper.getByUserId(merchantmapper.get(tmg.getMerchantid()).getUserid());
			if (merchantaccount != null) {
				String msg = "账户可用余额：" + merchantaccount.getBalance() + "\n总充值金额：" + merchantaccount.getTotalincome() + "\n总支出金额：" + merchantaccount.getWithdrawamount();
				sendReplyText(chatid, messageid, msg);
			} else {
				sendText(chatid, "没有查询到账户信息！");
			}
		} else if (message.indexOf("#c") >= 0) {
			// 查單 催单
			String orderno = message.substring(message.indexOf("#") + 1);
			Payout po = payoutmapper.getByOrdernum(orderno);
			if (po == null) {
				String msg = "订单号：" + orderno + "\n没有查询到此订单，请核实订单号在发送。";
				sendReplyText(chatid, messageid, msg);
			} else {
				String msg = "订单号：" + orderno + "\n名字:" + po.getAccname() + "\n卡号:" + po.getAccnumer() + "\n金额:" + po.getAmount() + "\n已经联系通道部加急处理，稍后查看回复你结果。";
				sendReplyText(chatid, messageid, msg);
				Tgchannelgroup tcg = tgchannelgroupmapper.getByChannelId(po.getChannelid());
				String cmsg = "订单号：" + po.getChannelordernum() + "\n名字:" + po.getAccname() + "\n卡号:" + po.getAccnumer() + "\n金额:" + po.getAmount() + "\n客户加急催单。请回复！";
				cbot.sendText(tcg.getTgid(), cmsg);
			}
		} else if (message.equals("#z") && (username.equals(tmg.getAdminmangers()) || username.equals(tmg.getMangers())) || username.equals(tmg.getCustomermangers())) {
			// 订单统计
			StringBuffer sb = new StringBuffer();
			sb.append("今日订单：" + tmg.getTodaycountorder() + " 笔\n");
			sb.append("\n");
			sb.append("今日打款：" + tmg.getTodaycount() + "￥\n");
			sb.append("今日U款：" + String.format("%.0f", tmg.getTodayusdcount()) + " $\n");
			sb.append("\n");
			sb.append("总单量：" + tmg.getCountorder() + "\n");
			sb.append("总打款：" + tmg.getCount() + " ￥\n");
			sb.append("\n");
			sendText(chatid, sb.toString());
		} else if (message.indexOf("#e") >= 0 && (username.equals(tmg.getAdminmangers()) || username.equals(tmg.getMangers()))) {
			// 二维码收款单
			Double amount = 0.00;
			String str = message.substring(2, message.length());
			if (str.matches("-?\\d+(\\.\\d+)?")) {
				amount = Double.valueOf(str);
			}
			if (update.getMessage().getReplyToMessage() != null && update.getMessage().getReplyToMessage().getPhoto().size() != 0) {
				Integer replyid = update.getMessage().getReplyToMessage().getMessageId();
				PhotoSize photo = update.getMessage().getReplyToMessage().getPhoto().get(update.getMessage().getReplyToMessage().getPhoto().size() - 1);
				GetFile getFile = new GetFile();
				getFile.setFileId(photo.getFileId());
				try {
					String filePath = execute(getFile).getFilePath();

					StringBuffer sb = new StringBuffer();
					String filepath = FileUtil.createfilepath(new Date(), appConfig);
					// 保存图片数据
					YtFile fl = new YtFile();
					fl.setRoot_path(appConfig.getFilePath());
					fl.setRelative_path(filepath);
					fl.setCreatetime(new Date());
					fl.setFile_size(1);
					fl.setSuffix("jpg");
					fl.setFile_name("qrcode");
					filemapper.post(fl);

					// 保存图片到本地
					String name = sb.append(fl.getId()).append(".").append("jpg").toString();
					sb = new StringBuffer();
					String apath = sb.append(filepath).append(java.io.File.separator).append(name).toString();
					sb = new StringBuffer();
					String path = sb.append(appConfig.getFilePath()).append(apath).toString();
					java.io.File outputFile = new File(path);
					downloadFile(filePath, outputFile);

					String url = appConfig.getFileurl().replace("{id}", fl.getId() + "");

					// 下單
					PaySubmitDTO ss = new PaySubmitDTO();
					ss.setMerchantid(tmg.getMerchantid().toString());
					ss.setQrcode(url);
					ss.setPayamt(amount);
					ss.setBankowner("Qrcode");

					Exchange eg = exchangeservice.submit(ss);

					Double exchange = Double.valueOf(RedisUtil.get(SystemConstant.CACHE_SYS_EXCHANGE));
					// 提交转发数据
					Tgmerchantchannelmsg tmm = new Tgmerchantchannelmsg();
					tmm.setMid(tmg.getMerchantid());
					tmm.setCid(eg.getChannelid());
					tmm.setChatid(chatid);
					tmm.setMreplyid(messageid);
					tmm.setOrdernum(eg.getOrdernum());
					tmm.setQrcode(url);
					tmm.setTelegrameimgid(path);
					tmm.setAmount(amount);
					tmm.setExchange(exchange);
					tmm.setUsd(amount / exchange);
					tmm.setCreplyid(replyid);
					tgmerchantchannelmsgmapper.post(tmm);
				} catch (Exception e) {
					sendText(chatid, e.getMessage());
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
