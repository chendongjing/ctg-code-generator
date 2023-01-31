package cn.ctg.codegen.export.data;

import cn.ctg.codegen.database.pojo.Table;
import cn.ctg.codegen.export.pojo.JavaClass;

public class TemplateInfo extends BaseObject {
	private Table table;
	private JavaClass javaClass;
	private Output output;

	public TemplateInfo(String schema, String tableName) {
		this.setSchema(schema);
		this.setTableName(tableName);
	}

	public Table getTable() {
		return this.table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public JavaClass getJavaClass() {
		return this.javaClass;
	}

	public JavaClass getCloneJavaClass() {
		JavaClass jc = null;
		if (this.javaClass != null) {
			jc = (JavaClass) this.javaClass.clone();
		}

		return jc;
	}

	public void setJavaClass(JavaClass javaClass) {
		this.javaClass = javaClass;
	}

	public Output getOutput() {
		return this.output;
	}

	public void setOutput(Output output) {
		this.output = output;
	}

	public String toString() {
		return this.getKey();
	}
}