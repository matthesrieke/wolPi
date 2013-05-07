package de.matthesrieke.wolpi.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.matthesrieke.wolpi.R;
import de.matthesrieke.wolpi.settings.HostConfiguration;
import de.matthesrieke.wolpi.settings.SettingsProvider;
import android.app.Activity;
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
	
	private Map<String, HostConfiguration> stringToHost = new HashMap<String, HostConfiguration>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.host_list);
		
		loadHostConfigurations((ListView) findViewById(R.id.listView));
	}

	private void loadHostConfigurations(ListView listView) {
		ArrayList<String> list = new ArrayList<String>();
		for (HostConfiguration hc : SettingsProvider.Instance.getProvider().getHosts()) {
			list.add(createEntryForHost(hc));
		}
		
	    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1, list);
	        listView.setAdapter(adapter);
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final String item = (String) parent.getItemAtPosition(position);
				Intent intent = new Intent(HostListActivity.this, HostManagementActivity.class);
				Bundle b = new Bundle();
				b.putString("hostId", item);
				intent.putExtras(b);
				startActivity(intent);
				return true;
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final String item = (String) parent.getItemAtPosition(position);
				Intent intent = new Intent(HostListActivity.this, MainActivity.class);
				Bundle b = new Bundle();
				b.putString("hostId", item);
				intent.putExtras(b);
				startActivity(intent);
			}
		});
	}

	private String createEntryForHost(HostConfiguration hc) {
		String hostName = hc.getId();
		stringToHost.put(hostName, hc);
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
	        	startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
