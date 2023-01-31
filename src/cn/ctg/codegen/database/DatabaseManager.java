package cn.ctg.codegen.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ctg.codegen.database.pojo.Column;
import cn.ctg.codegen.database.pojo.DatabaseInfo;
import cn.ctg.codegen.database.pojo.ForeignKey;
import cn.ctg.codegen.database.pojo.PrimaryKey;
import cn.ctg.codegen.database.pojo.Table;
import cn.ctg.codegen.export.data.TemplateInfo;
import cn.ctg.codegen.util.Utils;

public class DatabaseManager {
	private static final Log _log = LogFactory.getLog(DatabaseManager.class);
	private Connection conn = null;
	private DatabaseMetaData dbMetaData = null;
	private ResultSet rs = null;
	private DatabaseInfo databaseInfo = null;
	private Map<String, Table> tablesMap = new HashMap();
	private Map<String, String> fkTables = new HashMap();
	private static String[] VALUETFLAGS = new String[] { "TABLE_COMMENT", "COLUMN_COMMENT" };
	private List<String>[] columnIndexs = new ArrayList[2];
	private String[] commentSqls = new String[2];
	private static Pattern pattern = Pattern.compile("\\{(.*?)\\}");

	public DatabaseManager(DatabaseInfo databaseInfo) {
		this.databaseInfo = databaseInfo;
		this.columnIndexs[0] = new ArrayList();
		this.columnIndexs[1] = new ArrayList();
	}

	private void initDB(String driverClassName, String url, String username, String password) {
		_log.debug("driverClassName:" + driverClassName);
		_log.debug("url:" + url);
		_log.debug("username:" + username);
		_log.debug("password:" + password);

		try {
			Class.forName(driverClassName);
			this.conn = DriverManager.getConnection(url, username, password);
			this.dbMetaData = this.conn.getMetaData();
		} catch (Exception var6) {
			_log.warn(var6.getMessage(), var6);
		}

	}

	private void closeConnection() {
		try {
			this.dbMetaData = null;
			this.conn.close();
		} catch (SQLException var10) {
			_log.warn(var10.getMessage(), var10);
		} finally {
			try {
				if (!this.conn.isClosed()) {
					this.conn.close();
				}
			} catch (SQLException var9) {
				_log.warn(var9.getMessage(), var9);
			}

		}

	}

	private void clear() {
		this.columnIndexs[0].clear();
		this.columnIndexs[1].clear();
		this.commentSqls[0] = null;
		this.commentSqls[1] = null;
		this.tablesMap.clear();
		this.fkTables.clear();
	}

	public Map<String, Table> getTablesMap(List<TemplateInfo> tis) {
		this.clear();
		this.initDB(this.databaseInfo.getDbOptions().getDriverClassName(), this.databaseInfo.getDbOptions().getUrl(),
				this.databaseInfo.getDbOptions().getUsername(), this.databaseInfo.getDbOptions().getPassword());
		this.parseCommentSql();
		Iterator tempIterator = tis.iterator();

		while (tempIterator.hasNext()) {
			TemplateInfo ti = (TemplateInfo) tempIterator.next();
			this.queryTable(ti);
		}

		Object[] tempKey = this.fkTables.keySet().toArray();
		int i = 0;

		for (int len = tempKey.length; i < len; ++i) {
			String[] temp = this.splitKey(tempKey[i].toString());
			this.queryTable(temp[0], temp[1], (String) this.fkTables.get(tempKey[i].toString()));
		}

		tempKey = null;
		this.closeConnection();
		return this.tablesMap;
	}

	private void queryTable(TemplateInfo ti) {
		this.queryTable(ti.getSchema(), ti.getTableName(), (String) null);
	}

	private void queryTable(String schema, String tableName, String masterTable) {
		try {
			String key = this.getKey(schema, tableName);
			if (this.tablesMap.containsKey(key)) {
				return;
			}

			Table table = this.table((String) null, schema, tableName, masterTable == null);
			this.tablesMap.put(key, table);
			if (masterTable != null) {
				Table t = (Table) this.tablesMap.get(masterTable);
				if (t != null) {
					t.addFkTables(table);
				}
			}

			if (this.fkTables.containsKey(key)) {
				this.fkTables.remove(key);
			}
		} catch (Exception var7) {
			_log.warn(this.getKey(schema, tableName) + " loading fail!", var7);
		}

	}

	private Table table(String catalog, String schema, String tableName, boolean isMasterTable) throws Exception {
		Table table = null;
		this.rs = this.dbMetaData.getTables(catalog, schema, tableName, new String[] { "TABLE", "VIEW" });

		while (this.rs.next()) {
			table = new Table();
			table.setCatalog(this.rs.getString(1));
			table.setSchema(this.rs.getString(2));
			table.setName(this.rs.getString(3));
			table.setType(this.rs.getString(4));
			table.setTableComment(this.rs.getString(5));
		}

		this.rs.close();
		this.columns(table);
		this.primaryKeys(table);
		this.importedKeys(table, isMasterTable);
		Map<String, String> valuesMap = new HashMap();
		valuesMap.put("{schema}", schema);
		valuesMap.put("{table}", tableName);
		table.setComment(this.getComment(this.commentSqls[0], this.columnIndexs[0], valuesMap, VALUETFLAGS[0]));
		valuesMap.clear();
		valuesMap = null;
		return table;
	}

