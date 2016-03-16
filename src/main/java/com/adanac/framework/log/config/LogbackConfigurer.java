package com.adanac.framework.log.config;

import java.io.FileNotFoundException;
import java.io.StringReader;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.util.SystemPropertyUtils;
import org.xml.sax.InputSource;

import com.adanac.framework.log.LogConfigManager;
import com.adanac.framework.log.exception.LogException;
import com.adanac.framework.uniconfig.client.UniconfigClient;
import com.adanac.framework.uniconfig.client.UniconfigClientImpl;
import com.adanac.framework.uniconfig.client.UniconfigListener;
import com.adanac.framework.uniconfig.client.UniconfigNode;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * 功能描述： 根据logback指定配置文件目录及文件
 * 名实现对logback的初始化
 * eg: convert property value of type 'java.lang.String' to required type 'java.lang.Class'
 * @author adanac
 * @version 1.0
 */
public class LogbackConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(LogConfigManager.class);

	/** Pseudo URL prefix for loading from the class path: "classpath:" */
	public static final String CLASSPATH_URL_PREFIX = "classpath:";

	/** Extension that indicates a log4j XML config file: ".xml" */
	public static final String XML_FILE_EXTENSION = ".xml";

	private static final String GLOBAL_LOG_CONFIG_PATH = "/log.global";

	private static volatile UniconfigNode appConfigNode;

	private static volatile UniconfigNode globalConfigNode;

	private static volatile String configLocation = "";

	private static volatile Map<String, UniconfigNode> globalPieceConfigNodes = new ConcurrentHashMap<String, UniconfigNode>();

	private static UniconfigListener listener = new UniconfigListener() {
		@Override
		public void execute(String oldValue, String newValue) {
			initWithUnitConfig(configLocation);
		}
	};

	public static void initLogging(String location) throws FileNotFoundException, JoranException {

		String lowerStr = location.toLowerCase();
		if (0 == lowerStr.indexOf(CLASSPATH_URL_PREFIX) && lowerStr.endsWith(XML_FILE_EXTENSION)) {
			initWithFile(location);
		} else {
			configLocation = location;
			initWithUnitConfig(location);
		}
	}

	private static void initWithFile(String location) throws FileNotFoundException, JoranException {
		String resolvedLocation = SystemPropertyUtils.resolvePlaceholders(location);
		URL url = ResourceUtils.getURL(resolvedLocation);
		if (resolvedLocation.toLowerCase().endsWith(XML_FILE_EXTENSION)) {
			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
			loggerContext.reset();
			JoranConfigurator joranConfigurator = new JoranConfigurator();
			joranConfigurator.setContext(loggerContext);
			joranConfigurator.doConfigure(url);
		}
	}

	private static void initWithUnitConfig(String location) {
		UniconfigClient client = UniconfigClientImpl.getInstance();
		if (appConfigNode == null) {
			appConfigNode = client.getConfig(location);
			appConfigNode.sync();
			appConfigNode.monitor(listener);
		}
		String appConfig = appConfigNode.getValue();
		String globalConfigs = getGlobalConfigs();
		if (appConfig != null && !appConfig.equals("")) {
			String logConfig = combineLogConfig(appConfig, globalConfigs);
			StringReader reader = new StringReader(logConfig);
			InputSource is = new InputSource(reader);
			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
			loggerContext.reset();
			JoranConfigurator joranConfigurator = new JoranConfigurator();
			joranConfigurator.setContext(loggerContext);
			try {
				joranConfigurator.doConfigure(is);
			} catch (JoranException ex) {
				logger.warn("Exception:", ex);
			}
		}
	}

	private static String getGlobalConfigs() {
		UniconfigClient client = UniconfigClientImpl.getInstance();
		if (globalConfigNode == null) {
			globalConfigNode = client.getGlobalConfig(GLOBAL_LOG_CONFIG_PATH);
			globalConfigNode.sync();
			globalConfigNode.monitor(listener);
		}
		String globalConfigPaths = globalConfigNode.getValue();
		Map<String, UniconfigNode> newPiecesMap = new ConcurrentHashMap<String, UniconfigNode>();
		if (globalConfigPaths != null && !globalConfigPaths.trim().equals("")) {
			String[] piecePaths = globalConfigPaths.split("\\,");
			for (String piecePath : piecePaths) {
				UniconfigNode node = globalPieceConfigNodes.get(piecePath);
				if (node == null) {
					node = client.getGlobalConfig(piecePath);
					node.sync();
					node.monitor(listener);
				}
				newPiecesMap.put(piecePath, node);
			}
		}
		for (Map.Entry<String, UniconfigNode> entry : globalPieceConfigNodes.entrySet()) {
			if (!newPiecesMap.containsKey(entry.getKey())) {
				entry.getValue().destroy();
			}
		}
		globalPieceConfigNodes = newPiecesMap;
		StringBuilder sb = new StringBuilder();
		for (UniconfigNode node : globalPieceConfigNodes.values()) {
			String value = node.getValue();
			if (value != null && !"".equals(value.trim())) {
				sb.append(node.getValue());
				sb.append("\n");
			}
		}
		String configs = sb.toString();
		checkGlobalConfigFormat(configs);
		return configs;
	}

	private static String combineLogConfig(String appConfig, String globalConfigs) {
		return appConfig.split("</configuration>")[0] + "\n" + globalConfigs + "\n" + "</configuration>";
	}

	private static void checkGlobalConfigFormat(String globalLogConfig) {
		if (globalLogConfig.contains("<configuration") || globalLogConfig.contains("</configuration>")
				|| globalLogConfig.contains("<root") || globalLogConfig.contains("</root>")) {
			throw new LogException("'<configuration>','<root>' can't exist in 'log.global'.");
		}
	}
}
