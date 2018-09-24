package com.fdmgroup.ses.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

	public static Date now() {
		return GregorianCalendar.getInstance().getTime();
	}

	public static boolean isXMinutesOld(Date date, Integer i) {
		return date.before(xMinutesFromNow(-i));
	}

	public static Date xMinutesFromNow(Integer i) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.add(Calendar.MINUTE, i);
		return cal.getTime();
	}
	
}