	private void columns(Table table) throws Exception {
		this.rs = this.dbMetaData.getColumns((String) null, table.getSchema(), table.getName(), "%");
		ResultSetMetaData var2 = this.rs.getMetaData();

		while (this.rs.next()) {
			Column column = new Column();
			column.setCatalog(this.rs.getString(1));
			column.setSchema(Utils.strip(this.rs.getString(2), table.getCatalog()));
			column.setTableName(this.rs.getString(3));
			column.setName(this.rs.getString(4));
			column.setDataType(this.rs.getInt(5));
			column.setTypeName(this.rs.getString(6));
			column.setDataPrecision(this.rs.getInt(7));
			Object _dataScale = this.rs.getObject(9);
			column.setDataScale(_dataScale == null ? null : _dataScale.toString());
			column.setNullAble(this.rs.getInt(11) != 0);
			column.setComment(this.rs.getString(12));
			column.setDefaultValue(this.rs.getString(13));
			column.setDataLength(this.rs.getInt(16));
			column.setColumnId(this.rs.getInt(17));
			Map<String, String> valuesMap = new HashMap();
			valuesMap.put("{schema}", column.getSchema());
			valuesMap.put("{table}", column.getTableName());
			valuesMap.put("{column}", column.getName());
//			column.setFullComment(
//					this.getComment(this.commentSqls[1], this.columnIndexs[1], valuesMap, VALUETFLAGS[1]));
			valuesMap.clear();
			valuesMap = null;
			table.addColumn(column);
		}

		this.rs.close();
	}

	private void primaryKeys(Table table) throws Exception {
		this.rs = this.dbMetaData.getPrimaryKeys((String) null, table.getSchema(), table.getName());

		while (this.rs.next()) {
			PrimaryKey primaryKey = new PrimaryKey();
			primaryKey.setTableCatalog(this.rs.getString(1));
			primaryKey.setTableSchema(this.rs.getString(2));
			primaryKey.setTableName(this.rs.getString(3));
			primaryKey.setColumnName(this.rs.getString(4));
			primaryKey.setKeySeq(this.rs.getInt(5));
			primaryKey.setPkName(this.rs.getString(6));
			table.addPrimaryKeys(primaryKey);
		}

		this.rs.close();
	}

	private void importedKeys(Table table, boolean isMasterTable) throws Exception {
		this.rs = this.dbMetaData.getImportedKeys((String) null, table.getSchema(), table.getName());

		while (this.rs.next()) {
			ForeignKey foreignKey = new ForeignKey();
			foreignKey.setPkTableCatalog(this.rs.getString(1));
			foreignKey.setPkTableSchema(this.rs.getString(2));
			foreignKey.setPkTableName(this.rs.getString(3));
			foreignKey.setPkColumnName(this.rs.getString(4));
			foreignKey.setFkTableCatalog(this.rs.getString(5));
			foreignKey.setFkTableSchema(this.rs.getString(6));
			foreignKey.setFkTableName(this.rs.getString(7));
			foreignKey.setFkColumnName(this.rs.getString(8));
			foreignKey.setKeySeq(this.rs.getInt(9));
			foreignKey.setFkName(this.rs.getString(12));
			foreignKey.setPkName(this.rs.getString(13));
			table.addForeignKeys(foreignKey);
			String key = this.getKey(foreignKey.getPkTableSchema(), foreignKey.getPkTableName());
			if (isMasterTable && !this.fkTables.containsKey(key)) {
				this.fkTables.put(key, this.getKey(foreignKey.getFkTableSchema(), foreignKey.getFkTableName()));
			}
		}

		this.rs.close();
	}

	private String getComment(String commentSql, List<String> columnIndexs, Map<String, String> valuesMap,
			String valueFlag) {
		String result = null;
		PreparedStatement pstmt = null;
		ResultSet rSet = null;

		try {
			pstmt = this.conn.prepareStatement(commentSql);
			int index = 1;
			Iterator var9 = columnIndexs.iterator();

			while (var9.hasNext()) {
				String key = (String) var9.next();
				pstmt.setString(index++, (String) valuesMap.get(key));
			}

			rSet = pstmt.executeQuery();
			if (rSet.next()) {
				result = rSet.getString(valueFlag);
			}

			rSet.close();
			pstmt.close();
		} catch (SQLException var23) {
			_log.warn(var23);
		} finally {
			if (rSet != null) {
				try {
					rSet.close();
				} catch (SQLException var22) {
					;
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException var21) {
					;
				}
			}

		}

		return result;
	}

	private void parseCommentSql() {
		this.commentSqls[0] = this.format2PreparedStatement(this.databaseInfo.getTableCommentSql(),
				this.columnIndexs[0]);
		this.commentSqls[1] = this.format2PreparedStatement(this.databaseInfo.getColumnCommentSql(),
				this.columnIndexs[1]);
	}

	protected String format2PreparedStatement(String origin, List<String> index) {
		String result = Utils.strip(origin).trim();
		if (!Utils.isRealEmpty(result)) {
			Matcher matcher = pattern.matcher(result);

			while (matcher.find()) {
				index.add(matcher.group(0));
			}

			matcher = null;

			String target;
			for (Iterator var5 = index.iterator(); var5.hasNext(); result = Utils.replace(result, target, "?")) {
				target = (String) var5.next();
			}
		}

		return result;
	}

	private String getKey(String schema, String tableName) {
		return "[Schema:" + schema + ", Table:" + tableName + "]";
	}

	private String[] splitKey(String key) {
		key = key.replaceAll("\\[Schema:", "").replaceAll(" Table:", "").replaceAll("]", "");
		return key.split(",");
	}
}