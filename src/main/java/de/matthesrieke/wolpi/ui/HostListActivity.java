package de.matthesrieke.wolpi.ui;

import java.util.ArrayList;
import java.util.List;

import de.matthesrieke.wolpi.R;
import de.matthesrieke.wolpi.settings.HostConfiguration;
import de.matthesrieke.wolpi.settings.SettingsProvider;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * activity for listing all stored hosts.
 * 
 * @author matthes rieke
 * 
 */
public class HostListActivity extends Activity {

	public static final int REQUEST_CODE = 1;
	private List<String> hostList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.host_list);

		loadHostConfigurations();
	}

	private void loadHostConfigurations() {
		ListView listView = (ListView) findViewById(R.id.listView);
		hostList = new ArrayList<String>();
		for (HostConfiguration hc : SettingsProvider.Instance.getProvider()
				.getHosts()) {
			hostList.add(createEntryForHost(hc));
		}

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, hostList);
		listView.setAdapter(adapter);

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final String item = (String) parent.getItemAtPosition(position);
				openLongClickMenu(item);
				return true;
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final String item = (String) parent.getItemAtPosition(position);
				Intent intent = new Intent();
				Bundle b = new Bundle();
				b.putString("hostId", item);
				intent.putExtras(b);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	protected void openLongClickMenu(final String item) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("host options");

		builder.setItems(new CharSequence[] {
				getText(R.string.dialog_host_edit),
				getText(R.string.dialog_host_delete) },
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:	
							startHostManagementActivity(item);
							break;
						case 1:
							deleteHostForId(item);
							break;
						default:
							break;
						}
					}
				});
		
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	protected void startHostManagementActivity(String item) {
		Intent intent = new Intent(HostListActivity.this, HostManagementActivity.class);
		Bundle b = new Bundle();
		b.putString("hostId", item);
		intent.putExtras(b);
		startActivityForResult(intent, HostManagementActivity.REQUEST_CODE);		
	}

	protected void deleteHostForId(String item) {
		if (hostList != null) {
			hostList.remove(item);
			SettingsProvider.Instance.getProvider().deleteHost(
					SettingsProvider.Instance.getProvider().getHostForId(item));
			loadHostConfigurations();
		}
	}

	private String createEntryForHost(HostConfiguration hc) {
		String hostName = hc.getId();
		return hostName;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.host_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add_new_host:
			Intent intent = new Intent(this, HostManagementActivity.class);
			startActivityForResult(intent, HostManagementActivity.REQUEST_CODE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == HostManagementActivity.REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				loadHostConfigurations();
			}
		}
	}

}
