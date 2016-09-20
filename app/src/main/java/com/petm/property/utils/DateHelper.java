package com.petm.property.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateHelper {

	public static String getStringTime(String timeNum) {
		Long timestamp = Long.parseLong(timeNum) * 1000;
		String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(timestamp));
		return date;

	}

	public static Date getUnixTime(String timeNum) {

		Long timestamp = Long.parseLong(timeNum) * 1000;
		return new Date(timestamp);

	}

	public static String getStringTime(Long timeNum) {
		Long timestamp = timeNum * 1000;
		String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(timestamp));
		return date;

	}

	public static Date Add(int unit, Date date, int number) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(unit, number);

		return gc.getTime();
	}

	//字符串转时间戳
	public static String getTime(String timeString,String formate){
		String timeStamp = null;
		SimpleDateFormat sdf = new SimpleDateFormat(formate);
		Date d;
		try{
			d = sdf.parse(timeString);
			long l = d.getTime();
			timeStamp = String.valueOf(l);
		} catch(ParseException e){
			e.printStackTrace();
		}
		return timeStamp;
	}

	//时间戳转字符串
	public static String getStrTime(String timeStamp,String formate){
		String timeString = null;
		SimpleDateFormat sdf = new SimpleDateFormat(formate);
		long  l = Long.valueOf(timeStamp);
		timeString = sdf.format(new Date(l));//单位秒
		return timeString;
	}
	//日期转化为字符串
	public static String getTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}
}
