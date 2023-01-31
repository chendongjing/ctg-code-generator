package cn.ctg.codegen.config;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ctg.codegen.Application;
import cn.ctg.codegen.database.DBOptions;
import cn.ctg.codegen.database.pojo.DataType;
import cn.ctg.codegen.database.pojo.DatabaseInfo;
import cn.ctg.codegen.util.Utils;

public class DbConfigInfo extends XmlReader {
	private static final Log _log = LogFactory.getLog(DbConfigInfo.class);
	private String dbName;
	private DatabaseInfo databaseInfo = new DatabaseInfo();
	private HierarchicalConfiguration dbXml;

	public DbConfigInfo(String xmlConfigFile, String dbName) {
		super(xmlConfigFile);
		this.dbName = dbName;
	}

	public void subLoad() {
		try {
			this.findDbXml();
			this.check();
			this.loadDataSource();
			this.loadDataTypeMap();
			this.loadCommentSql();
			this.dbName = null;
			this.dbXml = null;
			this.databaseInfo = null;
		} catch (Exception var2) {
			_log.warn(var2);
		}

	}

	private void findDbXml() {
		List<HierarchicalConfiguration> hcList = this.getConfig().configurationsAt("databases.database");
		Iterator var2 = hcList.iterator();

		HierarchicalConfiguration xmlDatabase;
		do {
			if (!var2.hasNext()) {
				return;
			}

			xmlDatabase = (HierarchicalConfiguration) var2.next();
		} while (!this.dbName.equals(xmlDatabase.getString("[@name]")));

		this.dbXml = xmlDatabase;
	}

	private void check() {
		if (this.dbXml == null) {
			throw new NullPointerException("Don't find database!");
		}
	}

	private void loadDataSource() {
		_log.debug("--------------------- dataSource ---------------------");
		String[] args = new String[] { "[@key]=value" };
		List<HierarchicalConfiguration> hcList = this.dbXml.configurationsAt("dataSource.prop");
		Map<String, String> valueMap = this.parseXmlList2Map(hcList, args);
		String driverClassName = (String) valueMap.get("driverClassName");
		_log.debug("driverClassName:" + driverClassName);
		String url = (String) valueMap.get("url");
		_log.debug("url:" + url);
		String username = (String) valueMap.get("username");
		_log.debug("username:" + username);
		String password = Utils.strip((String) valueMap.get("password"));
		_log.debug("password:" + password);
		hcList = null;
		valueMap = null;
		this.databaseInfo.setDbOptions(new DBOptions(driverClassName, url, username, password));
	}

	private void loadDataTypeMap() {
		_log.debug("--------------------- typeMap ---------------------");
		String[] args = new String[] { "{type}=[@key]", "[@precision]", "[@scale]", "[@length]",
				"{javaDataType}=value" };
		List<HierarchicalConfiguration> hcList = this.dbXml.configurationsAt("typeMap.prop");
		List<Object> temp = this.parseXmlList2List(hcList, DataType.class, args);
		Iterator var4 = temp.iterator();

		while (var4.hasNext()) {
			Object o = var4.next();
			_log.debug("DataType:" + o);
			this.databaseInfo.addDataType((DataType) o);
		}

		hcList = null;
		temp = null;
	}

	private void loadCommentSql() {
		_log.debug("--------------------- commentSql ---------------------");
		String[] args = new String[] { "[@key]=value" };
		List<HierarchicalConfiguration> hcList = this.dbXml.configurationsAt("commentSql.prop");
		Map<String, String> valueMap = this.parseXmlList2Map(hcList, args);
		this.databaseInfo.setTableCommentSql(Utils.strip((String) valueMap.get("table")));
		this.databaseInfo.setColumnCommentSql(Utils.strip((String) valueMap.get("column")));
		_log.debug("table:" + this.databaseInfo.getTableCommentSql());
		_log.debug("column:" + this.databaseInfo.getColumnCommentSql());
		hcList = null;
		valueMap = null;
		this.databaseInfo.setName(this.dbName);
		Application.getInstance().setDatabaseInfo(this.databaseInfo);
	}
}