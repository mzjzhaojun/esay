package com.yt.app.common.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
public class YtRoutingDataSource extends AbstractRoutingDataSource {
	private static final ThreadLocal<String> y = new ThreadLocal<String>();
	private List<String> z;
	private List<String> A;
	private Map<String, DataSource> B;
	private Map<String, DataSource> C;

	public YtRoutingDataSource(Map<String, DataSource> paramMap1, Map<String, DataSource> paramMap2, Object paramObject) {
		a(paramMap1);
		b(paramMap2);
		setDefaultTargetDataSource(paramObject);
	}

	@Override
	protected Object determineCurrentLookupKey() {
		return y.get();
	}

	@Override
	public void afterPropertiesSet() {
		Map<Object, Object> localHashMap = new HashMap<Object, Object>();
		localHashMap.putAll(this.C);
		if (this.B != null) {
			localHashMap.putAll(this.B);
		}
		super.setTargetDataSources(localHashMap);
		super.afterPropertiesSet();
	}

	public void a(Map<String, DataSource> paramMap) {
		if ((paramMap == null) || (paramMap.size() == 0)) {
			return;
		}
		this.B = paramMap;
		this.z = new ArrayList<String>();
		for (Entry<String, DataSource> localEntry : paramMap.entrySet())
			this.z.add(localEntry.getKey());
	}

	public void b(Map<String, DataSource> paramMap) {
		if (paramMap == null)
			;
		this.C = paramMap;
		this.A = new ArrayList<String>();
		for (Entry<String, DataSource> localEntry : paramMap.entrySet())
			this.A.add(localEntry.getKey());
	}

	public void n() {
		if (y.get() != null)
			;
		String str = r();
		setDataSource(str);
	}

	public void o() {
		if (y.get() != null)
			;
		String str = s();
		setDataSource(str);
	}

	public void p() {
		y.remove();
	}

	public boolean q() {
		boolean bool = y.get() != null;
		return bool;
	}

	private String r() {
		if (this.B == null) {
			return s();
		}
		return (String) this.z.get(RandomUtils.nextInt(this.z.size()));
	}

	private String s() {
		String str = (String) this.A.get(RandomUtils.nextInt(this.A.size()));
		return str;
	}

	private void setDataSource(String paramString) {
		y.set(paramString);
	}
}