package gov.utah.dts.det.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;

public class Util {

	/**
	 * returns the a random date
	 * 
	 * @param inTheFuture
	 *            (if you want a future date, set to true, else false will get a
	 *            date <= than now
	 * @return a random date
	 */
	public static Date getRandomDate(Boolean inTheFuture) {

		final Long FIFTY_YEARS = 1577846298735l;
		Random r = new Random();

		// start with the current time
		Long time = System.currentTimeMillis();

		// now add some time to it
		Long randomInc = r.nextLong() % FIFTY_YEARS;

		// if its in the past, negate it
		if (!inTheFuture)
			randomInc *= -1;

		// increment the time by the random amount
		time += randomInc;

		return new Date(time);

	}

	/**
	 * gets a random string
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length, Boolean includeNumbers) {
		final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTWXYZ";
		final String NUMBERS = "0123456789";

		String charList = ALPHABET + ALPHABET.toLowerCase();

		if (includeNumbers) {
			charList += NUMBERS;
		}

		return RandomStringUtils.random(length, charList);
	}

	/**
	 * returns a random name (names are taken from a hurricane name list)
	 * 
	 * @return String name
	 */
	public static String getRandomName() {
		Random r = new Random();
		final String[] NAMES = { "Alex", "Bonnie", "Colin", "Danielle", "Earl",
				"Fiona", "Gaston", "Hermine", "Igor", "Julia", "Karl", "Lisa",
				"Matthew", "Nicole", "Otto", "Paula", "Richard", "Shary",
				"Tomas", "Virginie", "Walter", "Arlene", "Bret", "Cindy",
				"Don", "Emily", "Franklin", "Gert", "Harvey", "Irene", "Jose",
				"Katia", "Lee", "Maria", "Nate", "Ophelia", "Philippe", "Rina",
				"Sean", "Tammy", "Vince", "Whitney", "Alberto", "Beryl",
				"Chris", "Debby", "Ernesto", "Florence", "Gordon", "Helene",
				"Isaac", "Joyce", "Kirk", "Leslie", "Michael", "Nadine",
				"Oscar", "Patty", "Rafael", "Sandy", "Tony", "Valerie",
				"William", "Andrea", "Barry", "Chantal", "Dorian", "Erin",
				"Fernand", "Gabrielle", "Humberto", "Ingrid", "Jerry", "Karen",
				"Lorenzo", "Melissa", "Nestor", "Olga", "Pablo", "Rebekah",
				"Sebastien", "Tanya", "Van", "Wendy", "Arthur", "Bertha",
				"Cristobal", "Dolly", "Edouard", "Fay", "Gonzalo", "Hanna",
				"Isaias", "Josephine", "Kyle", "Laura", "Marco", "Nana",
				"Omar", "Paulette", "Rene", "Sally", "Teddy", "Vicky",
				"Wilfred", "Ana", "Bill", "Claudette", "Danny", "Erika",
				"Fred", "Grace", "Henri", "Ida", "Joaquin", "Kate", "Larry",
				"Mindy", "Nicholas", "Odette", "Peter", "Rose", "Sam",
				"Teresa", "Victor", "Wanda " };

		return NAMES[r.nextInt(NAMES.length)];

	}

	/**
	 * gets a random string in the pattern of an email address
	 * 
	 * @return
	 */
	public static String getRandomEmail() {
		return (getRandomString(8, true) + "@" + getRandomString(5, true) + ".com")
				.toLowerCase();
	}

	/**
	 * gets a string full of random numbers with a given digit count
	 * 
	 * @param i
	 * @return
	 */
	public static String getRandomNumber(int digitCount) {
		return RandomStringUtils.random(digitCount, "0123456789");
	}
	
	/**
	 * Gets an integer number in range [min, max].
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandomNumberInRange(int min, int max) {
		return min + (int)(Math.random() * ((max - min) + 1));
	}

	/**
	 * Removes a character in string.
	 * @param s
	 * @param c
	 * @return string
	 */
	public static String removeChar(String s, char c) {
		StringBuffer r = new StringBuffer(s.length());
		r.setLength(s.length());
		int current = 0;
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			char cur = s.charAt(i);
			if (cur != c) {
				r.setCharAt(current++, cur);
			} else {
				count++;
			}
		}
		
