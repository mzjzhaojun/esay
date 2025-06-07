package test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.yt.app.common.util.SelfPayUtil;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@SpringBootTest
public class TestEfps {

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void getUserInfoById() {

		String URL = "http://192.168.110.231:8090";
		BigDecimal decimal = new BigDecimal("1000000");
		// 测试代码
		long beginTime = System.currentTimeMillis();

//		String sub_url = URL + "/wallet/getaccount";
//		String body = HttpRequest.post(sub_url).header("Content-Type", "application/json").body(JSONUtil.toJsonStr(map)).execute().body();
//		System.out.println(body);
//		BigInteger balance = BigInteger.ZERO;
//		JSONObject obj = JSONUtil.parseObj(body);
//		BigInteger b = obj.getBigInteger("balance");
//		if (b != null) {
//			balance = b;
//		}
//		System.out.println(Double.valueOf(new BigDecimal(balance).divide(decimal, 6, RoundingMode.FLOOR).toString()));

		boolean str = SelfPayUtil.generalRequest("6220212545854555123");
		System.out.println(str);
		long time = System.currentTimeMillis() - beginTime;
		System.out.println(">>>  Time " + time);
	}
}
