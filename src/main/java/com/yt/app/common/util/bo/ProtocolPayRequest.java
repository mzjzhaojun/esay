package com.yt.app.common.util.bo;

import java.util.List;

/**
 * 协议支付请求
 * 
 * @author yjw
 *
 */
public class ProtocolPayRequest {

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 版本号
	 */
	private String version;
	/**
	 * 商户交易订单号
	 */
	private String outTradeNo;
	/**
	 * 客户编码
	 */
	private String customerCode;
	/**
	 * 会员编号
	 */
	private String memberId;
	/**
	 * 协议号
	 */
	private String protocol;

	private Long payAmount;
	/**
	 * 支付币种 CNY:人民币
	 */
	private String payCurrency;
	/**
	 * 异步通知地址
	 */
	private String notifyUrl;
	/**
	 * 商品订单信息
	 */
	private OrderInfo orderInfo;
	/**
	 * 附加信息
	 */
	private String attachData;
	private String transactionStartTime;
	private String transactionEndTime;

	private Boolean needSplit;
	/**
	 * 分账信息列表
	 */
	private List<SplitInfo> splitInfoList;
	/**
	 * 分账结果异步通知URL
	 */
	private String splitNotifyUrl;
	/**
	 * 分账的通知和查询接口中原样返回
	 */
	private String splitAttachData;

	private String nonceStr;

	/**
	 * 绑卡流水号
	 */
	private String smsNo;

	/**
	 * 委托方客户编号
	 */
	private String commissionedCustomerCode;

	/**
	 * 是否已开通交易
	 */
	private String isOpen;
	/**
	 * 短息验证码
	 */
	private String smsCode;

	/**
	 * 上送子商户号
	 */
	private String uploadMerchantNo;

	private String isSendSmsCode;

	public String getCommissionedCustomerCode() {
		return commissionedCustomerCode;
	}

	public void setCommissionedCustomerCode(String commissionedCustomerCode) {
		this.commissionedCustomerCode = commissionedCustomerCode;
	}

	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

	public String getUploadMerchantNo() {
		return uploadMerchantNo;
	}

	public void setUploadMerchantNo(String uploadMerchantNo) {
		this.uploadMerchantNo = uploadMerchantNo;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Long getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Long payAmount) {
		this.payAmount = payAmount;
	}

	public String getPayCurrency() {
		return payCurrency;
	}

	public void setPayCurrency(String payCurrency) {
		this.payCurrency = payCurrency;
	}

	public OrderInfo getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}

	public String getAttachData() {
		return attachData;
	}

	public void setAttachData(String attachData) {
		this.attachData = attachData;
	}

	public String getTransactionStartTime() {
		return transactionStartTime;
	}

	public void setTransactionStartTime(String transactionStartTime) {
		this.transactionStartTime = transactionStartTime;
	}

	public String getTransactionEndTime() {
		return transactionEndTime;
	}

	public void setTransactionEndTime(String transactionEndTime) {
		this.transactionEndTime = transactionEndTime;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public Boolean getNeedSplit() {
		return needSplit;
	}

	public void setNeedSplit(Boolean needSplit) {
		this.needSplit = needSplit;
	}

	public List<SplitInfo> getSplitInfoList() {
		return splitInfoList;
	}

	public void setSplitInfoList(List<SplitInfo> splitInfoList) {
		this.splitInfoList = splitInfoList;
	}

	public String getSplitNotifyUrl() {
		return splitNotifyUrl;
	}

	public void setSplitNotifyUrl(String splitNotifyUrl) {
		this.splitNotifyUrl = splitNotifyUrl;
	}

	public String getSplitAttachData() {
		return splitAttachData;
	}

	public void setSplitAttachData(String splitAttachData) {
		this.splitAttachData = splitAttachData;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getSmsNo() {
		return smsNo;
	}

	public void setSmsNo(String smsNo) {
		this.smsNo = smsNo;
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	@Override
	public String toString() {
		return "ProtocolPayRequest [outTradeNo=" + outTradeNo + ", customerCode=" + customerCode + ", memberId=" + memberId + ", payAmount=" + payAmount + ", payCurrency=" + payCurrency + ", orderInfo=" + orderInfo + ", attachData=" + attachData
				+ ", transactionStartTime=" + transactionStartTime + ", transactionEndTime=" + transactionEndTime + ", protocol=" + protocol + ", needSplit=" + needSplit + ", splitInfoList=" + splitInfoList + ", splitNotifyUrl=" + splitNotifyUrl
				+ ", splitAttachData=" + splitAttachData + ", nonceStr=" + nonceStr + "]";
	}

	public String getIsSendSmsCode() {
		return isSendSmsCode;
	}

	public void setIsSendSmsCode(String isSendSmsCode) {
		this.isSendSmsCode = isSendSmsCode;
	}
}
