package cn.ctg.codegen.export.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import cn.ctg.codegen.database.pojo.Column;
import cn.ctg.codegen.database.pojo.Table;
import cn.ctg.codegen.util.Utils;

public class JavaClass implements Cloneable {
	private String name = "";
	private String fileName = "";
	private String packageName = "";
	private Set<String> pojoImpClasses = new TreeSet();
	private Set<String> importClasses = new TreeSet();
	private List<JavaField> fields = new ArrayList();
	private List<String> fieldNames = null;
	private Table table = null;
	private List<JavaField> primaryKeyFields = null;
	private List<JavaField> foreignKeyFields = null;
	private List<JavaField> nonPrimaryKeyFields = null;
	private List<JavaField> nonKeyFields = null;
	private List<JavaClass> fkJavaClasses = new ArrayList();
	private static HashMap<String, String> defaultInValuesMap = new HashMap();

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<JavaField> getFields() {
		return this.fields;
	}

	public void addField(JavaField field) {
		if (field != null) {
			if (field.getColumn() != null && (field.getColumn().getDefaultValue().length() > 0
					|| !field.getColumn().isNullAble() && !field.getColumn().isKeyFlag())) {
				this.setDefaultInValue(field);
			}

			this.fields.add(field);
		}
	}

	public Table getTable() {
		return this.table;
	}

	public void setTable(Table table) {
		if (table != null) {
			this.table = table;
			Iterator var2 = table.getColumns().iterator();

			while (var2.hasNext()) {
				Column c = (Column) var2.next();
				JavaField field = new JavaField();
				field.setColumn(c);
				this.addField(field);
			}

		}
	}

