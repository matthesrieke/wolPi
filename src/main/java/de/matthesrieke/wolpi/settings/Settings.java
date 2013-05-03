package de.matthesrieke.wolpi.settings;


/**
 * Settings class for managing the 
 * configured hosts.
 * 
 * @author matthes rieke
 *
 */
public class Settings {

	private static Settings instance;
	
	private Settings() {
		
	}
	
	/**
	 * @return singleton instance
	 */
	public synchronized static Settings getInstance() {
		if (instance == null)
			instance = new Settings();
		return instance;
	}

	public HostConfiguration getSelectedHostConfiguration() {
		return null;
	}

}
