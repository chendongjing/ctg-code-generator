package cn.ctg.codegen.database.pojo;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ctg.codegen.database.DBOptions;

public class DatabaseInfo {
	private static final Log _log = LogFactory.getLog(DatabaseInfo.class);
	private Map<String, DataType> typeMap = new HashMap();
	private DBOptions dbOptions = null;
	private String tableCommentSql = null;
	private String columnCommentSql = null;
	private String name = null;

	public void addDataType(DataType dataType) {
		this.typeMap.put(dataType.toString(), dataType);
	}

	public DataType getDataType(Column column) {
		DataType dataType = null;
		String key = DataType.convertKey(column);
		int firstPos = key.indexOf(":");

		for (boolean var5 = true; (dataType = (DataType) this.typeMap.get(key)) == null; key = key.substring(0,
				key.lastIndexOf(",")) + ")") {
			int lastPos = key.lastIndexOf(":");
			if (firstPos == lastPos) {
				break;
			}
		}

		if (dataType == null) {
			dataType = new DataType();
			dataType.setType("NULL");
			dataType.setJavaDataType("NULL<" + column.getTypeName() + ">");
		}

		_log.trace(column.getName() + ":" + key + ":" + dataType);
		return dataType;
	}

	public Map<String, DataType> getTypeMap() {
		return this.typeMap;
	}

	public void setTypeMap(Map<String, DataType> typeMap) {
		this.typeMap = typeMap;
	}

	public String getTableCommentSql() {
		return this.tableCommentSql;
	}

	public void setTableCommentSql(String tableCommentSql) {
		this.tableCommentSql = tableCommentSql;
	}

	public String getColumnCommentSql() {
		return this.columnCommentSql;
	}

	public void setColumnCommentSql(String columnCommentSql) {
		this.columnCommentSql = columnCommentSql;
	}

	public DBOptions getDbOptions() {
		return this.dbOptions;
	}

	public void setDbOptions(DBOptions dbOptions) {
		this.dbOptions = dbOptions;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}