	public String getPackageName() {
		return this.packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public List<JavaField> getPrimaryKeyFields() {
		if (this.primaryKeyFields == null) {
			this.primaryKeyFields = new ArrayList();
			Iterator var1 = this.fields.iterator();

			while (var1.hasNext()) {
				JavaField field = (JavaField) var1.next();
				if (field.getColumn().isPkFlag()) {
					this.primaryKeyFields.add(field);
				}
			}
		}

		return this.primaryKeyFields;
	}

	public List<JavaField> getForeignKeyFields() {
		if (this.foreignKeyFields == null) {
			this.foreignKeyFields = new ArrayList();
			Iterator var1 = this.fields.iterator();

			while (var1.hasNext()) {
				JavaField field = (JavaField) var1.next();
				if (field.getColumn().isFkFlag()) {
					this.foreignKeyFields.add(field);
				}
			}
		}

		return this.foreignKeyFields;
	}

	public List<JavaField> getNonKeyFields() {
		if (this.nonKeyFields == null) {
			this.nonKeyFields = new ArrayList();
			Iterator var1 = this.fields.iterator();

			while (var1.hasNext()) {
				JavaField field = (JavaField) var1.next();
				if (!field.getColumn().isKeyFlag()) {
					this.nonKeyFields.add(field);
				}
			}
		}

		return this.nonKeyFields;
	}

	public List<JavaField> getNonPrimaryKeyFields() {
		if (this.nonPrimaryKeyFields == null) {
			this.nonPrimaryKeyFields = new ArrayList();
			Iterator var1 = this.fields.iterator();

			while (var1.hasNext()) {
				JavaField field = (JavaField) var1.next();
				if (!field.getColumn().isPkFlag()) {
					this.nonPrimaryKeyFields.add(field);
				}
			}
		}

		return this.nonPrimaryKeyFields;
	}

	public Set<String> getPojoImpClasses() {
		return this.pojoImpClasses;
	}

	public void addPojoImpClass(String clazz) {
		this.pojoImpClasses.add(clazz);
	}

	public List<String> getFieldNames() {
		if (this.fieldNames == null) {
			this.fieldNames = new ArrayList();
			Iterator var1 = this.fields.iterator();

			while (var1.hasNext()) {
				JavaField field = (JavaField) var1.next();
				this.fieldNames.add(field.getName());
			}
		}

		return this.fieldNames;
	}

	public List<JavaClass> getFkJavaClasses() {
		return this.fkJavaClasses;
	}

	public void addFkJavaClass(JavaClass fkJavaClass) {
		if (fkJavaClass != null && this.foreignKeyFields != null) {
			Iterator var2 = this.foreignKeyFields.iterator();

			while (var2.hasNext()) {
				JavaField field = (JavaField) var2.next();
				if (field.getColumn().getForeignKeyTable() == fkJavaClass.getTable()) {
					field.setFkJavaClass(fkJavaClass);
					field.setFkJavaField(
							fkJavaClass.findJavaField(field.getColumn().getForeignKey().getPkColumnName()));
					break;
				}
			}

			this.fkJavaClasses.add(fkJavaClass);
		}
	}

	public JavaField findJavaField(Column column) {
		return this.findJavaField(column.getName());
	}

	public JavaField findJavaField(String columnName) {
		columnName = Utils.strip(columnName);
		JavaField field = null;
		Iterator var3 = this.fields.iterator();

		while (var3.hasNext()) {
			JavaField jf = (JavaField) var3.next();
			if (columnName.equals(jf.getColumn().getName())) {
				field = jf;
				break;
			}
		}

		return field;
	}

	public Set<String> getImportClasses() {
		return this.importClasses;
	}

	public void addImportClazz(String clazz) {
		this.importClasses.add(clazz);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n=========================================================\n");
		sb.append("name:" + this.name);
		sb.append("fileName:" + this.fileName);
		int i = 0;

		for (int length = this.fields.size(); i < length; ++i) {
			sb.append("\n-------------------------" + i + "--------------------------------\n");
			sb.append(this.fields.get(i));
		}

		sb.append("\n=========================================================\n");
		return sb.toString();
	}

	public Object clone() {
		JavaClass javaClass = new JavaClass();
		javaClass.name = this.name;
		javaClass.fileName = this.fileName;
		javaClass.packageName = this.packageName;
		Iterator var2 = this.pojoImpClasses.iterator();

		String clazz;
		while (var2.hasNext()) {
			clazz = (String) var2.next();
			javaClass.addPojoImpClass(clazz);
		}

		var2 = this.importClasses.iterator();

		while (var2.hasNext()) {
			clazz = (String) var2.next();
			javaClass.addImportClazz(clazz);
		}

		var2 = this.fields.iterator();

		while (var2.hasNext()) {
			JavaField field = (JavaField) var2.next();
			javaClass.addField(field);
		}

		javaClass.table = this.table;
		var2 = this.fkJavaClasses.iterator();

		while (var2.hasNext()) {
			JavaClass jc = (JavaClass) var2.next();
			javaClass.addFkJavaClass((JavaClass) jc.clone());
		}

		return javaClass;
	}

	private void setDefaultInValue(JavaField field) {
		String result = "";
		result = (String) defaultInValuesMap.get(field.getDateType());
		if (",byte,short,int,integer,long,float,double,".indexOf("," + field.getDateType() + ",") == -1
				&& "String".equals(field.getDateType())) {
			result = result.replaceAll("'", "");
		}

		field.setDefaultInValue(result);
	}

	private void setDefaultOutValue(JavaField field) {
		String result = "";
		if (",byte,short,int,integer,long,float,double,".indexOf("," + field.getDateType() + ",") != -1) {
			result = "0";
		}

		field.setDefaultOutValue(result);
	}

	static {
		defaultInValuesMap.put("byte", "0");
		defaultInValuesMap.put("Byte", "Byte.valueOf(0)");
		defaultInValuesMap.put("short", "0");
		defaultInValuesMap.put("Short", "Short.valueOf(0)");
		defaultInValuesMap.put("int", "0");
		defaultInValuesMap.put("Integer", "Integer.valueOf(0)");
		defaultInValuesMap.put("long", "0L");
		defaultInValuesMap.put("Long", "Long.valueOf(0L)");
		defaultInValuesMap.put("float", "0F");
		defaultInValuesMap.put("Float", "Float.valueOf(0F)");
		defaultInValuesMap.put("double", "0D");
		defaultInValuesMap.put("Double", "Double.valueOf(0D)");
		defaultInValuesMap.put("boolean", "false");
		defaultInValuesMap.put("Boolean", "Boolean.FALSE");
		defaultInValuesMap.put("String", "");
		defaultInValuesMap.put("Date", "new Date(System.currentTimeMillis())");
		defaultInValuesMap.put("Timestamp", "");
		defaultInValuesMap.put("byte[]", "new byte[]");
	}
}