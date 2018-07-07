package com.fdmgroup.ses.reports;

import java.lang.reflect.InvocationTargetException;

public class ReportWriterUtils {

	/**
	 * Uses Reflection API to gets the value of a specified field from a given object
	 */
	public static String getFieldValue(Class<?> type, Object row, String fieldName) {
		String output = "";
		try {
			output += type.getMethod("get" + fieldName).invoke(row);
		
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
			// TODO: handle properly
			e.printStackTrace();
			System.out.println("Something went wrong attempting to get field with name " + fieldName + " from an "
					+ "object of type " + type.toString());
		
		} catch (NoSuchMethodException e) {
			// TODO: handle properly
			e.printStackTrace();
			System.out.println("No such method: " + type.toString() + ".get" + fieldName);
		}
		
		return output;
	}

}
