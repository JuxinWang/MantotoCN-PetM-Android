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

	//日期转化为字符串
	public static String getTimes(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		return format.format(date);
	}

	//通过日期获取星期
	public static String getWeekends(Date date){
		int y=date.getYear() -1;
		int m=date.getMonth() + 1;
		int c=20;
		int d=date.getDay()+12;
		int w=(y+(y/4)+(c/4)-2*c+(26*(m+1)/10)+d-1)%7;
		String week = null;
		switch(w)
		{
			case 0:
				week="星期日";
				break;
			case 1:
				week="星期一";
				break;
			case 2:
				week="星期二";
				break;
			case 3:
				week="星期三";
				break;
			case 4:
				week="星期四";
				break;
			case 5:
				week="星期五";
				break;
			case 6:
				week="星期六";
				break;
		}
		return week;
	}
}
