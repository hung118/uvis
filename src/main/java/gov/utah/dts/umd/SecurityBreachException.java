package gov.utah.dts.umd;

/**
 * Security exception class.
 * 
 * @author hnguyen
 *
 */
public class SecurityBreachException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SecurityBreachException() {
		super("Security Exception");
	}
	
	public SecurityBreachException(String message) {
		super(message);
	}
	
}
