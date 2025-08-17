package com.yt.app.api.v1.service.impl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.AgentMapper;
import com.yt.app.api.v1.mapper.AisleMapper;
import com.yt.app.api.v1.mapper.AislechannelMapper;
import com.yt.app.api.v1.mapper.ChannelMapper;
import com.yt.app.api.v1.mapper.MerchantMapper;
import com.yt.app.api.v1.mapper.PayoutMerchantaccountMapper;
import com.yt.app.api.v1.mapper.QrcodeMapper;
import com.yt.app.api.v1.mapper.QrcodeaisleMapper;
import com.yt.app.api.v1.mapper.QrcodeaisleqrcodeMapper;
import com.yt.app.api.v1.mapper.PayoutMapper;
import com.yt.app.api.v1.service.AgentaccountService;
import com.yt.app.api.v1.service.ChannelaccountService;
import com.yt.app.api.v1.service.FileService;
import com.yt.app.api.v1.service.PayoutMerchantaccountService;
import com.yt.app.api.v1.service.MerchantcustomerbanksService;
import com.yt.app.api.v1.service.PayoutService;
import com.yt.app.api.v1.service.QrcodeaccountService;
import com.yt.app.api.v1.service.SystemaccountService;
import com.yt.app.api.v1.vo.PayoutVO;
import com.yt.app.api.v1.vo.SysSnOrder;
import com.yt.app.api.v1.vo.PayResultVO;
import com.yt.app.api.v1.vo.SysTyOrder;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.AuthContext;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.common.bot.ChannelBot;
import com.yt.app.common.bot.MerchantBot;
import com.yt.app.api.v1.dbo.PaySubmitDTO;
import com.yt.app.api.v1.dbo.SysQueryDTO;
import com.yt.app.api.v1.entity.Agent;
import com.yt.app.api.v1.entity.Aisle;
import com.yt.app.api.v1.entity.Aislechannel;
import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Merchant;
import com.yt.app.api.v1.entity.PayoutMerchantaccount;
import com.yt.app.api.v1.entity.Qrcode;
import com.yt.app.api.v1.entity.Qrcodeaisle;
import com.yt.app.api.v1.entity.Qrcodeaisleqrcode;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.exption.YtException;
import com.yt.app.common.resource.DictionaryResource;
import com.yt.app.common.util.DateTimeUtil;
import com.yt.app.common.util.FileUtil;
import com.yt.app.common.util.NumberUtil;
import com.yt.app.common.util.PayUtil;
import com.yt.app.common.util.PayoutProduct;
import com.yt.app.common.util.RedisUtil;
import com.yt.app.common.util.SelfPayUtil;
import com.yt.app.common.util.StringUtil;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zj default
 * 
 * @version v1 @createdate2023-11-15 09:51:11
 */

@Slf4j
@Service
public class PayoutServiceImpl extends YtBaseServiceImpl<Payout, Long> implements PayoutService {
	@Autowired
	private PayoutMapper mapper;
	@Autowired
	private MerchantMapper merchantmapper;
	@Autowired
	private AisleMapper aislemapper;
	@Autowired
	private ChannelMapper channelmapper;
	@Autowired
	private QrcodeMapper qrcodemapper;
	@Autowired
	private AislechannelMapper aislechannelmapper;
	@Autowired
	private QrcodeaisleqrcodeMapper qrcodeaisleqrcodemapper;
	@Autowired
	private AgentMapper agentmapper;
	@Autowired
	private QrcodeaisleMapper qrcodeaislemapper;
	@Autowired
	private PayoutMerchantaccountService payoutmerchantaccountservice;
	@Autowired
	private AgentaccountService agentaccountservice;
	@Autowired
	private ChannelaccountService channelaccountservice;
	@Autowired
	private QrcodeaccountService qrcodeaccountservice;
	@Autowired
	private SystemaccountService systemaccountservice;
	@Autowired
	private PayoutMerchantaccountMapper merchantaccountmapper;
	@Autowired
	private MerchantcustomerbanksService merchantcustomerbanksservice;
	@Autowired
	private ChannelBot channelbot;
	@Autowired
	private FileService fileservice;
	@Autowired
	private MerchantBot merchantbot;

	@Override
	public Integer success(Long id) {
		Payout pt = mapper.get(id);
		paySuccess(pt);
		return 1;
	}

	@Override
	public Integer fail(Long id) {
		Payout pt = mapper.get(id);
		payFail(pt, "失败");
		return 1;
	}

	@Override
	public Integer handleCertificate(Long id) {
		Payout pt = mapper.get(id);
		Channel channel = channelmapper.get(pt.getChannelid());
		// 申请回单
		String rstate = PayUtil.SendTYPayCertificate(pt.getOrdernum(), channel);
		if (rstate == null) {
			pt.setStatus(DictionaryResource.ORDERSTATUS_56);
		} else {
			pt.setStatus(DictionaryResource.ORDERSTATUS_57);
		}
		return mapper.put(pt);
	}

