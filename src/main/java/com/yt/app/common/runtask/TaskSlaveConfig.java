package com.yt.app.common.runtask;

import java.util.Date;
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
import com.yt.app.api.v1.entity.Tronmemberorder;
import com.yt.app.api.v1.mapper.AgentaccountorderMapper;
import com.yt.app.api.v1.mapper.IncomeMapper;
import com.yt.app.api.v1.mapper.IncomemerchantaccountorderMapper;
import com.yt.app.api.v1.mapper.QrcodeaccountorderMapper;
import com.yt.app.api.v1.mapper.TronmemberorderMapper;
import com.yt.app.api.v1.service.AgentaccountService;
import com.yt.app.api.v1.service.IncomemerchantaccountService;
import com.yt.app.api.v1.service.QrcodeaccountService;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.bot.TronBot;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.runnable.StatisticsThread;
import com.yt.app.common.util.DateTimeUtil;
import com.yt.app.common.util.NumberUtil;
import com.yt.app.common.util.RedisUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("master")
@Component
public class TaskSlaveConfig {

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

	@Autowired
	private TronmemberorderMapper tronmemberordermapper;

	@Autowired
	private TronBot tronbot;

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
	}

	/**
	 * 代收超时分钟未支付订单处理
	 */
	@Scheduled(cron = "0/30 * * * * ?")
	public void income() throws InterruptedException {
		TenantIdContext.removeFlag();
		List<Income> list = incomemapper.selectAddlist();
		for (Income p : list) {
			if (p.getExpireddate().getTime() < new Date().getTime()) {

				TenantIdContext.setTenantId(p.getTenant_id());
				log.info("代收支付超时单号ID：" + p.getOrdernum() + " 状态：" + p.getStatus());
				p.setStatus(DictionaryResource.PAYOUTSTATUS_53);
				p.setRemark("超時取消代收资金￥：" + p.getAmount());
				incomemapper.put(p);
				// 處理渠道
				Qrcodeaccountorder qao = qrcodeaccountordermapper.getByOrderNum(p.getQrcodeordernum());
				qao.setStatus(DictionaryResource.PAYOUTSTATUS_53);
				qrcodeaccountordermapper.put(qao);
				qrcodeaccountservice.cancleTotalincome(qao);
				// 處理商戶
				Incomemerchantaccountorder imqao = incomemerchantaccountordermapper.getByOrderNum(p.getMerchantorderid());
				imqao.setStatus(DictionaryResource.PAYOUTSTATUS_53);
				incomemerchantaccountordermapper.put(imqao);
				incomemerchantaccountservice.cancleTotalincome(imqao);

				// 释放收款码数据
				String key = SystemConstant.CACHE_SYS_QRCODE + p.getQrcodeid() + "" + p.getFewamount();
				if (RedisUtil.hasKey(key))
					RedisUtil.delete(key);

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

	/**
	 * 充值超时分钟未支付订单处理
	 */
	@Scheduled(cron = "0/30 * * * * ?")
	public void tronorder() throws InterruptedException {
		TenantIdContext.removeFlag();
		List<Tronmemberorder> list = tronmemberordermapper.selectAddlist();
		for (Tronmemberorder p : list) {
			if (p.getExpireddate().getTime() < new Date().getTime()) {

				TenantIdContext.setTenantId(p.getTenant_id());
				log.info("充值兑换超时单号ID：" + p.getOrdernum() + " 状态：" + p.getStatus());
				p.setStatus(DictionaryResource.PAYOUTSTATUS_53);
				p.setRemark("超时充值兑换￥：" + p.getAmount());
				tronmemberordermapper.put(p);
				NumberUtil.removeExchangeFewAmount(p.getFewamount());
				tronbot.notifyMermberCancel(p.getTgid(), p.getOrdernum(), p.getAmount());
				TenantIdContext.remove();
			}
		}
	}
}
