package cn.ctg.codegen.database.pojo;

import cn.ctg.codegen.util.Utils;

public class ForeignKey {
	private String pkTableCatalog;
	private String pkTableSchema = "";
	private String pkTableName = "";
	private String pkColumnName = "";
	private String fkTableCatalog;
	private String fkTableSchema = "";
	private String fkTableName = "";
	private String fkColumnName = "";
	private String pkName = "";
	private String fkName = "";
	private int keySeq;

	public String getPkTableCatalog() {
		return this.pkTableCatalog;
	}

	public void setPkTableCatalog(String pkTableCatalog) {
		this.pkTableCatalog = pkTableCatalog;
	}

	public String getPkTableSchema() {
		return this.pkTableSchema;
	}

	public void setPkTableSchema(String pkTableSchema) {
		this.pkTableSchema = Utils.strip(pkTableSchema);
	}

	public String getPkTableName() {
		return this.pkTableName;
	}

	public void setPkTableName(String pkTableName) {
		this.pkTableName = Utils.strip(pkTableName);
	}

	public String getPkColumnName() {
		return this.pkColumnName;
	}

	public void setPkColumnName(String pkColumnName) {
		this.pkColumnName = Utils.strip(pkColumnName);
	}

	public String getFkTableCatalog() {
		return this.fkTableCatalog;
	}

	public void setFkTableCatalog(String fkTableCatalog) {
		this.fkTableCatalog = fkTableCatalog;
	}

	public String getFkTableSchema() {
		return this.fkTableSchema;
	}

	public void setFkTableSchema(String fkTableSchema) {
		this.fkTableSchema = Utils.strip(fkTableSchema);
	}

	public String getFkTableName() {
		return this.fkTableName;
	}

	public void setFkTableName(String fkTableName) {
		this.fkTableName = Utils.strip(fkTableName);
	}

	public String getFkColumnName() {
		return this.fkColumnName;
	}

	public void setFkColumnName(String fkColumnName) {
		this.fkColumnName = Utils.strip(fkColumnName);
	}

	public String getPkName() {
		return this.pkName;
	}

	public void setPkName(String pkName) {
		this.pkName = Utils.strip(pkName);
	}

	public String getFkName() {
		return this.fkName;
	}

	public void setFkName(String fkName) {
		this.fkName = Utils.strip(fkName);
	}

	public int getKeySeq() {
		return this.keySeq;
	}

	public void setKeySeq(int keySeq) {
		this.keySeq = keySeq;
	}

	public String getPkKey() {
		return this.getKey(this.pkTableSchema, this.pkTableName);
	}

	public String getFkKey() {
		return this.getKey(this.fkTableSchema, this.fkTableName);
	}

	public String getKey(String schema, String tableName) {
		return "[Schema:" + schema + ", Table:" + tableName + "]";
	}
}