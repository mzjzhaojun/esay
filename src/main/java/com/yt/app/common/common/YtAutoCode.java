
package com.yt.app.common.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.yt.app.common.util.DateTimeUtil;
import com.yt.app.common.util.DbConnectionUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
@Slf4j
public class YtAutoCode {

	File file;
	FileWriter K;
	BufferedWriter L;
	private DbConnectionUtil M = DbConnectionUtil.getInstance();

	private YtAutoCode() {
	}

	public List<String> getTables() {
		List<String> localArrayList = new ArrayList<String>();
		try {
			Connection localConnection = this.M.getCon();
			Statement localStatement = localConnection.createStatement(1004, 1007);
			ResultSet localResultSet = null;
			localResultSet = localStatement.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '" + DbConnectionUtil.dbName + "'");
			while (localResultSet.next()) {
				localArrayList.add(localResultSet.getString("TABLE_NAME"));
			}
			localResultSet.close();
		} catch (SQLException localSQLException) {
			localSQLException.printStackTrace();
		}
		return localArrayList;
	}

	public void p(List<String> tables) {
		this.p(tables, true);
	}

	public void p(List<String> tables, boolean code) {
		this.p(tables, code, false, false, "");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void p(List<String> tables, boolean code, boolean html, boolean data, String sysid) {
		try {
			File localFile1 = new File("");
			DbConnectionUtil.basePage = "com.yt.app.api.v1";
			DbConnectionUtil.commndPage = "com.yt.app.common.base";
			DbConnectionUtil.filePath = localFile1.getCanonicalPath() + "/src/main/java/com/yt/app/api/v1";
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			Connection conn = this.M.getCon();
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = null;
			if (tables == null || tables.size() == 0)
				return;
			List<String[]> db2java = new ArrayList<String[]>();
			db2java.add(new String[] { "bit", "Boolean" });
			db2java.add(new String[] { "varchar", "String" });
			db2java.add(new String[] { "numeric", "Double" });
			db2java.add(new String[] { "double", "Double" });
			db2java.add(new String[] { "int", "Integer" });
			db2java.add(new String[] { "datetime", "java.util.Date" });
			db2java.add(new String[] { "date", "java.util.Date" });
			db2java.add(new String[] { "tinyint", "Integer" });
			db2java.add(new String[] { "smallint", "Integer" });
			db2java.add(new String[] { "bigint", "Long" });
			db2java.add(new String[] { "char", "String" });
			db2java.add(new String[] { "float", "float" });
			for (int ti = 0; ti < tables.size(); ti++) {
				String tb = tables.get(ti);
				log.info("========>>>>>>：" + tb);
				String tn = "";
				if (tb.indexOf("_") != -1) {
					String tns = tb.substring(tb.indexOf("_") + 1).toLowerCase();
					tn = tb.substring(0, tb.indexOf("_")) + tns.substring(0, 1).toUpperCase() + tns.substring(1);
					tn = tn.replaceAll("_", "");
				} else {
					tn = tb;
				}
				b(tn);
				HashMap[] r = (HashMap[]) null;
				int iRowNum;
				r = (HashMap[]) null;
				iRowNum = 0;
				int iColCnt = 0;
				rs = stmt.executeQuery("select column_name,data_type,column_comment,character_maximum_length from information_schema.columns where table_schema = '" + DbConnectionUtil.dbName + "' and table_name='" + tb + "'");
				ResultSetMetaData MetaData = rs.getMetaData();
				iColCnt = MetaData.getColumnCount();
				if (rs.next()) {
					rs.last();
					r = new HashMap[rs.getRow()];
					rs.beforeFirst();
				} else {
					log.info("table not exist");
					return;
				}

				while (rs.next()) {
					r[iRowNum] = new HashMap();
					for (int j = 0; j < iColCnt; j++) {
						String szColName = MetaData.getColumnName(j + 1);
						String szColValue = rs.getString(szColName);
						if (szColValue == null)
							szColValue = "";
						r[iRowNum].put(szColName, szColValue);
					}
					iRowNum++;
				}
				rs.close();
				String bt = tn.substring(0, 1).toUpperCase() + tn.substring(1);
				String[][] ts = new String[r.length][4];
				String data_type = "DATA_TYPE";
				String column_name = "COLUMN_NAME";
				String column_comment = "COLUMN_COMMENT";
				String character_maximum_length = "CHARACTER_MAXIMUM_LENGTH";
				for (int i = 0; i < r.length; i++) {
					ts[i][0] = "Object";
					for (String[] temp : db2java)
						if (r[i].get(data_type).equals(temp[0]))
							ts[i][0] = temp[1];
					ts[i][1] = r[i].get(column_name).toString().toLowerCase();
					ts[i][2] = r[i].get(column_comment).toString();
					ts[i][3] = r[i].get(character_maximum_length).toString();
				}
				if (code) {
					file = g(bt);
					this.K = new FileWriter(file);
					this.L = new BufferedWriter(this.K);
					this.L.write("package " + DbConnectionUtil.basePage + ".entity;\r\n\n");
					this.L.write("import lombok.AllArgsConstructor;\r\n");
					this.L.write("import lombok.Data;\r\n");
					this.L.write("import lombok.NoArgsConstructor;\r\n");
					this.L.write("import lombok.Builder;\r\n");
					this.L.write("import lombok.EqualsAndHashCode;\r\n");
					this.L.write("\r\n");
					this.L.write("import com.yt.app.common.base.YtBaseEntity;\r\n");
					this.L.write("/**\r\n");
					this.L.write("* @author zj default\r\n");
					this.L.write("* \r\n");
					this.L.write("* @version " + DbConnectionUtil.version + "\r\n");
					this.L.write("* @createdate" + DateTimeUtil.getDateTime() + "\r\n");
					this.L.write("*/\r\n");
					this.L.write("@Data\r\n");
					this.L.write("@Builder\r\n");
					this.L.write("@AllArgsConstructor\r\n");
					this.L.write("@NoArgsConstructor\r\n");
					this.L.write("@EqualsAndHashCode(callSuper = true)\r\n");
					this.L.write("public class " + bt + " extends YtBaseEntity<" + bt + ">{\r\n");
					this.L.write("\r\n");
					this.L.write("private static final long serialVersionUID=1L;\r\n");
					this.L.write("\r\n");
					for (int l = 0; l < r.length; l++) {
						this.L.write("" + ts[l][0] + " " + ts[l][1] + ";\r\n");
					}
					this.L.write("}");
					this.L.flush();
					String column = "", value = "", update = "", insertColumn = "", insertValue = "";
					for (int i = 0; i < r.length; i++) {
						column += "" + ts[i][1] + ",";
						value += "#{" + ts[i][1] + "},";
						update += "" + ts[i][1] + "" + "=#{" + ts[i][1] + "},";
						if (i >= 0) {
							if (!ts[i][1].equals("version")) {
								insertColumn += "" + ts[i][1] + ",";
								insertValue += "#{" + ts[i][1] + "},";
							}
						}
					}
					column = (column + ",").replace(",,", "");
					value = (value + ",").replace(",,", "");
					update = (update + ",").replace(",,", "");

					insertColumn = (insertColumn + ",").replace(",,", "");
					insertValue = (insertValue + ",").replace(",,", "");
					this.L.flush();

					file = dt(bt);
					this.K = new FileWriter(file);
					this.L = new BufferedWriter(this.K);
					this.L.write("package " + DbConnectionUtil.basePage + ".dbo;\r\n\n");
					this.L.write("import lombok.AllArgsConstructor;\r\n");
					this.L.write("import lombok.Data;\r\n");
					this.L.write("import lombok.NoArgsConstructor;\r\n");
					this.L.write("import lombok.experimental.SuperBuilder;\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* @author zj default\r\n");
					this.L.write("* \r\n");
					this.L.write("* @version " + DbConnectionUtil.version + "\r\n");
					this.L.write("* @createdate" + DateTimeUtil.getDateTime() + "\r\n");
					this.L.write("*/\r\n");
					this.L.write("@Data\r\n");
					this.L.write("@SuperBuilder\r\n");
					this.L.write("@NoArgsConstructor\r\n");
					this.L.write("@AllArgsConstructor\r\n");
					this.L.write("public class " + bt + "DTO {\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					for (int l = 0; l < r.length; l++) {
						this.L.write("" + ts[l][0] + " " + ts[l][1] + ";\r\n");
					}
					this.L.write("}");
					this.L.flush();
					column = "";
					value = "";
					update = "";
					insertColumn = "";
					insertValue = "";
					for (int i = 0; i < r.length; i++) {
						column += "" + ts[i][1] + ",";
						value += "#{" + ts[i][1] + "},";
						update += "" + ts[i][1] + "" + "=#{" + ts[i][1] + "},";
						if (i >= 0) {
							if (!ts[i][1].equals("version")) {
								insertColumn += "" + ts[i][1] + ",";
								insertValue += "#{" + ts[i][1] + "},";
							}
						}
					}
					column = (column + ",").replace(",,", "");
					value = (value + ",").replace(",,", "");
					update = (update + ",").replace(",,", "");

					insertColumn = (insertColumn + ",").replace(",,", "");
					insertValue = (insertValue + ",").replace(",,", "");
					this.L.flush();

					file = v(bt);
					this.K = new FileWriter(file);
					this.L = new BufferedWriter(this.K);
					this.L.write("package " + DbConnectionUtil.basePage + ".vo;\r\n\n");
					this.L.write("import lombok.AllArgsConstructor;\r\n");
					this.L.write("import lombok.Data;\r\n");
					this.L.write("import lombok.NoArgsConstructor;\r\n");
					this.L.write("import lombok.experimental.SuperBuilder;\r\n");
					this.L.write("import lombok.EqualsAndHashCode;\r\n");
					this.L.write("\r\n");
					this.L.write("import com.yt.app.common.base.BaseVO;\r\n");
					this.L.write("/**\r\n");
					this.L.write("* @author zj default\r\n");
					this.L.write("* \r\n");
					this.L.write("* @version " + DbConnectionUtil.version + "\r\n");
					this.L.write("* @createdate" + DateTimeUtil.getDateTime() + "\r\n");
					this.L.write("*/\r\n");
					this.L.write("@Data\r\n");
					this.L.write("@SuperBuilder\r\n");
					this.L.write("@NoArgsConstructor\r\n");
					this.L.write("@AllArgsConstructor\r\n");
					this.L.write("@EqualsAndHashCode(callSuper = true)\r\n");
					this.L.write("public class " + bt + "VO extends BaseVO{\r\n");
					this.L.write("\r\n");
					this.L.write("private static final long serialVersionUID = 1L;\r\n");
					this.L.write("\r\n");
					for (int l = 0; l < r.length; l++) {
						this.L.write("" + ts[l][0] + " " + ts[l][1] + ";\r\n");
					}
					this.L.write("}");
					this.L.flush();
					column = "";
					value = "";
					update = "";
					insertColumn = "";
					insertValue = "";
					for (int i = 0; i < r.length; i++) {
						column += "" + ts[i][1] + ",";
						value += "#{" + ts[i][1] + "},";
						update += "" + ts[i][1] + "" + "=#{" + ts[i][1] + "},";
						if (i >= 0) {
							if (!ts[i][1].equals("version")) {
								insertColumn += "" + ts[i][1] + ",";
								insertValue += "#{" + ts[i][1] + "},";
							}
						}
					}
					column = (column + ",").replace(",,", "");
					value = (value + ",").replace(",,", "");
					update = (update + ",").replace(",,", "");

					insertColumn = (insertColumn + ",").replace(",,", "");
					insertValue = (insertValue + ",").replace(",,", "");
					this.L.flush();

					file = h(bt);
					this.K = new FileWriter(file);
					this.L = new BufferedWriter(this.K);
					this.L.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
					this.L.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n");
					this.L.write("<mapper namespace=\"" + DbConnectionUtil.basePage + ".mapper." + bt + "Mapper\">\r\n");
					this.L.write("<!-- Base_Column_List -->\r\n");
					this.L.write("<sql id=\"Base_Column_List\">\r\n");
					this.L.write("" + column + "\r\n");
					this.L.write("</sql>\r\n");
					this.L.write("<!-- ResultMap -->\r\n");
					this.L.write("<resultMap id=\"ResultMap\" type=\"" + DbConnectionUtil.basePage + ".entity." + bt + "\">\r\n");
					this.L.write("</resultMap>\r\n");
					this.L.write("<!-- ResultMap -->\r\n");
					this.L.write("<resultMap id=\"ResultMapVO\" type=\"" + DbConnectionUtil.basePage + ".vo." + bt + "VO\">\r\n");
					this.L.write("</resultMap>\r\n");
					this.L.write("<!-- add -->\r\n");
					this.L.write("<insert id=\"post\" parameterType=\"" + DbConnectionUtil.basePage + ".entity." + bt + "\">\r\n");
					this.L.write("insert into " + tb + "(" + insertColumn + ")\r\n");
					this.L.write("values (" + insertValue + ")\r\n");
					this.L.write("</insert>\r\n");

					this.L.write("<!-- batchSava -->\r\n");
					this.L.write("<insert id=\"batchSava\" parameterType=\"java.util.List\">\r\n");
					this.L.write("insert into " + tb + "(" + insertColumn + ")\r\n");
					this.L.write("values\r\n");
					this.L.write("<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\" >\r\n");
					this.L.write("(" + insertValue + ")\r\n");
					this.L.write("</foreach>\r\n");
					this.L.write("</insert>\r\n");

					this.L.write("<!-- delById -->\r\n");
					this.L.write("<delete id=\"delById\" parameterType=\"java.lang.Long\">\r\n");
					this.L.write("delete from " + tb + "\r\n");
					this.L.write("where id = #{id}\r\n");
					this.L.write("</delete>\r\n");
					this.L.write("<!-- update -->\r\n");
					this.L.write("<update id=\"put\" parameterType=\"" + DbConnectionUtil.basePage + ".entity." + bt + "\">\r\n");
					this.L.write("update " + tb + "\r\n");
					this.L.write("<set>\r\n");
					for (int i = 0; i < r.length; i++) {
						if (ts[i][1].equals("id"))
							continue;
						this.L.write("<if test=\"" + ts[i][1] + " != null\">\r\n");
						if (i < r.length - 1) {
							if (ts[i][1].equals("version"))
								this.L.write("" + ts[i][1] + "= #{" + ts[i][1] + "}+1,\r\n");
							else
								this.L.write("" + ts[i][1] + "= #{" + ts[i][1] + "},\r\n");
						} else if (i == r.length - 1) {
							if (ts[i][1].equals("version"))
								this.L.write("" + ts[i][1] + "= #{" + ts[i][1] + "}+1\r\n");
							else
								this.L.write("" + ts[i][1] + "= #{" + ts[i][1] + "}\r\n");
						}
						this.L.write("</if>\r\n");
					}
					this.L.write("</set>\r\n");
					this.L.write("where id = #{id} and version = #{version}\r\n");
					this.L.write("</update>\r\n");
					this.L.write("<!--getById -->\r\n");
					this.L.write("<select id=\"get\" parameterType=\"java.lang.Long\" resultMap=\"ResultMap\">\r\n");
					this.L.write("select\r\n");
					this.L.write("<include refid=\"Base_Column_List\"/>\r\n");
					this.L.write("from " + tb + "\r\n");
					this.L.write("where id = #{id}\r\n");
					this.L.write("</select>\r\n");
					this.L.write("<!-- getList -->\r\n");
					this.L.write("<select id=\"list\" parameterType=\"java.util.HashMap\" resultMap=\"ResultMap\">\r\n");
					this.L.write("select\r\n");
					this.L.write("<include refid=\"Base_Column_List\"/>\r\n");
					this.L.write("from " + tb + "\r\n");
					this.L.write("<where>\r\n");
					this.L.write("1=1\r\n");
					this.L.write("<if test=\"name != null and name != ''\">\r\n");
					this.L.write("and name like \"%\"#{name}\"%\"\r\n");
					this.L.write("</if>\r\n");
					this.L.write("</where>\r\n");
					this.L.write("</select>\r\n");
					this.L.write("<!-- getMap -->\r\n");
					this.L.write("<select id=\"page\" parameterType=\"java.util.HashMap\" resultMap=\"ResultMapVO\">\r\n");
					this.L.write("select\r\n");
					this.L.write("<include refid=\"Base_Column_List\"/>\r\n");
					this.L.write("from " + tb + "\r\n");
					this.L.write("<where>\r\n");
					this.L.write("1=1\r\n");
					this.L.write("<if test=\"name != null and name != ''\">\r\n");
					this.L.write("and name like \"%\"#{name}\"%\"\r\n");
					this.L.write("</if>\r\n");
					this.L.write("<if test=\"orderBy != null and dir != null\">\r\n");
					this.L.write("order by ${orderBy} ${dir}\r\n");
					this.L.write("</if>\r\n");
					this.L.write("<if test=\"pageStart != null and pageEnd != null\">\r\n");
					this.L.write("LIMIT #{pageStart},#{pageEnd}\r\n");
					this.L.write("</if>\r\n");
					this.L.write("</where>\r\n");
					this.L.write("</select>\r\n");
					this.L.write("<!-- getCount -->\r\n");
					this.L.write("<select id=\"countlist\" parameterType=\"java.util.HashMap\" resultType=\"int\">\r\n");
					this.L.write("select count(*) from " + tb + "\r\n");
					this.L.write("<where>\r\n");
					this.L.write("1=1\r\n");
					this.L.write("<if test=\"name != null and name != ''\">\r\n");
					this.L.write("and name like \"%\"#{name}\"%\"\r\n");
					this.L.write("</if>\r\n");
					this.L.write("</where>\r\n");
					this.L.write("</select>\r\n");
					this.L.write("<!-- listByArrayId -->\r\n");
					this.L.write("<select id=\"listByArrayId\" parameterType=\"java.util.HashMap\" resultMap=\"ResultMap\">\r\n");
					this.L.write("select\r\n");
					this.L.write("<include refid=\"Base_Column_List\"/>\r\n");
					this.L.write("from " + tb + "\r\n");
					this.L.write("<where>\r\n");
					this.L.write("1=1\r\n");
					this.L.write("<if test=\"array != null and array.length > 0\">\r\n");
					this.L.write("and id in\r\n");
					this.L.write("<foreach item=\"item\" index=\"index\" collection=\"array\" open=\"(\" separator=\",\" close=\")\">\r\n");
					this.L.write("#{item}\r\n");
					this.L.write("</foreach>\r\n");
					this.L.write("</if>\r\n");
					this.L.write("</where>\r\n");
					this.L.write("</select>\r\n");
					this.L.write("</mapper>");
					this.L.flush();

					file = c(bt);
					this.K = new FileWriter(file);
					this.L = new BufferedWriter(this.K);
					this.L.write("package " + DbConnectionUtil.basePage + ".mapper;\r\n");
					this.L.write("import java.util.List;\r\n");
					this.L.write("import java.util.Map;\r\n");
					this.L.write("import " + DbConnectionUtil.basePage + "." + "entity." + bt + ";\r\n");
					this.L.write("import " + DbConnectionUtil.basePage + "." + "vo." + bt + "VO;\r\n");
					this.L.write("import com.yt.app.common.annotation.YtRedisCacheAnnotation;\r\n");
					this.L.write("import com.yt.app.common.annotation.YtRedisCacheEvictAnnotation;\r\n");
					this.L.write("import " + DbConnectionUtil.commndPage + ".YtIBaseMapper;\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* @author zj default\r\n");
					this.L.write("* \r\n");
					this.L.write("* @version " + DbConnectionUtil.version + "\r\n");
					this.L.write("* @createdate" + DateTimeUtil.getDateTime() + "\r\n");
					this.L.write("*/\r\n");
					this.L.write("\r\n");
					this.L.write("public interface " + bt + "Mapper extends YtIBaseMapper<" + bt + "> {\r\n");
					this.L.write("/**\r\n");
					this.L.write("* add\r\n");
					this.L.write("* \r\n");
					this.L.write("* @param o\r\n");
					this.L.write("* " + bt + "\r\n");
					this.L.write("* @return count\r\n");
					this.L.write("*/\r\n");
					this.L.write("@YtRedisCacheEvictAnnotation(classs = { " + bt + ".class})\r\n");
					this.L.write("public Integer post(Object t);\r\n");
					this.L.write("/**\r\n");
					this.L.write("* sava batch\r\n");
					this.L.write("* \r\n");
					this.L.write("* @param o\r\n");
					this.L.write("*  " + bt + "list\r\n");
					this.L.write("* @return count\r\n");
					this.L.write("*/\r\n");
					this.L.write("@YtRedisCacheEvictAnnotation(classs = { " + bt + ".class})\r\n");
					this.L.write("public Integer batchSava(List<" + bt + "> list);\r\n");
					this.L.write("/**\r\n");
					this.L.write("* update\r\n");
					this.L.write("* \r\n");
					this.L.write("* @param o\r\n");
					this.L.write("* " + bt + "\r\n");
					this.L.write("* @return count\r\n");
					this.L.write("*/\r\n");
					this.L.write("@YtRedisCacheEvictAnnotation(classs = { " + bt + ".class})\r\n");
					this.L.write("public Integer put(Object t);\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* get\r\n");
					this.L.write("*\r\n");
					this.L.write("* @param id\r\n");
					this.L.write("* id\r\n");
					this.L.write("* @return " + bt + "\r\n");
					this.L.write("*/\r\n");
					this.L.write("@YtRedisCacheAnnotation(classs = " + bt + ".class)\r\n");
					this.L.write("public " + bt + " get(Long id);\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* delete\r\n");
					this.L.write("*\r\n");
					this.L.write("* @param id\r\n");
					this.L.write("* id\r\n");
					this.L.write("* @return count\r\n");
					this.L.write("*/\r\n");
					this.L.write("@YtRedisCacheEvictAnnotation(classs = { " + bt + ".class})\r\n");
					this.L.write("public Integer delById(Long id);\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* listcount\r\n");
					this.L.write("* \r\n");
					this.L.write("* @param param\r\n");
					this.L.write("* map\r\n");
					this.L.write("* @return count\r\n");
					this.L.write("*/\r\n");
					this.L.write("@YtRedisCacheAnnotation(classs = " + bt + ".class)\r\n");
					this.L.write("public Integer countlist(Map<String, Object> param);\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* list\r\n");
					this.L.write("* \r\n");
					this.L.write("* @param param\r\n");
					this.L.write("* map\r\n");
					this.L.write("* @return list" + bt + "\r\n");
					this.L.write("*/\r\n");
					this.L.write("@YtRedisCacheAnnotation(classs = " + bt + ".class)\r\n");
					this.L.write("public List<" + bt + "> list(Map<String, Object> param);\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* map\r\n");
					this.L.write("* \r\n");
					this.L.write("* @param param\r\n");
					this.L.write("* map\r\n");
					this.L.write("* @return map" + bt + "\r\n");
					this.L.write("*/\r\n");
					this.L.write("@YtRedisCacheAnnotation(classs = " + bt + ".class)\r\n");
					this.L.write("public List<" + bt + "VO> page(Map<String, Object> param);\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* listbyids\r\n");
					this.L.write("* \r\n");
					this.L.write("* @param id\r\n");
					this.L.write("* long[]ids\r\n");
					this.L.write("* @return list" + bt + "\r\n");
					this.L.write("*/\r\n");
					this.L.write("@YtRedisCacheAnnotation(classs = " + bt + ".class)\r\n");
					this.L.write("public List<" + bt + "> listByArrayId(long[] id);\r\n");
					this.L.write("}");
					this.L.flush();

					file = d(bt);
					this.K = new FileWriter(file);
					this.L = new BufferedWriter(this.K);
					this.L.write("package " + DbConnectionUtil.basePage + ".service;\r\n");
					this.L.write("\r\n");
					this.L.write("import java.util.Map;\r\n");
					this.L.write("\r\n");
					this.L.write("import " + DbConnectionUtil.basePage + "." + "entity." + bt + ";\r\n");
					this.L.write("import " + DbConnectionUtil.basePage + "." + "vo." + bt + "VO;\r\n");
					this.L.write("import " + DbConnectionUtil.commndPage + ".YtIBaseService;\r\n");
					this.L.write("import com.yt.app.common.common.yt.YtIPage;\r\n");
					this.L.write("/**\r\n");
					this.L.write("* @author zj default\r\n");
					this.L.write("* \r\n");
					this.L.write("* @version " + DbConnectionUtil.version + "\r\n");
					this.L.write("* @createdate" + DateTimeUtil.getDateTime() + "\r\n");
					this.L.write("*/\r\n");
					this.L.write("\r\n");
					this.L.write("public interface " + bt + "Service extends YtIBaseService<" + bt + ", Long>{\r\n");
					this.L.write("YtIPage<" + bt + "VO> page(Map<String, Object> param);\r\n");
					this.L.write("}");
					this.L.flush();

					file = e(bt);
					this.K = new FileWriter(file);
					this.L = new BufferedWriter(this.K);
					this.L.write("package " + DbConnectionUtil.basePage + ".service.impl;\r\n");
					this.L.write("\r\n");
					this.L.write("import org.springframework.beans.factory.annotation.Autowired;\r\n");
					this.L.write("import org.springframework.transaction.annotation.Transactional;\r\n");
					this.L.write("import org.springframework.stereotype.Service;\r\n");
					this.L.write("import " + DbConnectionUtil.basePage + "." + "mapper." + bt + "Mapper;\r\n");
					this.L.write("import " + DbConnectionUtil.basePage + "." + "service." + bt + "Service;\r\n");
					this.L.write("import " + DbConnectionUtil.commndPage + ".impl.YtBaseServiceImpl;\r\n");
					this.L.write("import " + DbConnectionUtil.basePage + "." + "entity." + bt + ";\r\n");
					this.L.write("import " + DbConnectionUtil.basePage + "." + "vo." + bt + "VO;\r\n");
					this.L.write("import com.yt.app.common.common.yt.YtIPage;\r\n");
					this.L.write("import com.yt.app.common.common.yt.YtPageBean;\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("import java.util.Collections;\r\n");
					this.L.write("import java.util.List;\r\n");
					this.L.write("import java.util.Map;\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* @author zj default\r\n");
					this.L.write("* \r\n");
					this.L.write("* @version " + DbConnectionUtil.version + "\r\n");
					this.L.write("* @createdate" + DateTimeUtil.getDateTime() + "\r\n");
					this.L.write("*/\r\n");
					this.L.write("\r\n");
					this.L.write("@Service\r\n");
					this.L.write("public class " + bt + "ServiceImpl extends YtBaseServiceImpl<" + bt + ", Long> implements " + bt + "Service{\r\n");
					this.L.write("@Autowired\r\n");
					this.L.write("private " + bt + "Mapper mapper;\r\n");
					this.L.write("\r\n");
					this.L.write("@Override\r\n");
					this.L.write("@Transactional\r\n");
					this.L.write("public Integer post(" + bt + " t) {\r\n");
					this.L.write("Integer i = mapper.post(t);\r\n");
					this.L.write("return i;\r\n");
					this.L.write("}\r\n");
					this.L.write("\r\n");
					this.L.write("@Override\r\n");
					this.L.write("public YtIPage<" + bt + "> list(Map<String, Object> param) {\r\n");
					this.L.write("List<" + bt + "> list = mapper.list(param);\r\n");
					this.L.write("return new YtPageBean<" + bt + ">(list);\r\n");
					this.L.write("}\r\n");
					this.L.write("\r\n");
					this.L.write("@Override\r\n");
					this.L.write("public " + bt + " get(Long id) {\r\n");
					this.L.write("" + bt + " t = mapper.get(id);\r\n");
					this.L.write("return t;\r\n");
					this.L.write("}\r\n");
					this.L.write("@Override\r\n");
					this.L.write("public YtIPage<" + bt + "VO> page(Map<String, Object> param) {\r\n");
					this.L.write("int count = mapper.countlist(param);\r\n");
					this.L.write("if (count == 0) {\r\n");
					this.L.write("return new YtPageBean<" + bt + "VO>(Collections.emptyList());\r\n");
					this.L.write("}\r\n");
					this.L.write("List<" + bt + "VO> list = mapper.page(param);\r\n");
					this.L.write("return new YtPageBean<" + bt + "VO>(param, list, count);\r\n");
					this.L.write("}\r\n");
					this.L.write("}");
					this.L.flush();

					file = f(bt);
					this.K = new FileWriter(file);
					this.L = new BufferedWriter(this.K);
					this.L.write("package " + DbConnectionUtil.basePage + ".controller;\r\n");
					this.L.write("import javax.servlet.http.HttpServletRequest;\r\n");
					this.L.write("import javax.servlet.http.HttpServletResponse;\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("import org.springframework.beans.factory.annotation.Autowired;\r\n");
					this.L.write("import org.springframework.http.MediaType;\r\n");
					this.L.write("import com.yt.app.common.common.yt.YtResponseEncryptEntity;\r\n");
					this.L.write("import org.springframework.web.bind.annotation.RequestMethod;\r\n");
					this.L.write("import org.springframework.web.bind.annotation.RestController;\r\n");
					this.L.write("import org.springframework.web.bind.annotation.RequestMapping;\r\n");
					this.L.write("import com.yt.app.common.common.yt.YtRequestDecryptEntity;\r\n");
					this.L.write("import com.yt.app.common.common.yt.YtIPage;\r\n");
					this.L.write("import com.yt.app.common.common.yt.YtBody;\r\n");
					this.L.write("import com.yt.app.common.util.RequestUtil;\r\n");
					this.L.write("\r\n");
					this.L.write("import " + DbConnectionUtil.commndPage + ".impl.YtBaseEncipherControllerImpl;\r\n");
					this.L.write("import " + DbConnectionUtil.basePage + "." + "service." + bt + "Service;\r\n");
					this.L.write("import " + DbConnectionUtil.basePage + "." + "entity." + bt + ";\r\n");
					this.L.write("import " + DbConnectionUtil.basePage + "." + "vo." + bt + "VO;\r\n");
					this.L.write("\r\n");
					this.L.write("/**\r\n");
					this.L.write("* @author yyds\r\n");
					this.L.write("* \r\n");
					this.L.write("* @version " + DbConnectionUtil.version + "\r\n");
					this.L.write("* @createdate" + DateTimeUtil.getDateTime() + "\r\n");
					this.L.write("*/\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("@RestController\r\n");
					this.L.write("@RequestMapping(\"/" + DbConnectionUtil.osName + "/" + DbConnectionUtil.version + "/" + bt.toLowerCase() + "\")\r\n");
					this.L.write("public class " + bt + "Controller extends YtBaseEncipherControllerImpl<" + bt + ", Long> {\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("@Autowired\r\n");
					this.L.write("private " + bt + "Service service;\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("@Override\r\n");
					this.L.write("@RequestMapping(value = \"/list\", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)\r\n");
					this.L.write("public YtResponseEncryptEntity<Object> list(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {\r\n");
					this.L.write("YtIPage<" + bt + "> list = service.list(RequestUtil.requestDecryptEntityToParamMap(requestEntity));\r\n");
					this.L.write("return new YtResponseEncryptEntity<Object>(new YtBody(list));\r\n");
					this.L.write("}\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("@RequestMapping(value = \"/page\", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)\r\n");
					this.L.write("public YtResponseEncryptEntity<Object> page(YtRequestDecryptEntity<Object> requestEntity, HttpServletRequest request, HttpServletResponse response) {\r\n");
					this.L.write("YtIPage<" + bt + "VO> pagebean = service.page(RequestUtil.requestDecryptEntityToParamMap(requestEntity));\r\n");
					this.L.write("return new YtResponseEncryptEntity<Object>(new YtBody(pagebean));\r\n");
					this.L.write("}\r\n");
					this.L.write("}\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.write("\r\n");
					this.L.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private File c(String paramString) throws Exception {
		this.file = new File(DbConnectionUtil.filePath + "/mapper/" + paramString + "Mapper.java");
		if (this.file.exists())
			this.file.delete();
		else
			this.file.createNewFile();
		return this.file;
	}

	private File d(String paramString) throws Exception {
		this.file = new File(DbConnectionUtil.filePath + "/service/" + paramString + "Service.java");
		if (this.file.exists())
			this.file.delete();
		else
			this.file.createNewFile();
		return this.file;
	}

	private File e(String paramString) throws Exception {
		this.file = new File(DbConnectionUtil.filePath + "/service/impl/" + paramString + "ServiceImpl.java");
		if (this.file.exists())
			this.file.delete();
		else
			this.file.createNewFile();
		return this.file;
	}

	private File f(String paramString) throws Exception {
		this.file = new File(DbConnectionUtil.filePath + "/controller/" + paramString + "Controller.java");
		if (this.file.exists())
			this.file.delete();
		else
			this.file.createNewFile();
		return this.file;
	}

	private File g(String paramString) throws Exception {
		this.file = new File(DbConnectionUtil.filePath + "/entity/" + paramString + ".java");
		if (this.file.exists())
			this.file.delete();
		else
			this.file.createNewFile();
		return this.file;
	}

	private File v(String paramString) throws Exception {
		this.file = new File(DbConnectionUtil.filePath + "/vo/" + paramString + "VO.java");
		if (this.file.exists())
			this.file.delete();
		else
			this.file.createNewFile();
		return this.file;
	}

	private File dt(String paramString) throws Exception {
		this.file = new File(DbConnectionUtil.filePath + "/dbo/" + paramString + "DTO.java");
		if (this.file.exists())
			this.file.delete();
		else
			this.file.createNewFile();
		return this.file;
	}

	private File h(String paramString) throws Exception {
		this.file = new File(DbConnectionUtil.filePath + "/mapper/impl/" + paramString + "Mapper.xml");
		if (this.file.exists())
			this.file.delete();
		else
			this.file.createNewFile();
		return this.file;
	}

	private void b(String paramString) {
		this.file = new File(DbConnectionUtil.filePath + "/mapper/impl");
		this.file.mkdirs();
		this.file = new File(DbConnectionUtil.filePath + "/service/impl");
		this.file.mkdirs();
		this.file = new File(DbConnectionUtil.filePath + "/controller");
		this.file.mkdirs();
		this.file = new File(DbConnectionUtil.filePath + "/entity");
		this.file.mkdirs();
		this.file = new File(DbConnectionUtil.filePath + "/mapper");
		this.file.mkdirs();
	}

	public static YtAutoCode u() {

		return new YtAutoCode();
	}
}
