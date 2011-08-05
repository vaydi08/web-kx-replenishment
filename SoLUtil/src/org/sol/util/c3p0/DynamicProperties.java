package org.sol.util.c3p0;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class DynamicProperties {
	URL resUrl = null;
	File resFile = null;
	Properties properties = new Properties();
	long lastModified = 0;
	int interval = 20000;
	long lastSystime = System.currentTimeMillis(); 

	public DynamicProperties(String resource) {
		try {
			resUrl = this.getClass().getResource(resource);
			properties.load(resUrl.openStream());
			String filename = resUrl.getFile();
			if (filename != null) {
				resFile = new File(filename);
				lastModified = resFile.lastModified();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void reload() {
		try {
			properties.load(resUrl.openStream());
		} catch (IOException e) {
			e.printStackTrace();
			// Logger.sys.error(e.getMessage(),e);
		}
	}

	public Properties getProperties() {
		if (resFile != null) {
			long sysTime = System.currentTimeMillis();
			long currLastModified = resFile.lastModified();
			if (sysTime - lastSystime > interval
					&& lastModified != currLastModified) {
				reload();
				lastSystime = sysTime;
				lastModified = currLastModified;
				// System.out.println("Reload Properties");
			}
			return properties;
		} else {
			return properties;
		}
	}

	public String getProperty(String key) {
		return getProperties().getProperty(key);
	}

	public String getProperty(String key, String def) {
		return getProperties().getProperty(key, def);
	}
}
