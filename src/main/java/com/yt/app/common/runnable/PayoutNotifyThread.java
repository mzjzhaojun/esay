package com.yt.app.common.runnable;

import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.api.v1.vo.PayResultVO;
import com.yt.app.common.base.context.BeanContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.PayUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PayoutNotifyThread implements Runnable {

	private Long id;

	public PayoutNotifyThread(Long _id) {
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
		log.info("代付通知 start---------------------商户单号：" + payout.getOrdernum());
		int i = 1;
		while (true) {
			try {
				String result = PayUtil.SendPayoutNotify(payout.getNotifyurl(), ss, merchant.getAppkey());
				// 通知到
				if (result.equals("success")) {
					log.info("代付通知成功，商户单号：" + payout.getOrdernum());
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
					Thread.sleep(1000 * 60 * 5);
					i++;
					if (i >= 3) {
						log.info("代付通知失败XXXXXX商户单号：" + payout.getOrdernum());
						payout.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_64);
						mapper.put(payout);
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		log.info("代付通知 end---------------------" + payout.getOrdernum());
	}

}
