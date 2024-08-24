package com.yt.app.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class DateTimeUtil {
	public static final String DEFAULT_YEAR_FORMAT = "yyyy";
	public static final String DEFAULT_SHORT_YEAR_FORMAT = "YY";
	public static final String DEFAULT_MONTH_FORMAT = "MM";
	public static final String DEFAULT_DAY_FORMAT = "dd";
	public static final String DEFAULT_SHORT_DATE_FORMAT = "YY-MM-dd";
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String DEFAULT_TIME_FORMAT = "HH:mm";
	public static final String DEFAULT_HOUR_FORMAT = "HH";
	public static final String DEFAULT_MINUTE_FORMAT = "mm";
	public static final String DEFAULT_SECOND_FORMAT = "ss";
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DEFAULT_DATE_HOUR_MINUTE_FORMAT = "yyyy-MM-dd HH:mm";

	public static Date getNow() {
		return Calendar.getInstance().getTime();
	}

	public static String getDate() {
		return getDateTime("yyyy-MM-dd");
	}

	public static String getDateTime() {
		return getDateTime("yyyy-MM-dd HH:mm:ss");
	}

	public static String getDateTime(String pattern) {
		Date datetime = Calendar.getInstance().getTime();
		return getDateTime(datetime, pattern);
	}

	public static String getDateTime(Date date, String pattern) {
		if ((pattern == null) || ("".equals(pattern))) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}

	public static int getCurrentYear() {
		return Calendar.getInstance().get(1);
	}

	public static int getCurrentMonth() {
		return Calendar.getInstance().get(2) + 1;
	}

	public static int getCurrentDay() {
		return Calendar.getInstance().get(5);
	}

	public static Date addDays(int days) {
		return add(getNow(), days, 5);
	}

	public static Date addDays(Date date, int days) {
		return add(date, days, 5);
	}

	public static Date addMonths(int months) {
		return add(getNow(), months, 2);
	}

	public static Date addMinute(int minute) {
		return add(getNow(), minute, 12);
	}

	public static Date addMonths(Date date, int months) {
		return add(date, months, 2);
	}

	private static Date add(Date date, int amount, int field) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	public static long diffDays(Date one, Date two) {
		return (one.getTime() - two.getTime()) / 86400000L;
	}

	public static int diffMonths(Date one, Date two) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(one);
		int yearOne = calendar.get(1);
		int monthOne = calendar.get(2);

		calendar.setTime(two);
		int yearTwo = calendar.get(1);
		int monthTwo = calendar.get(2);
		return (yearOne - yearTwo) * 12 + (monthOne - monthTwo);
	}

	public static Date parse(String datestr, String pattern) {
		Date date = null;
		if ((pattern == null) || ("".equals(pattern)))
			pattern = "yyyy-MM-dd";
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			date = dateFormat.parse(datestr);
		} catch (ParseException localParseException) {
		}
		return date;
	}

	public static Date getMonthLastDay() {
		return getMonthLastDay(getNow());
	}

	public static Date getMonthLastDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(calendar.get(1), calendar.get(2) + 1, 1);

		calendar.add(5, -1);
		return calendar.getTime();
	}

	public static String[] getTimeInterval(String date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parse = null;
		try {
			parse = simpleDateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(parse);
		int dayWeek = cal.get(7);
		if (1 == dayWeek) {
			cal.add(5, -1);
		}
		cal.setFirstDayOfWeek(2);
		int day = cal.get(7);
		cal.add(5, cal.getFirstDayOfWeek() - day);
		String imptimeBegin = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		cal.add(5, 6);
		String imptimeEnd = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		return (imptimeBegin + "," + imptimeEnd).split(",");
	}

	public static Map<String, String> getFirstday_Lastday_Month(String date) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(df.parse(date));
		Date theDate = calendar.getTime();
		GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
		gcLast.setTime(theDate);
		gcLast.set(5, 1);
		String day_first = df.format(gcLast.getTime());
		StringBuffer str = new StringBuffer().append(day_first);
		day_first = str.toString();
		calendar.add(2, 1);
		calendar.set(5, 1);
		calendar.add(5, -1);
		String day_last = df.format(calendar.getTime());
		StringBuffer endStr = new StringBuffer().append(day_last);
		day_last = endStr.toString();
		Map<String, String> map = new HashMap<String, String>();
		map.put("first", day_first);
		map.put("last", day_last);
		return map;
	}

	public static int getMonthDays(String date) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parse = simpleDateFormat.parse(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(parse);
		int maximum = cal.getActualMaximum(5);
		return maximum;
	}
}