package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TronmemberMapper;
import com.yt.app.api.v1.service.TronmemberService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tronmember;
import com.yt.app.api.v1.vo.TronmemberVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.enums.YtDataSourceEnum;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
* @author zj default
* 
* @version v1
* @createdate2024-09-08 01:31:33
*/

@Service
public class TronmemberServiceImpl extends YtBaseServiceImpl<Tronmember, Long> implements TronmemberService{
@Autowired
private TronmemberMapper mapper;

@Override
@Transactional
public Integer post(Tronmember t) {
Integer i = mapper.post(t);
return i;
}

@Override
@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
public YtIPage<Tronmember> list(Map<String, Object> param) {
List<Tronmember> list = mapper.list(param);
return new YtPageBean<Tronmember>(list);
}

@Override
@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
public Tronmember get(Long id) {
Tronmember t = mapper.get(id);
return t;
}
@Override
@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
public YtIPage<TronmemberVO> page(Map<String, Object> param) {
int count = mapper.countlist(param);
if (count == 0) {
return new YtPageBean<TronmemberVO>(Collections.emptyList());
}
List<TronmemberVO> list = mapper.page(param);
return new YtPageBean<TronmemberVO>(param, list, count);
}
}