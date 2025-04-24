package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.yt.app.common.util.SelfPayUtil;

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
		// 测试代码
		long beginTime = System.currentTimeMillis();
//		SelfPayUtil.quickbuckle(null);
		long time = System.currentTimeMillis() - beginTime;
		System.out.println(">>>  Time " + time);
	}
}
