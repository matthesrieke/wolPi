package de.matthesrieke.wolpi.settings;


/**
 * Configuration instance for a (stored) host
 * 
 * @author matthes rieke
 *
 */
public class HostConfiguration {

	private SSHConnection sshConnection;
	private WolSettings wolSettings;
	
	public HostConfiguration(SSHConnection c,
			WolSettings w) {
		this.sshConnection = c;
		this.wolSettings = w;
	}
	
	public SSHConnection getSSHConnection() {
		return sshConnection;
	}
	
	public WolSettings getWolSettings() {
		return wolSettings;
	}
	
}
