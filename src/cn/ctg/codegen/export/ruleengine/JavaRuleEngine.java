package cn.ctg.codegen.export.ruleengine;

import java.util.concurrent.locks.ReentrantLock;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.apache.log4j.Logger;

import cn.ctg.codegen.export.data.Rule;
import cn.ctg.codegen.util.Utils;

public class JavaRuleEngine implements RuleEngine {
	private static final Logger _log = Logger.getLogger(JavaRuleEngine.class);
	private final ReentrantLock lock = new ReentrantLock();
	private static JavaRuleEngine _instance = null;
	private ScriptEngineManager factory = null;
	private ScriptEngine engine = null;

	private JavaRuleEngine() {
		this.factory = new ScriptEngineManager();
	}

	public static JavaRuleEngine getInstance() {
		if (_instance == null) {
			_instance = new JavaRuleEngine();
		}

		return _instance;
	}

	public String process(Rule rule, Object... args) {
		this.lock.lock();
		String result = null;

		try {
			if (Utils.isRealEmpty(rule.getFunctionName())) {
				throw new IllegalArgumentException(rule.getName() + ": function of name is null!");
			}

			this.engine = this.factory.getEngineByName(rule.getType());
			this.engine.eval(rule.getValue());
			Invocable inv = (Invocable) this.engine;
			Object o = inv.invokeFunction(rule.getFunctionName(), args);
			result = Utils.strip(o.toString());
		} catch (Exception var9) {
			_log.warn(var9.getMessage(), var9);
		} finally {
			this.lock.unlock();
		}

		return result;
	}

	public String toString() {
		return JavaRuleEngine.class.getName();
	}
}