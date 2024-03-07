package com.yt.app.common.runnable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yt.app.api.v1.entity.Exchange;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.mapper.ExchangeMapper;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.vo.SysResultVO;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.PayUtil;

public class NotifyExchangeThread implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(NotifyExchangeThread.class);

	private ExchangeMapper mapper;
	private MerchantMapper merchantmapper;
	private Exchange exchange;
	private Integer code;

	public NotifyExchangeThread(ExchangeMapper _mapper, MerchantMapper _merchantmapper, Exchange pt, Integer _code) {
		mapper = _mapper;
		merchantmapper = _merchantmapper;
		exchange = pt;
		code = _code;
	}

	@Override
	public void run() {
		Merchant merchant = merchantmapper.get(exchange.getMerchantid());
		SysResultVO ss = new SysResultVO();
		ss.setMerchantid(exchange.getMerchantcode());
		ss.setPayorderid(exchange.getChannelordernum());
		ss.setMerchantorderid(exchange.getMerchantordernum());
		ss.setPayamt(exchange.getAmount());
		ss.setBankcode(exchange.getBankcode());
		ss.setCode(code);
		ss.setRemark("remark");
		logger.info("通知 start---------------------商户单号：" + exchange.getMerchantordernum());
		int i = 1;
		while (true) {
			if (i > 3) {
				exchange.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_63);
				mapper.put(exchange);
				break;
			}
			YtBody result = PayUtil.SendNotify(ss, merchant.getAppkey());
			// 通知到
			if (result.getCode() == 200) {
				exchange.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_62);
				mapper.put(exchange);
				break;
			} else {
				try {
					Thread.sleep(1000 * 60 * 10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			i++;
		}
		logger.info("通知 end---------------------");
	}

}
