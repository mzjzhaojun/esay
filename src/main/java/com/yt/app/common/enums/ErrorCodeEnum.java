
package com.yt.app.common.enums;

/**
 * @ClassName: DataTypeEnum
 * @Description: TODO
 * @author: gaoyb
 * @date: 2016年12月12日 下午3:35:03
 */
public enum ErrorCodeEnum {

	SUCCESS(100000, "成功"), FAIL(2000, "失败"), PARAM_ERROR(1000, "参数错误"), CARD_CHARGE_PUSH_ERROR(1001, "充值卡扣费推送消息失败"),
	PRECHARGE_PUSH_ERROR(1002, "预存款扣费推送消息失败"), DHCARD_CHARGE_PUSH_ERROR(1003, "东汇卡扣费推送消息失败"),
	INSUFFICIENT_BALANCE_ERROR(1004, "余额不足扣费失败"), NOT_AUTO_CHARGE_ERROR(1005, "用户非自动扣费"),
	UPDATE_FREE_POSITION_ERROR(1006, "更新空余车位失败"), NOT_EXIST_PARKING_SN_ERROR(1007, "停车场编号不存在"),
	CONFIG_TIME_ERROR(1008, "重设时间错误"), IMG_NAME_NOT_EXIST_ERROR(1009, "图片名称不存在"), FILE_SAVE_ERROR(1010, "存储失败"),
	NOT_EXIST_PARAM_ERROR(1011, "部分参数不存在"), NOT_EXIST_ORDER_ERROR(1012, "订单不存在"), UPDATE_ORDER_ERROR(1013, "订单更新失败"),
	ORDER_CHARGE_ERROR(1014, "订单计费异常"), RENEW_TIME_SEGEMENT_ERROR(1015, "续费时间段异常"),
	MONTHLY_RENEW_ERROR(1016, "包月续费计费异常"), CARNUMBER_REPEAT_BIND(1017, "车牌已被当前用户绑定"),
	CARNUMBER_OTHER_ADD(1018, "车辆已被别人添加，（客户端处理申诉流程）"), CARNUMBER_MAX_ERROR(1019, "您最多只能添加3辆车"),
	CAR_BIND_ERROR(1020, "车牌绑定失败"), UPDATE_CAR_ERROR(1021, "更新数据失败"), QUERY_NO_CARNUMBER(1022, "查无此牌照"),
	CARNUMBER_REPEAT_AUTH(1023, "车牌已被当前用户申诉"), CARNUMBER_NOBIND_NOAUTH(1024, "该车牌未被绑定 无需申诉"),
	CARNUMBER_NOBIND(1025, "该车牌未被绑定 无需申诉"), CAR_ISAPPELAING(1026, "车辆正在被申诉"), CAR_ISAUTHING(1027, "车辆正在被认证"),
	DELETE_CAR_ERROR(1030, "车辆有未出停车,不能删除"), UPDATE_MEM_CAR_BIND_ERROR(1028, "只有已绑定未认证的车辆才能修改车辆信息"),
	CAR_INFO_IS_EMPTY(1029, "车辆为空"), CAR_NUMER_IS_NOT_EXIST(1031, "车牌号不能为空"), CAR_IS_BINDING(1032, "您的车辆在绑定中,请稍等"),
	CAR_IS_NOT_BIND(1033, "未绑定车辆"), CAR_NOT_ALLOWED_AUTH(1034, "只有未认证的车辆才能进行认证"), FEE_REFRESHED(1035, "费用已更新,请重新刷新费用"),
	QR_CODE_NOT_EXIST(1036, "二维码不存在"), QUERY_ALIPAY_CARNUMBER_ERROR(1037, "查询支付宝车牌失败"),
	ORDER_EXIT_ERROR(1038, "订单已存在异常，请用新的订单号提交"), MEMBER_ERROR(1200, "是会员"), NOT_MEMBER_ERROR(1300, "不是会员（该车牌未绑定会员）"),
	TOKEN_ERROR(1400, "Token校验值不正确"), HEADER_TOKEN_NOT_EXIST_ERROR(1401, "header中token未传值"),
	HEADER_SPARKING_NOT_EXIST_ERROR(1402, "header中parkingNo未传值"), SIGN_IN_ERROR(1500, "签到失败"),
	NOT_SIGN_ERROR(1501, "还未签到"), DEVICE_SIGN_ERROR(1502, "该设备已签到"), SIGN_OUT_ERROR(1600, "签退失败"),
	MEMBER_NOT_EXIST(3000, "会员不存在"), MEMBER_NOT_AVAILABLE(3001, "会员已经停用"), DUPLICATE_AUTO_PAY(3002, "重复的自动支付上报"),
	MEMBER_ACCOUNT_AMOUNT_ERROR(3003, "会员账本余额不足"), AUTO_PAY_ERROR(3004, "自动支付未设置"),
	AUTH_CODE_EXPIRED(3005, "验证码已经过期，请重新获取验证码"), AUTH_CODE_ERROR(3008, "验证码错误"), UPLOAD_FILE_ERROR(3006, "上传文件失败"),
	FILE_UPLOADED(3007, "文件已经上传"), USER_NAME_OR_PWD_ERROR(3009, "用户名或者密码不正确"), OPERATOR_ERROR(3010, "运营商异常"),
	API_SERVICE_CONFIG_ERROR(3011, "API服务未配置"), PARKING_AUTHCODE_ERROR(3012, "停车场授权码未配置"),
	PARKING_SIGN_ERROR(3013, "停车场sign签名校验失败"), USER_NAME_EXIST(3014, "该用户名已被使用"), USER_PHONE_EXIST(3015, "该手机号已被使用"),
	USER_NOT_EXIST(3016, "当前用户不存在"), ROLE_NAME_EXIST(3017, "角色名称已经存在"), ROLE_NOT_EXIST(3018, "角色不存在"),
	OLD_PASSWORD_ERROR(3019, "原密码错误"), AREA_NOT_AVAILABLE(3020, "区域不存在"), PARKING_NAME_EXIST(3021, "停车场名称已存在"),
	MERCHANT_NO_NOT_EXIST(3022, "商户不存在"), MERCHANT_SERVICE_NO_AUTH(3023, "商户无此api服务权限"),
	MERCHANT_SIGN_ERROR(3024, "验签失败"), AUTH_ERROR(3025, "鉴权失败"), REQUEST_HEADER_ERROR(3026, "请求头参数错误"),
	SERVER_ERROR(1700, "系统错误"), SERVER_TIMEOUT_ERROR(1800, "服务器异常，停车场请求服务端超过20秒无响应"),
	MEMBER_ACCOUNT_ITEM_NOT_EXIST(4000, "会员账本不存在"), PARKINGRECORD_NOT_EXIST(5000, "停车记录不存在"),
	MEMBERCOUPON_NOT_EXIST(6000, "用户优惠券不存在"), ORDER_PAY_PUSH_ERROR(7000, "订单支付推送失败");

	private Integer code;
	private String msg;

	private ErrorCodeEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static String getMsgByCode(Integer code) {
		String msg = "";
		ErrorCodeEnum[] array = ErrorCodeEnum.values();
		for (ErrorCodeEnum obj : array) {
			if (code.equals(obj.code)) {
				msg = obj.msg;
				break;
			}
		}
		return msg;
	}

}
