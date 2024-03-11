package com.yt.app.common.runnable;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Channelaccountorder;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.entity.Tgchannelgroup;
import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.ChannelaccountorderMapper;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.api.v1.mapper.TgchannelgroupMapper;
import com.yt.app.api.v1.service.ChannelaccountService;
import com.yt.app.common.base.context.BeanContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.bot.Channelbot;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.DateTimeUtil;
import com.yt.app.common.util.PayUtil;
import com.yt.app.common.util.StringUtil;

public class GetPayoutChannelOrderNumThread implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(GetPayoutChannelOrderNumThread.class);

	private Long id;

	public GetPayoutChannelOrderNumThread(Long _id) {
		id = _id;
	}

	@Override
	public void run() {
		TenantIdContext.removeFlag();
		PayoutMapper mapper = BeanContext.getApplicationContext().getBean(PayoutMapper.class);
		ChannelMapper channelmapper = BeanContext.getApplicationContext().getBean(ChannelMapper.class);
		TgchannelgroupMapper tgchannelgroupmapper = BeanContext.getApplicationContext()
				.getBean(TgchannelgroupMapper.class);
		ChannelaccountorderMapper channelaccountordermapper = BeanContext.getApplicationContext()
				.getBean(ChannelaccountorderMapper.class);
		ChannelaccountService channelaccountservice = BeanContext.getApplicationContext()
				.getBean(ChannelaccountService.class);
		Channelbot cbot = BeanContext.getApplicationContext().getBean(Channelbot.class);
		Payout payout = mapper.get(id);
		Channel channel = channelmapper.get(payout.getChannelid());
		Random rd = new Random();
		logger.info("代付获取渠道单号 start---------------------商户单号：" + payout.getMerchantordernum());
		int i = 1;
		while (true) {
			try {
				String channelordernum = "PC" + StringUtil.getOrderNum();
				if (channel.getIfordernum()) {
					channelordernum = PayUtil.SendTySubmit(payout, channel);
				}
				// 获取到单号
				payout.setChannelordernum(channelordernum);
				payout.setStatus(DictionaryResource.PAYOUTSTATUS_51);
				int j = mapper.put(payout);
				if (j > 0) {
					TenantIdContext.setTenantId(payout.getTenant_id());
					Channel cll = channelmapper.get(payout.getChannelid());
					Channelaccountorder cat = new Channelaccountorder();
					cat.setUserid(cll.getUserid());
//
					cat.setChannelid(cll.getId());
					cat.setUsername(cll.getName());
					cat.setNkname(cll.getNkname());
					cat.setChannelcode(cll.getCode());
					cat.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
					cat.setAmount(payout.getChanneldeal());
					cat.setExchange(cll.getExchange());
					cat.setChannelexchange(cll.getExchange());
					cat.setAmountreceived(payout.getChannelpay());
					cat.setType(DictionaryResource.ORDERTYPE_22);
					cat.setOrdernum(payout.getChannelordernum());
					cat.setRemark("资金：" + payout.getAmount() + " 交易费：" + String.format("%.2f", cat.getAmount())
							+ " 手续费：" + cll.getOnecost());
					channelaccountordermapper.post(cat);
//
					channelaccountservice.withdrawamount(cat);

					Tgchannelgroup tgchannelgroup = tgchannelgroupmapper.getByChannelId(payout.getChannelid());
					StringBuffer what = new StringBuffer();
					what.append("状态：新增代付\n");
					what.append("单号：" + payout.getChannelordernum() + "\n");
					what.append("姓名：" + payout.getAccname() + "\n");
					what.append("卡号：" + payout.getAccnumer() + "\n");
					what.append("金额：" + payout.getAmount() + "\n");
					what.append("发起时间：" + DateTimeUtil.getDateTime() + "\n");
					what.append("客户请你们尽快处理\n");
					if (tgchannelgroup != null)
						cbot.sendText(tgchannelgroup.getTgid(), what.toString());
				}
				break;
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					Thread.sleep(1000 * rd.nextInt(60));
					i++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (i > 3) {
				payout.setStatus(DictionaryResource.PAYOUTSTATUS_54);
				mapper.put(payout);
				break;
			}
		}
		TenantIdContext.remove();
		logger.info("获取 end---------------------");
	}

}
