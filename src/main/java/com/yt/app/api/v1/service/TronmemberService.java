package com.yt.app.api.v1.service;

import java.util.Map;

import com.yt.app.api.v1.entity.Tronmember;
import com.yt.app.api.v1.vo.TronmemberVO;
import com.yt.app.common.base.YtIBaseService;
import com.yt.app.common.common.yt.YtIPage;
/**
* @author zj default
* 
* @version v1
* @createdate2024-09-08 01:31:33
*/

public interface TronmemberService extends YtIBaseService<Tronmember, Long>{
YtIPage<TronmemberVO> page(Map<String, Object> param);
}