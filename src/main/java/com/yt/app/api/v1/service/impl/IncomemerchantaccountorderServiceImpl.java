package com.yt.app.api.v1.service.impl;

import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.yt.app.api.v1.mapper.IncomemerchantaccountorderMapper;
import com.yt.app.api.v1.service.IncomemerchantaccountorderService;
import com.yt.app.common.annotation.YtDataSourceAnnotation;
import com.yt.app.common.base.constant.SystemConstant;
import com.yt.app.common.base.impl.YtBaseServiceImpl;
import com.yt.app.api.v1.entity.Incomemerchantaccountorder;
import com.yt.app.api.v1.vo.IncomemerchantaccountorderVO;
import com.yt.app.common.common.yt.YtIPage;
import com.yt.app.common.common.yt.YtPageBean;
import com.yt.app.common.enums.YtDataSourceEnum;
import com.yt.app.common.util.DateTimeUtil;
import com.yt.app.common.util.RedisUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zj default
 * 
 * @version v1 @createdate2024-08-23 23:31:35
 */

@Service
public class IncomemerchantaccountorderServiceImpl extends YtBaseServiceImpl<Incomemerchantaccountorder, Long> implements IncomemerchantaccountorderService {
	@Autowired
	private IncomemerchantaccountorderMapper mapper;

