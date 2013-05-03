package de.matthesrieke.wolpi;

/**
 * generic Wol-related exception
 * 
 * @author matthes rieke
 *
 */
public class WolPiException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WolPiException() {
		super();
	}

	public WolPiException(String message, Throwable cause) {
		super(message, cause);
	}

	public WolPiException(String message) {
		super(message);
	}

	public WolPiException(Throwable cause) {
		super(cause);
	}

	
}
