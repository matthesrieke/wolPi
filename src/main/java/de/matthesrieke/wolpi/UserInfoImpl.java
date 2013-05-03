package de.matthesrieke.wolpi;

import com.jcraft.jsch.UserInfo;

/**
 * Basic {@link UserInfo} implementation.
 * 
 * @author matthes rieke
 *
 */
public class UserInfoImpl implements UserInfo {

	private String password;

	public UserInfoImpl(String password) {
		this.password = password;
	}

	@Override
	public String getPassphrase() {
		return null;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public boolean promptPassphrase(String arg0) {
		return false;
	}

	@Override
	public boolean promptPassword(String arg0) {
		//TODO Ask UI?
		return true;
	}

	@Override
	public boolean promptYesNo(String arg0) {
		// TODO Ask UI for fingerprint acceptance
		return true;
	}

	@Override
	public void showMessage(String arg0) {
		
	}

}
