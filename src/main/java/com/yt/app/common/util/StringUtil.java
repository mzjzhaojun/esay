package com.yt.app.common.util;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

/**
 * 字符串处理工具类
 * 
 */
public class StringUtil {

	/**
	 * 功能描述：判断是否为整数
	 * 
	 * @param str 传入的字符串
	 * @return 是整数返回true,否则返回false
	 */
	public static boolean isInteger(String str) {
		return str.matches("^[+-]?\\d+$");
	}

	/**
	 * 判断是否为浮点数，包括double和float
	 * 
	 * @param str 传入的字符串
	 * @return 是浮点数返回true,否则返回false
	 */
	public static boolean isDouble(String str) {
		return str.matches("^[+-]?\\d+(\\.\\d+$)?");
	}

	/**
	 * 字母数字下划线,并以字母开头
	 */
	public static boolean isSafe(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}
		return str.matches("[\\w]{4,}");
	}

	/**
	 * 功能描述：判断输入的字符串是否符合Email样式.
	 * 
	 * @param str 传入的字符串
	 * @return 是Email样式返回true,否则返回false
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.length() < 1 || email.length() > 256) {
			return false;
		}
		return email.matches("^[-+.\\w]+@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
	}

	/**
	 * 功能描述：判断输入的字符串是否为纯汉字
	 * 
	 * @param str 传入的字符窜
	 * @return 如果是纯汉字返回true,否则返回false
	 */
	public static boolean isChinese(String str) {
		return str.matches("^[\u0391-\uFFE5]+$");
	}

	/**
	 * 功能描述：判断是不是合法的手机号码
	 * 
	 * @param pn 手机号
	 * @return boolean
	 */
	public static boolean isPhoneNumber(String pn) {
		try {
			return pn.matches("^1[\\d]{10}$");

		} catch (RuntimeException e) {
			return false;
		}
	}

	/**
	 * 获取UUID的String值
	 * 
	 * @return UUID
	 */
	public synchronized static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	// 增加随机小数
	public static String getDouble(String str) {
		Random random = new Random();
		double randomnum = random.nextDouble() * (0.3 - 0.01) + 0.01;
		return String.format("%.2f", Double.valueOf(str) - randomnum);
	}

	// 增加随机小数
	public static String getInt(String str) {
		Random random = new Random();
		double randomnum = random.nextInt(10);
		randomnum = randomnum + random.nextDouble() * (0.90 - 0.01) + 0.01;
		return String.format("%.2f", Double.valueOf(str) - randomnum);
	}

	/**
	 * 获取count位随机十六进制字符串
	 * 
	 * @return
	 */
	public static String getNonceStr(int count) {
		return getHexString(NumberUtil.random((int) Math.pow(16, count)), count);
	}

	/**
	 * 获取十六进制数据
	 * 
	 * @param number 被转化的数据
	 * @param length 需转化为的长度,为0则不强制长度
	 * @return
	 */
	public static String getHexString(int number, int length) {
		String value = Integer.toHexString(number);
		return fullStringBefore(value, '0', length);
	}

	/**
	 * 在字符串前填充字符
	 * 
	 * @param src    原字符
	 * @param full   填充的字符
	 * @param length 总长度,为0则不强制长度
	 * @return 如("ff",'0',5),返回000ff,如("fffff",'0',3),返回fff
	 */
	public static String fullStringBefore(String src, char full, int length) {
		if (length > 0) {
			if (src.length() > length) {
				return src.substring(src.length() - length);
			} else if (src.length() < length) {
				return appendStringNotNull(null, getNString(full, length - src.length()), src);
			}
		}
		return src;
	}

	/**
	 * 在字符串后填充字符
	 * 
	 * @param src    原字符
	 * @param full   填充的字符
	 * @param length 总长度,为0则不强制长度
	 * @return 如("ff",'0',5),返回ff000,如("fffff",'0',3),返回fff
	 */
	public static String fullStringAfter(String src, char full, int length) {
		if (length > 0) {
			if (src.length() > length) {
				return src.substring(src.length() - length);
			} else if (src.length() < length) {
				return appendStringNotNull(null, src, getNString(full, length - src.length()));
			}
		}
		return src;
	}

	/**
	 * 获取N个相同的String组合的字符串
	 * 
	 * @param src
	 * @param count
	 * @return
	 */
	public static String getNString(char src, int count) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count; i++) {
			sb.append(src);
		}
		return sb.toString();
	}

	/**
	 * 获取N个相同的String组合的字符串
	 * 
	 * @param src
	 * @param count
	 * @return
	 */
	public static String getNString(String src, int count) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count; i++) {
			sb.append(src);
		}
		return sb.toString();
	}

	/**
	 * 寻找指定字符串并截取
	 * 
	 * @param str
	 * @param tar
	 * @param startIndex 开始寻找下标
	 * @return 返回{tar前的字符串,tar后的字符串},如str=offott,tar=o,startIndex=1,{"ff","tt"},
	 *         若未找到则返回{"",str}
	 */
	public static String[] indexOf(String str, String tar, int startIndex) {
		if (str != null) {
			int index = str.indexOf(tar, startIndex);
			if (index > 0) {
				return new String[] { str.substring(startIndex, index), str.substring(index + tar.length()) };
			}
		}
		return new String[] { "", str };
	}

	/**
	 * 判断字符串是否存在于content中
	 * 
	 * @param str
	 * @param content
	 * @return
	 */
	public static boolean inStrings(String str, String content) {
		StringBuffer sb = new StringBuffer();
		sb.append(".+,");
		sb.append(str);
		sb.append(",.+|^");
		sb.append(str);
		sb.append(",.+|^");
		sb.append(str);
		sb.append("$|,.+");
		sb.append(str);
		sb.append("$");
		return content.matches(sb.toString());
	}

	/**
	 * 执行substring方法,不会因为长度错误而异常
	 * 
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 */
	public static String subString(String str, int start, int end) {
		if (StringUtils.isNotBlank(str) && start < end) {
			if (str.length() > end) {
				return str.substring(start, end);
			} else {
				if (str.length() > start) {
					return str.substring(start);
				} else {
					return "";
				}
			}
		}
		return str;
	}

	/**
	 * 批量验证字符串是否不为空及空字符串
	 * 
	 * @param strings
	 * @return
	 */
	public static boolean checkNotEmpty(String... strings) {
		for (int i = 0; i < strings.length; i++) {
			if (StringUtils.isBlank(strings[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 拼接字符串
	 * 
	 * @param split   分隔符,如无则不分割
	 * @param strings
	 * @return
	 */
	public static String appendStringByObject(String split, Object... strings) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strings.length;) {
			sb.append(strings[i]);
			if (++i < strings.length && StringUtils.isNotEmpty(split)) {
				sb.append(split);
			}
		}
		return sb.toString();
	}

	/**
	 * 拼接字符串
	 * 
	 * @param split   分隔符,如无则不分割
	 * @param strings
	 * @return
	 */
	public static String appendStringNotNull(String split, String... strings) {
		if (checkNotEmpty(strings)) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < strings.length;) {
				sb.append(strings[i]);
				if (++i < strings.length && StringUtils.isNotEmpty(split)) {
					sb.append(split);
				}
			}
			return sb.toString();
		}
		return "";
	}

	/**
	 * 拼接字符串
	 * 
	 * @param split        分隔符,如无则不分割
	 * @param nullCharpter 当为null时,用于替换的字符
	 * @param strings
	 * @return
	 */
	public static String appendString(String split, String nullCharpter, String... strings) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strings.length;) {
			String str = strings[i];
			if (str != null) {
				sb.append(str);
			} else {
				sb.append(nullCharpter);
			}
			if (++i < strings.length && StringUtils.isNotEmpty(split)) {
				sb.append(split);
			}
		}
		return sb.toString();
	}

	/**
	 * 生成订单号
	 * 
	 * @return
	 */
	public static String getOrderNum() {
		String now = DateUtil.format("yyMMddHHmmssSSS", new Date());
		int num = NumberUtil.random(6);
		return now + num;
	}

	/**
	 * BeanName转为表名 即驼峰转表名
	 * 
	 * @param beanName
	 * @return
	 */
	public static String beanNameToTablleName(String beanName) {
		StringBuffer sb = new StringBuffer();
		for (char c : beanName.toCharArray()) {
			int code = (int) c;
			if (code >= 65 && code <= 90) {
				if (sb.length() > 0) {
					sb.append('_');
				}
				sb.append((char) (code + 32));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 校验字符串长度是否在 min 与 max 之间
	 * 
	 * @param str
	 * @param minLength
	 * @param maxLength
	 * @return
	 */
	public static boolean checkLength(String str, int minLength, int maxLength) {
		if (str == null) {
			return false;
		}
		if (str.length() >= minLength && str.length() <= maxLength) {
			return true;
		}
		return false;
	}

	/**
	 * 校验字符是否为 数字字母下划线组成的 6-20位字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isLoginName(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}
		return str.matches("^[A-Za-z0-9_]{6,20}$");
	}

	/**
	 * 随机生成指定长度的验证码
	 *
	 * @param length 验证码长度
	 * @return 生成的验证码
	 */
	public static String generateNumCode(int length) {
		String val = "";
		String charStr = "char";
		String numStr = "num";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			String charOrNum = random.nextInt(2) % 2 == 0 ? charStr : numStr;
			if (charStr.equalsIgnoreCase(charOrNum)) {
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (random.nextInt(26) + temp);
			} else if (numStr.equalsIgnoreCase(charOrNum)) {
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}

	private static final int SURNAME_PROBABILITY = 5;
	private static final Random RANDOM = new Random();

	private static final String FAMILY_ONE_NAME = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻水云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任" + "袁柳鲍史唐费岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计成戴宋茅庞熊纪舒屈项祝董粱杜阮"
			+ "席季麻强贾路娄危江童颜郭梅盛林刁钟徐邱骆高夏蔡田胡凌霍万柯卢莫房缪干解应宗丁宣邓郁单杭洪包诸左石崔吉龚程邢滑裴陆荣翁荀羊甄家封芮储靳邴" + "松井富乌焦巴弓牧隗山谷车侯伊宁仇祖武符刘景詹束龙叶幸司韶黎乔苍双闻莘劳逄姬冉宰桂牛寿通边燕冀尚农温庄晏瞿茹习鱼容向古戈终居衡步都耿满弘" + "国文东殴沃曾关红游盖益桓公晋楚闫";

	private static final String FAMILY_TWO_NAME = "欧阳太史端木上官司马东方独孤南宫万俟闻人夏侯诸葛尉迟公羊赫连澹台皇甫宗政濮阳公冶太叔申屠公孙慕容仲孙钟离长孙宇" + "文司徒鲜于司空闾丘子车亓官司寇巫马公西颛孙壤驷公良漆雕乐正宰父谷梁拓跋夹谷轩辕令狐段干百里呼延东郭南门羊舌微生公户公玉公仪梁丘公仲公上" + "公门公山公坚左丘公伯西门公祖第五公乘贯丘公皙南荣东里东宫仲长子书子桑即墨达奚褚师吴铭";

	public static int randomInt(int maxNum) {
		return RANDOM.nextInt(maxNum);
	}

	public static String getRandomBoyName() {
		String boyName = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达" + "安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽" + "晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘";
		int bodNameIndexOne = randomInt(boyName.length());
		int bodNameIndexTwo = randomInt(boyName.length());
		if (randomInt(100) > SURNAME_PROBABILITY) {
			int familyOneNameIndex = randomInt(FAMILY_ONE_NAME.length());
			return FAMILY_ONE_NAME.substring(familyOneNameIndex, familyOneNameIndex + 1) + boyName.substring(bodNameIndexOne, bodNameIndexOne + 1) + boyName.substring(bodNameIndexTwo, bodNameIndexTwo + 1);
		} else {
			int familyTwoNameIndex = randomInt(FAMILY_TWO_NAME.length());
			familyTwoNameIndex = familyTwoNameIndex % 2 == 0 ? familyTwoNameIndex : familyTwoNameIndex - 1;
			return FAMILY_TWO_NAME.substring(familyTwoNameIndex, familyTwoNameIndex + 2) + boyName.substring(bodNameIndexOne, bodNameIndexOne + 1) + boyName.substring(bodNameIndexTwo, bodNameIndexTwo + 1);
		}
	}

	public static void main(String[] args) {
		int i = 0;
		while (i < 1000) {
			System.out.println(getDouble("199.12"));
			i++;
		}
	}
}
