package de.matthesrieke.wolpi.ui;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import android.app.Activity;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import de.matthesrieke.wolpi.Interactor;

/**
 * {@link Interactor} implementation writing
 * content to a {@link TextView}, emulating consolish style.
 * 
 * @author matthes rieke
 *
 */
public class TextViewInteractor implements Interactor {
	
	private Activity parentActivity;
	private TextView targetTextView;

	public TextViewInteractor(Activity parentActivity, TextView targetTextView) {
		this.parentActivity = parentActivity;
		this.targetTextView = targetTextView;
	}

	@Override
	public boolean requestConfirmation(String request) {
		return ConfirmationDialog.issueConfirmationDialog(request, parentActivity);
	}
	
	@Override
	public void onOutput(final String output, final Located l, final Object... inlineStrings) {
		parentActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				targetTextView.append(System.getProperty("line.separator"));
				int color = resolveColor(l,	targetTextView.getTextColors().getDefaultColor());
				FormattingTuple result = MessageFormatter.arrayFormat(output, inlineStrings);
				appendColoredText(targetTextView, result.getMessage(), color);
			}
		});
	}
	
	protected int resolveColor(Located l, int defaultColor) {
		if (l == Located.LOCAL) {
			return defaultColor;
		}
		else {
			return Color.RED;
		}
	}

	@Override
	public void onOutput(String output, Object... inlineStrings) {
		onOutput(output, Located.LOCAL, inlineStrings);
	}
	
	@Override
	public void onError(String error, Located l, Object... inlineErrors) {
		onOutput(error, l, inlineErrors);
	}
	
	@Override
	public void onError(String error, Object... inlineErrors) {
		onError(error, Located.LOCAL, inlineErrors);
	}
	
	private void appendColoredText(TextView tv, String text, int color) {
	    int start = tv.getText().length();
	    tv.append(text);
	    int end = tv.getText().length();

	    Spannable spannableText = (Spannable) tv.getText();
	    spannableText.setSpan(new ForegroundColorSpan(color), start, end, 0);
	}

}
