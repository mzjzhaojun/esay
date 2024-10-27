package com.yt.app.api.v1.controller;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import com.yt.app.common.common.yt.YtResponseEncryptEntity;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.yt.app.common.common.yt.YtRequestDecryptEntity;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtBody;
import com.yt.app.common.util.RequestUtil;

import com.yt.app.common.base.impl.YtBaseEncipherControllerImpl;
import com.yt.app.api.v1.service.TronService;
import com.yt.app.api.v1.entity.Tron;
import com.yt.app.api.v1.vo.TronVO;

/**
 * @author yyds
 * 
 * @version v1 @createdate2024-09-06 16:03:13
 */

@RestController
@RequestMapping("/rest/v1/tron")
public class TronController extends YtBaseEncipherControllerImpl<Tron, Long> {

	@Autowired
	private TronService service;

	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> page(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {
		YtIPage<TronVO> pagebean = service.page(RequestUtil.requestDecryptEntityToParamMap(requestEntity));
		return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));
	}

	@RequestMapping(value = "/wallet/getnodeinfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> getnodeinfo(HttpServletRequest request, HttpServletResponse response) {
		service.getnodeinfo();
		return new YtResponseEncryptEntity<Object>(new YtBody(1));
	}

	private static String privatekey = "63d99b74511082f06e3f5f4b6e02e663c9a43939525368060da19b704f2b9aa4";

	// # 手续费 1trx = 1000000 sun
	private static BigDecimal decimal = new BigDecimal("1000000");

	String owner_address = "TUrntwm5t9umKhC7jv89RXGo33qcTFAAAA";

	String to_address = "TNeVJQ1kN8NmNosDeXwYEb3D4Nep6h3eXU";

	/**
	 * 验证账号是否存在
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/wallet/validateaddress", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> validateaddress(HttpServletRequest request, HttpServletResponse response) {
		service.validateaddress(owner_address);
		return new YtResponseEncryptEntity<Object>(new YtBody(1));
	}

	/**
	 * 获取账号信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/wallet/getaccount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> getaccount(HttpServletRequest request, HttpServletResponse response) {
		service.getaccount(owner_address);
		return new YtResponseEncryptEntity<Object>(new YtBody(1));
	}

	/**
	 * 创建账号
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/wallet/createaccount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> createaccount(HttpServletRequest request, HttpServletResponse response) {
		service.createaccount(owner_address, to_address);
		return new YtResponseEncryptEntity<Object>(new YtBody(1));
	}

	/**
	 * trx交易
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/wallet/createtransaction", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> createtransaction(HttpServletRequest request, HttpServletResponse response) {
		BigDecimal amount = new BigDecimal("100");
		service.createtransaction(privatekey, to_address, owner_address, amount.multiply(decimal).toBigInteger());
		return new YtResponseEncryptEntity<Object>(new YtBody(1));
	}

	/**
	 * 查询账户资源
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/wallet/getaccountresource", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> getaccountresource(HttpServletRequest request, HttpServletResponse response) {
		service.getaccountresource(owner_address);
		return new YtResponseEncryptEntity<Object>(new YtBody(1));
	}

	/**
	 * 查询宽带信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/wallet/getaccountnet", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> getaccountnet(HttpServletRequest request, HttpServletResponse response) {
		service.getaccountnet(owner_address);
		return new YtResponseEncryptEntity<Object>(new YtBody(1));
	}

	/**
	 * 质押trx
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/wallet/freezebalancev2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> freezebalancev2(HttpServletRequest request, HttpServletResponse response) {
		service.freezebalancev2(privatekey, owner_address, new BigInteger("1000000000"), "ENERGY");
		return new YtResponseEncryptEntity<Object>(new YtBody(1));
	}

	/**
	 * 能量转给别的账户
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/wallet/delegateresource", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public YtResponseEncryptEntity<Object> delegateresource(HttpServletRequest request, HttpServletResponse response) {
		service.delegateresource(privatekey, owner_address, to_address, new BigInteger("1000000000"), "ENERGY", true, 1200);
		return new YtResponseEncryptEntity<Object>(new YtBody(1));
	}

}
