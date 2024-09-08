package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.TronrecordMapper;
import com.yt.app.api.v1.service.TronrecordService;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Tronrecord;
import com.yt.app.api.v1.vo.TronrecordVO;
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
public class TronrecordServiceImpl extends YtBaseServiceImpl<Tronrecord, Long> implements TronrecordService{
@Autowired
private TronrecordMapper mapper;

@Override
@Transactional
public Integer post(Tronrecord t) {
Integer i = mapper.post(t);
return i;
}

@Override
@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
public YtIPage<Tronrecord> list(Map<String, Object> param) {
List<Tronrecord> list = mapper.list(param);
return new YtPageBean<Tronrecord>(list);
}

@Override
@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
public Tronrecord get(Long id) {
Tronrecord t = mapper.get(id);
return t;
}
@Override
@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
public YtIPage<TronrecordVO> page(Map<String, Object> param) {
int count = mapper.countlist(param);
if (count == 0) {
return new YtPageBean<TronrecordVO>(Collections.emptyList());
}
List<TronrecordVO> list = mapper.page(param);
return new YtPageBean<TronrecordVO>(param, list, count);
}
}