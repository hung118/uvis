package gov.utah.dts.det.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validation utility class
 * 
 * @author hnguyen
 * 
 */
public class Validate {

	public static boolean isEmpty(String str) {
		if (str == null) {
			return true;
		} else {
			return str.trim().length() == 0;
		}
	}

	public static boolean isNull(Object obj) {
		return obj == null;
	}

	/**
	 * This method checks if the input email address is a valid email addrees.
	 * 
	 * @param email
	 *            String. Email adress to validate
	 * @return boolean: true if email address is valid, false otherwise.
	 */
	public static boolean isEmailValid(String email) {
		boolean isValid = false;
		/*
		 * Email format: A valid email address will have following format
		 * [\\w\\.-]+ : Begins with word characters, (may include periods and
		 * hypens).
		 * 
		 * @: It must have a '@' symbol after initial characters.
		 * ([\\w\\-]+\\.)+ : @ must follow by more alphanumeric characters (may
		 * include hypens.). This part must also have a "." to separate domain
		 * and subdomain names. [A-Z]{2,4}$: Must end with two to four
		 * alaphabets. (This will allow domain names with 2, 3 and 4 characters
		 * e.g pa, com, net, wxyz)
		 */
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

		// Make the comparison case-insensitive.
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);

		CharSequence inputStr = email;
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}

		return isValid;
	}

	/**
	 * This method checks if the input text contains all numeric characters.
	 * 
	 * @param email
	 *            String. Number to validate
	 * @return boolean: true if the input is all numeric, false otherwise.
	 */
	public static boolean isNumeric(String number) {
		boolean isValid = false;
		// Number:
		/*
		 * ^\\d{3} : Starts with three numeric digits. [- ]? : Followed by an
		 * optional - and space \\d{2}: Two numeric digits after the optional
		 * "-" [- ]? : May contains an optional second "-" character. \\d{4}:
		 * ends with four numeric digits.
		 */
		String expression = "[-+]?[0-9]*\\.?[0-9]+$";
		// expression = "/^(\(\\d+\) ?)?(\d+[\- ])*\d+$/";
		// expression = "^\\d{3}$";
		CharSequence inputStr = number;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * This method checks if the input phone number is a valid phone number.
	 * 
	 * @param email
	 *            String. Phone number to validate
	 * @return boolean: true if phone number is valid, false otherwise.
	 */
	public static boolean isPhoneNumberValid(String phoneNumber) {
		
		if (phoneNumber == null || phoneNumber == "" || phoneNumber.length() == 0) return true;
		
		boolean isValid = false;
		/*
		 * Phone Number format: ^\\(? : May start with an option "(" . (\\d{3}):
		 * Followed by 3 digits. \\)? : May have an optional ")" [- ]? : May
		 * have an optional "-" after the first 3 digits or after optional )
		 * character. (\\d{3}) : Followed by 3 digits. [- ]? : May have another
		 * optional "-" after numeric digits. (\\d{4})$: ends with four digits.
		 * Matches following: (123)456-7890, 123-456-7890, 1234567890,
		 * (123)-456-7890
		 */
		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
		CharSequence inputStr = phoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * This method checks if the input social security number is valid.
	 * 
	 * @param email
	 *            String. Social Security number to validate
	 * @return boolean: true if social security number is valid, false
	 *         otherwise.
	 */
	public static boolean isSSNValid(String ssn) {
		
		if (ssn == null || ssn == "" || ssn.length() == 0) return true;
		
		boolean isValid = false;
		// SSN format:
		/*
		 * ^\\d{3} : Starts with three numeric digits. [- ]? : Followed by an
		 * optional - and space \\d{2}: Two numeric digits after the optional
		 * "-" [- ]? : May contains an optional second "-" character. \\d{4}:
		 * ends with four numeric digits.
		 * Matches following: 345-45-4545, 345456565
		 */
		String expression = "^\\d{3}[- ]?\\d{2}[- ]?\\d{4}$";
		CharSequence inputStr = ssn;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * Checks if the input zip code is valid. 00000-0000
	 * @param zip
	 * @return
	 */
	public static boolean isZipValid(String zip) {
		boolean isValid = false;
		// Zip code format:
		/*
		 * ^\\d{5} : Starts with five numeric digits. [- ]? : Followed by an
		 * optional - and space (\\d{4})?$ : May optionally ends with four numeric digits.
		 */		
		String expression = "^\\d{5}[- ]?(\\d{4})?$";
		//String expression = "^\\d{5}(-\\d{4})?$";
		CharSequence inputStr = zip;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}
	
	/**
	 * Checks for valid date string with specified pattern (eg. MM/dd/yyyy). 
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static boolean isDateValid(String dateStr, String pattern) {
		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date testDate = null;
		
		try {
			testDate = sdf.parse(dateStr);
		} catch (Exception e) {
			return false;
		}
		
		if (!sdf.format(testDate).equals(dateStr)) return false;
		
		return true;
	}
	
	/**
	 * Compares two date objects.
	 * @param date1
	 * @param date2
	 * @return
	 * 	-1: date1 is before date2,
	 * 	1: date1 is after date2,
	 * 	0: date1 equals to date2.
	 */
	public static int compareDate(Date date1, Date date2) {
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
		
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		c2.set(Calendar.HOUR_OF_DAY, 0);
		c2.set(Calendar.MINUTE, 0);
		c2.set(Calendar.SECOND, 0);
		c2.set(Calendar.MILLISECOND, 0);
		
		if (c1.before(c2)) return -1;
		if (c1.after(c2)) return  1;
		if (c1.equals(c2)) return 0;

		return 0;
	}
	
	/**
	 * Compares two date strings with specified pattern (eg MM/dd/yyyy).
	 * @param date1Str
	 * @param date2Str
	 * @param pattern
	 * @return
	 * @throws Exception
	 */
	public static int compareDate(String date1Str, String date2Str, String pattern) throws Exception {
		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return compareDate(sdf.parse(date1Str), sdf.parse(date2Str));		
	}
	
	/**
	 * Checks if birth date is under 17 years old. 
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isUnder17(Date birthDate) {
		
		Date today = new Date();
		long diff = today.getTime() - birthDate.getTime();
		
		if (diff < 0) return false;
		
		long diffDays = diff / (24 * 60 * 60 * 1000);
	    if (diffDays < 17 * 365) {
	    	return true;
	    } else {
	    	return false;
	    }
	}
}
