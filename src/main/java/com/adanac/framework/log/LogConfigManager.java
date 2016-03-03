package com.adanac.framework.log;

import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.logging.LogException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.xml.sax.InputSource;

import com.adanac.framework.client.UniconfigClient;
import com.adanac.framework.client.UniconfigClientImpl;
import com.adanac.framework.client.UniconfigListener;
import com.adanac.framework.client.UniconfigNode;
import com.adanac.framework.statistics.VersionStatistics;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * 
 * @author adanac
 * @version 1.0
 */
public class LogConfigManager {
	private static final Logger logger = LoggerFactory.getLogger(LogConfigManager.class);

	private static final String APP_LOG_CONFIG_PATH = "/log.config";

	private static final String GLOBAL_LOG_CONFIG_PATH = "/log.global";

	private static volatile UniconfigNode appConfigNode;

	private static volatile UniconfigNode globalConfigNode;

	private static volatile Map<String, UniconfigNode> globalPieceConfigNodes = new ConcurrentHashMap<String, UniconfigNode>();

	private static UniconfigListener listener = new UniconfigListener() {
		@Override
		public void execute(String oldValue, String newValue) {
			doLogConfigure();
		}
	};

	static {
		SLF4JBridgeHandler.install();
		VersionStatistics.reportVersion(LogConfigManager.class);
	}

	public static void init() {
		doLogConfigure();
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

	protected synchronized static void doLogConfigure() {
		logger.info("begin do log configure.");
		UniconfigClient client = UniconfigClientImpl.getInstance();
		if (appConfigNode == null) {
			appConfigNode = client.getConfig(APP_LOG_CONFIG_PATH);
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
