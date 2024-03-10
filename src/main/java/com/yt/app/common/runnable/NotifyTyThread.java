package com.yt.app.common.runnable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.api.v1.vo.SysResultVO;
import com.yt.app.common.base.context.BeanContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.PayUtil;

public class NotifyTyThread implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(NotifyTyThread.class);

	private Long id;

	public NotifyTyThread(Long _id) {
		id = _id;
	}

	@Override
	public void run() {
		TenantIdContext.removeFlag();
		PayoutMapper mapper = BeanContext.getApplicationContext().getBean(PayoutMapper.class);
		MerchantMapper merchantmapper = BeanContext.getApplicationContext().getBean(MerchantMapper.class);
		Payout payout = mapper.get(id);
		Merchant merchant = merchantmapper.get(payout.getMerchantid());
		SysResultVO ss = new SysResultVO();
		ss.setMerchantid(payout.getMerchantcode());
		ss.setPayorderid(payout.getChannelordernum());
		ss.setMerchantorderid(payout.getMerchantordernum());
		ss.setPayamt(payout.getAmount());
		ss.setBankcode(payout.getBankcode());
		ss.setCode(payout.getStatus());
		ss.setRemark("remark");
		logger.info("通知 start---------------------商户单号：" + payout.getMerchantordernum());
		int i = 1;
		while (true) {
			if (i > 3) {
				payout.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_64);
				mapper.put(payout);
				break;
			}
			YtBody result;
			try {
				result = PayUtil.SendNotify(payout.getNotifyurl(), ss, merchant.getAppkey());
				// 通知到
				if (result.getCode() == 200) {
					payout.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_63);
					mapper.put(payout);
					break;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					Thread.sleep(1000 * 60 * 10);
					i++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		logger.info("通知 end---------------------");
	}

}
