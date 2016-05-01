package gov.utah.dts.det.test.util;

import java.util.Date;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

/**
 * used for generating random content for tests
 * 
 * @author cardwell
 * 
 */
public class TestUtils {

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
	 * @return
	 */
	public static String getRandomEmail() {
		return (getRandomString(8, true) + "@" + getRandomString(5, true) + ".com").toLowerCase();
	}

	/**
	 * gets a string full of random numbers with a given digit count
	 * @param i
	 * @return
	 */
	public static String getRandomNumber(int digitCount) {
		return RandomStringUtils.random(digitCount, "0123456789");	
	}

}
