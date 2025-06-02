package com.yt.app.common.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.yt.app.common.base.constant.SystemConstant;

/**
 * 数字工具类
 * 
 *
 */
public class NumberUtil {

	private static long orderNum = 0l;
	private static String date;

	/**
	 * 获取一个不大于max的整数
	 * 
	 * @param max
	 * @return
	 */
	public static long random(long max) {
		return (long) (Math.random() * max);
	}

	public static int random(int max) {
		return (int) (Math.random() * max);
	}

	public static BigDecimal makeRandom(Double max, Double min, int scale) {
		BigDecimal cha = new BigDecimal(Math.random() * (max - min) + min);
		return cha.setScale(scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 格式化金额 两位小数
	 * 
	 * @param money
	 * @param scale
	 * @return
	 */
	public static BigDecimal round(BigDecimal money, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
		}
		BigDecimal one = new BigDecimal("1");
		return money.divide(one, scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 百分比
	 * 
	 * @param money
	 * @param scale
	 * @return
	 */
	public static BigDecimal multiply(String value, String percentage, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
		}
		BigDecimal one = new BigDecimal(value);
		BigDecimal two = new BigDecimal(percentage);
		return one.multiply(two).setScale(scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 
	 * @param number   被转换的String
	 * @param digits   小数位数
	 * @param fullZero 是否在小数位补0
	 * @return
	 */
	public static String decimal(String number, int digits, boolean fullZero) {
		int index = number.indexOf(".");
		int endWith = 0;
		String value = number;
		if (index > 0) {
			if (digits == 0) {
				endWith = index;
			} else {
				endWith = index + 1 + digits;
			}
			if (endWith != value.length()) {
				value = StringUtil.subString(number, 0, endWith);
			}
			if (fullZero) {
				value = StringUtil.fullStringAfter(value, '0', endWith);
			}
		} else if (fullZero) {
			if (digits > 0) {
				value = StringUtil.appendStringNotNull(".", value, StringUtil.getNString('0', digits));
			}
		}
		return value;
	}

	/**
	 * 生成订单编号
	 * 
	 * @return
	 */
	public static synchronized String getOrderNo() {
		String str = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
		if (date == null || !date.equals(str)) {
			date = str;
			orderNum = 0l;
		}
		orderNum++;
		long orderNo = Long.parseLong((date)) * 10000;
		orderNo += orderNum;
		return orderNo + "";
	}

	/**
	 * 获取4位随机验证码
	 * 
	 * @return
	 */
	public static String getValidationCodeFour() {
		return String.valueOf((new Random().nextInt(8999) + 100000));
	}

	/**
	 * 获取6位随机验证码
	 * 
	 * @return
	 */
	public static String getValidationCodeSix() {
		return String.valueOf((new Random().nextInt(899999) + 100000));
	}

	/**
	 * 获取流水编号
	 * 
	 * @return
	 */
	public static String getRunningCode() {
		String code = String.valueOf(new Date().getTime());
		return String.valueOf(Long.parseLong(code.substring(code.length() - 8)) + 10000000); // 流水编号
	}

	public static Double getIncomeFewAmount(Long id) {
		Double min = 0.01;
		for (int i = 1; i <= 30; i++) {
			String key = SystemConstant.CACHE_SYS_QRCODE + id + "" + min;
			if (!RedisUtil.hasKey(key)) {
				RedisUtil.set(key, min.toString());
				return min;
			} else {
				min = min + 0.01;
			}
		}
		min = 10.00;
		return min;
	}

	public static Double getExchangeFewAmount() {
		Double min = 0.001;
		for (int i = 1; i <= 100; i++) {
			String key = SystemConstant.CACHE_SYS_EXCHANGETRX + min;
			if (!RedisUtil.hasKey(key)) {
				RedisUtil.set(key, min.toString());
				return min;
			} else {
				min = min + 0.001;
			}
		}
		min = 10.00;
		return min;
	}

	public static void removeExchangeFewAmount(Double min) {
		String key = SystemConstant.CACHE_SYS_EXCHANGETRX + min;
		if (RedisUtil.hasKey(key)) {
			RedisUtil.delete(key);
		}
	}

	/**
	 * @param start 数值的起范围
	 * @param end   数值的止范围
	 * @return 一个在这个范围内的整数值
	 */
	public static int randomInt(int start, int end) {
		int rtnn = new Long(start + (long) (Math.random() * (end - start))).intValue();
		if (rtnn == start || rtnn == end) {
			return randomInt(start, end);
		}
		return rtnn;
	}

	public static void main(String args[]) {
		Random random = new Random();
		double randomnum = random.nextInt(10);
		randomnum = randomnum + random.nextDouble() * (0.90 - 0.01) + 0.01;
		System.out.println(randomnum);
	}
}
