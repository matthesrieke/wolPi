package de.matthesrieke.wolpi;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

public class SysoutInteractor implements Interactor {

	@Override
	public boolean requestConfirmation(String request) {
		return true;
	}

	@Override
	public void onOutput(String output, Object... inlineStrings) {
		FormattingTuple result = MessageFormatter.arrayFormat(output, inlineStrings);
		System.out.println(result.getMessage());
	}

	@Override
	public void onError(String error, Object... inlineErrors) {
		onOutput(error, inlineErrors);
	}

	@Override
	public void onOutput(String output, Located l, Object... inlineStrings) {
		onOutput(output, inlineStrings);
	}

	@Override
	public void onError(String error, Located l, Object... inlineErrors) {
		onError(error, inlineErrors);
	}

}