	@Override
	public Integer handleCertificateDownload(Long id) {
		Payout pt = mapper.get(id);
		Channel channel = channelmapper.get(pt.getChannelid());
		// 申请回单
		String imgurl = PayUtil.SendTYPayCertificateDownload(pt.getOrdernum(), channel);
		Assert.notNull(imgurl, "下载回单失败!");
		try {
			URL url = new URL(imgurl);
			URLConnection urlConnection = url.openConnection();
			try (PDDocument document = PDDocument.load(urlConnection.getInputStream())) {
				PDFRenderer renderer = new PDFRenderer(document);
				BufferedImage bufferedimage = renderer.renderImageWithDPI(0, 500); // DPI can be adjusted
				String returnspayimg = FileUtil.bufferedImageToBase64(bufferedimage);
				String resurl = fileservice.addBase64String(returnspayimg);
				pt.setImgurl(resurl);
				pt.setStatus(DictionaryResource.ORDERSTATUS_58);
				return mapper.put(pt);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<PayoutVO> page(Map<String, Object> param) {
		int count = 0;
		if (YtPageBean.isPaging(param)) {
			count = mapper.countlist(param);
			if (count == 0) {
				return new YtPageBean<PayoutVO>(Collections.emptyList());
			}
		}
		List<PayoutVO> list = mapper.page(param);
		list.forEach(mco -> {
			mco.setStatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getStatus()));
			mco.setNotifystatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getNotifystatus()));
		});
		PayoutVO invos = mapper.countSuccess(param);
		PayoutVO invoa = mapper.countAll(param);
		return new YtPageBean<PayoutVO>(param, list, count, invos.getOrdercount() == null ? 0 : invos.getOrdercount(), invos.getAmount() == null ? 0 : invos.getAmount(), invoa.getAmount() == null ? 0 : invoa.getAmount());
	}

	/**
	 * 本地提交
	 */
	@Override
	public Integer submitOrder(Payout t) {
		PayoutMerchantaccount maccount = merchantaccountmapper.getByMerchantId(t.getMerchantid());

		if (t.getAmount() <= 0 || t.getAmount() > maccount.getBalance()) {
			throw new YtException("账户余额不足");
		}
		///////////////////////////////////////////////////// 录入代付订单/////////////////////////////////////////////////////
		Merchant m = merchantmapper.get(t.getMerchantid());

		if (!m.getStatus()) {
			throw new YtException("商户被冻结!");
		}
		t.setAccname(t.getAccname().replaceAll(" ", ""));
		t.setAccnumer(t.getAccnumer().replaceAll(" ", ""));
		t.setUserid(m.getUserid());
		t.setMerchantid(m.getId());
		t.setNotifyurl(m.getApireusultip());
		t.setMerchantcode(m.getCode());
		t.setMerchantname(m.getName());
		if (t.getBankname().equals("支付宝"))
			t.setType(DictionaryResource.ORDERTYPE_19);
		else
			t.setType(DictionaryResource.ORDERTYPE_18);
		t.setOrdernum("OUT" + StringUtil.getOrderNum());// 系统单号
		t.setMerchantorderid("OUTM" + StringUtil.getOrderNum());// 商户单号
		t.setMerchantordernum(NumberUtil.getOrderNo());
		t.setMerchantcost(m.getOnecost());// 手续费
		t.setMerchantdeal(t.getAmount() * (m.getExchange() / 1000));// 交易费
		t.setMerchantpay(t.getAmount() + t.getMerchantcost() + t.getMerchantdeal());// 商户支付总额
		t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_60);// 商戶發起
		t.setRemark("新增代付￥:" + String.format("%.2f", t.getAmount()));
		Aisle a = aislemapper.get(t.getAisleid());
		t.setAislename(a.getName());

		////////////////////////////////////////////////////// 计算渠道渠道/////////////////////////////////////
		List<Aislechannel> listac = aislechannelmapper.getByAisleId(t.getAisleid());
		Assert.notEmpty(listac, "没有可用通道!");
		long[] cids = listac.stream().mapToLong(ac -> ac.getChannelid()).distinct().toArray();
		List<Channel> listc = channelmapper.listByArrayId(cids);
		Assert.notEmpty(listc, "没有可用渠道!");
		List<Channel> listcmm = listc.stream().filter(c -> c.getMax() >= t.getAmount() && c.getMin() <= t.getAmount() && c.getStatus()).collect(Collectors.toList());
		Assert.notEmpty(listcmm, "代付金额超出限额");
		List<Channel> listcf = listc.stream().filter(c -> c.getFirstmatch() == true).collect(Collectors.toList());
		Channel cl = null;
		if (listcf.size() > 0) {
			for (int j = 0; j < listcf.size(); j++) {
				Channel cc = listcf.get(j);
				String[] match = cc.getFirstmatchmoney().split(",");
				for (int i = 0; i < match.length; i++) {
					String number = match[i];
					if (number.indexOf("-") == -1 && t.getAmount().intValue() == Integer.parseInt(number)) {
						cl = cc;
					} else {
						String[] matchs = number.split("-");
						Integer min = Integer.parseInt(matchs[0]);
						Integer max = Integer.parseInt(matchs[1]);
						if (t.getAmount().intValue() >= min && t.getAmount().intValue() <= max) {
							cl = cc;
						}
					}
				}
			}
		} else {
			List<WeightRandom.WeightObj<String>> weightList = new ArrayList<>(); //
			double count = 0;
			for (Channel cml : listcmm) {
				count = count + cml.getWeight();
			}
			for (Channel cmm : listcmm) {
				weightList.add(new WeightRandom.WeightObj<String>(cmm.getCode(), (cmm.getWeight() / count) * 100));
			}
			WeightRandom<String> wr = RandomUtil.weightRandom(weightList);
			String code = wr.next();
			cl = listc.stream().filter(c -> c.getCode() == code).collect(Collectors.toList()).get(0);
			Assert.notNull(cl, "没有可用的渠道!");
			t.setChannelid(cl.getId());
			t.setChannelname(cl.getName());
			t.setChannelcost(cl.getOnecost());// 渠道手续费
			if (cl.getExchange() >= 1) {
				t.setChanneldeal(t.getAmount() * (cl.getExchange() / 1000));
				t.setChannelpay(t.getAmount() + t.getChannelcost() + t.getChanneldeal());// 渠道总支付费用
			} else {
				t.setChanneldeal(t.getAmount() + t.getChannelcost());
				t.setChannelpay(t.getChanneldeal());// 渠道总支付费用
			}
			t.setStatus(DictionaryResource.ORDERSTATUS_50);
		}
		RedisUtil.setEx(SystemConstant.CACHE_SYS_PAYOUT_EXIST + t.getOrdernum(), t.getOrdernum(), 60, TimeUnit.SECONDS);
		// 获取渠道单号
		boolean flage = PayoutProduct.getPayoutProduct(t, cl);
		if (flage) {
			channelbot.notifyChannel(cl);
			throw new YtException("渠道错误!");
		}

		payoutmerchantaccountservice.withdrawamount(t);

		channelaccountservice.withdrawamount(t);

		///////////////////////////////////////////////////// 计算代理订单/////////////////////////////////////////////////////
		if (m.getAgentid() != null) {
			Agent ag = agentmapper.get(m.getAgentid());
			t.setAgentid(ag.getId());
			agentaccountservice.totalincome(t);
		} else {
			t.setAgentincome(0.00);
		}
		// 渠道余额
		t.setChannelbalance(cl.getBalance() - t.getChannelpay());
		// 小计
		t.setIncome(t.getMerchantpay() - t.getChannelpay() - t.getAgentincome()); // 此订单完成后预计总收入
		return mapper.post(t);
	}

	/**
	 * 本地自营渠道提交
	 */
	@Override
	public Integer submitOrderSelf(Payout t) {
		PayoutMerchantaccount maccount = merchantaccountmapper.getByMerchantId(t.getMerchantid());

		if (t.getAmount() <= 0 || t.getAmount() > maccount.getBalance()) {
			throw new YtException("账户余额不足");
		}
		///////////////////////////////////////////////////// 录入代付订单/////////////////////////////////////////////////////
		Merchant m = merchantmapper.get(t.getMerchantid());

		if (!m.getStatus()) {
			throw new YtException("商户被冻结!");
		}
		t.setAccname(t.getAccname().replaceAll(" ", ""));
		t.setAccnumer(t.getAccnumer().replaceAll(" ", ""));
		t.setUserid(m.getUserid());
		t.setMerchantid(m.getId());
		t.setNotifyurl(m.getApireusultip());
		t.setMerchantcode(m.getCode());
		t.setMerchantname(m.getName());
		t.setType(DictionaryResource.ORDERTYPE_18);
		t.setOrdernum("OUT" + StringUtil.getOrderNum());// 系统单号
		t.setMerchantorderid("OUTM" + StringUtil.getOrderNum());// 商户单号
		t.setMerchantordernum(NumberUtil.getOrderNo());
		t.setMerchantcost(m.getOnecost());// 手续费
		t.setMerchantdeal(t.getAmount() * (m.getExchange() / 1000));// 交易费
		t.setMerchantpay(t.getAmount() + t.getMerchantcost() + t.getMerchantdeal());// 商户支付总额
		t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_60);// 商戶發起
		t.setRemark("新增代付￥:" + String.format("%.2f", t.getAmount()));
		Qrcodeaisle a = qrcodeaislemapper.get(t.getAisleid());
		t.setAislename(a.getName());

		////////////////////////////////////////////////////// 计算渠道渠道/////////////////////////////////////

		List<Qrcodeaisleqrcode> listqaq = qrcodeaisleqrcodemapper.getByQrcodeAisleId(t.getAisleid());
		long[] qaqids = listqaq.stream().mapToLong(qaq -> qaq.getQrcodelid()).distinct().toArray();
		List<Qrcode> listqrcode = qrcodemapper.listByArrayId(qaqids);
		Double amount = Double.valueOf(t.getAmount());
		// 小于设置限额
		List<Qrcode> listcmm = listqrcode.stream().filter(c -> c.getMax() >= amount && c.getMin() <= amount && c.getStatus()).collect(Collectors.toList());
		Assert.notEmpty(listcmm, "代付金额超出限额");
		List<Qrcode> listcf = listcmm.stream().filter(c -> c.getFirstmatch() == true).collect(Collectors.toList());
		Qrcode qd = null;
		if (listcf.size() > 0) {
			for (int j = 0; j < listcf.size(); j++) {
				Qrcode cc = listcf.get(j);
				String[] match = cc.getFirstmatchmoney().split(",");
				for (int i = 0; i < match.length; i++) {
					String number = match[i];
					if (number.indexOf("-") == -1 && t.getAmount().intValue() == Integer.parseInt(number)) {
						qd = cc;
					} else {
						String[] matchs = number.split("-");
						Integer min = Integer.parseInt(matchs[0]);
						Integer max = Integer.parseInt(matchs[1]);
						if (t.getAmount().intValue() >= min && t.getAmount().intValue() <= max) {
							qd = cc;
						}
					}
				}
			}
		} else {
			List<WeightRandom.WeightObj<String>> weightList = new ArrayList<>(); //
			double count = 0;
			for (Qrcode cml : listcmm) {
				count = count + cml.getWeight();
			}
			for (Qrcode cmm : listcmm) {
				weightList.add(new WeightRandom.WeightObj<String>(cmm.getCode(), (cmm.getWeight() / count) * 100));
			}
			WeightRandom<String> wr = RandomUtil.weightRandom(weightList);
			String code = wr.next();
			qd = listcmm.stream().filter(c -> c.getCode() == code).collect(Collectors.toList()).get(0);
			Assert.notNull(qd, "没有可用的渠道!");
			t.setChannelid(qd.getId());
			t.setChannelname(qd.getName());
			t.setChannelcost(2.00);// 渠道手续费
			t.setChanneldeal(t.getAmount() + t.getChannelcost());
			t.setChannelpay(t.getChanneldeal());// 渠道总支付费用
			t.setStatus(DictionaryResource.ORDERSTATUS_50);
		}
		RedisUtil.setEx(SystemConstant.CACHE_SYS_PAYOUT_EXIST + t.getOrdernum(), t.getOrdernum(), 60, TimeUnit.SECONDS);
		// 获取渠道单号
		getQrcodelOrderNo(t, qd);

		payoutmerchantaccountservice.withdrawamount(t);

		qrcodeaccountservice.withdrawamount(t);

		///////////////////////////////////////////////////// 计算代理订单/////////////////////////////////////////////////////
		if (m.getAgentid() != null) {
			Agent ag = agentmapper.get(m.getAgentid());
			t.setAgentid(ag.getId());
			agentaccountservice.totalincome(t);
		} else {
			t.setAgentincome(0.00);
		}
		// 渠道余额
		t.setIncome(t.getMerchantpay() - t.getChannelpay() - t.getAgentincome()); // 此订单完成后预计总收入
		return mapper.post(t);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Payout get(Long id) {
		Payout t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public PayResultVO query(SysQueryDTO squery) {
		log.info("查单:" + squery.toString());
		Merchant mc = merchantmapper.getByCode(squery.getMerchantid());
		if (mc == null) {
			throw new YtException("商户不存在!");
		}

		if (!mc.getStatus()) {
			throw new YtException("商户被冻结!");
		}
		String ip = AuthContext.getIp();
		log.info(ip);
		if (mc.getIpstatus()) {
			if (ip == null || ip.equals("")) {
				throw new YtException("非法请求,IP加白名单后重试!");
			}
			if (mc.getIpaddress() == null || mc.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求,IP加白名单后重试!");
			}
		}
		Payout pt = mapper.getByMerchantOrderId(squery.getMerchantorderid());
		if (pt == null) {
			throw new YtException("订单不存在!");
		}
		PayResultVO srv = new PayResultVO();
		srv.setOutorderid(pt.getOrdernum());
		srv.setStatus(pt.getStatus());
		srv.setMerchantid(pt.getMerchantcode());
		srv.setMerchantorderid(squery.getMerchantorderid());
		srv.setPayamount(pt.getAmount());
		srv.setSign(PayUtil.Md5Notify(srv, mc.getAppkey()));
		return srv;
	}

	// 盘口提交订单
	@Override
	public PayResultVO submit(PaySubmitDTO ss) {
		Merchant mc = checkparam(ss);
		// 下單
		Payout pt = new Payout();
		pt.setAccname(ss.getBankowner());
		pt.setAccnumer(ss.getBanknum());
		pt.setBankcode(ss.getBankcode());
		pt.setBankaddress(ss.getBankaddress());
		pt.setAmount(ss.getPayamount());
		pt.setNotifyurl(ss.getNotifyurl());
		pt.setBankname(ss.getBankname());
		pt.setMerchantorderid(ss.getMerchantorderid());
		pt.setType(Integer.parseInt(ss.getPaytype()));
		addPayout(pt, mc, ss.getPayaisle());
		// 返回
		PayResultVO sr = new PayResultVO();
		sr.setMerchantid(sr.getMerchantid());
		sr.setMerchantorderid(ss.getMerchantorderid());
		sr.setPayamount(ss.getPayamount());
		sr.setOutorderid(pt.getOrdernum());
		sr.setSign(PayUtil.Md5QueryResult(sr, mc.getAppkey()));
		// 返回给盘口订单号
		return sr;
	}

	public void addPayout(Payout t, Merchant m, String aislecode) {
		TenantIdContext.setTenantId(m.getTenant_id());
		t.setUserid(m.getUserid());
		t.setMerchantid(m.getId());
		t.setMerchantcode(m.getCode());
		t.setMerchantname(m.getName());
		t.setOrdernum("OUT" + StringUtil.getOrderNum());// 系统单号
		t.setMerchantordernum(NumberUtil.getOrderNo());
		t.setMerchantcost(m.getOnecost());// 手续费
		t.setMerchantdeal(t.getAmount() * (m.getExchange() / 1000));// 交易费
		t.setMerchantpay(t.getAmount() + t.getMerchantcost() + t.getMerchantdeal());// 商户支付总额
		if (t.getNotifyurl().equals("block"))
			t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_60);// 无需通知
		else
			t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_61);// 需要通知
		t.setRemark("新增代付￥:" + String.format("%.2f", t.getAmount()));
		Aisle aisle = aislemapper.getByCode(aislecode);
		if (aisle != null) {
			t.setAisleid(aisle.getId());
			t.setAislename(aisle.getName());
			////////////////////////////////////////////////////// 计算渠道渠道/////////////////////////////////////
			List<Aislechannel> listac = aislechannelmapper.getByAisleId(t.getAisleid());
			Assert.notEmpty(listac, "没有可用通道!");
			long[] cids = listac.stream().mapToLong(ac -> ac.getChannelid()).distinct().toArray();
			List<Channel> listc = channelmapper.listByArrayId(cids);
			Assert.notEmpty(listc, "没有可用渠道!");
			List<Channel> listcmm = listc.stream().filter(c -> c.getMax() >= t.getAmount() && c.getMin() <= t.getAmount() && c.getStatus()).collect(Collectors.toList());
			Assert.notEmpty(listcmm, "代付金额超出限额");
			List<Channel> listcf = listc.stream().filter(c -> c.getFirstmatch() == true).collect(Collectors.toList());
			Channel cl = null;
			if (listcf.size() > 0) {
				for (int j = 0; j < listcf.size(); j++) {
					Channel cc = listcf.get(j);
					String[] match = cc.getFirstmatchmoney().split(",");
					for (int i = 0; i < match.length; i++) {
						String number = match[i];
						if (number.indexOf("-") == -1 && t.getAmount().intValue() == Integer.parseInt(number)) {
							cl = cc;
						} else {
							String[] matchs = number.split("-");
							Integer min = Integer.parseInt(matchs[0]);
							Integer max = Integer.parseInt(matchs[1]);
							if (t.getAmount().intValue() >= min && t.getAmount().intValue() <= max) {
								cl = cc;
							}
						}
					}
				}
			} else {
				List<WeightRandom.WeightObj<String>> weightList = new ArrayList<>(); //
				double count = 0;
				for (Channel cml : listcmm) {
					count = count + cml.getWeight();
				}
				for (Channel cmm : listcmm) {
					weightList.add(new WeightRandom.WeightObj<String>(cmm.getCode(), (cmm.getWeight() / count) * 100));
				}
				WeightRandom<String> wr = RandomUtil.weightRandom(weightList);
				String code = wr.next();
				cl = listc.stream().filter(c -> c.getCode() == code).collect(Collectors.toList()).get(0);
				Assert.notNull(cl, "没有可用的渠道!");
				t.setChannelid(cl.getId());
				t.setChannelname(cl.getName());
				t.setChannelcost(cl.getOnecost());// 渠道手续费
				if (cl.getExchange() >= 1) {
					t.setChanneldeal(t.getAmount() * (cl.getExchange() / 1000));
					t.setChannelpay(t.getAmount() + t.getChannelcost() + t.getChanneldeal());// 渠道总支付费用
				} else {
					t.setChanneldeal(t.getAmount() + t.getChannelcost());
					t.setChannelpay(t.getChanneldeal());// 渠道总支付费用
				}
				t.setStatus(DictionaryResource.ORDERSTATUS_50);
			}

			boolean flage = PayoutProduct.getPayoutProduct(t, cl);

			if (flage) {
				channelbot.notifyChannel(cl);
				throw new YtException("渠道错误!");
			}
			// 渠道余额
			t.setChannelbalance(cl.getBalance() - t.getChannelpay());
			///////////////////////////////////////////////////// 计算商户订单/////////////////////////////////////////////////////
			payoutmerchantaccountservice.withdrawamount(t);

			channelaccountservice.withdrawamount(t);
		} else {
			Qrcodeaisle qa = qrcodeaislemapper.getByCode(aislecode);
			Assert.notNull(qa, "没有可用通道!");
			t.setAisleid(qa.getId());
			t.setAislename(qa.getName());
			if (qa.getType() == DictionaryResource.BANK_TYPE_121)
				t.setType(DictionaryResource.ORDERTYPE_18);
			List<Qrcodeaisleqrcode> listqaq = qrcodeaisleqrcodemapper.getByQrcodeAisleId(t.getAisleid());
			long[] qaqids = listqaq.stream().mapToLong(qaq -> qaq.getQrcodelid()).distinct().toArray();
			List<Qrcode> listqrcode = qrcodemapper.listByArrayId(qaqids);
			Double amount = Double.valueOf(t.getAmount());
			// 小于设置限额
			List<Qrcode> listcmm = listqrcode.stream().filter(c -> c.getMax() >= amount && c.getMin() <= amount && c.getStatus()).collect(Collectors.toList());
			Assert.notEmpty(listcmm, "代付金额超出限额");
			List<Qrcode> listcf = listcmm.stream().filter(c -> c.getFirstmatch() == true).collect(Collectors.toList());
			Qrcode qd = null;
			if (listcf.size() > 0) {
				for (int j = 0; j < listcf.size(); j++) {
					Qrcode cc = listcf.get(j);
					String[] match = cc.getFirstmatchmoney().split(",");
					for (int i = 0; i < match.length; i++) {
						String number = match[i];
						if (number.indexOf("-") == -1 && t.getAmount().intValue() == Integer.parseInt(number)) {
							qd = cc;
						} else {
							String[] matchs = number.split("-");
							Integer min = Integer.parseInt(matchs[0]);
							Integer max = Integer.parseInt(matchs[1]);
							if (t.getAmount().intValue() >= min && t.getAmount().intValue() <= max) {
								qd = cc;
							}
						}
					}
				}
			} else {
				List<WeightRandom.WeightObj<String>> weightList = new ArrayList<>(); //
				double count = 0;
				for (Qrcode cml : listcmm) {
					count = count + cml.getWeight();
				}
				for (Qrcode cmm : listcmm) {
					weightList.add(new WeightRandom.WeightObj<String>(cmm.getCode(), (cmm.getWeight() / count) * 100));
				}
				WeightRandom<String> wr = RandomUtil.weightRandom(weightList);
				String code = wr.next();
				qd = listcmm.stream().filter(c -> c.getCode() == code).collect(Collectors.toList()).get(0);
				Assert.notNull(qd, "没有可用的渠道!");
				t.setChannelid(qd.getId());
				t.setChannelname(qd.getName());
				t.setChannelcost(2.00);// 渠道手续费
				t.setChanneldeal(t.getAmount() + t.getChannelcost());
				t.setChannelpay(t.getChanneldeal());// 渠道总支付费用
				t.setStatus(DictionaryResource.ORDERSTATUS_50);
			}
			RedisUtil.setEx(SystemConstant.CACHE_SYS_PAYOUT_EXIST + t.getOrdernum(), t.getOrdernum(), 60, TimeUnit.SECONDS);
			// 获取渠道单号
			getQrcodelOrderNo(t, qd);
			///////////////////////////////////////////////////// 计算商户订单/////////////////////////////////////////////////////
			payoutmerchantaccountservice.withdrawamount(t);

			qrcodeaccountservice.withdrawamount(t);
		}

		///////////////////////////////////////////////////// 计算代理订单
		if (m.getAgentid() != null) {
			Agent ag = agentmapper.get(m.getAgentid());
			t.setAgentid(ag.getId());
			agentaccountservice.totalincome(t);
		} else {
			t.setAgentincome(0.00);
		}
		// 小计
		t.setIncome(t.getMerchantpay() - t.getChannelpay() - t.getAgentincome()); // 此订单完成后预计总收入
		//
		mapper.post(t);
		TenantIdContext.remove();
	}

	private Merchant checkparam(PaySubmitDTO ss) {
		if (ss.getMerchantid().length() > 10) {
			throw new YtException("商户号错误!");
		}

		Merchant mc = merchantmapper.getByCode(ss.getMerchantid());

		if (mc == null) {
			throw new YtException("商户不存在!");
		}

		if (!mc.getStatus()) {
			throw new YtException("商户被冻结!");
		}
		String ip = AuthContext.getIp();
		log.info(ip);
		if (mc.getIpstatus()) {
			if (ip == null || ip.equals("")) {
				throw new YtException("非法请求,IP加白名单后重试!");
			}
			if (mc.getIpaddress() == null || mc.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求,IP加白名单后重试!");
			}
		}

		PayoutMerchantaccount ma = merchantaccountmapper.getByUserId(mc.getUserid());
		if (ma.getBalance() < ss.getPayamount() || ss.getPayamount() <= 0) {
			throw new YtException("账户余额不足");
		}

		Boolean val = PayUtil.Md5Submit(ss, mc.getAppkey());
		if (!val) {
			throw new YtException("签名不正确!");
		}

		Payout pt = mapper.getByMerchantOrderId(ss.getMerchantorderid());
		if (pt != null) {
			throw new YtException("已经存在的订单!");
		}
		return mc;
	}

	/***
	 * 成功
	 */
	public void paySuccess(Payout pt) {
		Payout t = mapper.get(pt.getId());
		if (t.getStatus().equals(DictionaryResource.ORDERSTATUS_50)) {
			// 商户账户
			payoutmerchantaccountservice.updateWithdrawamount(t);
			// 系统账户
			systemaccountservice.updatePayout(t);

			// 计算代理
			if (t.getAgentid() != null) {
				// 代理账户
				agentaccountservice.updateTotalincome(t);
			}

			// 渠道账户
			channelaccountservice.updateWithdrawamount(t);

			// ------------------更新代付订单-----------------
			t.setStatus(DictionaryResource.ORDERSTATUS_52);
			if (t.getNotifystatus().equals(DictionaryResource.PAYOUTNOTIFYSTATUS_61)) {
				t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_62);
			}
			t.setRemark("代付成功￥" + pt.getAmount());
			t.setSuccesstime(DateTimeUtil.getNow());
			t.setBacklong(DateUtil.between(t.getSuccesstime(), t.getCreate_time(), DateUnit.SECOND));

			t.setImgurl(pt.getImgurl());
			//
			int i = mapper.put(t);
			if (i > 0) {
				// 保存客户信息
				merchantcustomerbanksservice.add(t);
				// 通知
				channelbot.getOrderResultImg(pt.getChannelid(), pt.getOrdernum());
				if (pt.getImgurl() != null && !pt.getImgurl().equals(""))
					merchantbot.sendOrderResultImg(pt.getMerchantid(), pt.getImgurl());
				StringBuffer message = new StringBuffer();
				message.append("\r\n姓名：*" + pt.getAccname() + "*\r\n卡号：" + pt.getAccnumer() + " \r\n金额：" + pt.getAmount() + " \r\n单笔：" + pt.getMerchantcost() + " \r\n状态：成功✔✔✔" + "\r\n*" + pt.getOrdernum() + "*");
				merchantbot.sendOrderResultSuccess(pt.getMerchantid(), message.toString());
			}

		}
	}

	/**
	 * 
	 * 回调失败
	 * 
	 */
	public void payFail(Payout pt, String msg) {
		Payout t = mapper.get(pt.getId());
		if (t.getStatus().equals(DictionaryResource.ORDERSTATUS_50)) {
			// 计算商户订单/////////////////////////////////////////////////////
			payoutmerchantaccountservice.cancleWithdrawamount(t);

			// 计算代理
			if (t.getAgentid() != null) {
				agentaccountservice.cancleTotalincome(t);
			}

			// 计算渠道
			channelaccountservice.cancleWithdrawamount(t);

			//
			t.setStatus(DictionaryResource.ORDERSTATUS_53);
			if (t.getNotifystatus().equals(DictionaryResource.PAYOUTNOTIFYSTATUS_61)) {
				t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_62);
			}
			t.setRemark(msg);
			t.setSuccesstime(DateTimeUtil.getNow());
			t.setBacklong(DateTimeUtil.diffDays(t.getSuccesstime(), t.getCreate_time()));
			int i = mapper.put(t);
			if (i > 0) {
				// 保存客户信息
				merchantcustomerbanksservice.add(t);
				StringBuffer message = new StringBuffer();
				message.append("\r\n姓名：*" + pt.getAccname() + "*\r\n卡号：" + pt.getAccnumer() + " \r\n金额：" + pt.getAmount() + " \r\n状态：失败❌❌❌" + "\r\n*" + pt.getOrdernum() + "*");
				merchantbot.sendOrderResultFail(pt.getMerchantid(), message.toString());
			}
		}
	}

	/***
	 * 成功
	 */
	public void paySuccessSelf(Payout pt) {
		Payout t = mapper.get(pt.getId());
		if (t.getStatus().equals(DictionaryResource.ORDERSTATUS_50)) {
			// 计算商户订单/////////////////////////////////////////////////////
			payoutmerchantaccountservice.updateWithdrawamount(t);
			// 系统账户
			systemaccountservice.updatePayout(t);

			// 计算代理
			if (t.getAgentid() != null) {
				// 代理账户
				agentaccountservice.updateTotalincome(t);
			}
			// 计算渠道
//			channelaccountservice.turndownWithdrawamount(cao);

			// ------------------更新代付订单-----------------
			t.setStatus(DictionaryResource.ORDERSTATUS_52);
			if (t.getNotifystatus().equals(DictionaryResource.PAYOUTNOTIFYSTATUS_61)) {
				t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_62);
			}
			t.setRemark("代付成功￥" + pt.getAmount());
			t.setSuccesstime(DateTimeUtil.getNow());
			t.setBacklong(DateUtil.between(t.getSuccesstime(), t.getCreate_time(), DateUnit.SECOND));

			t.setImgurl(pt.getImgurl());
			//
			int i = mapper.put(t);
			if (i > 0) {
				// 保存客户信息
				merchantcustomerbanksservice.add(t);
				// 通知
				channelbot.getOrderResultImg(pt.getChannelid(), pt.getOrdernum());
				if (pt.getImgurl() != null && !pt.getImgurl().equals(""))
					merchantbot.sendOrderResultImg(pt.getMerchantid(), pt.getImgurl());
				StringBuffer message = new StringBuffer();
				message.append("\r\n姓名：*" + pt.getAccname() + "*\r\n卡号：" + pt.getAccnumer() + " \r\n金额：" + pt.getAmount() + " \r\n单笔：" + pt.getMerchantcost() + " \r\n状态：成功✔✔✔" + "\r\n*" + pt.getOrdernum() + "*");
				merchantbot.sendOrderResultSuccess(pt.getMerchantid(), message.toString());
			}

		}
	}

	/**
	 * 
	 * 回调失败
	 * 
	 */
	public void payFailSelf(Payout pt) {
		Payout t = mapper.get(pt.getId());
		if (t.getStatus().equals(DictionaryResource.ORDERSTATUS_50)) {
			// 计算商户订单/////////////////////////////////////////////////////
			payoutmerchantaccountservice.cancleWithdrawamount(t);

			// 计算代理
			if (t.getAgentid() != null) {
				//
				agentaccountservice.cancleTotalincome(t);
			}

			// 计算渠道
//			channelaccountservice.turndownWithdrawamount(cao);

			//
			t.setStatus(DictionaryResource.ORDERSTATUS_53);
			if (t.getNotifystatus().equals(DictionaryResource.PAYOUTNOTIFYSTATUS_61)) {
				t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_62);
			}
			t.setSuccesstime(DateTimeUtil.getNow());
			t.setBacklong(DateTimeUtil.diffDays(t.getSuccesstime(), t.getCreate_time()));
			int i = mapper.put(t);
			if (i > 0) {
				// 保存客户信息
				merchantcustomerbanksservice.add(t);
				StringBuffer message = new StringBuffer();
				message.append("\r\n姓名：*" + pt.getAccname() + "*\r\n卡号：" + pt.getAccnumer() + " \r\n金额：" + pt.getAmount() + " \r\n状态：失败❌❌❌" + "\r\n*" + pt.getOrdernum() + "*");
				merchantbot.sendOrderResultFail(pt.getMerchantid(), message.toString());
			}
		}
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public PayResultVO queryblance(String merchantid) {
		Merchant mc = merchantmapper.getByCode(merchantid);
		if (mc == null) {
			throw new YtException("商户不存在!");
		}

		if (!mc.getStatus()) {
			throw new YtException("商户被冻结!");
		}
		String ip = AuthContext.getIp();
		log.info(ip);
		if (mc.getIpstatus()) {
			if (ip == null || ip.equals("")) {
				throw new YtException("非法请求,IP加白名单后重试!");
			}
			if (mc.getIpaddress() == null || mc.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求,IP加白名单后重试!");
			}
		}
		PayoutMerchantaccount mtt = merchantaccountmapper.getByUserId(mc.getUserid());
		PayResultVO srv = new PayResultVO();
		srv.setBalance(mtt.getBalance());
		srv.setMerchantid(merchantid);
		return srv;
	}

	@Override
	public YtBody txcallbackpay(SysTyOrder so) {
		Payout pt = mapper.getByOrdernum(so.getMerchant_order_id());
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			if (pt.getStatus().equals(DictionaryResource.ORDERSTATUS_50)) {
				Channel cl = channelmapper.get(pt.getChannelid());
				String ip = AuthContext.getIp();
				if (cl.getIpaddress() == null || cl.getIpaddress().indexOf(ip) == -1) {
					throw new YtException("非法请求!");
				}
				// 查询渠道是否真实成功
				SysTyOrder sto = PayUtil.SendTxSelectOrder(pt.getOrdernum(), cl);
				if (sto != null && sto.getPay_message() == 1) {
					// md5值是否被篡改
					if (PayUtil.valMd5TyResultOrder(so, cl.getApikey()) && so.getPay_message() == 1) {
						pt.setImgurl(so.getImage_address());
						paySuccess(pt);
					}
				} else if (sto != null && (sto.getPay_message() == -2 || sto.getPay_message() == -3) && so.getPay_message() == -2) {
					payFail(pt, "失败");
				}
			}
		}
		SysUserContext.remove();
		TenantIdContext.remove();
		return new YtBody("成功", 200);
	}

	@Override
	public YtBody exist(SysTyOrder so) {
		if (RedisUtil.hasKey(SystemConstant.CACHE_SYS_PAYOUT_EXIST + so.getMerchant_order_id())) {
			return new YtBody("成功", 200);
		}
		return new YtBody("成功", 400);
	}

	@Override
	public String upFile(MultipartFile file, String aisleid) throws IOException {
		Aisle aisle = aislemapper.get(Long.valueOf(aisleid));
		PayoutMerchantaccount maccount = merchantaccountmapper.getByUserId(SysUserContext.getUserId());
		Merchant m = merchantmapper.getByUserId(SysUserContext.getUserId());
		Workbook wb = WorkbookFactory.create(file.getInputStream());
		Sheet sheet = wb.getSheetAt(0);
		Double countamount = 0.0;
		int maxRow = sheet.getLastRowNum();
		for (int i = 1; i <= maxRow; i++) {
			Row row = sheet.getRow(i);
			if (row != null && row.getCell(0) != null) {
				countamount = countamount + Double.valueOf(row.getCell(3).toString());
			}
		}
		if (countamount <= 0 || countamount > maccount.getBalance()) {
			throw new YtException("账户余额不足");
		}

		////////////////////////////////////////////////////// 计算渠道渠道/////////////////////////////////////
		List<Aislechannel> listac = aislechannelmapper.getByAisleId(aisle.getId());
		Assert.notEmpty(listac, "没有可用通道!");
		long[] cids = listac.stream().mapToLong(ac -> ac.getChannelid()).distinct().toArray();
		List<Channel> listc = channelmapper.listByArrayId(cids);
		Assert.notEmpty(listc, "没有可用渠道!");
		List<WeightRandom.WeightObj<String>> weightList = new ArrayList<>(); //
		double count = 0;
		for (Channel cml : listc) {
			count = count + cml.getWeight();
		}
		for (Channel cmm : listc) {
			weightList.add(new WeightRandom.WeightObj<String>(cmm.getCode(), (cmm.getWeight() / count) * 100));
		}
		WeightRandom<String> wr = RandomUtil.weightRandom(weightList);
		String code = wr.next();
		Channel cl = listc.stream().filter(c -> c.getCode() == code).collect(Collectors.toList()).get(0);
		Assert.notNull(cl, "没有可用的渠道!");
		String batchid = NumberUtil.getOrderNo();
		for (int i = 1; i <= maxRow; i++) {
			Row row = sheet.getRow(i);
			if (row != null && row.getCell(0) != null) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				importOrder(batchid, aisle, row.getCell(0).toString().replace(" ", ""), row.getCell(1).toString().replace(" ", ""), row.getCell(2).toString().replace(" ", ""), Double.valueOf(row.getCell(3).toString()), m, cl);
			}
		}
		return file.getOriginalFilename();
	}

	@Override
	public String uploadself(MultipartFile file, String aisleid) throws IOException {
		Qrcodeaisle a = qrcodeaislemapper.get(Long.valueOf(aisleid));
		PayoutMerchantaccount maccount = merchantaccountmapper.getByUserId(SysUserContext.getUserId());
		Merchant m = merchantmapper.getByUserId(SysUserContext.getUserId());
		Workbook wb = WorkbookFactory.create(file.getInputStream());
		Sheet sheet = wb.getSheetAt(0);
		Double countamount = 0.0;
		int maxRow = sheet.getLastRowNum();
		for (int i = 1; i <= maxRow; i++) {
			Row row = sheet.getRow(i);
			if (row != null && row.getCell(0) != null) {
				countamount = countamount + Double.valueOf(row.getCell(3).toString());
			}
		}
		if (countamount <= 0 || countamount > maccount.getBalance()) {
			throw new YtException("账户余额不足");
		}

		////////////////////////////////////////////////////// 计算渠道渠道/////////////////////////////////////

		List<Qrcodeaisleqrcode> listqaq = qrcodeaisleqrcodemapper.getByQrcodeAisleId(Long.valueOf(aisleid));
		long[] qaqids = listqaq.stream().mapToLong(qaq -> qaq.getQrcodelid()).distinct().toArray();
		List<Qrcode> listqrcode = qrcodemapper.listByArrayId(qaqids);
		List<WeightRandom.WeightObj<String>> weightList = new ArrayList<>(); //
		double count = 0;
		for (Qrcode cml : listqrcode) {
			count = count + cml.getWeight();
		}
		for (Qrcode cmm : listqrcode) {
			weightList.add(new WeightRandom.WeightObj<String>(cmm.getCode(), (cmm.getWeight() / count) * 100));
		}
		WeightRandom<String> wr = RandomUtil.weightRandom(weightList);
		String code = wr.next();
		Qrcode qd = listqrcode.stream().filter(c -> c.getCode() == code).collect(Collectors.toList()).get(0);
		Assert.notNull(qd, "没有可用的渠道!");
		String batchid = NumberUtil.getOrderNo();
		for (int i = 1; i <= maxRow; i++) {
			Row row = sheet.getRow(i);
			if (row != null && row.getCell(0) != null) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Integer j = importOrderSelf(batchid, a, row.getCell(0).toString().replace(" ", ""), row.getCell(1).toString().replace(" ", ""), row.getCell(2).toString().replace(" ", ""), Double.valueOf(row.getCell(3).toString()), m, qd);
				if (j == 0)
					break;
			}
		}
		return file.getOriginalFilename();
	}

	public synchronized Integer importOrder(String batchid, Aisle al, String name, String cardno, String bankname, Double amount, Merchant m, Channel cl) {

		Payout t = new Payout();
		t.setAccname(name.replaceAll(" ", ""));
		t.setAccnumer(cardno.replaceAll(" ", ""));
		t.setBankname(bankname.replaceAll(" ", ""));
		t.setAmount(amount);
		if (!m.getStatus()) {
			throw new YtException("商户被冻结!");
		}

		t.setChannelid(cl.getId());
		t.setChannelname(cl.getName());
		t.setChannelcost(cl.getOnecost());// 渠道手续费
		if (cl.getExchange() >= 1) {
			t.setChanneldeal(t.getAmount() * (cl.getExchange() / 1000));
			t.setChannelpay(t.getAmount() + t.getChannelcost() + t.getChanneldeal());// 渠道总支付费用
		} else {
			t.setChanneldeal(t.getAmount() + t.getChannelcost());
			t.setChannelpay(t.getChanneldeal());// 渠道总支付费用
		}
		t.setStatus(DictionaryResource.ORDERSTATUS_50);

		t.setUserid(m.getUserid());
		t.setMerchantid(m.getId());
		t.setNotifyurl(m.getApireusultip());
		t.setMerchantcode(m.getCode());
		t.setMerchantname(m.getName());
		if (t.getBankname().equals("支付宝"))
			t.setType(DictionaryResource.ORDERTYPE_19);
		else
			t.setType(DictionaryResource.ORDERTYPE_18);
		t.setOrdernum("OUT" + StringUtil.getOrderNum());// 系统单号
		t.setMerchantorderid("OUTM" + StringUtil.getOrderNum());// 商户单号
		t.setMerchantordernum(batchid);
		t.setMerchantcost(m.getOnecost());// 手续费
		t.setMerchantdeal(t.getAmount() * (m.getExchange() / 1000));// 交易费
		t.setMerchantpay(t.getAmount() + t.getMerchantcost() + t.getMerchantdeal());// 商户支付总额
		t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_60);// 商戶發起
		t.setRemark("新增代付￥:" + String.format("%.2f", t.getAmount()));

		t.setAislename(al.getName());

		RedisUtil.setEx(SystemConstant.CACHE_SYS_PAYOUT_EXIST + t.getOrdernum(), t.getOrdernum(), 60, TimeUnit.SECONDS);

		boolean flage = PayoutProduct.getPayoutProduct(t, cl);
		if (flage) {
			channelbot.notifyChannel(cl);
			throw new YtException("渠道错误!");
		}

		///////////////////////////////////////////////////// 计算商户订单
		payoutmerchantaccountservice.withdrawamount(t);

		channelaccountservice.withdrawamount(t);

		///////////////////////////////////////////////////// 计算代理订单/////////////////////////////////////////////////////
		if (m.getAgentid() != null) {
			Agent ag = agentmapper.get(m.getAgentid());
			t.setAgentid(ag.getId());
			agentaccountservice.totalincome(t);
		} else {
			t.setAgentincome(0.00);
		}
		// 渠道余额
		t.setChannelbalance(cl.getBalance() - t.getChannelpay());
		cl.setBalance(cl.getBalance() - t.getChannelpay());
		// 小计
		t.setIncome(t.getMerchantpay() - t.getChannelpay() - t.getAgentincome()); // 此订单完成后预计总收入
		return mapper.post(t);
	}

	public synchronized Integer importOrderSelf(String batchid, Qrcodeaisle a, String name, String cardno, String bankname, Double amount, Merchant m, Qrcode qd) {

		Payout t = new Payout();
		t.setAccname(name.replaceAll(" ", ""));
		t.setAccnumer(cardno.replaceAll(" ", ""));
		t.setBankname(bankname.replaceAll(" ", ""));
		t.setAmount(amount);
		if (!m.getStatus()) {
			throw new YtException("商户被冻结!");
		}

		t.setAccname(t.getAccname().replaceAll(" ", ""));
		t.setAccnumer(t.getAccnumer().replaceAll(" ", ""));
		t.setUserid(m.getUserid());
		t.setMerchantid(m.getId());
		t.setNotifyurl(m.getApireusultip());
		t.setMerchantcode(m.getCode());
		t.setMerchantname(m.getName());
		t.setType(DictionaryResource.ORDERTYPE_18);
		t.setOrdernum("OUT" + StringUtil.getOrderNum());// 系统单号
		t.setMerchantorderid("OUTM" + StringUtil.getOrderNum());// 商户单号
		t.setMerchantordernum(batchid);
		t.setMerchantcost(m.getOnecost());// 手续费
		t.setMerchantdeal(t.getAmount() * (m.getExchange() / 1000));// 交易费
		t.setMerchantpay(t.getAmount() + t.getMerchantcost() + t.getMerchantdeal());// 商户支付总额
		t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_60);// 商戶發起
		t.setRemark("新增代付￥:" + String.format("%.2f", t.getAmount()));

		t.setAisleid(a.getId());
		t.setAislename(a.getName());
		t.setChannelid(qd.getId());
		t.setChannelname(qd.getName());
		t.setChannelcost(2.00);// 渠道手续费
		t.setChanneldeal(t.getAmount() + t.getChannelcost());
		t.setChannelpay(t.getChanneldeal());// 渠道总支付费用
		t.setStatus(DictionaryResource.ORDERSTATUS_50);
		RedisUtil.setEx(SystemConstant.CACHE_SYS_PAYOUT_EXIST + t.getOrdernum(), t.getOrdernum(), 60, TimeUnit.SECONDS);

		// 获取渠道单号
		getQrcodelOrderNo(t, qd);

		///////////////////////////////////////////////////// 计算商户订单
		payoutmerchantaccountservice.withdrawamount(t);

		///////////////////////////////////////////////////// 计算代理订单/////////////////////////////////////////////////////
		if (m.getAgentid() != null) {
			Agent ag = agentmapper.get(m.getAgentid());
			t.setAgentid(ag.getId());
			agentaccountservice.totalincome(t);
		} else {
			t.setAgentincome(0.00);
		}
		// 渠道余额
		t.setIncome(t.getMerchantpay() - t.getChannelpay() - t.getAgentincome()); // 此订单完成后预计总收入
		return mapper.post(t);
	}

	void getQrcodelOrderNo(Payout t, Qrcode qd) {
		// 获取渠道单号
		switch (qd.getCode()) {
		case DictionaryResource.PRODUCT_YPLWAP:
			JSONObject returndata = SelfPayUtil.eplwithdrawalToCard(qd, t.getOrdernum(), t.getAccname(), t.getAccnumer(), t.getBankname(), Long.valueOf(String.format("%.2f", t.getAmount()).replace(".", "")));
			if (returndata.getStr("returnCode").equals("0000")) {
				String transactionNo = returndata.getStr("transactionNo");
				t.setChannelordernum(transactionNo);
			} else {
				t.setRemark(returndata.getStr("returnMsg"));
				t.setChannelordernum(StringUtil.getOrderNum());
				t.setStatus(DictionaryResource.ORDERSTATUS_53);
				t.setNotifystatus(DictionaryResource.PAYOUTNOTIFYSTATUS_62);
			}
			break;
		}
	}

	@Override
	public void sncallback(Map<String, Object> params) {
		String orderid = params.get("OrderNo").toString();
		String status = params.get("Status").toString();
		log.info("十年通知返回消息：orderid" + orderid + " status:" + status);
		Payout pt = mapper.getByOrdernum(orderid);
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			Channel channel = channelmapper.get(pt.getChannelid());
			String ip = AuthContext.getIp();
			if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求!");
			}
			// 查询渠道是否真实成功
			SysSnOrder data = PayUtil.SendSnSelectOrder(pt.getOrdernum(), channel);
			Assert.notNull(data, "十年代付通知反查订单失败!");
			if (data.getData().getStatus() == 4) {
				pt.setImgurl(data.getData().getAttachments());
				paySuccess(pt);
			} else if (data.getData().getStatus() == 16) {
				payFail(pt, "失败");
			}
			SysUserContext.remove();
			TenantIdContext.remove();
		}
	}

	@Override
	public void sscallback(Map<String, Object> params) {
		String orderid = params.get("OrderNo").toString();
		String status = params.get("Status").toString();
		log.info("盛世通知返回消息：orderid" + orderid + " status:" + status);
		Payout pt = mapper.getByOrdernum(orderid);
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			Channel channel = channelmapper.get(pt.getChannelid());
			String ip = AuthContext.getIp();
			if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求!");
			}
			// 查询渠道是否真实成功
			Integer returnstate = PayUtil.SendSSSelectOrder(pt.getOrdernum(), channel);
			Assert.notNull(returnstate, "盛世代付通知反查订单失败!");
			if (returnstate == 4) {
				paySuccess(pt);
			} else if (returnstate == 16) {
				payFail(pt, "失败");
			}
			SysUserContext.remove();
			TenantIdContext.remove();
		}
	}

	@Override
	public void ysdfcallback(Map<String, String> params) {
		String orderid = params.get("mchOrderNo").toString();
		String status = params.get("status").toString();
		log.info("易生通知返回消息：orderid" + orderid + " status:" + status);
		Payout pt = mapper.getByOrdernum(orderid);
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			Channel channel = channelmapper.get(pt.getChannelid());
			String ip = AuthContext.getIp();
			if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求!");
			}
			// 查询渠道是否真实成功
			Integer returnstate = PayUtil.SendYSSelectOrder(pt.getOrdernum(), channel);
			Assert.notNull(returnstate, "易生代付通知反查订单失败!");
			if (returnstate == 2) {
				paySuccess(pt);
			} else if (returnstate == 3) {
				payFail(pt, "失败");
			}
			SysUserContext.remove();
			TenantIdContext.remove();
		}
	}

	@Override
	public void xrcallback(Map<String, String> params) {
		String orderid = params.get("mchOrderNo").toString();
		String status = params.get("status").toString();
		log.info("旭日通知返回消息：orderid" + orderid + " status:" + status);
		Payout pt = mapper.getByOrdernum(orderid);
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			Channel channel = channelmapper.get(pt.getChannelid());
			String ip = AuthContext.getIp();
			if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求!");
			}
			// 查询渠道是否真实成功
			String returnstate = PayUtil.SendXRSelectOrder(pt.getOrdernum(), channel);
			Assert.notNull(returnstate, "旭日代付通知反查订单失败!");
			if (returnstate.equals("2")) {
				paySuccess(pt);
			} else if (returnstate.equals("3")) {
				payFail(pt, "失败");
			}
			SysUserContext.remove();
			TenantIdContext.remove();
		}

	}

	@Override
	public void sxcallback(Map<String, String> params) {
		String orderid = params.get("out_trade_no").toString();
		String status = params.get("trade_status").toString();
		log.info("守信通知返回消息：orderid" + orderid + " status:" + status);
		Payout pt = mapper.getByOrdernum(orderid);
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			Channel channel = channelmapper.get(pt.getChannelid());
			String ip = AuthContext.getIp();
			if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求!");
			}
			// 查询渠道是否真实成功
			Integer returnstate = PayUtil.SendSXSelectOrder(pt.getOrdernum(), channel);
			Assert.notNull(returnstate, "守信代付通知反查订单失败!");
			if (returnstate == 1) {
				paySuccess(pt);
			} else if (returnstate == 2) {
				payFail(pt, "失败");
			}
			SysUserContext.remove();
			TenantIdContext.remove();
		}
	}

	@Override
	public void ljcallback(Map<String, String> params) {
		String orderid = params.get("out_trade_no").toString();
		String status = params.get("trade_status").toString();
		log.info("灵境通知返回消息：orderid" + orderid + " status:" + status);
		Payout pt = mapper.getByOrdernum(orderid);
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			Channel channel = channelmapper.get(pt.getChannelid());
			String ip = AuthContext.getIp();
			if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求!");
			}
			// 查询渠道是否真实成功
			Integer returnstate = PayUtil.SendLJSelectOrder(pt.getOrdernum(), channel);
			Assert.notNull(returnstate, "灵境代付通知反查订单失败!");
			if (returnstate == 1) {
				paySuccess(pt);
			} else if (returnstate == 2) {
				payFail(pt, "失败");
			}
			SysUserContext.remove();
			TenantIdContext.remove();
		}
	}

	@Override
	public void epfcallback(Map<String, Object> params) {
		String orderid = params.get("outTradeNo").toString();
		String status = params.get("payState").toString();
		log.info("易票联通知返回消息：orderid" + orderid + " status:" + status);
		Payout pt = mapper.getByOrdernum(orderid);
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			Qrcode qrcode = qrcodemapper.get(pt.getChannelid());
			// 查询渠道是否真实成功
			JSONObject returnstate = SelfPayUtil.eplwithdrawalToCardQuery(qrcode, orderid);
			Assert.notNull(returnstate, "易票联代付通知反查订单失败!");
			if (returnstate.getStr("returnCode").equals("0000") && returnstate.getStr("payState").equals("00")) {
				JSONObject returnspayimg = SelfPayUtil.eplwithdrawalCertification(qrcode, orderid);
				if (returnspayimg.getStr("returnCode").equals("0000")) {
					String imgurl = fileservice.addBase64String(returnspayimg.getStr("imageContent"));
					pt.setImgurl(imgurl);
				}
				paySuccessSelf(pt);
			} else {
				pt.setRemark(returnstate.getStr("returnMsg"));
				payFailSelf(pt);
			}
			SysUserContext.remove();
			TenantIdContext.remove();
		}

	}

	@Override
	public void hytcallback(Map<String, String> params) {
		String orderid = params.get("MerchantUniqueOrderId").toString();
		String status = params.get("WithdrawOrderStatus").toString();
		log.info("HYT通知返回消息：orderid" + orderid + " status:" + status);
		Payout pt = mapper.getByOrdernum(orderid);
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			Channel channel = channelmapper.get(pt.getChannelid());
			String ip = AuthContext.getIp();
			if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求!");
			}
			// 查询渠道是否真实成功
			String returnstate = PayUtil.SendHYTSelectOrder(pt.getOrdernum(), channel);
			Assert.notNull(returnstate, "HYT代付通知反查订单失败!");
			if (returnstate.equals("100")) {
				paySuccess(pt);
			} else if (returnstate.equals("-90")) {
				payFail(pt, "失败");
			}
			SysUserContext.remove();
			TenantIdContext.remove();
		}
	}

	@Override
	public void xjcallback(Map<String, Object> params) {
		String orderid = params.get("outTradeNo").toString();
		String status = params.get("state").toString();
		log.info("仙剑通知返回消息：orderid" + orderid + " status:" + status);
		Payout pt = mapper.getByOrdernum(orderid);
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			Channel channel = channelmapper.get(pt.getChannelid());
			String ip = AuthContext.getIp();
			if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求!");
			}
			// 查询渠道是否真实成功
			Integer returnstate = PayUtil.SendXJSelectOrder(pt.getOrdernum(), channel);
			Assert.notNull(returnstate, "仙剑代付通知反查订单失败!");
			if (returnstate == 2) {
				paySuccess(pt);
			} else if (returnstate == 3) {
				payFail(pt, "失败");
			}
			SysUserContext.remove();
			TenantIdContext.remove();
		}
	}

	@Override
	public void qwcallback(Map<String, String> params) {
		String orderid = params.get("mchOrderNo").toString();
		String status = params.get("status").toString();
		log.info("青蛙通知返回消息：orderid" + orderid + " status:" + status);
		Payout pt = mapper.getByOrdernum(orderid);
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			Channel channel = channelmapper.get(pt.getChannelid());
			String ip = AuthContext.getIp();
			if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求!");
			}
			// 查询渠道是否真实成功
			Integer returnstate = PayUtil.SendQWSelectOrder(pt.getOrdernum(), channel);
			Assert.notNull(returnstate, "青蛙代付通知反查订单失败!");
			if (returnstate == 2) {
				paySuccess(pt);
			} else if (returnstate == 3) {
				payFail(pt, "失败");
			}
			SysUserContext.remove();
			TenantIdContext.remove();
		}
	}

	@Override
	public void tycallback(Map<String, String> params) {
		String orderid = params.get("externalBatchOrderId").toString();
		String status = params.get("status").toString();
		log.info("通银通知返回消息：orderid" + orderid + " status:" + status);
		Payout pt = mapper.getByOrdernum(orderid);
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			Channel channel = channelmapper.get(pt.getChannelid());
			String ip = AuthContext.getIp();
			if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求!");
			}
			if (status.equals("FINISHED")) {
//				PayUtil.SendTYSelectOrder(orderid, channel);
				// 查询渠道是否真实成功
				JSONArray arrays = JSONUtil.parseArray(params.get("transList"));
				for (int i = 0; i < arrays.size(); i++) {
					JSONObject returnstate = JSONUtil.parseObj(arrays.get(i));
					if (returnstate.getStr("status").equals("SUCCEED")) {
						paySuccess(pt);
					} else if (returnstate.getStr("status").equals("FAILED")) {
						payFail(pt, returnstate.getStr("failMsg"));
					}
				}
			}
			SysUserContext.remove();
			TenantIdContext.remove();
		}
	}

	@Override
	public void jocallback(Map<String, String> params) {
		String orderid = params.get("orderCode").toString();
		String status = params.get("status").toString();
		log.info("捷哦通知返回消息：orderid" + orderid + " status:" + status);
		Payout pt = mapper.getByOrdernum(orderid);
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			Channel channel = channelmapper.get(pt.getChannelid());
			String ip = AuthContext.getIp();
			if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求!");
			}
			JSONObject data = PayUtil.SendJOSelectOrder(orderid, channel);
			// 查询渠道是否真实成功
			JSONObject result = data.getJSONObject("result");
			if (result.getInt("orderStatus")==1) {
				paySuccess(pt);
			} else if (result.getInt("orderStatus")==4) {
				payFail(pt, data.getStr("message"));
			}
			SysUserContext.remove();
			TenantIdContext.remove();
		}
	}

	@Override
	public void ftcallback(Map<String, String> params) {
		String orderid = params.get("merchantorderid").toString();
		String status = params.get("status").toString();
		log.info("飞兔通知返回消息：orderid" + orderid + " status:" + status);
		Payout pt = mapper.getByOrdernum(orderid);
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			Channel channel = channelmapper.get(pt.getChannelid());
			String ip = AuthContext.getIp();
			if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求!");
			}
			// 查询渠道是否真实成功
			Integer returnstate = PayUtil.SendFTSelectOrder(pt.getOrdernum(), channel);
			Assert.notNull(returnstate, "飞兔代付通知反查订单失败!");
			if (returnstate == 52) {
				paySuccess(pt);
			} else if (returnstate == 53) {
				payFail(pt, "失败");
			}
			SysUserContext.remove();
			TenantIdContext.remove();
		}
	}

	@Override
	public void g8callback(Map<String, Object> params) {
		String orderid = params.get("merchantOrderNo").toString();
		String status = params.get("status").toString();
		log.info("8g通知返回消息：orderid" + orderid + " status:" + status);
		Payout pt = mapper.getByOrdernum(orderid);
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			Channel channel = channelmapper.get(pt.getChannelid());
			String ip = AuthContext.getIp();
			if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求!");
			}
			// 查询渠道是否真实成功
			String returnstate = PayUtil.Send8GSelectOrder(pt.getOrdernum(), channel);
			Assert.notNull(returnstate, "8g代付通知反查订单失败!");
			if (returnstate.equals("SUCCESS")) {
				paySuccess(pt);
			} else if (returnstate.equals("FAIL")) {
				payFail(pt, "失败");
			}
			SysUserContext.remove();
			TenantIdContext.remove();
		}
	}

	@Override
	public void hycallback(String orderid) {
		log.info("环宇通知返回消息：orderid" + orderid);
		Payout pt = mapper.getByOrdernum(orderid);
		if (pt != null) {
			SysUserContext.setUserId(pt.getUserid());
			TenantIdContext.setTenantId(pt.getTenant_id());
			Channel channel = channelmapper.get(pt.getChannelid());
			String ip = AuthContext.getIp();
			if (channel.getIpaddress() == null || channel.getIpaddress().indexOf(ip) == -1) {
				throw new YtException("非法请求!");
			}
			// 查询渠道是否真实成功
			String returnstate = PayUtil.SendHYSelectOrder(pt.getOrdernum(), channel);
			Assert.notNull(returnstate, "环宇代付通知反查订单失败!");
			if (returnstate.equals("成功")) {
				paySuccess(pt);
			} else if (returnstate.equals("失败")) {
				payFail(pt, "失败");
			}
			SysUserContext.remove();
			TenantIdContext.remove();
		}
	}

	@Override
	public ByteArrayOutputStream download(Map<String, Object> param) throws IOException {
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		SXSSFSheet sheet = workbook.createSheet("Sheet");
		List<PayoutVO> list = mapper.page(param);
		list.forEach(mco -> {
			mco.setStatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getStatus()));
		});
		SXSSFRow titleRow = sheet.createRow(0);
		SXSSFCell titleCell0 = titleRow.createCell(0);
		titleCell0.setCellValue("编号");
		SXSSFCell titleCell1 = titleRow.createCell(1);
		titleCell1.setCellValue("系统单号");
		SXSSFCell titleCell2 = titleRow.createCell(2);
		titleCell2.setCellValue("账户名");
		SXSSFCell titleCell3 = titleRow.createCell(3);
		titleCell3.setCellValue("银行名称");
		SXSSFCell titleCell4 = titleRow.createCell(4);
		titleCell4.setCellValue("账号");
		SXSSFCell titleCell5 = titleRow.createCell(5);
		titleCell5.setCellValue("提现金额");
		SXSSFCell titleCell6 = titleRow.createCell(6);
		titleCell6.setCellValue("手续费");
		SXSSFCell titleCell7 = titleRow.createCell(7);
		titleCell7.setCellValue("扣款");
		SXSSFCell titleCell8 = titleRow.createCell(8);
		titleCell8.setCellValue("订单状态");
		SXSSFCell titleCell9 = titleRow.createCell(9);
		titleCell9.setCellValue("创建时间");
		SXSSFCell titleCell10 = titleRow.createCell(10);
		titleCell10.setCellValue("渠道");
		// 填充数据
		for (int i = 0; i < list.size(); i++) {
			PayoutVO imao = list.get(i);
			SXSSFRow row = sheet.createRow(i + 1);
			SXSSFCell cell0 = row.createCell(0);
			cell0.setCellValue(i + 1);
			SXSSFCell cell1 = row.createCell(1);
			cell1.setCellValue(imao.getOrdernum());
			SXSSFCell cell2 = row.createCell(2);
			cell2.setCellValue(imao.getAccname());
			SXSSFCell cell3 = row.createCell(3);
			cell3.setCellValue(imao.getBankname());
			SXSSFCell cell4 = row.createCell(4);
			cell4.setCellValue(imao.getAccnumer());
			SXSSFCell cell5 = row.createCell(5);
			cell5.setCellValue(imao.getAmount());
			SXSSFCell cell6 = row.createCell(6);
			cell6.setCellValue(imao.getMerchantcost());
			SXSSFCell cell7 = row.createCell(7);
			cell7.setCellValue(imao.getMerchantpay());
			SXSSFCell cell8 = row.createCell(8);
			cell8.setCellValue(imao.getStatusname());
			SXSSFCell cell9 = row.createCell(9);
			cell9.setCellValue(DateTimeUtil.getDateTime(imao.getCreate_time(), DateTimeUtil.DEFAULT_DATETIME_FORMAT));
			SXSSFCell cell10 = row.createCell(10);
			cell10.setCellValue(imao.getChannelname());
		}
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();
		return outputStream;
	}

}