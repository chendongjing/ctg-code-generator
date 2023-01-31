package cn.ctg.codegen.export.ruleengine;

import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import cn.ctg.codegen.export.data.Rule;
import cn.ctg.codegen.util.Utils;

public class RhinoRuleEngine implements RuleEngine {
	private static final Logger _log = Logger.getLogger(RhinoRuleEngine.class);
	private final ReentrantLock lock = new ReentrantLock();
	private static RhinoRuleEngine _instance = null;
	private Context ctx = null;
	private Scriptable scope = null;

	private RhinoRuleEngine() {
		this.ctx = Context.enter();
		this.scope = this.ctx.initStandardObjects();
	}

	public static RhinoRuleEngine getInstance() {
		if (_instance == null) {
			_instance = new RhinoRuleEngine();
		}

		return _instance;
	}

	public String process(Rule rule, Object... args) {
		String result = null;

		try {
			try {
				if (Utils.isRealEmpty(rule.getFunctionName())) {
					throw new IllegalArgumentException(rule.getName() + ": function of name is null!");
				}

				Function f = this.ctx.compileFunction(this.scope, rule.getValue(), "ddd", 1, (Object) null);
				Object o = f.call(this.ctx, this.scope, (Scriptable) null, args);
				_log.debug("Rule:" + rule);
				result = Utils.strip(o.toString());
			} catch (Exception var9) {
				_log.warn(var9.getMessage(), var9);
			}

			return result;
		} finally {
			;
		}
	}

	public String toString() {
		return RhinoRuleEngine.class.getName();
	}
}