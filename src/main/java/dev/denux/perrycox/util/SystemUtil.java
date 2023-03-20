package dev.denux.perrycox.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * A utility class that gets you information about the runtime and system.
 */
@Slf4j
public class SystemUtil {

	private SystemUtil() {}

	/**
	 * Tries to get the current operating system.
	 * @return the current system, as a {@link String}.
	 */
	public static String getOperationSystem() {
		String os = System.getProperty("os.name");
		if (os.equals("Linux")) {
			try {
				String[] cmd = {"/bin/sh", "-c", "cat /etc/*-release"};
				Process p = Runtime.getRuntime().exec(cmd);
				try (BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
					String line;
					while ((line = bri.readLine()) != null) {
						if (line.startsWith("PRETTY_NAME")) {
							return line.split("\"")[1];
						}
					}
				}
			} catch (IOException exception) {
				log.error("Error while getting Linux Distribution.");
			}
		}
		return os;
	}

	/**
	 * Tries to get the current name of the host or server.
	 * @return the name, as a {@link String}.
	 */
	public static String getHostname() {
		String hostname = "unknown";
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException exception) {
			log.error("Error while getting the host name.");
		}
		return hostname;
	}
}
