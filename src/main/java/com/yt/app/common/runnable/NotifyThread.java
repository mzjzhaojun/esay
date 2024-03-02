package com.yt.app.common.runnable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.api.v1.vo.SysResultVO;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.PayUtil;

public class NotifyThread implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(NotifyThread.class);

	private PayoutMapper mapper;
	private MerchantMapper merchantmapper;
	private Payout payout;
	private Integer code;

	public NotifyThread(PayoutMapper _mapper, MerchantMapper _merchantmapper, Payout pt, Integer _code) {
		mapper = _mapper;
		merchantmapper = _merchantmapper;
		payout = pt;
		code = _code;
	}

	@Override
	public void run() {
		Merchant merchant = merchantmapper.get(payout.getMerchantid());
		SysResultVO ss = new SysResultVO();
		ss.setMerchantid(payout.getMerchantcode());
		ss.setPayorderid(payout.getChannelordernum());
		ss.setMerchantorderid(payout.getMerchantordernum());
		ss.setPayamt(payout.getAmount());
		ss.setBankcode(payout.getBankcode());
		ss.setCode(code);
		ss.setRemark("remark");
		logger.info("通知 start---------------------商户单号：" + payout.getMerchantordernum());
		int i = 1;
		while (true) {
			if (i > 3) {
				payout.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_63);
				mapper.put(payout);
				break;
			}
			YtBody result = PayUtil.SendNotify(ss, merchant.getAppkey());
			// 通知到
			if (result.getCode() == 200) {
				payout.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_62);
				mapper.put(payout);
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
