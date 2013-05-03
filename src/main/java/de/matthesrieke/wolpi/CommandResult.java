package de.matthesrieke.wolpi;

/**
 * Result of a SSH Command containing
 * stdout, stderr and exit code.
 * 
 * @author matthes rieke
 *
 */
public class CommandResult {

	private String stdout;
	private String stderr;
	private int exitStatus;
	
	
	public CommandResult(String stdout, String stderr, int exitStatus) {
		this.stdout = stdout;
		this.stderr = stderr;
		this.exitStatus = exitStatus;
	}
	
	public String getStdout() {
		return stdout;
	}

	public String getStderr() {
		return stderr;
	}

	public int getExitStatus() {
		return exitStatus;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(":");
		sb.append(System.getProperty("line.separator"));
		sb.append("stdout: ");
		sb.append(stdout);
		sb.append(System.getProperty("line.separator"));
		sb.append("stderr: ");
		sb.append(stderr);
		sb.append(System.getProperty("line.separator"));
		sb.append("exit code: ");
		sb.append(exitStatus);
		return sb.toString();
	}
	
}
