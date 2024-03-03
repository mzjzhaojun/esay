package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.AgentMapper;
import com.yt.app.api.v1.mapper.AgentaccountMapper;
import com.yt.app.api.v1.mapper.AgentaccountapplyjournaMapper;
import com.yt.app.api.v1.mapper.AgentaccountbankMapper;
import com.yt.app.api.v1.service.AgentService;
import com.yt.app.api.v1.service.AgentaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.context.JwtUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Agent;
import com.yt.app.api.v1.entity.Agentaccount;
import com.yt.app.api.v1.entity.Agentaccountapplyjourna;
import com.yt.app.api.v1.entity.Agentaccountbank;
import com.yt.app.api.v1.entity.Agentaccountorder;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.RedissonUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:44:15
 */

@Service
public class AgentaccountServiceImpl extends YtBaseServiceImpl<Agentaccount, Long> implements AgentaccountService {
	@Autowired
	private AgentMapper agentmapper;

	@Autowired
	private AgentaccountMapper mapper;

	@Autowired
	private AgentaccountbankMapper agentaccountbankmapper;

	@Autowired
	private AgentaccountapplyjournaMapper agentaccountapplyjournamapper;

	@Autowired
	private AgentService agentservice;

	@Override
	@Transactional
	public Integer post(Agentaccount t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Agentaccount> list(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<Agentaccount>(Collections.emptyList());
			}
		}
		List<Agentaccount> list = mapper.list(param);
		return new YtPageBean<Agentaccount>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Agentaccount get(Long id) {
		Agentaccount t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Agentaccount getData() {
		Agentaccount ac = mapper.getByUserId(JwtUserContext.get().getUserId());
		return ac;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Agentaccount getDataBank() {
		Agentaccount t = mapper.getByUserId(JwtUserContext.get().getUserId());
		List<Agentaccountbank> listbanks = agentaccountbankmapper.listByUserid(JwtUserContext.get().getUserId());
		t.setListbanks(listbanks);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Agentaccount getDataBank(Long id) {
		Agent m = agentmapper.get(id);
		Agentaccount t = mapper.getByUserId(m.getUserid());
		List<Agentaccountbank> listbanks = agentaccountbankmapper.listByUserid(m.getUserid());
		t.setListbanks(listbanks);
		return t;
	}

	/**
	 * =============================================================充值
	 * 
	 */

	// 待确认收入
	@Override
	@Transactional
	public void totalincome(Agentaccountorder t) {
		Agentaccount ma = mapper.getByUserId(t.getUserid());
		// 资金记录
		Agentaccountapplyjourna aaaj = new Agentaccountapplyjourna();

		aaaj.setUserid(t.getUserid());
		aaaj.setAgentname(t.getUsername());
		aaaj.setOrdernum(t.getOrdernum());
		aaaj.setType(DictionaryResource.RECORDTYPE_30);
		// 变更前
		aaaj.setPretotalincome(ma.getTotalincome());// 总收入
		aaaj.setPretoincomeamount(ma.getToincomeamount() + t.getAmountreceived());// 待确认收入
		aaaj.setPrewithdrawamount(ma.getWithdrawamount());// 总支出
		aaaj.setPretowithdrawamount(ma.getWithdrawamount());// 待确认支出
		// 变更后
		aaaj.setPosttotalincome(ma.getTotalincome());// 总收入
		aaaj.setPosttoincomeamount(0.00);// 确认收入
		aaaj.setPostwithdrawamount(ma.getWithdrawamount());// 总支出
		aaaj.setPosttowithdrawamount(0.00);// 确认支出
		aaaj.setRemark("待确认代理收入金额：" + t.getRemark());
		//
		agentaccountapplyjournamapper.post(aaaj);
		RLock lock = RedissonUtil.getLock(t.getId());
		try {
			lock.lock();
			ma.setToincomeamount(aaaj.getPretoincomeamount());
			mapper.put(ma);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 确认收入
	@Override
	@Transactional
	public void updateTotalincome(Agentaccountorder mao) {
		Agentaccount t = mapper.getByUserId(mao.getUserid());

		//
		Agentaccountapplyjourna aaaj = new Agentaccountapplyjourna();
		aaaj.setUserid(t.getUserid());
		aaaj.setAgentname(mao.getUsername());
		aaaj.setOrdernum(mao.getOrdernum());
		aaaj.setType(DictionaryResource.RECORDTYPE_32);

		// 变更前
		aaaj.setPretotalincome(t.getTotalincome());// 总收入
		aaaj.setPretoincomeamount(t.getToincomeamount() - mao.getAmountreceived());// 待确认收入
		aaaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
		aaaj.setPretowithdrawamount(t.getWithdrawamount());// 待确认支出
		// 变更后
		aaaj.setPosttotalincome(t.getTotalincome() + mao.getAmountreceived());// 总收入
		aaaj.setPosttoincomeamount(mao.getAmountreceived());// 确认收入
		aaaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
		aaaj.setPosttowithdrawamount(0.00);// 确认支出
		aaaj.setRemark("收入成功金额：" + String.format("%.2f", mao.getAmountreceived()));
		//
		agentaccountapplyjournamapper.post(aaaj);

		RLock lock = RedissonUtil.getLock(t.getId());
		try {
			lock.lock();
			t.setTotalincome(aaaj.getPosttotalincome());// 收入增加金额
			t.setToincomeamount(aaaj.getPretoincomeamount());// 待收入减去金额.
			t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}

		agentservice.updateIncome(t);
	}

	// 拒绝收入
	@Override
	@Transactional
	public void turndownTotalincome(Agentaccountorder mao) {
		Agentaccount t = mapper.getByUserId(mao.getUserid());
		//
		Agentaccountapplyjourna aaaj = new Agentaccountapplyjourna();
		aaaj.setUserid(t.getUserid());
		aaaj.setAgentname(mao.getUsername());
		aaaj.setOrdernum(mao.getOrdernum());
		aaaj.setType(DictionaryResource.RECORDTYPE_34);

		// 变更前
		aaaj.setPretotalincome(t.getTotalincome());// 总收入
		aaaj.setPretoincomeamount(t.getToincomeamount() - mao.getAmountreceived());// 待确认收入
		aaaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
		aaaj.setPretowithdrawamount(t.getWithdrawamount());// 待确认支出
		// 变更后
		aaaj.setPosttotalincome(t.getTotalincome());// 总收入
		aaaj.setPosttoincomeamount(0.00);// 确认收入
		aaaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
		aaaj.setPosttowithdrawamount(0.00);// 确认支出
		aaaj.setRemark("收入失败，金额：" + String.format("%.2f", mao.getAmountreceived()));
		//
		agentaccountapplyjournamapper.post(aaaj);

		RLock lock = RedissonUtil.getLock(t.getId());
		try {
			lock.lock();
			t.setToincomeamount(aaaj.getPretoincomeamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 客户取消
	@Override
	@Transactional
	public void cancleTotalincome(Agentaccountorder mao) {
		Agentaccount t = mapper.getByUserId(mao.getUserid());
		//
		Agentaccountapplyjourna aaaj = new Agentaccountapplyjourna();
		aaaj.setUserid(t.getUserid());
		aaaj.setAgentname(mao.getUsername());
		aaaj.setOrdernum(mao.getOrdernum());
		aaaj.setType(DictionaryResource.RECORDTYPE_36);

		// 变更前
		aaaj.setPretotalincome(t.getTotalincome());// 总收入
		aaaj.setPretoincomeamount(t.getToincomeamount() - mao.getAmountreceived());// 待确认收入
		aaaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
		aaaj.setPretowithdrawamount(t.getWithdrawamount());// 待确认支出
		// 变更后
		aaaj.setPosttotalincome(t.getTotalincome());// 总收入
		aaaj.setPosttoincomeamount(0.00);// 确认收入
		aaaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
		aaaj.setPosttowithdrawamount(0.00);// 确认支出
		aaaj.setRemark("收入取消，金额：" + String.format("%.2f", mao.getAmountreceived()));
		//
		agentaccountapplyjournamapper.post(aaaj);

		RLock lock = RedissonUtil.getLock(t.getId());
		try {
			lock.lock();
			t.setToincomeamount(aaaj.getPretoincomeamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * =============================================================提现
	 * 
	 */

	// 待确认支出
	@Override
	@Transactional
	public void withdrawamount(Agentaccountorder t) {
		Agentaccount ma = mapper.getByUserId(t.getUserid());
		Agentaccountapplyjourna aaaj = new Agentaccountapplyjourna();

		aaaj.setUserid(t.getUserid());
		aaaj.setAgentname(t.getUsername());
		aaaj.setOrdernum(t.getOrdernum());
		aaaj.setType(DictionaryResource.RECORDTYPE_31);
		// 变更前
		aaaj.setPretotalincome(ma.getTotalincome());// 总收入
		aaaj.setPretoincomeamount(ma.getToincomeamount());// 待确认收入
		aaaj.setPrewithdrawamount(ma.getWithdrawamount());// 总支出
		aaaj.setPretowithdrawamount(ma.getTowithdrawamount() + t.getAmountreceived());// 待确认支出
		// 变更后
		aaaj.setPosttotalincome(ma.getTotalincome());// 总收入
		aaaj.setPosttoincomeamount(0.00);// 确认收入
		aaaj.setPostwithdrawamount(ma.getWithdrawamount());// 总支出
		aaaj.setPosttowithdrawamount(0.00);// 确认支出
		aaaj.setRemark("冻结待提现金额：" + String.format("%.2f", t.getAmountreceived()));
		//
		agentaccountapplyjournamapper.post(aaaj);

		RLock lock = RedissonUtil.getLock(t.getId());
		try {
			lock.lock();
			ma.setTowithdrawamount(aaaj.getPretowithdrawamount());
			mapper.put(ma);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 确认支出
	@Override
	@Transactional
	public void updateWithdrawamount(Agentaccountorder mao) {
		Agentaccount t = mapper.getByUserId(mao.getUserid());
		//
		Agentaccountapplyjourna aaaj = new Agentaccountapplyjourna();
		//
		aaaj.setUserid(t.getUserid());
		aaaj.setAgentname(mao.getUsername());
		aaaj.setOrdernum(mao.getOrdernum());
		aaaj.setType(DictionaryResource.RECORDTYPE_33);

		// 变更前
		aaaj.setPretotalincome(t.getTotalincome());// 总收入
		aaaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
		aaaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
		aaaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmountreceived());// 待确认支出
		// 变更后
		aaaj.setPosttotalincome(t.getTotalincome());// 总收入
		aaaj.setPosttoincomeamount(0.00);// 确认收入
		aaaj.setPostwithdrawamount(t.getWithdrawamount() + mao.getAmountreceived());// 总支出
		aaaj.setPosttowithdrawamount(mao.getAmountreceived());// 确认支出
		aaaj.setRemark("提现成功，金额：" + String.format("%.2f", mao.getAmountreceived()));
		//
		agentaccountapplyjournamapper.post(aaaj);
		RLock lock = RedissonUtil.getLock(t.getId());
		try {
			lock.lock();
			t.setWithdrawamount(aaaj.getPostwithdrawamount());// 支出增加金额
			t.setTowithdrawamount(aaaj.getPretowithdrawamount());// 待支出减去金额
			t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
		agentservice.updateWithdraw(t);
	}

	// 拒絕支出
	@Override
	@Transactional
	public void turndownWithdrawamount(Agentaccountorder mao) {
		Agentaccount t = mapper.getByUserId(mao.getUserid());
		//
		Agentaccountapplyjourna aaaj = new Agentaccountapplyjourna();
		aaaj.setUserid(t.getUserid());
		aaaj.setAgentname(mao.getUsername());
		aaaj.setOrdernum(mao.getOrdernum());
		aaaj.setType(DictionaryResource.RECORDTYPE_35);

		// 变更前
		aaaj.setPretotalincome(t.getTotalincome());// 总收入
		aaaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
		aaaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
		aaaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmountreceived());// 待确认支出
		// 变更后
		aaaj.setPosttotalincome(t.getTotalincome());// 总收入
		aaaj.setPosttoincomeamount(0.00);// 确认收入
		aaaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
		aaaj.setPosttowithdrawamount(0.00);// 确认支出
		aaaj.setRemark("审核拒绝，提现失败，金额：" + String.format("%.2f", mao.getAmountreceived()));
		//
		agentaccountapplyjournamapper.post(aaaj);
		RLock lock = RedissonUtil.getLock(t.getId());
		try {
			lock.lock();
			t.setTowithdrawamount(aaaj.getPretowithdrawamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 取消支出
	@Override
	@Transactional
	public void cancleWithdrawamount(Agentaccountorder mao) {
		Agentaccount t = mapper.getByUserId(mao.getUserid());
		//
		//
		Agentaccountapplyjourna aaaj = new Agentaccountapplyjourna();
		aaaj.setUserid(t.getUserid());
		aaaj.setAgentname(mao.getUsername());
		aaaj.setOrdernum(mao.getOrdernum());
		aaaj.setType(DictionaryResource.RECORDTYPE_37);

		// 变更前
		aaaj.setPretotalincome(t.getTotalincome());// 总收入
		aaaj.setPretoincomeamount(t.getToincomeamount());// 待确认收入
		aaaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
		aaaj.setPretowithdrawamount(t.getTowithdrawamount() - mao.getAmountreceived());// 待确认支出
		// 变更后
		aaaj.setPosttotalincome(t.getTotalincome());// 总收入
		aaaj.setPosttoincomeamount(0.00);// 确认收入
		aaaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
		aaaj.setPosttowithdrawamount(0.00);// 确认支出
		aaaj.setRemark("客户取消提现，金额：" + String.format("%.2f", mao.getAmountreceived()));
		//
		agentaccountapplyjournamapper.post(aaaj);

		RLock lock = RedissonUtil.getLock(t.getId());
		try {
			lock.lock();
			t.setTowithdrawamount(aaaj.getPretowithdrawamount());
			mapper.put(t);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

}