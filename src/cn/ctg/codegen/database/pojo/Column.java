package cn.ctg.codegen.database.pojo;

import cn.ctg.codegen.util.Utils;

public class Column extends DbObject {
	private String tableName = "";
	private int dataType;
	private String typeName = "";
	private int dataLength;
	private int dataPrecision;
	private String dataScale;
	private boolean nullAble;
	private int columnId;
	private String defaultValue = "";
	private String jdbcType = "";
	private boolean pkFlag;
	private boolean fkFlag;
	private ForeignKey foreignKey = null;
	private Table foreignKeyTable = null;
	private boolean keyFlag;
	private String dataTypeDesc = "";

	public int getDataPrecision() {
		return this.dataPrecision;
	}

	public void setDataPrecision(int dataPrecision) {
		this.dataPrecision = dataPrecision;
	}

	public String getDataScale() {
		return this.dataScale;
	}

	public void setDataScale(String dataScale) {
		this.dataScale = dataScale;
	}

	public int getDataType() {
		return this.dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
		this.jdbcType = JdbcTypeNameTranslator.getJdbcTypeName(this.dataType);
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = Utils.strip(typeName);
	}

	public int getDataLength() {
		return this.dataLength;
	}

	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}

	public boolean isNullAble() {
		return this.nullAble;
	}

	public void setNullAble(boolean nullAble) {
		this.nullAble = nullAble;
	}

	public int getColumnId() {
		return this.columnId;
	}

	public void setColumnId(int columnId) {
		this.columnId = columnId;
	}

	public String getDefaultValue() {
		return this.defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = Utils.strip(defaultValue);
	}

	public String getJdbcType() {
		return this.jdbcType;
	}

	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}

	public ForeignKey getForeignKey() {
		return this.foreignKey;
	}

	public void setForeignKey(ForeignKey foreignKey) {
		this.foreignKey = foreignKey;
		if (this.foreignKey != null) {
			this.setFkFlag(true);
		}

	}

	public Table getForeignKeyTable() {
		return this.foreignKeyTable;
	}

	public void setForeignKeyTable(Table foreignKeyTable) {
		this.foreignKeyTable = foreignKeyTable;
		if (this.foreignKeyTable != null) {
			this.setFkFlag(true);
		}

	}

	public boolean isKeyFlag() {
		return this.keyFlag;
	}

	private void setKeyFlag() {
		this.keyFlag = this.pkFlag || this.fkFlag;
	}

	public String getDataTypeDesc() {
		return this.dataTypeDesc;
	}

	public void setDataTypeDesc(String dataTypeDesc) {
		this.dataTypeDesc = dataTypeDesc;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = Utils.strip(tableName);
	}

	public boolean isPkFlag() {
		return this.pkFlag;
	}

	public void setPkFlag(boolean pkFlag) {
		this.pkFlag = pkFlag;
		this.setKeyFlag();
	}

	public boolean isFkFlag() {
		return this.fkFlag;
	}

	public void setFkFlag(boolean fkFlag) {
		this.fkFlag = fkFlag;
		this.setKeyFlag();
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (o == this) {
			return true;
		} else if (!(o instanceof Column)) {
			return false;
		} else {
			Column o2 = (Column) o;
			return o2.getCatalog().equals(this.getCatalog()) && o2.getSchema().equals(this.getSchema())
					&& o2.getTableName().equals(this.getTableName()) && o2.getName().equals(this.getName());
		}
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.getCatalog().hashCode();
		result = 37 * result + this.getSchema().hashCode();
		result = 37 * result + this.getTableName().hashCode();
		result = 37 * result + this.getName().hashCode();
		return result;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("columnId:" + this.columnId);
		sb.append("\nname:" + this.getName());
		sb.append("\nfullComment:" + this.getFullComment());
		sb.append("\ndataType:" + this.dataType);
		sb.append("\ndataTypeDesc:" + this.dataTypeDesc);
		sb.append("\ndataLength:" + this.dataLength);
		sb.append("\ndataPrecision:" + this.dataPrecision);
		sb.append("\ndataScale:" + this.dataScale);
		sb.append("\nnullAble:" + this.nullAble);
		return sb.toString();
	}
}