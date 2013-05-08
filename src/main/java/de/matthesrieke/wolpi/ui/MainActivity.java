package de.matthesrieke.wolpi.ui;

import de.matthesrieke.wolpi.Interactor;
import de.matthesrieke.wolpi.R;
import de.matthesrieke.wolpi.WolPi;
import de.matthesrieke.wolpi.settings.HostConfiguration;
import de.matthesrieke.wolpi.settings.SettingsProvider;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
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

	private HostConfiguration host;

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

		this.host = resolveSelectedHostConfiguration(savedInstanceState);
	}

	private HostConfiguration resolveSelectedHostConfiguration(
			Bundle savedInstanceState) {
		Bundle b;
		if (savedInstanceState != null)
			b = savedInstanceState;
		else
			b = getIntent().getExtras();

		if (b == null)
			return null;

		String value = b.getString("hostId");
		return SettingsProvider.Instance.getProvider().getHostForId(value);
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

		if (host == null) {
			in.onError("No configuration available! Define one in the Settings.");
			return;
		}

		WolPi wolPi = new WolPi(host.getSSHConnection(), in);

		wolPi.executeWakeOnLan(host.getWolSettings());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(this, HostListActivity.class);
			startActivityForResult(intent, HostListActivity.REQUEST_CODE);
			return true;
		case R.id.action_about:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == HostListActivity.REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Bundle result = data.getExtras();
				String hostId = result.getString("hostId");
				host = SettingsProvider.Instance.getProvider().getHostForId(hostId);
			}
		}
	}
}
