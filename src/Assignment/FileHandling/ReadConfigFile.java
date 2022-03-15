package Assignment.FileHandling;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadConfigFile {

	static Logger logg = LogManager.getLogger(EmployeeRecordMaintainance.class.getName());
	public static Properties p = new Properties();

	// the following function returns the properties instance which can be used
	// to access the configuration files
	public static void getFile() {
		// try with resources to open and access the configuration file
		try (FileInputStream propertyfile = new FileInputStream("./resources/config.properties")) {
			p.load(propertyfile);
		}
		// logs the error and exists the system in case of empty config file
		catch (IOException e) {
			logg.error("Issue With user defined config file: empty/missing, Defalut Configs used");
		}
	}
	public static String getResources(String key) {
		return p.getProperty(key);
	}

}
