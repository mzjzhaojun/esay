package com.yt.app.api.v1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.yt.app.api.v1.mapper.IncomeMapper;
import com.yt.app.api.v1.mapper.SystemaccountMapper;
import com.yt.app.api.v1.mapper.SystemstatisticalreportsMapper;
import com.yt.app.api.v1.service.SystemstatisticalreportsService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.BaseConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Systemaccount;
import com.yt.app.api.v1.entity.Systemstatisticalreports;
import com.yt.app.api.v1.vo.IncomeVO;
import com.yt.app.api.v1.vo.SystemstatisticalreportsVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-09-02 14:45:16
 */

@Service
public class SystemstatisticalreportsServiceImpl extends YtBaseServiceImpl<Systemstatisticalreports, Long> implements SystemstatisticalreportsService {
	@Autowired
	private SystemstatisticalreportsMapper mapper;

	@Autowired
	private SystemaccountMapper systemaccountmapper;

	@Autowired
	private IncomeMapper incomemapper;

	@Override
	@Transactional
	public Integer post(Systemstatisticalreports t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Systemstatisticalreports> list(Map<String, Object> param) {
		List<Systemstatisticalreports> list = mapper.list(param);
		return new YtPageBean<Systemstatisticalreports>(list);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Systemstatisticalreports get(Long id) {
		Systemstatisticalreports t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<SystemstatisticalreportsVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<SystemstatisticalreportsVO>(Collections.emptyList());
		}
		List<SystemstatisticalreportsVO> list = mapper.page(param);
		return new YtPageBean<SystemstatisticalreportsVO>(param, list, count);
	}

	@Override
	@Transactional
	public void updateDayValue(String date) {
		Systemaccount sa = systemaccountmapper.getByTenantId(BaseConstant.FEITU_TENANT_ID);
		Systemstatisticalreports t = new Systemstatisticalreports();
		t.setDateval(date);
		
		t.setIncomecount(sa.getTotalincome());
		// 查询每日统计数据
		IncomeVO imaov = incomemapper.countOrder(date);
		t.setTodayorder(imaov.getOrdercount());
		t.setTodayorderamount(imaov.getAmount());
		t.setTodaysuccessorderamount(imaov.getIncomeamount());

		IncomeVO imaovsuccess = incomemapper.countSuccessOrder(date);
		t.setSuccessorder(imaovsuccess.getOrdercount());
		t.setIncomeuserpaycount(imaovsuccess.getAmount());
		t.setIncomeuserpaysuccesscount(imaovsuccess.getIncomeamount());
		t.setTodayincome(imaovsuccess.getIncomeamount());
		try {
			t.setPayoutrate(Double.valueOf((t.getSuccessorder() / (t.getTodayorder() / 100))));
		} catch (Exception e) {
			t.setPayoutrate(0.0);
		}

		mapper.post(t);
	}
}