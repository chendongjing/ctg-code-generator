package cn.ctg.codegen.database.pojo;

import cn.ctg.codegen.util.Utils;

public class DataType {
	private String type;
	private int precision;
	private String scale;
	private int length;
	private String javaDataType;
	private String javaDataTypeName;
	private String javaDataTypePackage;

	public String getJavaDataType() {
		return this.javaDataType;
	}

	public void setJavaDataType(String javaDataType) {
		this.javaDataType = Utils.strip(javaDataType);
		int pos = this.javaDataType.lastIndexOf(".");
		if (pos != -1) {
			this.setJavaDataTypePackage(this.javaDataType.substring(0, pos));
			this.setJavaDataTypeName(this.javaDataType.substring(pos + 1));
		} else {
			this.setJavaDataTypePackage("");
			this.setJavaDataTypeName(this.javaDataType);
			this.javaDataType = "";
		}

	}

	public int getPrecision() {
		return this.precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public String getScale() {
		return this.scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLength() {
		return this.length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getJavaDataTypeName() {
		return this.javaDataTypeName;
	}

	public void setJavaDataTypeName(String javaDataTypeName) {
		this.javaDataTypeName = javaDataTypeName;
	}

	public String getJavaDataTypePackage() {
		return this.javaDataTypePackage;
	}

	public void setJavaDataTypePackage(String javaDataTypePackage) {
		this.javaDataTypePackage = javaDataTypePackage;
	}

	public String toString() {
		return convertKey(this);
	}

	public static String convertKey(Column column) {
		return convertKey(column.getTypeName(), column.getDataPrecision(), column.getDataScale(),
				column.getDataLength());
	}

	public static String convertKey(DataType dataType) {
		return convertKey(dataType.getType(), dataType.getPrecision(), dataType.getScale(), dataType.getLength());
	}

	public static String convertKey(String type, int precision, String scale, int length) {
		StringBuffer sb = new StringBuffer(128);
		sb.append("(");
		sb.append("type:");
		sb.append(type == null ? null : type.toLowerCase());
		if (scale == null) {
			if (length > 0) {
				sb.append(",length:");
				sb.append(precision);
			}
		} else {
			sb.append(",precision:");
			sb.append(precision);
			if (!scale.equals("0")) {
				sb.append(",scale:");
				sb.append(scale);
			}
		}

		sb.append(")");
		return sb.toString();
	}
}