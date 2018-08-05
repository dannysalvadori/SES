package com.fdmgroup.ses.reports;

import java.lang.reflect.InvocationTargetException;

public class ReportWriterUtils {

	/**
	 * Uses Reflection API to gets the value of a specified field from a given object
	 */
	public static String getFieldValue(Object sourceObject, String fieldPath) {
		String output = "";
		Object intermediateObject = sourceObject;
		
		// Split field path such that Company_Name --> Company, Name
		String[] fields = fieldPath.split("\\.");
		
		try {
			
			for (Integer i = 0; i < fields.length; i++) {
				String field = fields[i];
				intermediateObject = intermediateObject.getClass().getMethod("get" + field).invoke(intermediateObject);
				output = String.valueOf(intermediateObject);
			}
		
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
			// TODO: handle properly
			e.printStackTrace();
			System.out.println("Something went wrong attempting to get field with name " + fieldPath + " from an "
					+ "object of type " + intermediateObject.getClass().toString());
		
		} catch (NoSuchMethodException e) {
			// TODO: handle properly
			e.printStackTrace();
			System.out.println("No such method: " + intermediateObject.getClass().toString() + ".get" + fieldPath);
		}
		
		return output;
	}

}
