package de.matthesrieke.wolpi;

import org.junit.Test;
import org.slf4j.helpers.MessageFormatter;

import de.matthesrieke.wolpi.settings.SSHConnection;
import de.matthesrieke.wolpi.settings.WolSettings;

public class ExpectExceptionTest {

	@Test(expected=WolPiException.class)
	public void awaitException() throws WolPiException, InterruptedException {
		final Object mutex = new Object();
		final ExceptionWrapper wrapper = new ExceptionWrapper();
		
		WolPi wolpi = new WolPi(new SSHConnection("localhost", -1, "/dev/null", "no"),
				new SysoutInteractor() {

			@Override
			public void onError(String error, Located l,
					Object... inlineErrors) {
				this.onError(error, inlineErrors);
			}

			@Override
			public void onError(String error, Object... inlineErrors) {
				synchronized (mutex) {
					wrapper.setEx(new Exception(MessageFormatter.arrayFormat(error,
							inlineErrors).getMessage()));
					mutex.notifyAll();
				}
			}

		});

		wolpi.executeWakeOnLan(new WolSettings("d"));
		
		synchronized (mutex) {
			while (wrapper.getEx() == null) {
				mutex.wait(5000);
			}
		}
		
		if (wrapper.getEx() != null) throw new WolPiException(wrapper.getEx());
	}
	
	private class ExceptionWrapper {
		
		private Exception ex = null;

		public Exception getEx() {
			return ex;
		}

		public void setEx(Exception ex) {
			this.ex = ex;
		}
		
	}

}
