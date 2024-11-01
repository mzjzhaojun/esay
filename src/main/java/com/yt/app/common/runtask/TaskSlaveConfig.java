package com.yt.app.common.runtask;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.yt.app.api.v1.entity.Agentaccountorder;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.entity.Incomemerchantaccountorder;
import com.yt.app.api.v1.entity.Qrcodeaccountorder;
import com.yt.app.api.v1.entity.Tgchannelgroup;
import com.yt.app.api.v1.entity.Tgmerchantgroup;
import com.yt.app.api.v1.mapper.AgentaccountorderMapper;
import com.yt.app.api.v1.mapper.IncomeMapper;
import com.yt.app.api.v1.mapper.IncomemerchantaccountorderMapper;
import com.yt.app.api.v1.mapper.QrcodeaccountorderMapper;
import com.yt.app.api.v1.mapper.TgchannelgroupMapper;
import com.yt.app.api.v1.mapper.TgmerchantgroupMapper;
import com.yt.app.api.v1.service.AgentaccountService;
import com.yt.app.api.v1.service.IncomemerchantaccountService;
import com.yt.app.api.v1.service.QrcodeaccountService;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.runnable.StatisticsThread;
import com.yt.app.common.util.DateTimeUtil;
import com.yt.app.common.util.RedisUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("dev")
@Component
public class TaskSlaveConfig {

	@Autowired
	private TgmerchantgroupMapper tgmerchantgroupmapper;

	@Autowired
	private TgchannelgroupMapper tgchannelgroupmapper;

	@Autowired
	private ThreadPoolTaskExecutor threadpooltaskexecutor;

	@Autowired
	private QrcodeaccountorderMapper qrcodeaccountordermapper;

	@Autowired
	private IncomemerchantaccountorderMapper incomemerchantaccountordermapper;

	@Autowired
	private QrcodeaccountService qrcodeaccountservice;

	@Autowired
	private IncomemerchantaccountService incomemerchantaccountservice;

	@Autowired
	private AgentaccountorderMapper agentaccountordermapper;

	@Autowired
	private AgentaccountService agentaccountservice;

	@Autowired
	private IncomeMapper incomemapper;

	/**
	 * 更新每日新数据
	 * 
	 * @throws InterruptedException
	 */
	@Scheduled(cron = "30 59 23 * * ?")
	public void updateTodayValue() throws InterruptedException {
		TenantIdContext.removeFlag();
		String date = DateTimeUtil.getDateTime(new Date(), DateTimeUtil.DEFAULT_DATE_FORMAT);

		StatisticsThread statisticsthread = new StatisticsThread(date);
		threadpooltaskexecutor.execute(statisticsthread);

		// 飞机商户
		List<Tgmerchantgroup> listtmg = tgmerchantgroupmapper.list(new HashMap<String, Object>());
		listtmg.forEach(mg -> {
			tgmerchantgroupmapper.updatetodayvalue(mg.getId());
		});
		// 飞机渠道
		List<Tgchannelgroup> listtcg = tgchannelgroupmapper.list(new HashMap<String, Object>());
		listtcg.forEach(mg -> {
			tgchannelgroupmapper.updatetodayvalue(mg.getId());
		});
	}

	/**
	 * 代收超时分钟未支付订单处理
	 */
	@Scheduled(cron = "0/50 * * * * ?")
	public void income() throws InterruptedException {
		TenantIdContext.removeFlag();
		List<Income> list = incomemapper.selectAddlist();
		for (Income p : list) {
			if (p.getExpireddate().getTime() < new Date().getTime()) {
				TenantIdContext.setTenantId(p.getTenant_id());
				log.info("代收支付超时单号ID：" + p.getOrdernum() + " 状态：" + p.getStatus());
				p.setStatus(DictionaryResource.PAYOUTSTATUS_53);
				p.setRemark("取消代收资金￥：" + p.getAmount());
				incomemapper.put(p);
				//
				Qrcodeaccountorder qao = qrcodeaccountordermapper.getByOrderNum(p.getQrcodeordernum());
				qao.setStatus(DictionaryResource.PAYOUTSTATUS_53);
				qrcodeaccountordermapper.put(qao);
				//
				Incomemerchantaccountorder imqao = incomemerchantaccountordermapper.getByOrderNum(p.getMerchantorderid());
				imqao.setStatus(DictionaryResource.PAYOUTSTATUS_53);
				incomemerchantaccountordermapper.put(imqao);

				// 释放收款码数据
				String key = SystemConstant.CACHE_SYS_QRCODE + p.getQrcodeid() + "" + p.getFewamount();
				if (RedisUtil.hasKey(key))
					RedisUtil.delete(key);
				// 处理账户
				qrcodeaccountservice.cancleTotalincome(qao);
				incomemerchantaccountservice.cancleTotalincome(imqao);

				// 计算代理
				if (p.getAgentid() != null) {
					Agentaccountorder aao = agentaccountordermapper.getByOrdernum(p.getAgentordernum());
					aao.setStatus(DictionaryResource.PAYOUTSTATUS_53);
					// 代理订单
					agentaccountordermapper.put(aao);
					// 代理账户
					agentaccountservice.cancleTotalincome(aao);
				}

				TenantIdContext.remove();
			}
		}
	}
}
