package cn.ctg.codegen.export.ruleengine;

import cn.ctg.codegen.export.data.Rule;

public interface RuleEngine {
	String process(Rule var1, Object... var2);
}