		String resultStr = r.toString();
		return resultStr.substring(0, resultStr.length() - count);		
	}
	
	/**
	 * Removes all spaces in string.
	 * @param s
	 * @return string
	 */
	public static String removeSpaces(String s) {
		return removeChar(s, ' ');
	}
	
	/**
	 * Removes all non-numeric characters in string.
	 * @param s
	 * @return string
	 */
	public static String removeNonNumeric(String s) {
		final String NUMBERS = "0123456789";

		StringBuffer r = new StringBuffer(s.length());
		r.setLength(s.length());
		int current = 0;
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			char cur = s.charAt(i);
			if (isCharInString(NUMBERS, cur)) {
				r.setCharAt(current++, cur);
			} else {
				count++;
			}
		}
		
		String resultStr = r.toString();
		return resultStr.substring(0, resultStr.length() - count);			
	}
	
	public static boolean isNumber(String s) {
		final String NUMBERS = "0123456789";
		
		boolean ret = true;
		for (int i = 0; i < s.length(); i++) {
			char cur = s.charAt(i);
			if (!isCharInString(NUMBERS, cur)) {
				ret = false;
				break;
			}
		}
		
		return ret;
	}
	
	
	/**
	 * Removes all non alphanumeric characters not including spaces.
	 * @param s
	 * @return string
	 */
	public static String removeNonAlphaNumeric(String s) {
		return s.replaceAll("[^A-Za-z0-9 ]", "");
	}
	
	/**
	 * Checks if character is in string.
	 * @param s
	 * @param c
	 * @return boolean
	 */
	public static boolean isCharInString(String s, char c) {
		boolean found = false;

		for (int i = 0; i < s.length(); i++){
			char cur = s.charAt(i);
			if (cur == c) {
				found = true;
				break;
			}
		}
		
		return found;
	}

	public static Date convertToDate(String mon, String day, String year) throws Exception {
		
		SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
		return sdf.parse(mon + "/" + day + "/" + year);
	}
	
	public static String convertNullToBlank(String obj) {
		return obj == null ? "" : obj;
	}
	
	public static String toUpperCaseNull(String str) {
		
		if (str == null) {
			return "";
		} else {
			return str.trim().toUpperCase();
		}
	}
	
	public static String trimNull(String str) {
		
		if (str == null) {
			return "";
		} else {
			return str.trim();
		}		
	}
	
	public static String[] getStringArray(String str) {
		
		String[] result = { str };
		return result;
	}
	
	public static String[] getStringArray(String str1, String str2) {
		
		String[] result = { str1, str2 };
		return result;
	}

	public static String formatSsn(String ssn) {
		
		return ssn.substring(0, 3) + "-" + ssn.substring(3, 5) + "-" + ssn.substring(5);
	}
	
	public static String formatPhone(String phone) {
		
		if (phone == null || phone.length() < 10) {
			return null;
		} else {
			return phone.substring(0, 3) + "-" + phone.substring(3, 6) + "-" + phone.substring(6);
		}
	}
	
	public static String formatZipCode(String zip) {
		
		return zip.substring(0, 5) + "-" + zip.substring(5);
	}
	
	public static boolean isStringEqual(String s1, String s2) {
		boolean ret = false;
		if (s1 == null && s2 == null) {
			ret = true;
		} else if (s1 == null && s2 != null) {
			ret = true;
		} else if (s1 != null && s1.equalsIgnoreCase(s2)) {
			ret = true;
		}
		
		return ret;
	}
	
	public static Date addDaysToDate(int days, Date date) {
		
		Date ret = new Date();
		ret.setTime(date.getTime());
		
		long daysInLong = 1000 * 60 * 60 * 24 * days;
		ret.setTime(date.getTime() + daysInLong);
				
		return ret;
	}
		
	public static boolean isDateInRange(Date startDate, Date endDate, Date startDate2, Date endDate2) {
		
		if ((Validate.compareDate(startDate, startDate2) >= 0 && Validate.compareDate(startDate, endDate2) <= 0) ||
				(Validate.compareDate(endDate, startDate2) >= 0 && Validate.compareDate(endDate, endDate2) <= 0) ||
				(Validate.compareDate(startDate2, startDate) >= 0 && Validate.compareDate(startDate2, endDate) <= 0) ||
				(Validate.compareDate(endDate2, startDate) >= 0 && Validate.compareDate(endDate2, endDate) <= 0)) {
			
			return true;
		} else {
			return false;
		}	
	}
	
	/**
	 * Copies data from input stream to file object.
	 * 
	 * @param is
	 * @param f
	 * @throws Exception
	 */
	public static void copyInputStreamToFile(InputStream is, File f) throws Exception {
		
		OutputStream os = new FileOutputStream(f);
		byte[] buf = new byte[1024];
		int len;
		while((len = is.read(buf)) > 0) {
			os.write(buf, 0, len);
		}
		
		os.close();
	}
	
	/**
	 * Gets keys by value from many-to-one map object.
	 * @param map
	 * @param value
	 * @return
	 */
	public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
		Set<T> keys = new HashSet<T>();
		for (Entry<T, E> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				keys.add(entry.getKey());
			}
		}
		return keys;
	}
	
	/**
	 * Gets key by value from one-to-one map object.
	 * @param map
	 * @param value
	 * @return
	 */
	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
		for (Entry<T, E> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	public static String initCap(String str) {
		StringBuffer sb = new StringBuffer();
		String[] words = str.split(" ");
		for (int i = 0; i < words.length; i++){
			sb.append(words[i].substring(0, 1).toUpperCase()).append(words[i].substring(1).toLowerCase()).append(" ");
		}
		
		return sb.toString().trim();
	}
	
}
