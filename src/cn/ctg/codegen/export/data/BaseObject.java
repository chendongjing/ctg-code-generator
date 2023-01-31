package cn.ctg.codegen.export.data;

public class BaseObject {
	private String schema;
	private String tableName;

	public String getSchema() {
		return this.schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getKey() {
		return "[Schema:" + this.schema + ", Table:" + this.tableName + "]";
	}
}