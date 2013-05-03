package de.matthesrieke.wolpi.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;

/**
 * A convenience class providing a simple dialog
 * implementation run on the UI thread.
 * 
 * @author matthes rieke
 *
 */
public class ConfirmationDialog {

	/**
	 * result wrapper to overcome "final" declarations
	 */
	private static class DialogButtonResultProcessor {
		
		private Boolean result = null;

		public Boolean isResult() {
			return result;
		}

		public void setResult(Boolean result) {
			this.result = result;
		}
		
	}
	
	/**
	 * true/false listener which invokes a notifyAll() on
	 * a dedicated waiter mutex object.
	 */
	private class TwoWayClickListener implements OnClickListener {

		private boolean resultState;
		private DialogButtonResultProcessor target;
		private Object waiterMutex;

		public TwoWayClickListener(boolean resultState, DialogButtonResultProcessor target,
				Object waiterMutex) {
			this.resultState = resultState;
			this.target = target;
			this.waiterMutex = waiterMutex;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			this.target.setResult(this.resultState);
			
			synchronized (waiterMutex) {
				waiterMutex.notifyAll();
			}
		}
		
	}

	/**
	 * Issue a confirmation dialog. The dialog execution is pushed
	 * onto the UI thread. The current thread will wait until the dialog
	 * is confirmed or dismissed/cancelled. 
	 * 
	 * @param request the question
	 * @param parentActivity the owning {@link Activity}
	 * @return true/false to the answer
	 */
	public static boolean issueConfirmationDialog(final String request, Activity parentActivity) {
		ConfirmationDialog confDialog = new ConfirmationDialog(request, parentActivity);
		return confDialog.evaluate();
	}
	
	private String message;
	private Activity parentActivity;
	
	
	private ConfirmationDialog(String request, Activity parentActivity) {
		this.parentActivity = parentActivity;
		this.message = request;
	}
	
	private boolean evaluate() {
		final Object mutex = new Object();
		final DialogButtonResultProcessor result = new DialogButtonResultProcessor();
		
		final OnClickListener yesListener = new TwoWayClickListener(true, result, mutex);
		final OnClickListener noListener = new TwoWayClickListener(false, result, mutex);
		
		/*
		 * execute on UI thread
		 */
		this.parentActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Builder builder = new AlertDialog.Builder(ConfirmationDialog.this.parentActivity);
				builder.setMessage(ConfirmationDialog.this.message);
				builder.setPositiveButton("Yes", yesListener);
				builder.setNegativeButton("No", noListener);
				AlertDialog dialog = builder.create();
				dialog.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						result.setResult(false);
						
						synchronized (mutex) {
							mutex.notifyAll();
						}
					}
				});
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
			}
			
		});
		
		/*
		 * wait for the dialog to close
		 */
		synchronized (mutex) {
			while (result.isResult() == null) {
				try {
					mutex.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		return result.isResult();
	}

}
