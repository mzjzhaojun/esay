package com.yt.app.common.mybatis;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

import com.yt.app.common.base.context.TenantIdContext;
import com.yt.app.common.common.YtRoutingDataSource;
import com.yt.app.common.config.YtMysql;

import cn.hutool.core.lang.Assert;

import javax.sql.DataSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
@Configuration
@MapperScan(basePackages = { "com.yt.app.api.v1.mapper" })
public class MybatisPlusConfig implements TransactionManagementConfigurer {

	@Autowired
	YtMysql g;

	public DataSource dataSource1() {
		BasicDataSource localBasicDataSource = new BasicDataSource();
		localBasicDataSource.setDriverClassName(this.g.getSlavedriver());
		localBasicDataSource.setUsername(this.g.getSlaveuser());
		localBasicDataSource.setPassword(this.g.getSlavepassword());
		localBasicDataSource.setUrl(this.g.getSlavejdbcurl());
		localBasicDataSource.setMaxActive(this.g.getMaxActive());
		localBasicDataSource.setValidationQuery(this.g.getValidationQuery());
		localBasicDataSource.setTestOnBorrow(this.g.isTestOnBorrow());
		localBasicDataSource.setTestOnReturn(this.g.isTestOnReturn());
		localBasicDataSource.setTestWhileIdle(this.g.isTestWhileIdle());
		localBasicDataSource.setTimeBetweenEvictionRunsMillis(this.g.getTimeBetweenEvictionRunsMillis());
		localBasicDataSource.setMinEvictableIdleTimeMillis(this.g.getMinEictableIdleTimeMillis());
		localBasicDataSource.setPoolPreparedStatements(this.g.isPoolPreparedStatements());
		localBasicDataSource.setMaxOpenPreparedStatements(this.g.getMaxOpenPreparedStatements());
		return localBasicDataSource;
	}

	public DataSource dataSource2() {
		BasicDataSource localBasicDataSource = new BasicDataSource();
		localBasicDataSource.setDriverClassName(this.g.getMasterdriver());
		localBasicDataSource.setUsername(this.g.getMasteruser());
		localBasicDataSource.setPassword(this.g.getMasterpassword());
		localBasicDataSource.setUrl(this.g.getMasterjdbcurl());
		localBasicDataSource.setMaxActive(this.g.getMaxActive());
		localBasicDataSource.setValidationQuery(this.g.getValidationQuery());
		localBasicDataSource.setTestOnBorrow(this.g.isTestOnBorrow());
		localBasicDataSource.setTestOnReturn(this.g.isTestOnReturn());
		localBasicDataSource.setTestWhileIdle(this.g.isTestWhileIdle());
		localBasicDataSource.setTimeBetweenEvictionRunsMillis(this.g.getTimeBetweenEvictionRunsMillis());
		localBasicDataSource.setMinEvictableIdleTimeMillis(this.g.getMinEictableIdleTimeMillis());
		localBasicDataSource.setPoolPreparedStatements(this.g.isPoolPreparedStatements());
		localBasicDataSource.setMaxOpenPreparedStatements(this.g.getMaxOpenPreparedStatements());
		return localBasicDataSource;
	}

	@Bean(name = "SqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
		GlobalConfig globalConfig = new GlobalConfig();
		globalConfig.setMetaObjectHandler(new MyMetaObjectHandler());
		sqlSessionFactory.setGlobalConfig(globalConfig);
		sqlSessionFactory.setDataSource(ytabstractroutingdatasource());
		sqlSessionFactory.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactory.setMapperLocations(
				resolver.getResources("classpath:com/yt/app/api/" + g.getVersion() + "/mapper/impl/*.xml"));
		sqlSessionFactory.setPlugins(mybatisPlusInterceptor());
		return sqlSessionFactory.getObject();
	}

	@Bean(name = "SqlSessionTemplate")
	public SqlSessionTemplate sqlsessiontemplate(SqlSessionFactory paramSqlSessionFactory) {
		return new SqlSessionTemplate(paramSqlSessionFactory);
	}

	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(ytabstractroutingdatasource());
	}

	@Primary
	@Bean(name = "YtAbstractRoutingDataSource")
	public YtRoutingDataSource ytabstractroutingdatasource() {
		Map<String, DataSource> localHashMap1 = new HashMap<String, DataSource>();
		localHashMap1.put("slaveDataSource", dataSource1());
		Map<String, DataSource> localHashMap2 = new HashMap<String, DataSource>();
		localHashMap2.put("masterDataSource", dataSource2());
		return new YtRoutingDataSource(localHashMap1, localHashMap2, dataSource2());
	}

	/**
	 * 需要设置租户ID的表
	 */
	public static Set<String> TENANT_ID_TABLE = new HashSet<>();

	/**
	 * 需要逻辑删除的表
	 */
	public static Set<String> LOGIC_DELETE_TABLE = new HashSet<>();

	static {
//        TENANT_ID_TABLE.add("t_demo");
	}

	/**
	 * mybatis-plus分页插件 文档：https://baomidou.com/pages/2976a3/#spring-boot
	 */
	@Bean(name = "mybatisPlusInterceptor")
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
			@Override
			public Expression getTenantId() {
				return new LongValue(TenantIdContext.getTenantId());
			}

			@Override
			public String getTenantIdColumn() {
				return "tenant_id";
			}

			// 返回 true 表示此表不需要多租户
			@Override
			public boolean ignoreTable(String tableName) {
				if (!TENANT_ID_TABLE.contains(tableName)) {
					// 不需要租户id
					return true;
				}
				Boolean tenantIdFlag = TenantIdContext.getFlag();
				Assert.notNull(tenantIdFlag, "租户id不能为空！");
				boolean isIgnore = !tenantIdFlag;
				return isIgnore;
			}
		}));

		interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

		return interceptor;
	}
}