package de.matthesrieke.wolpi.settings;

import java.util.List;

import de.matthesrieke.wolpi.util.AndroidServiceLoader;

/**
 * Settings provider interface.
 * Implementations manage the available {@link HostConfiguration}s.
 * 
 * @author matthes rieke
 *
 */
public interface SettingsProvider {
	
	/**
	 * @return all available hosts
	 */
	public List<HostConfiguration> getHosts();

	/**
	 * @param value the id equivalent to {@link HostConfiguration#getId()}
	 * @return the host for the specified id
	 */
	public HostConfiguration getHostForId(String value);
	
	/**
	 * trigger a configuration reload
	 */
	public void reloadConfiguration();
	
	/**
	 * trigger a storage of the current configuration
	 */
	public void saveConfiguration();

	public void addHost(HostConfiguration host);

	public void deleteHost(HostConfiguration host);
	
	/**
	 * Provide access to the register {@link SettingsProvider}
	 * implementations.
	 * 
	 * @author matthes rieke
	 *
	 */
	public static class Instance {
		
		static SettingsProvider provider;
		
		/**
		 * @return the registered provider (first available, see
		 * {@link AndroidServiceLoader})
		 */
		public static synchronized SettingsProvider getProvider() {
			if (provider == null)
				provider = resolveProvider();
			
			return provider;
		}

		private static SettingsProvider resolveProvider() {
			AndroidServiceLoader<SettingsProvider> loader = AndroidServiceLoader.load(SettingsProvider.class);
			for (SettingsProvider settingsProvider : loader.implementations()) {
				return settingsProvider;
			}
			throw new IllegalStateException("No SettingsProvider available!");
		}
		
	}


}
