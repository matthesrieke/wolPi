package de.matthesrieke.wolpi.settings;

/**
 * Pojo for an SSH connection
 * 
 * @author matthes rieke
 *
 */
public class SSHConnection {
	
	private String host;
	private int port;
	private String user;
	private String password;
	
	public SSHConnection(String host, int port, String user,
			String password) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Host: ");
		sb.append(host);
		sb.append(", Port: ");
		sb.append(port);
		sb.append(", User: ");
		sb.append(user);
		
		return sb.toString();
	}

}
