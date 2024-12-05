package com.yt.app.api.v1.service.impl;

import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.AgentMapper;
import com.yt.app.api.v1.mapper.AgentaccountMapper;
import com.yt.app.api.v1.mapper.AgentaccountrecordMapper;
import com.yt.app.api.v1.mapper.AgentaccountbankMapper;
import com.yt.app.api.v1.service.AgentaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Agent;
import com.yt.app.api.v1.entity.Agentaccount;
import com.yt.app.api.v1.entity.Agentaccountrecord;
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
	private AgentaccountrecordMapper agentaccountapplyjournamapper;


	@Override
	@Transactional
	public Integer post(Agentaccount t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Agentaccount> page(Map<String, Object> param) {
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
		Agentaccount ac = mapper.getByUserId(SysUserContext.getUserId());
		return ac;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Agentaccount getDataBank() {
		Agentaccount t = mapper.getByUserId(SysUserContext.getUserId());
		List<Agentaccountbank> listbanks = agentaccountbankmapper.listByUserid(SysUserContext.getUserId());
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
	 * =============================================================收入
	 * 
	 */
	@Transactional
	private void setToincomeamount(Agentaccount ma, Agentaccountrecord aaaj) {
		ma.setToincomeamount(aaaj.getPretoincomeamount());
		mapper.put(ma);
		
		agentaccountapplyjournamapper.post(aaaj);
	}

	@Transactional
	private void setTotalincome(Agentaccount t, Agentaccountrecord aaaj) {
		t.setTotalincome(aaaj.getPosttotalincome());// 收入增加金额
		t.setToincomeamount(aaaj.getPretoincomeamount());// 待收入减去金额.
		t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
		mapper.put(t);

		Agent a = agentmapper.get(t.getAgentid());
		a.setBalance(t.getBalance());
		agentmapper.put(a);
		
		agentaccountapplyjournamapper.post(aaaj);
	}

	// 待确认收入
	@Override
	public  void totalincome(Agentaccountorder t) {
		RLock lock = RedissonUtil.getLock(t.getAgentid());
		try {
			lock.lock();
			Agentaccount ma = mapper.getByUserId(t.getUserid());
			// 资金记录
			Agentaccountrecord aaaj = new Agentaccountrecord();

			aaaj.setUserid(t.getUserid());
			aaaj.setAgentname(t.getUsername());
			aaaj.setOrdernum(t.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_30);
			// 变更前
			aaaj.setPretotalincome(ma.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(ma.getToincomeamount() + t.getAmountreceived());// 待确认收入
			aaaj.setPrewithdrawamount(ma.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(ma.getTowithdrawamount());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(ma.getTotalincome());// 总收入
			aaaj.setPosttoincomeamount(0.00);// 确认收入
			aaaj.setPostwithdrawamount(ma.getWithdrawamount());// 总支出
			aaaj.setPosttowithdrawamount(0.00);// 确认支出
			aaaj.setRemark("收入待确认￥:" + String.format("%.2f", t.getAmountreceived()));
			//
			setToincomeamount(ma, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 确认收入
	@Override
	public  void updateTotalincome(Agentaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getAgentid());
		try {
			lock.lock();
			Agentaccount t = mapper.getByUserId(mao.getUserid());

			//
			Agentaccountrecord aaaj = new Agentaccountrecord();
			aaaj.setUserid(t.getUserid());
			aaaj.setAgentname(mao.getUsername());
			aaaj.setOrdernum(mao.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_31);

			// 变更前
			aaaj.setPretotalincome(t.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(t.getToincomeamount() - mao.getAmountreceived());// 待确认收入
			aaaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(t.getTowithdrawamount());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(t.getTotalincome() + mao.getAmountreceived());// 总收入
			aaaj.setPosttoincomeamount(mao.getAmountreceived());// 确认收入
			aaaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPosttowithdrawamount(0.00);// 确认支出
			aaaj.setRemark("收入￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			setTotalincome(t, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 拒绝收入
	@Override
	public  void turndownTotalincome(Agentaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getAgentid());
		try {
			lock.lock();
			Agentaccount t = mapper.getByUserId(mao.getUserid());
			//
			Agentaccountrecord aaaj = new Agentaccountrecord();
			aaaj.setUserid(t.getUserid());
			aaaj.setAgentname(mao.getUsername());
			aaaj.setOrdernum(mao.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_32);

			// 变更前
			aaaj.setPretotalincome(t.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(t.getToincomeamount() - mao.getAmountreceived());// 待确认收入
			aaaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(t.getTowithdrawamount());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(t.getTotalincome());// 总收入
			aaaj.setPosttoincomeamount(0.00);// 确认收入
			aaaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPosttowithdrawamount(0.00);// 确认支出
			aaaj.setRemark("收入失败￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			setToincomeamount(t, aaaj);

		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 客户取消
	@Override
	public  void cancleTotalincome(Agentaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getAgentid());
		try {
			lock.lock();
			Agentaccount t = mapper.getByUserId(mao.getUserid());
			//
			Agentaccountrecord aaaj = new Agentaccountrecord();
			aaaj.setUserid(t.getUserid());
			aaaj.setAgentname(mao.getUsername());
			aaaj.setOrdernum(mao.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_33);

			// 变更前
			aaaj.setPretotalincome(t.getTotalincome());// 总收入
			aaaj.setPretoincomeamount(t.getToincomeamount() - mao.getAmountreceived());// 待确认收入
			aaaj.setPrewithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPretowithdrawamount(t.getTowithdrawamount());// 待确认支出
			// 变更后
			aaaj.setPosttotalincome(t.getTotalincome());// 总收入
			aaaj.setPosttoincomeamount(0.00);// 确认收入
			aaaj.setPostwithdrawamount(t.getWithdrawamount());// 总支出
			aaaj.setPosttowithdrawamount(0.00);// 确认支出
			aaaj.setRemark("收入取消￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			setToincomeamount(t, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	/**
	 * =============================================================提现
	 * 
	 */

	@Transactional
	private void setWithdrawamount(Agentaccount t, Agentaccountrecord aaaj) {
		// 支出总金额
		t.setWithdrawamount(aaaj.getPostwithdrawamount());
		// 待支出金额
		t.setTowithdrawamount(aaaj.getPretowithdrawamount());
		t.setBalance(t.getTotalincome() - t.getWithdrawamount() - t.getTowithdrawamount());
		mapper.put(t);

		Agent a = agentmapper.get(t.getAgentid());
		a.setBalance(t.getBalance());
		agentmapper.put(a);
		
		agentaccountapplyjournamapper.post(aaaj);
	}

	// 待确认支出
	@Override
	public  void withdrawamount(Agentaccountorder t) {
		RLock lock = RedissonUtil.getLock(t.getAgentid());
		try {
			lock.lock();
			Agentaccount ma = mapper.getByUserId(t.getUserid());
			Agentaccountrecord aaaj = new Agentaccountrecord();

			aaaj.setUserid(t.getUserid());
			aaaj.setAgentname(t.getUsername());
			aaaj.setOrdernum(t.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_34);
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
			aaaj.setRemark("支出￥：" + String.format("%.2f", t.getAmountreceived()));
			//
			setWithdrawamount(ma, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 确认支出
	@Override
	public  void updateWithdrawamount(Agentaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getAgentid());
		try {
			lock.lock();
			Agentaccount t = mapper.getByUserId(mao.getUserid());
			//
			Agentaccountrecord aaaj = new Agentaccountrecord();
			//
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
			aaaj.setPostwithdrawamount(t.getWithdrawamount() + mao.getAmountreceived());// 总支出
			aaaj.setPosttowithdrawamount(mao.getAmountreceived());// 确认支出
			aaaj.setRemark("支出成功￥：" + String.format("%.2f", mao.getAmountreceived()));
			//
			setWithdrawamount(t, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 拒絕支出
	@Override
	public  void turndownWithdrawamount(Agentaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getAgentid());
		try {
			lock.lock();
			Agentaccount t = mapper.getByUserId(mao.getUserid());
			//
			Agentaccountrecord aaaj = new Agentaccountrecord();
			aaaj.setUserid(t.getUserid());
			aaaj.setAgentname(mao.getUsername());
			aaaj.setOrdernum(mao.getOrdernum());
			aaaj.setType(DictionaryResource.RECORDTYPE_36);

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
			aaaj.setRemark("拒绝支出￥：" + String.format("%.2f", mao.getAmountreceived()));
			setWithdrawamount(t, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

	// 取消支出
	@Override
	public  void cancleWithdrawamount(Agentaccountorder mao) {
		RLock lock = RedissonUtil.getLock(mao.getAgentid());
		try {
			lock.lock();
			Agentaccount t = mapper.getByUserId(mao.getUserid());
			//
			Agentaccountrecord aaaj = new Agentaccountrecord();
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
			aaaj.setRemark("取消支出￥：" + String.format("%.2f", mao.getAmountreceived()));
			setWithdrawamount(t, aaaj);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

}