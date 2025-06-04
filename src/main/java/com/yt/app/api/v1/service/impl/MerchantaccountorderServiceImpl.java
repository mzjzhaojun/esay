package com.yt.app.api.v1.service.impl;

import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.MerchantaccountorderMapper;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.MerchantaccountorderMapper;
import com.yt.app.api.v1.mapper.UserMapper;
import com.yt.app.api.v1.service.IncomemerchantaccountService;
import com.yt.app.api.v1.service.MerchantaccountorderService;
import com.yt.app.api.v1.service.MerchantaccountorderService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Merchantaccountorder;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.Merchantaccountorder;
import com.yt.app.api.v1.entity.User;
import com.yt.app.api.v1.vo.MerchantaccountorderVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.exption.YtException;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.DateTimeUtil;
import com.yt.app.common.util.GoogleAuthenticatorUtil;
import com.yt.app.common.util.RedisUtil;
import com.yt.app.common.util.RedissonUtil;
import com.yt.app.common.util.StringUtil;

import cn.hutool.core.lang.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 23:31:35
 */

@Service
public class MerchantaccountorderServiceImpl extends YtBaseServiceImpl<Merchantaccountorder, Long> implements MerchantaccountorderService {
	@Autowired
	private MerchantaccountorderMapper mapper;
	@Autowired
	private IncomemerchantaccountService incomemerchantaccountservice;
	@Autowired
	private UserMapper usermapper;
	@Autowired
	private MerchantMapper merchantmapper;

	@Override
	@Transactional
	public Integer post(Merchantaccountorder t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Merchantaccountorder get(Long id) {
		Merchantaccountorder t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<MerchantaccountorderVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<MerchantaccountorderVO>(Collections.emptyList());
		}
		List<MerchantaccountorderVO> list = mapper.page(param);
		list.forEach(mco -> {
			mco.setStatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getStatus()));
			mco.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getType()));
		});
		return new YtPageBean<MerchantaccountorderVO>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public ByteArrayOutputStream download(Map<String, Object> param) throws IOException {
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		SXSSFSheet sheet = workbook.createSheet("Sheet");
		List<MerchantaccountorderVO> list = mapper.page(param);
		list.forEach(mco -> {
			mco.setStatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getStatus()));
			mco.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getType()));
		});
		SXSSFRow titleRow = sheet.createRow(0);
		SXSSFCell titleCell0 = titleRow.createCell(0);
		titleCell0.setCellValue("编号");
		SXSSFCell titleCell1 = titleRow.createCell(1);
		titleCell1.setCellValue("系统单号");
		SXSSFCell titleCell2 = titleRow.createCell(2);
		titleCell2.setCellValue("通道名称");
		SXSSFCell titleCell3 = titleRow.createCell(3);
		titleCell3.setCellValue("通道编码");
		SXSSFCell titleCell4 = titleRow.createCell(4);
		titleCell4.setCellValue("收款类型");
		SXSSFCell titleCell5 = titleRow.createCell(5);
		titleCell5.setCellValue("用户支付");
		SXSSFCell titleCell6 = titleRow.createCell(6);
		titleCell6.setCellValue("缺口金额");
		SXSSFCell titleCell7 = titleRow.createCell(7);
		titleCell7.setCellValue("收入金额");
		SXSSFCell titleCell8 = titleRow.createCell(8);
		titleCell8.setCellValue("订单状态");
		SXSSFCell titleCell9 = titleRow.createCell(9);
		titleCell9.setCellValue("创建时间");
		// 填充数据
		for (int i = 0; i < list.size(); i++) {
			MerchantaccountorderVO imao = list.get(i);
			SXSSFRow row = sheet.createRow(i + 1);
			SXSSFCell cell0 = row.createCell(0);
			cell0.setCellValue(i + 1);
			SXSSFCell cell1 = row.createCell(1);
			cell1.setCellValue(imao.getOrdernum());
			SXSSFCell cell2 = row.createCell(2);
			cell2.setCellValue(imao.getQrcodeaislename());
			SXSSFCell cell3 = row.createCell(3);
			cell3.setCellValue(imao.getQrcodeaislecode());
			SXSSFCell cell4 = row.createCell(4);
			cell4.setCellValue(imao.getTypename());
			SXSSFCell cell5 = row.createCell(5);
			cell5.setCellValue(imao.getRealamount());
			SXSSFCell cell6 = row.createCell(6);
			cell6.setCellValue(imao.getFewamount());
			SXSSFCell cell7 = row.createCell(7);
			cell7.setCellValue(imao.getIncomeamount());
			SXSSFCell cell8 = row.createCell(8);
			cell8.setCellValue(imao.getStatusname());
			SXSSFCell cell9 = row.createCell(9);
			cell9.setCellValue(DateTimeUtil.getDateTime(imao.getCreate_time(), DateTimeUtil.DEFAULT_DATETIME_FORMAT));
		}
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();
		return outputStream;
	}

