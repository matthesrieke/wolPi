package de.matthesrieke.wolpi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import de.matthesrieke.wolpi.Interactor.Located;
import de.matthesrieke.wolpi.settings.SSHConnection;
import de.matthesrieke.wolpi.settings.WolSettings;

/**
 * Wake-On-Lan through Raspberry Pi (or other Debian-based
 * Linux systems).
 * 
 * @author matthes rieke
 *
 */
public class WolPi {
	
	private static final Logger logger = LoggerFactory.getLogger(WolPi.class);

	private SSHConnection connection;
	private Interactor interactor;
	private JSch jsch;

	private Session session;

	public WolPi(SSHConnection conn, Interactor in) {
		this.connection = conn;
		this.interactor = in;
	}

	private boolean checkWakeOnLanAvailable() throws JSchException, IOException {
		CommandResult result = executeCommand("dpkg -s wakeonlan");
		
		if (result.getExitStatus() == 0) {
			//TODO do logging?
			return true;
		}
		
		return false;
	}

	private void closeConnection() {
		interactor.onOutput("Closing connection to host '{}'.", connection.getHost());
		
		session.disconnect();		
	}

	private void establishConnection() throws JSchException {
		interactor.onOutput("Connecting to '{}' with configuration: {}", connection.getHost(),
				connection);
		
		jsch = new JSch();	
		session = jsch.getSession(connection.getUser(), connection.getHost(),
				connection.getPort());
		session.setUserInfo(new UserInfoImpl(connection.getPassword()));
		session.connect();
	}

	private CommandResult executeCommand(String command) throws JSchException, IOException {
		interactor.onOutput("Executing shell command '{}'...", command);
		
		Channel channel = session.openChannel("exec");
		((ChannelExec) channel).setCommand(command);

		channel.setInputStream(null);

		ByteArrayOutputStream err = new ByteArrayOutputStream();
		((ChannelExec) channel).setErrStream(err);

		InputStream in = channel.getInputStream();

		channel.connect();

		StringBuilder sb = new StringBuilder();
		byte[] tmp = new byte[1024];
		while (true) {
			while (in.available() > 0) {
				int i = in.read(tmp, 0, 1024);
				if (i < 0)
					break;
				sb.append(new String(tmp, 0, i));
			}
			
			if (channel.isClosed())
				break;
			
			try {
				Thread.sleep(500);
			} catch (Exception ee) {
				logger.warn(ee.getMessage(), ee);
			}
		}
		
		channel.disconnect();
		
		interactor.onOutput("Execution of command '{}' terminated with exit status {}.",
				command, channel.getExitStatus());
		
		return new CommandResult(sb.toString().trim(), err.toString().trim(), channel.getExitStatus());
	}


	/**
	 * Execute a remote WOL call. informs the {@link #interactor}
	 * with status updates.
	 * 
	 * @param wolSettings the wol-specific settings
	 */
	public void executeWakeOnLan(final WolSettings wolSettings) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				interactor.onOutput("#### start wol call ####", Located.LOCAL);
				
				try {
					establishConnection();
				} catch (JSchException e) {
					interactor.onError("Error connecting to RPi: {}.", e);
					return;
				}
				
				try {
					if (!checkWakeOnLanAvailable()) {
						if (interactor.requestConfirmation("Module 'wakeonlan' not found on the remote machine. " +
								"Install 'wakeonlan' via apt-get?")) {
							installWakeOnLan();
						}
						else {
							interactor.onOutput("Aborting.");
							return;
						}
					}
				} catch (JSchException e) {
					interactor.onError("Exception: {}.", e);
					return;
				} catch (IOException e) {
					interactor.onError("Exception: {}.", e);
					return;
				} catch (WolPiException e) {
					interactor.onError("Exception: {}.", e);
					return;
				}
				
				
				String command = "wakeonlan -i " + wolSettings.getBroadcastIp() + " " + wolSettings.getMacAddress();
				
				command = "date";
				
				CommandResult result;
				try {
					result = executeCommand(command);

					interactor.onOutput("{}@{}:~$ {}", Located.REMOTE, connection.getUser(),
							connection.getHost(), command);
					if (result.getExitStatus() == 0) {
						interactor.onOutput(result.getStdout(), Located.REMOTE);
					}
					else {
						interactor.onOutput(result.getStderr(), Located.REMOTE);
					}
				} catch (JSchException e) {
					interactor.onError("Exception: {}.", e);
					return;
				} catch (IOException e) {
					interactor.onError("Exception: {}.", e);
					return;
				}
				
				closeConnection();
			}
		}).start();
	}

	private void installWakeOnLan() throws JSchException, IOException, WolPiException {
		CommandResult result = executeCommand("sudo apt-get --yes --force-yes install wakeonlan");
		
		if (result.getExitStatus() == 0) {
			//TODO do logging?
		}
		else {
			throw new WolPiException("Installation of 'wakeonlan' failed: "+ result);
		}
	}

}
