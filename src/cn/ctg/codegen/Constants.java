package cn.ctg.codegen;
import java.io.File;

public class Constants {
	
	public static final String SYSTEM_NAME = "代码生成器";
	public static final String SYSTEM_VERSION = " 1.0";
	public static final String TEMPLATE_ROOT_PATH;
	public static final String LOG4J_CONFIG_FILE_PATH = "log4j.properties";
	public static final String LOG4J_FILE_KEY = "log4j.appender.logfile.File";
	public static final String LOGS_PATH = "";

	static {
		TEMPLATE_ROOT_PATH = File.separatorChar + "template" + File.separatorChar;
	}
}