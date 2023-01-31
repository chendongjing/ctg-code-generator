package cn.ctg.codegen.export.ruleengine;

import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;

public class RuleEngineManager {
	private static final Logger _log = Logger.getLogger(RuleEngineManager.class);
	private static final ReentrantLock lock = new ReentrantLock();
	private static RuleEngineManager _instance = null;
	private RuleEngine ruleEngine;
	private String version = System.getProperty("java.version");

	private RuleEngineManager() {
		_log.debug("java.version:" + this.version);
		if ("1.6".compareTo(this.version) > 0) {
			this.ruleEngine = RhinoRuleEngine.getInstance();
		} else {
			this.ruleEngine = JavaRuleEngine.getInstance();
		}

		_log.debug("RuleEngine:" + this.ruleEngine);
	}

	public static RuleEngineManager getInstance() {
		lock.lock();

		try {
			if (_instance == null) {
				_instance = new RuleEngineManager();
			}
		} catch (Exception var4) {
			_log.warn(var4.getMessage(), var4);
		} finally {
			lock.unlock();
		}

		return _instance;
	}

	public RuleEngine getRuleEngine() {
		return this.ruleEngine;
	}
}