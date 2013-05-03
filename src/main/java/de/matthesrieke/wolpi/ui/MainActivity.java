package de.matthesrieke.wolpi.ui;


import de.matthesrieke.wolpi.Interactor;
import de.matthesrieke.wolpi.R;
import de.matthesrieke.wolpi.WolPi;
import de.matthesrieke.wolpi.settings.HostConfiguration;
import de.matthesrieke.wolpi.settings.Settings;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Basic activity for the app.
 * 
 * @author matthes rieke
 *
 */
public class MainActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button b = (Button) findViewById(R.id.button2);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startPressed();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void startPressed() {
		final TextView textView = (TextView) findViewById(R.id.textView);
		textView.append(System.getProperty("line.separator"));
		textView.append("Executing WOL call...");
		
		Interactor in = new TextViewInteractor(this, textView);
		
		HostConfiguration host = Settings.getInstance().getSelectedHostConfiguration();
		
		if (host == null) {
			in.onError("No configuration available! Define one in the Settings.");
			return;
		}
		
		WolPi wolPi = new WolPi(host.getSSHConnection(),
				in);
		
		wolPi.executeWakeOnLan(host.getWolSettings());
	}

}
