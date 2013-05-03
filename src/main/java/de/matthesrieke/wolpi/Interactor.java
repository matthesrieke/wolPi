package de.matthesrieke.wolpi;

/**
 * Interface providing interaction methods.
 * Those are called from the {@link WolPi} class.
 * 
 * @author matthes rieke
 *
 */
public interface Interactor {

	/**
	 * The location of the output. Can be used
	 * for different styling.
	 */
	enum  Located {
		LOCAL, REMOTE
	}
	
	/**
	 * request confirmation from user
	 * 
	 * @param request the question
	 * @return user answer
	 */
	boolean requestConfirmation(String request);

	/**
	 * vargs styled output. Usage:
	 * onOutput("test {}: {}", str1, obj2);
	 */
	void onOutput(String output, Object... inlineStrings);
	
	/**
	 * vargs styled output. Usage:
	 * onOutput("test {}: {}", Located.LOCAL, str1, obj2);
	 */
	void onOutput(String output, Located l, Object... inlineStrings);

	/**
	 * vargs styled output. Usage:
	 * onError("test {}: {}", str1, obj2);
	 */
	void onError(String error, Object... inlineErrors);
	
	/**
	 * vargs styled output. Usage:
	 * onError("test {}: {}", Located.LOCAL, str1, obj2);
	 */
	void onError(String error, Located l, Object... inlineErrors);
}
