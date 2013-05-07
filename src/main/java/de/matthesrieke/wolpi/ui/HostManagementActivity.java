package de.matthesrieke.wolpi.ui;

import de.matthesrieke.wolpi.R;
import de.matthesrieke.wolpi.settings.HostConfiguration;
import de.matthesrieke.wolpi.settings.SSHConnection;
import de.matthesrieke.wolpi.settings.SettingsProvider;
import de.matthesrieke.wolpi.settings.WolSettings;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * activity for editing an existing host
 * 
 * @author matthes rieke
 *
 */
public class HostManagementActivity extends Activity {

	private HostConfiguration currentHostConfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentHostConfig = resolveHostConfiguration(savedInstanceState);
		setContentView(R.layout.host_management);
		
		Button saveButton = (Button) findViewById(R.id.save_button);
		initSaveButton(saveButton);
		
		if (this.currentHostConfig != null) {
			setTitle("Edit Host "+ currentHostConfig.getId());
			initEditFields();
		}
		else {
			setTitle("Add New Host");
		}
			
	}

	private void initEditFields() {
		SSHConnection ssh = this.currentHostConfig.getSSHConnection();
		WolSettings wol = this.currentHostConfig.getWolSettings();
		
		TextView tmp = (TextView) findViewById(R.id.edit_host);
		tmp.setText(ssh.getHost());
		
		tmp = (TextView) findViewById(R.id.edit_port);
		tmp.setText(Integer.toString(ssh.getPort()));
		
		tmp = (TextView) findViewById(R.id.edit_ssh_user);
		tmp.setText(ssh.getUser());
		
		tmp = (TextView) findViewById(R.id.edit_ssh_password);
		tmp.setText(ssh.getPassword());
		
		tmp = (TextView) findViewById(R.id.edit_wol_mac);
		tmp.setText(wol.getMacAddress());
		
		tmp = (TextView) findViewById(R.id.edit_wol_broadcast_ip);
		tmp.setText(wol.getBroadcastIp() == null ? "" : wol.getBroadcastIp());
	}

	private void initSaveButton(Button saveButton) {
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				processHostConfiguration();
				SettingsProvider.Instance.getProvider().saveConfiguration();
				returnToHostList();
			}
		});
	}

	protected void returnToHostList() {
		Intent hostList = new Intent(this, HostListActivity.class);
		startActivity(hostList);
	}

	protected void processHostConfiguration() {
		String hostText = resolveTextValue(R.id.edit_host);
		int portNumber = resolveIntValue(R.id.edit_port);
		String userText = resolveTextValue(R.id.edit_ssh_user);
		String pwText = resolveTextValue(R.id.edit_ssh_password);
		String wolMac = resolveTextValue(R.id.edit_wol_mac);
		String wolBroadcast = resolveTextValue(R.id.edit_wol_broadcast_ip);
		
		storeHostConfiguration(hostText, portNumber, userText, pwText,
				wolMac, wolBroadcast);
	}

	private void storeHostConfiguration(String hostText, int portNumber,
			String userText, String pwText, String wolMac, String wolBroadcast) {
		SSHConnection ssh;
		WolSettings wol;
		if (this.currentHostConfig == null) {
			ssh = new SSHConnection();
			wol = new WolSettings(null);
		}
		else {
			ssh = this.currentHostConfig.getSSHConnection();
			wol = this.currentHostConfig.getWolSettings();	
		}
		
		ssh.setHost(hostText);
		ssh.setPort(portNumber);
		ssh.setUser(userText);
		ssh.setPassword(pwText);
		
		wol.setMacAddress(wolMac);
		if (wolBroadcast != null && !wolBroadcast.isEmpty()) {
			wol.setBroadcastIp(wolBroadcast);
		}
		
		if (this.currentHostConfig == null) {
			this.currentHostConfig = new HostConfiguration(ssh, wol);
			SettingsProvider.Instance.getProvider().addHost(this.currentHostConfig);
		}
	}

	private int resolveIntValue(int id) {
		String number = resolveTextValue(id);
		if (number != null && !number.isEmpty())
			return Integer.parseInt(number);
		return 22;
	}

	private String resolveTextValue(int id) {
		TextView text = (TextView) findViewById(id);
		if (text != null)
			return text.getText().toString().trim();
		return null;
	}

	private HostConfiguration resolveHostConfiguration(Bundle savedInstanceState) {
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
	
}
