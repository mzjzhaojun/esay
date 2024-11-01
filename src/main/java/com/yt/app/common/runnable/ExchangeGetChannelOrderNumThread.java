package com.yt.app.common.runnable;

import java.util.Random;

import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Channelaccountorder;
import com.yt.app.api.v1.entity.Exchange;
import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.ChannelaccountorderMapper;
import com.yt.app.api.v1.mapper.ExchangeMapper;
import com.yt.app.api.v1.service.ChannelaccountService;
import com.yt.app.common.base.context.BeanContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExchangeGetChannelOrderNumThread implements Runnable {

	private Long id;

	public ExchangeGetChannelOrderNumThread(Long _id) {
		id = _id;
	}

	@Override
	public void run() {
		TenantIdContext.removeFlag();
		ExchangeMapper mapper = BeanContext.getApplicationContext().getBean(ExchangeMapper.class);
		ChannelMapper channelmapper = BeanContext.getApplicationContext().getBean(ChannelMapper.class);
		ChannelaccountorderMapper channelaccountordermapper = BeanContext.getApplicationContext().getBean(ChannelaccountorderMapper.class);
		ChannelaccountService channelaccountservice = BeanContext.getApplicationContext().getBean(ChannelaccountService.class);
		Exchange exchange = mapper.get(id);
		Random rd = new Random();
		log.info("换汇获取渠道单号 start---------------------商户单号：" + exchange.getMerchantordernum());
		int i = 1;
		while (true) {
			try {
				String channelordernum = "EC" + StringUtil.getOrderNum();
				if (channelordernum == null || channelordernum.equals("")) {
					exchange.setStatus(DictionaryResource.PAYOUTSTATUS_54);
					mapper.put(exchange);
					break;
				}
				// 获取到单号
				exchange.setChannelordernum(channelordernum);
				exchange.setStatus(DictionaryResource.PAYOUTSTATUS_51);
				int j = mapper.put(exchange);
				if (j > 0) {
					TenantIdContext.setTenantId(exchange.getTenant_id());
					Channel cll = channelmapper.get(exchange.getChannelid());
					Channelaccountorder cat = new Channelaccountorder();
					cat.setUserid(cll.getUserid());
					cat.setChannelid(cll.getId());
					cat.setChannelname(cll.getName());
					cat.setOnecost(cll.getOnecost());
					cat.setNkname(cll.getNkname());
					cat.setChannelcode(cll.getCode());
					cat.setStatus(DictionaryResource.MERCHANTORDERSTATUS_10);
					cat.setAmount(exchange.getAmount());// 操作资金
					cat.setDeal(exchange.getChannelpay());// 交易费
					cat.setOnecost(exchange.getChannelonecost());// 手续费
					cat.setExchange(exchange.getChannelrealtimeexchange());
					cat.setAccname(exchange.getAccname());
					cat.setAccnumber(exchange.getAccnumer());
					cat.setChannelexchange(exchange.getChanneldowpoint());
					cat.setAmountreceived(exchange.getChannelpay());
					cat.setType(DictionaryResource.ORDERTYPE_22);
					cat.setOrdernum(exchange.getChannelordernum());
					cat.setRemark("换汇资金￥：" + cat.getAmount() + " 手续费：" + cat.getOnecost());
					channelaccountordermapper.post(cat);
					channelaccountservice.cancleWithdrawamount(cat);
					break;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					Thread.sleep(1000 * rd.nextInt(60));
					i++;
					if (i > 3) {
						exchange.setStatus(DictionaryResource.PAYOUTSTATUS_54);
						mapper.put(exchange);
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
