package com.yt.app.api.v1.service.impl;

import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.MerchantaccountorderMapper;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.api.v1.mapper.IncomeMapper;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.UserMapper;
import com.yt.app.api.v1.service.IncomemerchantaccountService;
import com.yt.app.api.v1.service.MerchantaccountorderService;
import com.yt.app.api.v1.service.PayoutMerchantaccountService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Merchantaccountorder;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.User;
import com.yt.app.api.v1.vo.IncomeVO;
import com.yt.app.api.v1.vo.MerchantaccountorderVO;
import com.yt.app.api.v1.vo.PayoutVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.exption.YtException;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.DateTimeUtil;
import com.yt.app.common.util.GoogleAuthenticatorUtil;
import com.yt.app.common.util.RedisUtil;
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
	private IncomeMapper incomemapper;
	@Autowired
	private PayoutMapper payoutmapper;
	@Autowired
	private UserMapper usermapper;
	@Autowired
	private MerchantMapper merchantmapper;
	@Autowired
	private PayoutMerchantaccountService payoutmerchantaccountservice;
	@Autowired
	private IncomemerchantaccountService incomemerchantaccountservice;

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
	public ByteArrayOutputStream downloadIncome(Map<String, Object> param) throws IOException {
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		SXSSFSheet sheet = workbook.createSheet("Sheet");
		List<IncomeVO> list = incomemapper.page(param);
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
			IncomeVO imao = list.get(i);
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

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public ByteArrayOutputStream downloadPayout(Map<String, Object> param) throws IOException {
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		SXSSFSheet sheet = workbook.createSheet("Sheet");
		List<PayoutVO> list = payoutmapper.page(param);
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
		titleCell5.setCellValue("支付");
		SXSSFCell titleCell6 = titleRow.createCell(6);
		titleCell6.setCellValue("手续费");
		SXSSFCell titleCell7 = titleRow.createCell(7);
		titleCell7.setCellValue("金额");
		SXSSFCell titleCell8 = titleRow.createCell(8);
		titleCell8.setCellValue("订单状态");
		SXSSFCell titleCell9 = titleRow.createCell(9);
		titleCell9.setCellValue("创建时间");
		// 填充数据
		for (int i = 0; i < list.size(); i++) {
			PayoutVO imao = list.get(i);
			SXSSFRow row = sheet.createRow(i + 1);
			SXSSFCell cell0 = row.createCell(0);
			cell0.setCellValue(i + 1);
			SXSSFCell cell1 = row.createCell(1);
			cell1.setCellValue(imao.getOrdernum());
			SXSSFCell cell2 = row.createCell(2);
			cell2.setCellValue(imao.getAislename());
			SXSSFCell cell3 = row.createCell(3);
			cell3.setCellValue(imao.getAislename());
			SXSSFCell cell4 = row.createCell(4);
			cell4.setCellValue(imao.getTypename());
			SXSSFCell cell5 = row.createCell(5);
			cell5.setCellValue(imao.getMerchantpay());
			SXSSFCell cell6 = row.createCell(6);
			cell6.setCellValue(imao.getMerchantcost());
			SXSSFCell cell7 = row.createCell(7);
			cell7.setCellValue(imao.getAmount());
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

	// 代收提现
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
		t.setUsername(m.getName());
		t.setNkname(m.getName());
		t.setMerchantcode(m.getCode());
		t.setStatus(DictionaryResource.ORDERSTATUS_50);
		t.setMerchantexchange(t.getExchange());
		t.setAmountreceived((t.getAmount()));
		t.setUsdtval(t.getAmount() / t.getExchange());
		t.setType(DictionaryResource.ORDERTYPE_22);
		t.setOrdernum("SHTX" + StringUtil.getOrderNum());
		t.setRemark("商户提现￥：" + String.format("%.2f", t.getAmount()));
		Integer i = mapper.post(t);

		// 支出账户和记录
		incomemerchantaccountservice.withdrawamount(t);
		//
		return i;
	}

	/// 代收提现处理
	@Override
	public Integer incomewithdrawmanual(Merchantaccountorder mco) {
		User u = usermapper.get(SysUserContext.getUserId());
		boolean isValid = GoogleAuthenticatorUtil.checkCode(u.getTwofactorcode(), Long.parseLong(mco.getRemark()), System.currentTimeMillis());
		Assert.isTrue(isValid, "验证码错误！");
		Integer i = 0;
		Merchantaccountorder mao = mapper.get(mco.getId());
		if (mao.getStatus().equals(DictionaryResource.ORDERSTATUS_50)) {
			mao.setStatus(mco.getStatus());
			if (mco.getImgurl() != null)
				mao.setImgurl(mco.getImgurl());
			i = mapper.put(mao);
			if (i > 0) {
				if (mco.getStatus().equals(DictionaryResource.ORDERSTATUS_52)) {
					incomemerchantaccountservice.updateWithdrawamount(mao);
				} else {
					incomemerchantaccountservice.cancelWithdrawamount(mao);
				}
			}
		}
		return i;
	}

	/**
	 * 代收提现取消
	 */
	@Override
	public Integer cancleincomewithdraw(Long id) {
		Merchantaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.ORDERSTATUS_53);
		Integer i = mapper.put(mao);
		//
		incomemerchantaccountservice.cancelWithdrawamount(mao);
		return i;
	}

	// 代付提现
	@Override
	public Integer payoutwithdraw(Merchantaccountorder t) {
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
		t.setUsername(m.getName());
		t.setNkname(m.getName());
		t.setMerchantcode(m.getCode());
		t.setStatus(DictionaryResource.ORDERSTATUS_50);
		t.setMerchantexchange(t.getExchange());
		t.setAmountreceived((t.getAmount()));
		t.setUsdtval(t.getAmount() / t.getExchange());
		t.setType(DictionaryResource.ORDERTYPE_21);
		t.setOrdernum("SHTX" + StringUtil.getOrderNum());
		t.setRemark("商户提现￥：" + String.format("%.2f", t.getAmount()));
		Integer i = mapper.post(t);

		// 支出账户和记录
		payoutmerchantaccountservice.withdrawamount(t);
		//
		return i;
	}

	/// 代付提现处理
	@Override
	public Integer payoutmanual(Merchantaccountorder mco) {
		User u = usermapper.get(SysUserContext.getUserId());
		boolean isValid = GoogleAuthenticatorUtil.checkCode(u.getTwofactorcode(), Long.parseLong(mco.getRemark()), System.currentTimeMillis());
		Assert.isTrue(isValid, "验证码错误！");
		Integer i = 0;
		Merchantaccountorder mao = mapper.get(mco.getId());
		if (mao.getStatus().equals(DictionaryResource.ORDERSTATUS_50)) {
			mao.setStatus(mco.getStatus());
			if (mco.getImgurl() != null)
				mao.setImgurl(mco.getImgurl());
			i = mapper.put(mao);
			if (i > 0) {
				if (mco.getStatus().equals(DictionaryResource.ORDERSTATUS_52)) {
					payoutmerchantaccountservice.updateWithdrawamount(mao);
				} else {
					payoutmerchantaccountservice.cancleWithdrawamount(mao);
				}
			}
		}
		return i;
	}

	/**
	 * 代付提现取消
	 */
	@Override
	public Integer payoutcancleWithdraw(Long id) {
		Merchantaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.ORDERSTATUS_53);
		Integer i = mapper.put(mao);
		//
		payoutmerchantaccountservice.cancleWithdrawamount(mao);
		return i;
	}

	/**
	 * 充值
	 */
	@Override
	public Integer post(Merchantaccountorder t) {
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
		// 收入订单
		t.setUsdtval(t.getAmount());
		t.setMerchantid(m.getId());
		t.setUsername(m.getName());
		t.setNkname(m.getNikname());
		t.setMerchantcode(m.getCode());
		t.setStatus(DictionaryResource.ORDERSTATUS_50);
		t.setAmountreceived((t.getAmount() * (t.getExchange() + t.getMerchantexchange())));
		t.setType(DictionaryResource.ORDERTYPE_20);
		t.setOrdernum("MT" + StringUtil.getOrderNum());
		t.setRemark("商户充值￥：" + String.format("%.2f", t.getAmountreceived()));
		Integer i = mapper.post(t);

		// 收入账户和记录
		payoutmerchantaccountservice.totalincome(t);
		//
		return i;
	}

	// 充值成功
	@Override
	public Integer incomemanual(Merchantaccountorder mco) {
		User u = usermapper.get(SysUserContext.getUserId());
		boolean isValid = GoogleAuthenticatorUtil.checkCode(u.getTwofactorcode(), Long.parseLong(mco.getRemark()), System.currentTimeMillis());
		Assert.isTrue(isValid, "验证码错误！");
		Integer i = 0;
		Merchantaccountorder mao = mapper.get(mco.getId());
		mao.setStatus(mco.getStatus());
		i = mapper.put(mao);
		if (i > 0) {
			if (mco.getStatus() == DictionaryResource.ORDERSTATUS_52) {
				payoutmerchantaccountservice.updateTotalincome(mao);
			} else {
				payoutmerchantaccountservice.cancleTotalincome(mao);
			}
		}
		return i;
	}

	/**
	 * 充值取消
	 */
	@Override
	public Integer incomecancle(Long id) {
		Merchantaccountorder mao = mapper.get(id);
		mao.setStatus(DictionaryResource.ORDERSTATUS_53);
		Integer i = mapper.put(mao);
		//
		payoutmerchantaccountservice.cancleTotalincome(mao);
		return i;
	}

	// 代收提现app
	@Override
	public Integer incomewithdrawapp(Merchantaccountorder t) {
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
		t.setNkname(m.getName());
		t.setStatus(DictionaryResource.ORDERSTATUS_50);
		t.setMerchantexchange(t.getExchange());
		t.setAmountreceived((t.getAmount()));
		t.setUsdtval(t.getAmount() / t.getExchange());
		t.setType(DictionaryResource.ORDERTYPE_11);
		t.setOrdernum("SHTX" + StringUtil.getOrderNum());
		t.setRemark("商户代收提现￥：" + String.format("%.2f", t.getAmount()));
		Integer i = mapper.post(t);

		// 支出账户和记录
//			incomemerchantaccountservice.withdrawamount(t);
		return i;
	}

}