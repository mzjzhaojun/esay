package com.yt.app.common.runnable;

import java.util.Random;

import com.yt.app.api.v1.entity.Agentaccountorder;
import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Channelaccountorder;
import com.yt.app.api.v1.entity.PayoutMerchantaccountorder;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.api.v1.mapper.AgentaccountorderMapper;
import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.ChannelaccountorderMapper;
import com.yt.app.api.v1.mapper.PayoutMerchantaccountorderMapper;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.api.v1.service.AgentaccountService;
import com.yt.app.api.v1.service.ChannelaccountService;
import com.yt.app.api.v1.service.PayoutMerchantaccountService;
import com.yt.app.common.base.context.BeanContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.DateTimeUtil;
import com.yt.app.common.util.PayUtil;
import com.yt.app.common.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PayoutGetChannelOrderNumThread implements Runnable {

	private Long id;

	public PayoutGetChannelOrderNumThread(Long _id) {
		id = _id;
	}

	@Override
	public void run() {
		TenantIdContext.removeFlag();
		PayoutMapper mapper = BeanContext.getApplicationContext().getBean(PayoutMapper.class);
		PayoutMerchantaccountorderMapper merchantaccountordermapper = BeanContext.getApplicationContext().getBean(PayoutMerchantaccountorderMapper.class);
		AgentaccountorderMapper agentaccountordermapper = BeanContext.getApplicationContext().getBean(AgentaccountorderMapper.class);
		PayoutMerchantaccountService merchantaccountservice = BeanContext.getApplicationContext().getBean(PayoutMerchantaccountService.class);
		AgentaccountService agentaccountservice = BeanContext.getApplicationContext().getBean(AgentaccountService.class);
		ChannelMapper channelmapper = BeanContext.getApplicationContext().getBean(ChannelMapper.class);
		ChannelaccountorderMapper channelaccountordermapper = BeanContext.getApplicationContext().getBean(ChannelaccountorderMapper.class);
		ChannelaccountService channelaccountservice = BeanContext.getApplicationContext().getBean(ChannelaccountService.class);
		Payout payout = mapper.get(id);
		Channel channel = channelmapper.get(payout.getChannelid());
		Random rd = new Random();
		log.info("代付获取渠道单号 start---------------------商户单号：" + payout.getMerchantordernum());
		int i = 1;
		while (true) {
			try {
				String channelordernum = "PC" + StringUtil.getOrderNum();
				if (channel.getIfordernum()) {
					channelordernum = PayUtil.SendTySubmit(payout, channel);
					if (channelordernum == null) {

						// 计算商户订单/////////////////////////////////////////////////////
						PayoutMerchantaccountorder mao = merchantaccountordermapper.getByOrdernum(payout.getMerchantordernum());
						mao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_12);
						merchantaccountordermapper.put(mao);
						//
						merchantaccountservice.failPayout(mao);

						// 计算代理
						if (payout.getAgentid() != null) {
							Agentaccountorder aao = agentaccountordermapper.getByOrdernum(payout.getAgentordernum());
							aao.setStatus(DictionaryResource.MERCHANTORDERSTATUS_12);
							agentaccountordermapper.put(aao);
							//
							agentaccountservice.turndownTotalincome(aao);
						}
						//
						payout.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_60);
						payout.setRemark("代付失败￥" + payout.getAmount());
						payout.setSuccesstime(DateTimeUtil.getNow());
						payout.setBacklong(10L);
						payout.setStatus(DictionaryResource.PAYOUTSTATUS_54);
						mapper.put(payout);
						break;
					}
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
					cat.setChannelid(cll.getId());
					cat.setChannelname(cll.getName());
					cat.setOnecost(cll.getOnecost());
					cat.setNkname(cll.getNkname());
					cat.setChannelcode(cll.getCode());
					cat.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
					cat.setAmount(payout.getAmount());// 操作资金
					cat.setDeal(payout.getChanneldeal());// 交易费
					cat.setOnecost(payout.getChannelcost());// 手续费
					cat.setAccname(payout.getAccname());
					cat.setAccnumber(payout.getAccnumer());
					cat.setExchange(cll.getExchange());
					cat.setChannelexchange(cll.getExchange());
					cat.setAmountreceived(payout.getChannelpay());
					cat.setType(DictionaryResource.ORDERTYPE_23);
					cat.setOrdernum(payout.getChannelordernum());
					cat.setRemark("代付资金￥：" + cat.getAmount() + " 交易费：" + String.format("%.2f", cat.getDeal()) + " 手续费：" + cat.getOnecost());
					channelaccountordermapper.post(cat);
					channelaccountservice.withdrawamount(cat);
					break;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					Thread.sleep(1000 * rd.nextInt(60));
					i++;
					if (i > 3) {
						payout.setStatus(DictionaryResource.PAYOUTSTATUS_54);
						mapper.put(payout);
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		TenantIdContext.remove();
		log.info("获取 end---------------------");
	}

}