	@Override
	@Transactional
	public Integer post(Incomemerchantaccountorder t) {
		Integer i = mapper.post(t);
		return i;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<Incomemerchantaccountorder> list(Map<String, Object> param) {
		List<Incomemerchantaccountorder> list = mapper.list(param);
		return new YtPageBean<Incomemerchantaccountorder>(list);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public Incomemerchantaccountorder get(Long id) {
		Incomemerchantaccountorder t = mapper.get(id);
		return t;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public YtIPage<IncomemerchantaccountorderVO> page(Map<String, Object> param) {
		int count = mapper.countlist(param);
		if (count == 0) {
			return new YtPageBean<IncomemerchantaccountorderVO>(Collections.emptyList());
		}
		List<IncomemerchantaccountorderVO> list = mapper.page(param);
		list.forEach(mco -> {
			mco.setStatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getStatus()));
			mco.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getType()));
		});
		return new YtPageBean<IncomemerchantaccountorderVO>(param, list, count);
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public ByteArrayOutputStream download(Map<String, Object> param) throws IOException {
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		SXSSFSheet sheet = workbook.createSheet("Sheet");
		List<IncomemerchantaccountorderVO> list = mapper.page(param);
		list.forEach(mco -> {
			mco.setStatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getStatus()));
			mco.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getType()));
		});
		SXSSFRow titleRow = sheet.createRow(0);
		SXSSFCell titleCell0 = titleRow.createCell(0);
		titleCell0.setCellValue("编号");
		SXSSFCell titleCell1 = titleRow.createCell(1);
		titleCell1.setCellValue("系统单号");
		SXSSFCell titleCell2 = titleRow.createCell(2);
		titleCell2.setCellValue("通道名称");
		SXSSFCell titleCell3 = titleRow.createCell(3);
		titleCell3.setCellValue("通道编码");
		SXSSFCell titleCell4 = titleRow.createCell(4);
		titleCell4.setCellValue("收款类型");
		SXSSFCell titleCell5 = titleRow.createCell(5);
		titleCell5.setCellValue("用户支付");
		SXSSFCell titleCell6 = titleRow.createCell(6);
		titleCell6.setCellValue("缺口金额");
		SXSSFCell titleCell7 = titleRow.createCell(7);
		titleCell7.setCellValue("收入金额");
		SXSSFCell titleCell8 = titleRow.createCell(8);
		titleCell8.setCellValue("订单状态");
		SXSSFCell titleCell9 = titleRow.createCell(9);
		titleCell9.setCellValue("创建时间");
		// 填充数据
		for (int i = 0; i < list.size(); i++) {
			IncomemerchantaccountorderVO imao = list.get(i);
			SXSSFRow row = sheet.createRow(i + 1);
			SXSSFCell cell0 = row.createCell(0);
			cell0.setCellValue(i + 1);
			SXSSFCell cell1 = row.createCell(1);
			cell1.setCellValue(imao.getOrdernum());
			SXSSFCell cell2 = row.createCell(2);
			cell2.setCellValue(imao.getQrcodeaislename());
			SXSSFCell cell3 = row.createCell(3);
			cell3.setCellValue(imao.getQrcodeaislecode());
			SXSSFCell cell4 = row.createCell(4);
			cell4.setCellValue(imao.getTypename());
			SXSSFCell cell5 = row.createCell(5);
			cell5.setCellValue(imao.getRealamount());
			SXSSFCell cell6 = row.createCell(6);
			cell6.setCellValue(imao.getFewamount());
			SXSSFCell cell7 = row.createCell(7);
			cell7.setCellValue(imao.getIncomeamount());
			SXSSFCell cell8 = row.createCell(8);
			cell8.setCellValue(imao.getStatusname());
			SXSSFCell cell9 = row.createCell(9);
			cell9.setCellValue(DateTimeUtil.getDateTime(imao.getCreate_time(), DateTimeUtil.DEFAULT_DATETIME_FORMAT));
		}
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();
		return outputStream;
	}

	@Override
	@YtDataSourceAnnotation(datasource = YtDataSourceEnum.SLAVE)
	public ByteArrayOutputStream reconciliation(Map<String, Object> param) throws IOException {
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		SXSSFSheet sheet = workbook.createSheet("Sheet");
		List<IncomemerchantaccountorderVO> list = mapper.page(param);
		list.forEach(mco -> {
			mco.setStatusname(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getStatus()));
			mco.setTypename(RedisUtil.get(SystemConstant.CACHE_SYS_DICT_PREFIX + mco.getType()));
		});
		SXSSFRow titleRow = sheet.createRow(0);
		SXSSFCell titleCell0 = titleRow.createCell(0);
		titleCell0.setCellValue("编号");
		SXSSFCell titleCell1 = titleRow.createCell(1);
		titleCell1.setCellValue("系统单号");
		SXSSFCell titleCell2 = titleRow.createCell(2);
		titleCell2.setCellValue("通道名称");
		SXSSFCell titleCell3 = titleRow.createCell(3);
		titleCell3.setCellValue("通道编码");
		SXSSFCell titleCell4 = titleRow.createCell(4);
		titleCell4.setCellValue("收款类型");
		SXSSFCell titleCell5 = titleRow.createCell(5);
		titleCell5.setCellValue("用户支付");
		SXSSFCell titleCell6 = titleRow.createCell(6);
		titleCell6.setCellValue("缺口金额");
		SXSSFCell titleCell7 = titleRow.createCell(7);
		titleCell7.setCellValue("收入金额");
		SXSSFCell titleCell8 = titleRow.createCell(8);
		titleCell8.setCellValue("订单状态");
		SXSSFCell titleCell9 = titleRow.createCell(9);
		titleCell9.setCellValue("创建时间");
		// 填充数据
		for (int i = 0; i < list.size(); i++) {
			IncomemerchantaccountorderVO imao = list.get(i);
			SXSSFRow row = sheet.createRow(i + 1);
			SXSSFCell cell0 = row.createCell(0);
			cell0.setCellValue(i + 1);
			SXSSFCell cell1 = row.createCell(1);
			cell1.setCellValue(imao.getOrdernum());
			SXSSFCell cell2 = row.createCell(2);
			cell2.setCellValue(imao.getQrcodeaislename());
			SXSSFCell cell3 = row.createCell(3);
			cell3.setCellValue(imao.getQrcodeaislecode());
			SXSSFCell cell4 = row.createCell(4);
			cell4.setCellValue(imao.getTypename());
			SXSSFCell cell5 = row.createCell(5);
			cell5.setCellValue(imao.getRealamount());
			SXSSFCell cell6 = row.createCell(6);
			cell6.setCellValue(imao.getFewamount());
			SXSSFCell cell7 = row.createCell(7);
			cell7.setCellValue(imao.getIncomeamount());
			SXSSFCell cell8 = row.createCell(8);
			cell8.setCellValue(imao.getStatusname());
			SXSSFCell cell9 = row.createCell(9);
			cell9.setCellValue(DateTimeUtil.getDateTime(imao.getCreate_time(), DateTimeUtil.DEFAULT_DATETIME_FORMAT));
		}
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();
		return outputStream;
	}
}