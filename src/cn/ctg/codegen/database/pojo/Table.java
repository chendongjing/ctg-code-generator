package cn.ctg.codegen.database.pojo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.ctg.codegen.util.Utils;

public class Table extends DbObject {
	private String type = "";
	private String tableComment = "";
	private List<Column> columns = new ArrayList();
	private List<String> columnNames = new ArrayList();
	private List<Table> fkTables = new ArrayList();
	private List<Column> pkColumns = new ArrayList();
	private List<Column> fkColumns = new ArrayList();
	private List<String> pkColumnNames = new ArrayList();
	private List<String> fkColumnNames = new ArrayList();
	private List<PrimaryKey> primaryKeys = new ArrayList();
	private List<ForeignKey> foreignKeys = new ArrayList();

	public void addColumn(Column column) {
		if (column != null) {
			this.setDataTypeDesc(column);
			this.columnNames.add(column.getName());
			this.columns.add(column);
		}
	}

	public List<Column> getColumns() {
		return this.columns;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = Utils.strip(type);
	}

	public String getTableComment() {
		return tableComment;
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}

	public List<String> getColumnNames() {
		return this.columnNames;
	}

	public List<Table> getFkTables() {
		return this.fkTables;
	}

	public void addFkTables(Table fkTable) {
		if (fkTable != null) {
			this.fkTables.add(fkTable);
			Iterator var2 = this.foreignKeys.iterator();

			while (var2.hasNext()) {
				ForeignKey fk = (ForeignKey) var2.next();
				if (fk.getPkTableSchema().equals(fkTable.getSchema())
						&& fk.getPkTableName().equals(fkTable.getName())) {
					String columnName = fk.getFkColumnName();
					Column column = this.findColumnByName(columnName);
					if (column != null) {
						column.setForeignKeyTable(fkTable);
						break;
					}
				}
			}

		}
	}

	public List<PrimaryKey> getPrimaryKeys() {
		return this.primaryKeys;
	}

	public void addPrimaryKeys(PrimaryKey primaryKey) {
		if (primaryKey != null) {
			this.primaryKeys.add(primaryKey);
			String columnName = primaryKey.getColumnName();
			Column column = this.findColumnByName(columnName);
			if (column != null) {
				this.pkColumnNames.add(columnName);
				column.setPkFlag(true);
				this.pkColumns.add(column);
			}

		}
	}

	public List<ForeignKey> getForeignKeys() {
		return this.foreignKeys;
	}

	public void addForeignKeys(ForeignKey foreignKey) {
		if (foreignKey != null) {
			this.foreignKeys.add(foreignKey);
			String columnName = foreignKey.getFkColumnName();
			Column column = this.findColumnByName(columnName);
			if (column != null) {
				this.fkColumnNames.add(columnName);
				column.setFkFlag(true);
				column.setForeignKey(foreignKey);
				this.fkColumns.add(column);
			}

		}
	}

	public List<Column> getPkColumns() {
		return this.pkColumns;
	}

	public List<Column> getFkColumns() {
		return this.fkColumns;
	}

	public List<String> getPkColumnNames() {
		return this.pkColumnNames;
	}

	public List<String> getFkColumnNames() {
		return this.fkColumnNames;
	}

	public Column findColumnByName(String columnName) {
		int pos = this.columnNames.indexOf(columnName);
		return pos == -1 ? null : (Column) this.columns.get(pos);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\n  type:" + this.type);
		sb.append("\n  tableComment:" + this.getTableComment());
		sb.append("\n  primaryKeys:[");
		int i = 0;

		int length;
		for (length = this.primaryKeys.size(); i < length; ++i) {
			sb.append(this.primaryKeys.get(i));
			sb.append(i > length - 1 ? ", " : "");
		}

		sb.append("]");
		sb.append("\nforeignKeys:[");
		i = 0;

		for (length = this.foreignKeys.size(); i < length; ++i) {
			sb.append(this.foreignKeys.get(i));
			sb.append(i > length - 1 ? ", " : "");
		}

		sb.append("]");
		i = 0;

		for (length = this.columns.size(); i < length; ++i) {
			sb.append("\n-------------------------" + i + "--------------------------------\n");
			sb.append(this.columns.get(i));
		}

		sb.append("\n=========================================================\n");
		return sb.toString();
	}

	private void setDataTypeDesc(Column column) {
		String result = "";
		String tempDataType = column.getTypeName();
		int pos = tempDataType.indexOf("(");
		if (pos != -1) {
			result = result + column.getTypeName().toUpperCase();
			column.setTypeName(tempDataType.substring(0, pos));
		} else {
			result = result + column.getTypeName().toUpperCase();
			if (column.getDataScale() == null) {
				result = result + "(";
				result = result + column.getDataPrecision();
				result = result + ")";
			} else {
				result = result + "(" + column.getDataPrecision();
				if (!column.getDataScale().equals("0")) {
					result = result + "," + column.getDataScale();
				}

				result = result + ")";
			}
		}

		column.setDataTypeDesc(result);
	}
}