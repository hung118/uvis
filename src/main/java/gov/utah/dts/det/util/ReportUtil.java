package gov.utah.dts.det.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for Jasper report import.
 * 
 * @author HNGUYEN
 * 
 */
public class ReportUtil {

	public static String concat2String(String str1, String str2) {

		String ret = "";
		if (str1 == null && str2 == null) {
			ret = "";
		} else if (str1 == null && str2 != null) {
			ret = str2;
		} else if (str1 != null && str2 == null) {
			ret = str1;
		} else {
			ret = str1 + " " + str2;
		}

		return ret;
	}

	public static String formatDate(Date date) {
		
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			return sdf.format(date);					
		} else {
			return "";
		}
	}
	
	public static String formatLongDate(Date date) {
		
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy");
			return sdf.format(date);					
		} else {
			return "";
		}
	}
	
	public static String convertNullToBlank(String obj) {
		return obj == null ? "" : obj;
	}
	
}
