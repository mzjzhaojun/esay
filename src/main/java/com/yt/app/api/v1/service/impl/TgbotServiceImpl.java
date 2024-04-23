package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TgbotMapper;
import com.yt.app.api.v1.service.TgbotService;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.common.bot.Messagebot;
import com.yt.app.api.v1.entity.Tgbot;
import com.yt.app.api.v1.vo.TgbotVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtCodeEnum;
import com.yt.app.common.exption.MyException;
import com.yt.app.common.util.RedisUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-03-31 17:29:46
 */

@Service
public class TgbotServiceImpl extends YtBaseServiceImpl<Tgbot, Long> implements TgbotService {
	@Autowired
	private TgbotMapper mapper;

	private TelegramBotsApi botsApi;

	public TgbotServiceImpl() {
		try {
			botsApi = new TelegramBotsApi(DefaultBotSession.class);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	@Override
	@Transactional
	public Integer post(Tgbot t) {
		Tgbot tb = mapper.getByToken(t.getToken());
		if (tb == null) {
			Integer i = mapper.post(t);
			try {
				Messagebot mb = new Messagebot(t.getName(), t.getToken());
				botsApi.registerBot(mb);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
			return i;
		}
		throw new MyException("已经存在的机器人,请不要重复添加!", YtCodeEnum.YT888);
	}

	@Override
	public YtIPage<Tgbot> list(Map<String, Object> param) {
		List<Tgbot> list = mapper.list(param);
		return new YtPageBean<Tgbot>(list);
	}

	@Override
	public Tgbot get(Long id) {
		Tgbot t = mapper.get(id);
		return t;
	}

	@Override
	public YtIPage<TgbotVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<TgbotVO>(Collections.emptyList());
		}
		List<TgbotVO> list = mapper.page(param);
		list.forEach(t -> {
			t.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + t.getType()));
		});
		return new YtPageBean<TgbotVO>(param, list, count);
	}

	@Override
	public void initBot() {
		TenantIdContext.setTenantId(1720395906240614400L);
		List<Tgbot> list = mapper.list(new HashMap<String, Object>());
		try {
			for (Tgbot tb : list) {
				Messagebot mb = new Messagebot(tb.getName(), tb.getToken());
				botsApi.registerBot(mb);
			}
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
		TenantIdContext.remove();
	}
}