package de.matthesrieke.wolpi.settings;

/**
 * Wake-On-Lan specific settings, covering
 * broadcast IP and target MAC address for the magic
 * packet.
 * 
 * @author matthes rieke
 *
 */
public class WolSettings {

	private static final String DEFAULT_BROADCAST_IP = "192.168.0.255";
	private String broadcastIp;
	private String macAddress;
	
	public WolSettings(String broadcastIp, String macAddress) {
		this.broadcastIp = broadcastIp;
		this.macAddress = macAddress;
	}

	public WolSettings(String mac) {
		this(DEFAULT_BROADCAST_IP, mac);
	}

	public String getBroadcastIp() {
		return broadcastIp;
	}

	public void setBroadcastIp(String broadcastIp) {
		this.broadcastIp = broadcastIp;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
}
