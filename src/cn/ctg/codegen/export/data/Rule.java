package cn.ctg.codegen.export.data;

import cn.ctg.codegen.util.Utils;

public class Rule implements Cloneable {
	private String name;
	private String type;
	private String value;
	private String functionName;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
		if (!Utils.isRealEmpty(this.value)) {
			this.functionName = this.parseName(this.value);
		}

	}

	public String getFunctionName() {
		return this.functionName;
	}

	public Object clone() throws CloneNotSupportedException {
		Rule r = new Rule();
		r.setName(this.name);
		r.setType(this.type);
		r.setValue(this.value);
		return r;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("name:" + this.name);
		sb.append("\ntype:" + this.type);
		sb.append("\nfunctionName:" + this.functionName);
		sb.append("\nvalue:" + this.value);
		return sb.toString();
	}

	private String parseName(String inStr) {
		String result = null;
		String key = "function";
		int pos = inStr.indexOf(key);
		int pos2 = 1;
		if (pos != -1) {
			pos2 = inStr.indexOf("(", pos);
			if (pos2 != -1) {
				result = Utils.strip(inStr.substring(pos + key.length(), pos2));
			}
		}

		return result;
	}
}