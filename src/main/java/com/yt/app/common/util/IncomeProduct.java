package com.yt.app.common.util;

import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Income;
import com.yt.app.api.v1.vo.SysFcOrder;
import com.yt.app.api.v1.vo.SysFhOrder;
import com.yt.app.api.v1.vo.SysGzOrder;
import com.yt.app.api.v1.vo.SysHsOrder;
import com.yt.app.api.v1.vo.SysRblOrder;
import com.yt.app.api.v1.vo.SysTdOrder;
import com.yt.app.api.v1.vo.SysWdOrder;
import com.yt.app.api.v1.vo.SysWjOrder;
import com.yt.app.api.v1.vo.SysXSOrder;
import com.yt.app.api.v1.vo.SysYSOrder;
import com.yt.app.common.resource.DictionaryResource;

import cn.hutool.json.JSONObject;

public class IncomeProduct {

	public static Income getIncomeProduct(Income income, Channel channel) {

		switch (channel.getName()) {
		case DictionaryResource.ALISAISLE:
			JSONObject dataali = PayUtil.SendALiSubmit(income, channel);
			if (dataali != null) {
				income.setResulturl(dataali.getStr("payurl"));
				income.setQrcodeordernum(dataali.getStr("sysorderno"));
			} else {
				return null;
			}
			break;
		case DictionaryResource.ONEPLUSAISLE:
			JSONObject dataoneplus = PayUtil.SendOnePlusSubmit(income, channel);
			if (dataoneplus != null) {
				income.setResulturl(dataoneplus.getStr("payData"));
				income.setQrcodeordernum(dataoneplus.getStr("payOrderId"));
			} else {
				return null;
			}
			break;
		case DictionaryResource.TONGYUSNAISLE:
			JSONObject data = PayUtil.SendTongYuanSubmit(income, channel);
			if (data != null) {
				income.setResulturl(data.getStr("payData"));
				income.setQrcodeordernum(data.getStr("payOrderId"));
			} else {
				return null;
			}
			break;
		case DictionaryResource.ZSAISLE:
			JSONObject zsjz = PayUtil.SendZSSubmit(income, channel);
			if (zsjz != null) {
				income.setResulturl(zsjz.getJSONObject("PayeeInfo").getStr("CashUrl"));
				income.setQrcodeordernum(zsjz.getStr("OrderNo"));
			} else {
				return null;
			}
			break;
		case DictionaryResource.XSAISLE:
			SysXSOrder xsjz = PayUtil.SendXSSubmit(income, channel);
			if (xsjz != null) {
				income.setResulturl(xsjz.getResult().getQrCode());
				income.setQrcodeordernum(xsjz.getResult().getOrderCode());
			} else {
				return null;
			}
			break;
		case DictionaryResource.YSAISLE:
			SysYSOrder syjz = PayUtil.SendYSSubmit(income, channel);
			if (syjz != null) {
				income.setResulturl(syjz.getPayParams().getPayUrl());
				income.setQrcodeordernum(syjz.getPayOrderId());
			} else {
				return null;
			}
			break;
		case DictionaryResource.FHLAISLE:
			SysFhOrder sfh = PayUtil.SendFhSubmit(income, channel);
			if (sfh != null) {
				income.setResulturl(sfh.getPayParams().getPayJumpUrl());
				income.setQrcodeordernum(sfh.getPayOrderId());
			} else {
				return null;
			}
			break;
		case DictionaryResource.EGAISLE:
			SysTdOrder seg = PayUtil.SendEgSubmit(income, channel);
			if (seg != null) {
				income.setResulturl(seg.getData().getPay_url());
			} else {
				return null;
			}
			break;
		case DictionaryResource.KFAISLE:
			SysHsOrder sho = PayUtil.SendKFSubmit(income, channel);
			if (sho != null) {
				income.setResulturl(sho.getPay_url());
				income.setQrcodeordernum(sho.getSys_order_no());
			} else {
				return null;
			}
			break;
		case DictionaryResource.WDAISLE:
			SysWdOrder wd = PayUtil.SendWdSubmit(income, channel);
			if (wd != null) {
				income.setResulturl(wd.getData().getPayData());
				income.setQrcodeordernum(wd.getData().getPayOrderId());
			} else {
				return null;
			}
			break;
		case DictionaryResource.RBLAISLE:
			SysRblOrder rbl = PayUtil.SendRblSubmit(income, channel);
			if (rbl != null) {
				income.setResulturl(rbl.getData().getPayUrl());
				income.setQrcodeordernum(rbl.getData().getTradeNo());
			} else {
				return null;
			}
			break;
		case DictionaryResource.GZAISLE:
			SysGzOrder gz = PayUtil.SendGzSubmit(income, channel);
			if (gz != null) {
				income.setResulturl(gz.getData().getPayUrl());
			} else {
				return null;
			}
			break;
		case DictionaryResource.WJAISLE:
			SysWjOrder wj = PayUtil.SendWjSubmit(income, channel);
			if (wj != null) {
				income.setResulturl(wj.getData().getPayData());
				income.setQrcodeordernum(wj.getData().getPayOrderId());
			} else {
				return null;
			}
			break;
		case DictionaryResource.FCAISLE:
			SysFcOrder fc = PayUtil.SendFcSubmit(income, channel);
			if (fc != null) {
				income.setResulturl(fc.getData().getPayUrl());
				income.setQrcodeordernum(fc.getData().getTradeNo());
			} else {
				return null;
			}
			break;
		case DictionaryResource.AKLAISLE:
			SysFcOrder akl = PayUtil.SendAklSubmit(income, channel);
			if (akl != null) {
				income.setResulturl(akl.getData().getPayUrl());
				income.setQrcodeordernum(akl.getData().getTradeNo());
			} else {
				return null;
			}
			break;
		}
		return income;
	}
}
