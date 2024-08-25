package com.yt.app.common.runnable;

import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.api.v1.vo.PayResultVO;
import com.yt.app.common.base.context.BeanContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.PayUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotifyTyPayoutThread implements Runnable {

	private Long id;

	public NotifyTyPayoutThread(Long _id) {
		id = _id;
	}

	@Override
	public void run() {
		TenantIdContext.removeFlag();
		PayoutMapper mapper = BeanContext.getApplicationContext().getBean(PayoutMapper.class);
		MerchantMapper merchantmapper = BeanContext.getApplicationContext().getBean(MerchantMapper.class);
		Payout payout = mapper.get(id);
		Merchant merchant = merchantmapper.get(payout.getMerchantid());
		PayResultVO ss = new PayResultVO();
		ss.setMerchantid(payout.getMerchantcode());
		ss.setPayorderid(payout.getMerchantordernum());
		ss.setMerchantorderid(payout.getMerchantorderid());
		ss.setPayamt(payout.getAmount());
		ss.setBankcode(payout.getBankcode());
		ss.setCode(payout.getStatus());
		ss.setRemark(payout.getRemark());
		log.info("通知 start---------------------商户单号：" + payout.getMerchantordernum());
		int i = 1;
		while (true) {
			YtBody result;
			try {
				result = PayUtil.SendPayoutNotify(payout.getNotifyurl(), ss, merchant.getAppkey());
				// 通知到
				if (result.getCode() == 200) {
					payout.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_63);
					int j = mapper.put(payout);
					if (j > 0) {
						payout.setVersion(payout.getVersion() + 1);
					}
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
				if (i > 3) {
					payout.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_64);
					mapper.put(payout);
					break;
				}
			}
		}
		log.info("通知 end---------------------");
	}

}
