package cn.ctg.codegen.export.pojo;

import cn.ctg.codegen.database.pojo.Column;
import cn.ctg.codegen.util.Utils;

public class JavaField implements Cloneable {
	private String name = "";
	private String dateType = "";
	private String columnName = "";
	private String accessorName = "";
	private Column column = null;
	private String defaultInValue = null;
	private String defaultOutValue = null;
	private JavaClass fkJavaClass = null;
	private JavaField fkJavaField = null;

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDateType() {
		return this.dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public String getAccessorName() {
		return this.accessorName;
	}

	public void setAccessorName(String dname) {
		this.accessorName = dname;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
		String dname = this.name;
		dname = dname == null ? "" : dname.trim();
		if (dname.length() > 0) {
			dname = dname.substring(0, 1).toUpperCase() + dname.substring(1);
			this.setAccessorName(dname);
		}

	}

	public Column getColumn() {
		return this.column;
	}

	public void setColumn(Column column) {
		if (column != null) {
			this.column = column;
			this.setColumnName(column.getName());
		}
	}

	public String getDefaultInValue() {
		return this.defaultInValue;
	}

	public void setDefaultInValue(String defaultInValue) {
		this.defaultInValue = defaultInValue;
	}

	public String getDefaultOutValue() {
		return Utils.strip(this.defaultOutValue, "");
	}

	public void setDefaultOutValue(String defaultOutValue) {
		this.defaultOutValue = defaultOutValue;
	}

	public JavaClass getFkJavaClass() {
		return this.fkJavaClass;
	}

	public void setFkJavaClass(JavaClass fkJavaClass) {
		this.fkJavaClass = fkJavaClass;
	}

	public JavaField getFkJavaField() {
		return this.fkJavaField;
	}

	public void setFkJavaField(JavaField fkJavaField) {
		this.fkJavaField = fkJavaField;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("name:" + this.name);
		sb.append("\ndateType:" + this.dateType);
		sb.append("\ncolumnName:" + this.columnName);
		sb.append("\naccessorName:" + this.accessorName);
		sb.append("\ndefaultInValue:" + this.defaultInValue);
		sb.append("\ndefaultOutValue:" + this.defaultOutValue);
		return sb.toString();
	}

	public Object clone() {
		JavaField field = new JavaField();
		field.name = this.name;
		field.dateType = this.dateType;
		field.columnName = this.columnName;
		field.accessorName = this.accessorName;
		field.column = this.column;
		field.defaultInValue = this.defaultInValue;
		field.defaultOutValue = this.defaultOutValue;
		if (this.fkJavaClass != null) {
			field.setFkJavaClass((JavaClass) this.fkJavaClass.clone());
		}

		if (this.fkJavaField != null) {
			field.setFkJavaField((JavaField) this.fkJavaField.clone());
		}

		return field;
	}
}