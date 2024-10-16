package com.yt.app.common.runnable;

import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.mapper.IncomeMapper;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.vo.QueryQrcodeResultVO;
import com.yt.app.common.base.context.BeanContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.PayUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InComeNotifyThread implements Runnable {

	private Long id;

	public InComeNotifyThread(Long _id) {
		id = _id;
	}

	@Override
	public void run() {
		TenantIdContext.removeFlag();
		IncomeMapper mapper = BeanContext.getApplicationContext().getBean(IncomeMapper.class);
		MerchantMapper merchantmapper = BeanContext.getApplicationContext().getBean(MerchantMapper.class);
		Income income = mapper.get(id);
		Merchant merchant = merchantmapper.get(income.getMerchantid());
		QueryQrcodeResultVO qqr = new QueryQrcodeResultVO();
		qqr.setPay_memberid(income.getMerchantcode());
		qqr.setPay_orderid(income.getMerchantorderid());
		qqr.setPay_amount(income.getAmount().toString());
		qqr.setPay_code(income.getStatus());
		log.info("代收通知 start-----------商户单号：" + income.getOrdernum() + " url:" + income.getNotifyurl());
		int i = 1;
		while (true) {
			YtBody result;
			try {
				result = PayUtil.SendIncomeNotify(income.getNotifyurl(), qqr, merchant.getAppkey());
				// 通知到
				if (result.getCode() == 200) {
					log.info("代收通知成功，商户单号：" + income.getOrdernum());
					income.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_63);
					int j = mapper.put(income);
					if (j > 0) {
						income.setVersion(income.getVersion() + 1);
					}
					break;
				}
			} catch (Exception e1) {
				log.info("代收通知错误：" + e1.getMessage());
			} finally {
				try {
					Thread.sleep(1000 * 60 * 5);
					i++;
					if (i >= 3) {
						log.info("代收通知失败XXXXXX商户单号：" + income.getOrdernum());
						income.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_64);
						mapper.put(income);
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		log.info("代收通知 end---------------------" + income.getOrdernum());
	}

}
