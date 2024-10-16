package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.QrcodeMapper;
import com.yt.app.api.v1.mapper.QrcodeaisleqrcodeMapper;
import com.yt.app.api.v1.model.result.AlipayF2FPrecreateResult;
import com.yt.app.api.v1.service.AlipayTradeService;
import com.yt.app.api.v1.service.QrcodeService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.ServiceConstant;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.context.SysUserContext;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.yt.app.api.v1.entity.Qrcode;
import com.yt.app.api.v1.entity.Qrcodeaisleqrcode;
import com.yt.app.api.v1.vo.QrcodeVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.util.AliPayUtil;
import com.yt.app.common.util.RedisUtil;
import com.yt.app.common.util.StringUtil;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-21 14:30:58
 */
@Slf4j
@Service
public class QrcodeServiceImpl extends YtBaseServiceImpl<Qrcode, Long> implements QrcodeService {
	@Autowired
	private QrcodeMapper mapper;

	@Autowired
	private QrcodeaisleqrcodeMapper qrcodeaisleqrcodemapper;

	@Autowired
	private AlipayTradeService tradeService;

	@Override
	@Transactional
	public Integer post(Qrcode t) {
		t.setUserid(SysUserContext.getUserId());
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Qrcode> list(Map<String, Object> param) {
		List<Qrcode> list = mapper.list(param);
		return new YtPageBean<Qrcode>(list);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Qrcode get(Long id) {
		Qrcode t = mapper.get(id);
		return t;
	}

	@Override
	@Transactional
	public Integer delete(Long id) {
		// 删除关联表
		Integer i = mapper.delete(id);
		Assert.equals(i, 1, ServiceConstant.DELETE_FAIL_MSG);
		qrcodeaisleqrcodemapper.deleteByQrcodelId(id);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<QrcodeVO> page(Map<String, Object> param) {

		if (param.get("qrcodeaisleid") != null) {
			List<Qrcodeaisleqrcode> listmqas = qrcodeaisleqrcodemapper.getByQrcodeAisleId(Long.valueOf(param.get("qrcodeaisleid").toString()));
			if (listmqas.size() > 0) {
				long[] qraids = listmqas.stream().mapToLong(mqa -> mqa.getQrcodelid()).distinct().toArray();
				param.put("existids", qraids);
			}
		}

		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<QrcodeVO>(Collections.emptyList());
		}
		List<QrcodeVO> list = mapper.page(param);
		list.forEach(p -> {
			p.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + p.getType()));
		});
		return new YtPageBean<QrcodeVO>(param, list, count);
	}

	@Override
	public QrcodeVO paytest(Qrcode qv) {
		AlipayTradePrecreateResponse atp = alif2f(qv, qv.getBalance(), 10);
		Assert.notNull(atp, "获取支付宝单号错误!");
		QrcodeVO qrv = new QrcodeVO();
		qrv.setPayurl(atp.getQrCode());
		return qrv;
	}

	public AlipayTradePrecreateResponse alif2f(Qrcode qrcode, Double amount, Integer exp) {
		try {
			AlipayClient client = AliPayUtil.initAliPay(qrcode.getAppid(), qrcode.getAppprivatekey(), qrcode.getApppublickey(), qrcode.getAlipaypublickey(), qrcode.getAlipayprovatekey());
			AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
			AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
			model.setOutTradeNo(StringUtil.getOrderNum());
			model.setTotalAmount(amount.toString());
			model.setSubject(qrcode.getName() + StringUtil.getUUID());
			model.setTimeoutExpress(exp + "m");
			request.setBizModel(model);
			request.setNotifyUrl(qrcode.getNotifyurl());
			AlipayF2FPrecreateResult result = tradeService.tradePrecreate(request, client);
			switch (result.getTradeStatus()) {
			case SUCCESS:
				log.info("支付宝预下单成功: )");
				AlipayTradePrecreateResponse response = result.getResponse();
				log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
				log.info(response.getBody());
				return response;
			case FAILED:
				log.error("支付宝预下单失败!!!");
				break;
			case UNKNOWN:
				log.error("系统异常，预下单状态未知!!!");
				break;
			default:
				log.error("不支持的交易状态，交易返回异常!!!");
				break;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}
}