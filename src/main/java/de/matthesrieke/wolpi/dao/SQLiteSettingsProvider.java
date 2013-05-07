package de.matthesrieke.wolpi.dao;

import java.util.ArrayList;
import java.util.List;

import de.matthesrieke.wolpi.settings.HostConfiguration;
import de.matthesrieke.wolpi.settings.SSHConnection;
import de.matthesrieke.wolpi.settings.SettingsProvider;
import de.matthesrieke.wolpi.settings.WolSettings;

/**
 * SQLite impl of a {@link SettingsProvider}.
 * 
 * @author matthes rieke
 *
 */
public class SQLiteSettingsProvider implements SettingsProvider {

	private List<HostConfiguration> hosts = new ArrayList<HostConfiguration>();
	
	public SQLiteSettingsProvider() {
		hosts.add(new HostConfiguration(new SSHConnection("test.org", 22, "pi", "pw"),
				new WolSettings("test:s:2")));
	}
	
	@Override
	public List<HostConfiguration> getHosts() {
		return this.hosts;
	}

	@Override
	public HostConfiguration getHostForId(String value) {
		for (HostConfiguration hc : hosts) {
			if (hc.getId().equals(value)) {
				return hc;
			}
		}
		return null;
	}

	@Override
	public synchronized void reloadConfiguration() {
		// TODO Auto-generated method stub
	}

	@Override
	public synchronized void saveConfiguration() {
		// TODO Auto-generated method stub
		hosts.size();
	}

}
