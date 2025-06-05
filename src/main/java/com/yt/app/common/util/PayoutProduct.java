package com.yt.app.common.util;

import com.yt.app.api.v1.entity.Channel;
import com.yt.app.api.v1.entity.Payout;
import com.yt.app.common.resource.DictionaryResource;

public class PayoutProduct {

	public static boolean getPayoutProduct(Payout t, Channel cl) {

		// 获取渠道单号
		boolean flage = true;
		switch (cl.getName()) {
		case DictionaryResource.DFHYAISLE:
			String orderflage = PayUtil.SendHYSubmit(t, cl);
			if (orderflage != null) {
				flage = false;
				t.setChannelordernum("outc" + StringUtil.getOrderNum());
			}
			break;
		case DictionaryResource.DF8GAISLE:
			String egordernum = PayUtil.Send8GSubmit(t, cl);
			if (egordernum != null) {
				flage = false;
				t.setChannelordernum(egordernum);
			}
			break;
		case DictionaryResource.DFQWAISLE:
			String qwordernum = PayUtil.SendQWSubmit(t, cl);
			if (qwordernum != null) {
				flage = false;
				t.setChannelordernum(qwordernum);
			}
			break;
		case DictionaryResource.DFXJAISLE:
			String xjordernum = PayUtil.SendXJSubmit(t, cl);
			if (xjordernum != null) {
				flage = false;
				t.setChannelordernum(xjordernum);
			}
			break;
		case DictionaryResource.DFHYTAISLE:
			String hytordernum = PayUtil.SendHYTSubmit(t, cl);
			if (hytordernum != null) {
				flage = false;
				t.setChannelordernum("outc" + StringUtil.getOrderNum());
			}
			break;
		case DictionaryResource.DFLJAISLE:
			String ljordernum = PayUtil.SendLJSubmit(t, cl);
			if (ljordernum != null) {
				flage = false;
				t.setChannelordernum(ljordernum);
			}
			break;
		case DictionaryResource.DFSXAISLE:
			String sxordernum = PayUtil.SendSXSubmit(t, cl);
			if (sxordernum != null) {
				flage = false;
				t.setChannelordernum(sxordernum);
			}
			break;
		case DictionaryResource.DFXRAISLE:
			String xrordernum = PayUtil.SendXRSubmit(t, cl);
			if (xrordernum != null) {
				flage = false;
				t.setChannelordernum(xrordernum);
			}
			break;
		case DictionaryResource.DFYSAISLE:
			String ysordernum = PayUtil.SendYSSubmit(t, cl);
			if (ysordernum != null) {
				flage = false;
				t.setChannelordernum(ysordernum);
			}
			break;
		case DictionaryResource.DFSNAISLE:
			String ordernum = PayUtil.SendSnSubmit(t, cl);
			if (ordernum != null) {
				flage = false;
				t.setChannelordernum(ordernum);
			}
			break;
		case DictionaryResource.DFSSAISLE:
			String ssordernum = PayUtil.SendSSSubmit(t, cl);
			if (ssordernum != null) {
				flage = false;
				t.setChannelordernum(ssordernum);
			}
			break;
		case DictionaryResource.DFTXAISLE:
			String txordernum = PayUtil.SendTxSubmit(t, cl);
			if (txordernum != null) {
				flage = false;
				t.setChannelordernum(txordernum);
			}
			break;
		}
		return flage;
	}
}
