package cn.ctg.codegen.database.pojo;

import cn.ctg.codegen.util.Utils;

public class PrimaryKey {
	private String tableCatalog;
	private String tableSchema = "";
	private String tableName = "";
	private String columnName = "";
	private String pkName = "";
	private int keySeq;

	public String getTableCatalog() {
		return this.tableCatalog;
	}

	public void setTableCatalog(String tableCatalog) {
		this.tableCatalog = tableCatalog;
	}

	public String getTableSchema() {
		return this.tableSchema;
	}

	public void setTableSchema(String tableSchema) {
		this.tableSchema = Utils.strip(tableSchema);
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = Utils.strip(tableName);
	}

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = Utils.strip(columnName);
	}

	public String getPkName() {
		return this.pkName;
	}

	public void setPkName(String pkName) {
		this.pkName = Utils.strip(pkName);
	}

	public int getKeySeq() {
		return this.keySeq;
	}

	public void setKeySeq(int keySeq) {
		this.keySeq = keySeq;
	}
}