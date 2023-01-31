package cn.ctg.codegen.export.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import cn.ctg.codegen.util.Utils;

public class Output extends BaseObject implements Cloneable {
	private String name;
	private String subOutputDirectory;
	private String template;
	private String filePattern;
	private String packageName;
	private String position;
	private List<String> matchContents;
	private String extend;
	private Rule tableNameRule;
	private Rule columnNameRule;
	private Rule pathNameRule;
	private Properties properties;

	public Output() {
		this((String) null, (String) null, (String) null);
	}

	public Output(String subOutputDirectory, String template, String filePattern) {
		this.name = "";
		this.subOutputDirectory = "";
		this.template = "";
		this.filePattern = "";
		this.packageName = "";
		this.position = "after";
		this.matchContents = new ArrayList();
		this.extend = "";
		this.setSubOutputDirectory(subOutputDirectory);
		this.setTemplate(template);
		this.setFilePattern(filePattern);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTemplate() {
		return this.template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = Utils.strip(packageName);
	}

	public String getSubOutputDirectory() {
		return this.subOutputDirectory;
	}

	public void setSubOutputDirectory(String subOutputDirectory) {
		this.subOutputDirectory = Utils.strip(subOutputDirectory);
	}

	public String getFilePattern() {
		return this.filePattern;
	}

	public void setFilePattern(String filePattern) {
		this.filePattern = Utils.strip(filePattern);
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public List<String> getMatchContents() {
		return this.matchContents;
	}

	public void setMatchContents(List<String> matchContents) {
		if (matchContents != null && matchContents.size() >= 1) {
			this.matchContents = matchContents;
		}
	}

	public Rule getTableNameRule() {
		return this.tableNameRule;
	}

	public void setTableNameRule(Rule tableNameRule) {
		this.tableNameRule = tableNameRule;
	}

	public Rule getColumnNameRule() {
		return this.columnNameRule;
	}

	public void setColumnNameRule(Rule columnNameRule) {
		this.columnNameRule = columnNameRule;
	}

	public Rule getPathNameRule() {
		return this.pathNameRule;
	}

	public void setPathNameRule(Rule pathNameRule) {
		this.pathNameRule = pathNameRule;
	}

	public Properties getProperties() {
		return this.properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getExtend() {
		return this.extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public Object clone() {
		Output op = new Output();
		op.name = this.name;
		op.subOutputDirectory = this.subOutputDirectory;
		op.template = this.template;
		op.filePattern = this.filePattern;
		op.packageName = this.packageName;
		op.position = this.position;
		op.extend = this.extend;
		Iterator var2 = this.matchContents.iterator();

		while (var2.hasNext()) {
			String mc = (String) var2.next();
			op.matchContents.add(mc);
		}

		op.tableNameRule = this.tableNameRule;
		op.columnNameRule = this.columnNameRule;
		op.pathNameRule = this.pathNameRule;
		op.properties = this.properties;
		return op;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append("subOutputDirectory:" + this.subOutputDirectory);
		sb.append(", template:" + this.template);
		sb.append(", filePattern:" + this.filePattern);
		sb.append(", packageName:" + this.packageName);
		sb.append(", name:" + this.name);
		sb.append(", position:" + this.position);
		sb.append("]");
		return sb.toString();
	}
}