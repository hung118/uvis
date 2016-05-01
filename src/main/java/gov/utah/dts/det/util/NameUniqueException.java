package gov.utah.dts.det.util;

/**
 * Exception class for thrown unique name exception.
 * 
 * @author hnguyen
 *
 */
public class NameUniqueException extends Exception {

	private static final long serialVersionUID = 1L;

	public NameUniqueException() {
		super("Unique Name Exception");
	}
	
	public NameUniqueException(String message) {
		super(message);
	}
}