//////////////////////////////////////////////////////////////提现处理
	@Override
	public void incomemanual(Merchantaccountorder mco) {
		RLock lock = RedissonUtil.getLock(mco.getId());
		User u = usermapper.get(SysUserContext.getUserId());
		boolean isValid = GoogleAuthenticatorUtil.checkCode(u.getTwofactorcode(), Long.parseLong(mco.getRemark()), System.currentTimeMillis());
		Assert.isTrue(isValid, "验证码错误！");
		try {
			lock.lock();
			Merchantaccountorder mao = mapper.get(mco.getId());
			if (mao.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
				mao.setStatus(mco.getStatus());
				Integer i = mapper.put(mao);
				if (i > 0) {
					if (mco.getStatus().equals(DictionaryResource.PAYOUTSTATUS_52)) {
//						incomemerchantaccountservice.updateTotalincome(mao);
					} else {
//						incomemerchantaccountservice.turndownWithdrawamount(mao);
					}
				}
			}
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}

//代收提现
	@Override
	public Integer incomewithdraw(Merchantaccountorder t) {
		if (t.getAmount() <= 0) {
			throw new YtException("金额不能小于1");
		}
		Merchant m = null;
		if (t.getMerchantid() == null) {
			m = merchantmapper.getByUserId(SysUserContext.getUserId());
			t.setUserid(SysUserContext.getUserId());
		} else {
			m = merchantmapper.get(t.getMerchantid());
			t.setUserid(m.getUserid());
		}
		// 支出订单
		t.setMerchantid(m.getId());
		t.setMerchantname(m.getName());
		t.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		t.setCollection(t.getCollection());
		t.setMerchantexchange(t.getCollection());
		t.setAmountreceived((t.getAmount()));
		t.setUsdtval(t.getAmount() / t.getCollection());
		t.setType("" + DictionaryResource.ORDERTYPE_28);
		t.setOrdernum("SHTX" + StringUtil.getOrderNum());
		t.setRemark("商户代收提现￥：" + String.format("%.2f", t.getAmount()));
		Integer i = mapper.post(t);

		// 支出账户和记录
//		incomemerchantaccountservice.withdrawamount(t);
		//
		return i;
	}

	// 代收提现app
	@Override
	public Long incomewithdrawapp(Merchantaccountorder t) {
		if (t.getAmount() <= 0) {
			throw new YtException("金额不能小于1");
		}
		Merchant m = null;
		if (t.getMerchantid() == null) {
			m = merchantmapper.getByUserId(SysUserContext.getUserId());
			t.setUserid(SysUserContext.getUserId());
		} else {
			m = merchantmapper.get(t.getMerchantid());
			t.setUserid(m.getUserid());
		}
		// 支出订单
		t.setMerchantid(m.getId());
		t.setMerchantname(m.getName());
		t.setStatus(DictionaryResource.PAYOUTSTATUS_50);
		t.setCollection(t.getCollection());
		t.setMerchantexchange(t.getCollection());
		t.setAmountreceived((t.getAmount()));
		t.setUsdtval(t.getAmount() / t.getCollection());
		t.setType("" + DictionaryResource.ORDERTYPE_28);
		t.setOrdernum("SHTX" + StringUtil.getOrderNum());
		t.setRemark("商户代收提现￥：" + String.format("%.2f", t.getAmount()));
		mapper.post(t);

		// 支出账户和记录
//		incomemerchantaccountservice.withdrawamount(t);
		return t.getId();
	}

	/**
	 * 飞机下发
	 */
	@Override
	public void incomewithdrawTelegram(Merchant m, double amount) {
		Merchantaccountorder t = new Merchantaccountorder();
		t.setUserid(m.getUserid());
		// 支出订单
		t.setMerchantid(m.getId());
		t.setTenant_id(m.getTenant_id());
		t.setMerchantname(m.getName());
		t.setStatus(DictionaryResource.PAYOUTSTATUS_52);
		t.setCollection(0.00);
		t.setAmount(amount);
		t.setMerchantexchange(0.00);
		t.setAmountreceived(amount);
		t.setUsdtval(amount);
		t.setType("" + DictionaryResource.ORDERTYPE_28);
		t.setOrdernum("SHTX" + StringUtil.getOrderNum());
		t.setRemark("商户代收飞机提现￥：" + String.format("%.2f", amount));
		mapper.add(t);

		// 支出账户和记录
//		incomemerchantaccountservice.withdrawamount(t);

//		incomemerchantaccountservice.updateWithdrawamount(t);
	}

	// 提现成功
	@Override
	public Integer success(Long id) {
		Merchantaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.PAYOUTSTATUS_52);
		Integer j = mapper.put(mao);
		if (j > 0) {
//			incomemerchantaccountservice.updateWithdrawamount(mao);
		}
		return j;
	}

	/**
	 * 提现取消
	 */
	@Override
	public Integer incomecancleWithdraw(Long id) {
		RLock lock = RedissonUtil.getLock(id);
		try {
			lock.lock();
			Merchantaccountorder mao = mapper.get(id);
//			mao.setStatus(DictionaryResource.PAYOUTSTATUS_51);
			Integer i = mapper.put(mao);
			//
//			incomemerchantaccountservice.cancelWithdrawamount(mao);
			return i;
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
		return 0;
	}

	// 通过提现
	@Override
	public void incomewithdrawmanual(Merchantaccountorder mco) {
		RLock lock = RedissonUtil.getLock(mco.getId());
		User u = usermapper.get(SysUserContext.getUserId());
		boolean isValid = GoogleAuthenticatorUtil.checkCode(u.getTwofactorcode(), Long.parseLong(mco.getRemark()), System.currentTimeMillis());
		Assert.isTrue(isValid, "验证码错误！");
		try {
			lock.lock();
			Merchantaccountorder mao = mapper.get(mco.getId());
			if (mao.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
				mao.setStatus(mco.getStatus());
				mao.setImgurl(mco.getImgurl());
				Integer i = mapper.put(mao);
				if (i > 0) {
					if (mco.getStatus().equals(DictionaryResource.PAYOUTSTATUS_52)) {
//						incomemerchantaccountservice.updateWithdrawamount(mao);
					} else {
//						incomemerchantaccountservice.turndownWithdrawamount(mao);
					}
				}
			}
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}

	}

	/**
	 * 充值
	 */
//	@Override
//	public Integer post(Merchantaccountorder t) {
//		if (t.getAmount() <= 0) {
//			throw new YtException("金额不能小于1");
//		}
//		Merchant m = null;
//		if (t.getMerchantid() == null) {
//			m = merchantmapper.getByUserId(SysUserContext.getUserId());
//			t.setUserid(SysUserContext.getUserId());
//		} else {
//			m = merchantmapper.get(t.getMerchantid());
//			t.setUserid(m.getUserid());
//		}
//		// 收入订单
//		t.setUsdtval(t.getAmount());
//		t.setMerchantid(m.getId());
//		t.setUsername(m.getName());
//		t.setNkname(m.getNikname());
//		t.setMerchantcode(m.getCode());
//		t.setStatus(DictionaryResource.PAYOUTSTATUS_50);
//		t.setAmountreceived((t.getAmount() * (t.getExchange() + t.getMerchantexchange())));
//		t.setType(DictionaryResource.ORDERTYPE_11);
//		t.setOrdernum("MT" + StringUtil.getOrderNum());
//		t.setRemark("商户充值￥：" + String.format("%.2f", t.getAmountreceived()));
//		Integer i = mapper.post(t);
//
//		// 收入账户和记录
//		merchantaccountservice.totalincome(t);
//		//
//		return i;
//	}

//	// 代付充值成功
//	@Override
//	public void payoutmanual(Merchantaccountorder mco) {
//		RLock lock = RedissonUtil.getLock(mco.getId());
//		User u = usermapper.get(SysUserContext.getUserId());
//		boolean isValid = GoogleAuthenticatorUtil.checkCode(u.getTwofactorcode(), Long.parseLong(mco.getRemark()), System.currentTimeMillis());
//		Assert.isTrue(isValid, "验证码错误！");
//		try {
//			lock.lock();
//			Merchantaccountorder mao = mapper.get(mco.getId());
//			if (mao.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
//				mao.setStatus(mco.getStatus());
//				Integer i = mapper.put(mao);
//				if (i > 0) {
//					if (mco.getStatus().equals(DictionaryResource.PAYOUTSTATUS_52)) {
//						//
////						merchantaccountservice.updateTotalincome(mao);
//						//
//					} else {
////						merchantaccountservice.turndownTotalincome(mao);
//					}
//				}
//			}
//		} catch (Exception e) {
//		} finally {
//			lock.unlock();
//		}
//	}
//
//	// 提现
//	@Override
//	public Integer save(Merchantaccountorder t) {
//		if (t.getAmount() <= 0) {
//			throw new YtException("金额不能小于1");
//		}
//		Merchant m = null;
//		if (t.getMerchantid() == null) {
//			m = merchantmapper.getByUserId(SysUserContext.getUserId());
//			t.setUserid(SysUserContext.getUserId());
//		} else {
//			m = merchantmapper.get(t.getMerchantid());
//			t.setUserid(m.getUserid());
//		}
//		Merchantaccountbank mab = merchantaccountbankmapper.get(Long.valueOf(t.getAccnumber()));
//		t.setAccname(mab.getAccname());
//		t.setAccnumber(mab.getAccnumber());
//		// 支出订单
//		t.setMerchantid(m.getId());
//		t.setUsername(m.getName());
//		t.setNkname(m.getNikname());
//		t.setMerchantcode(m.getCode());
//		t.setStatus(DictionaryResource.PAYOUTSTATUS_50);
//		t.setExchange(t.getMerchantexchange());
//		t.setAmountreceived((t.getAmount()));
//		t.setUsdtval(t.getAmount() / t.getMerchantexchange());
//		t.setType(DictionaryResource.ORDERTYPE_15);
//		t.setOrdernum("MW" + StringUtil.getOrderNum());
//		t.setRemark("商户代付提现￥：" + String.format("%.2f", t.getAmountreceived()));
//		Integer i = mapper.post(t);
//
//		// 支出账户和记录
//		merchantaccountservice.withdrawamount(t);
//		//
//		return i;
//	}
//
//	/**
//	 * app提现
//	 */
//	@Override
//	public Integer appsave(Merchantaccountorder t) {
//		if (t.getAmount() <= 0) {
//			throw new YtException("金额不能小于1");
//		}
//		Merchant m = null;
//		if (t.getMerchantid() == null) {
//			m = merchantmapper.getByUserId(SysUserContext.getUserId());
//			t.setUserid(SysUserContext.getUserId());
//		} else {
//			m = merchantmapper.get(t.getMerchantid());
//			t.setUserid(m.getUserid());
//		}
//
//		t.setAccname("App提现");
//		// 直接提到usdt地址
//		// 支出订单
//		t.setMerchantid(m.getId());
//		t.setUsername(m.getName());
//		t.setNkname(m.getNikname());
//		t.setMerchantcode(m.getCode());
//		t.setStatus(DictionaryResource.PAYOUTSTATUS_50);
//		t.setExchange(t.getMerchantexchange());
//		t.setAmountreceived((t.getAmount()));
//		t.setUsdtval(t.getAmount() / t.getMerchantexchange());
//		t.setType(DictionaryResource.ORDERTYPE_15);
//		t.setOrdernum("MW" + StringUtil.getOrderNum());
//		t.setRemark("商户代付提现￥：" + String.format("%.2f", t.getAmountreceived()));
//		Integer i = mapper.post(t);
//
//		// 支出账户和记录
//		merchantaccountservice.withdrawamount(t);
//		//
//		return i;
//	}
//
//	@Override
//	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
//	public YtIPage<PayoutMerchantaccountorder> page(Map<String, Object> param) {
//		int count = 0;
//		if (YtPageBean.isPaging(param)) {
//			count = mapper.countlist(param);
//			if (count == 0) {
//				return new YtPageBean<PayoutMerchantaccountorder>(Collections.emptyList());
//			}
//		}
//		List<PayoutMerchantaccountorder> list = mapper.list(param);
//		list.forEach(mco -> {
//			mco.setStatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getStatus()));
//		});
//		return new YtPageBean<PayoutMerchantaccountorder>(param, list, count);
//	}
//
//	@Override
//	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
//	public PayoutMerchantaccountorder get(Long id) {
//		PayoutMerchantaccountorder t = mapper.get(id);
//		return t;
//	}
//
//	/**
//	 * 充值取消
//	 */
//	@Override
//	public Integer cancle(Long id) {
//		RLock lock = RedissonUtil.getLock(id);
//		try {
//			lock.lock();
//			PayoutMerchantaccountorder mao = mapper.get(id);
//			mao.setStatus(DictionaryResource.PAYOUTSTATUS_51);
//			Integer i = mapper.put(mao);
//			//
//			merchantaccountservice.cancleTotalincome(mao);
//			return i;
//		} catch (Exception e) {
//		} finally {
//			lock.unlock();
//		}
//		return 0;
//	}
//
////////////////////////////////////////////////////////////////提现处理
//
//	@Override
//	public void withdrawmanual(PayoutMerchantaccountorder mco) {
//		RLock lock = RedissonUtil.getLock(mco.getId());
//		User u = usermapper.get(SysUserContext.getUserId());
//		boolean isValid = GoogleAuthenticatorUtil.checkCode(u.getTwofactorcode(), Long.parseLong(mco.getRemark()), System.currentTimeMillis());
//		Assert.isTrue(isValid, "验证码错误！");
//		try {
//			lock.lock();
//			PayoutMerchantaccountorder mao = mapper.get(mco.getId());
//			if (mao.getStatus().equals(DictionaryResource.PAYOUTSTATUS_50)) {
//				mao.setStatus(mco.getStatus());
//				mao.setImgurl(mco.getImgurl());
//				Integer i = mapper.put(mao);
//				if (i > 0) {
//					if (mco.getStatus().equals(DictionaryResource.PAYOUTSTATUS_52)) {
//						//
//						merchantaccountservice.updateWithdrawamount(mao);
//						//
//					} else {
//						merchantaccountservice.turndownWithdrawamount(mao);
//					}
//				}
//			}
//		} catch (Exception e) {
//		} finally {
//			lock.unlock();
//		}
//	}
//
//	/**
//	 * 提现取消
//	 */
//	@Override
//	public Integer cancleWithdraw(Long id) {
//		RLock lock = RedissonUtil.getLock(id);
//		try {
//			lock.lock();
//			PayoutMerchantaccountorder mao = mapper.get(id);
//			mao.setStatus(DictionaryResource.PAYOUTSTATUS_51);
//			Integer i = mapper.put(mao);
//			//
//			merchantaccountservice.cancleWithdrawamount(mao);
//			return i;
//		} catch (Exception e) {
//		} finally {
//			lock.unlock();
//		}
//		return 0;
//	}
}