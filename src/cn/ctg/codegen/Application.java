package cn.ctg.codegen;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.ctg.codegen.config.FreeMakerConfig;
import cn.ctg.codegen.database.DatabaseManager;
import cn.ctg.codegen.database.pojo.DatabaseInfo;
import cn.ctg.codegen.database.pojo.Table;
import cn.ctg.codegen.export.data.TemplateInfo;
import cn.ctg.codegen.export.pojo.JavaClass;
import cn.ctg.codegen.util.Utils;

public class Application {
	
	private static Application _instance = null;
	private String rootPath = null;
	private String userDir = null;
	private String[] paths = new String[] { "classes", "bin", "lib", "libs", "" };
	private List<TemplateInfo> templateInfos = new ArrayList();
	private DatabaseInfo databaseInfo = null;
	private FreeMakerConfig freeMakerConfig;
	private Map<String, TemplateInfo> templateInfosMap = new HashMap();
	private Map<String, Table> tablesMap = null;

	private Application() {
		this.userDir = System.getProperty("user.dir");
		this.rootPath = this.userDir;
	}

	public String getFile(String origonFile) {
		if (origonFile != null && origonFile.trim().length() >= 1) {
			String result = null;
			String[] var3 = this.paths;
			int var4 = var3.length;

			for (int var5 = 0; var5 < var4; ++var5) {
				String p = var3[var5];
				String tempFileName = "";
				tempFileName = Utils.concatPath(p, origonFile);
				tempFileName = Utils.concatPath(this.userDir, tempFileName);
				File f = new File(tempFileName);
				if (f.exists() && f.isFile()) {
					result = f.getAbsolutePath();
					break;
				}
			}

			if (result == null) {
				File f = new File(this.rootPath + origonFile);
				if (f.exists() && f.isFile()) {
					result = f.getAbsolutePath();
				}
			}

			if (result == null) {
				result = "";
			}

			return result;
		} else {
			return "";
		}
	}

	public static Application getInstance() {
		if (_instance == null) {
			_instance = new Application();
		}

		return _instance;
	}

	public String getRootPath() {
		return this.rootPath;
	}

	public String getUserDir() {
		return this.userDir;
	}

	public List<TemplateInfo> getTemplateInfos() {
		return this.templateInfos;
	}

	public void setTemplateInfos(List<TemplateInfo> tis) {
		if (tis != null && tis.size() >= 1) {
			this.templateInfos = tis;
		}
	}

	public Map<String, Table> getTablesMap() {
		if (this.tablesMap == null) {
			DatabaseManager dm = new DatabaseManager(this.getDatabaseInfo());
			this.tablesMap = dm.getTablesMap(this.templateInfos);
			dm = null;
			Iterator it;
			JavaClass jc;
			TemplateInfo ti;
			if (this.tablesMap != null && this.tablesMap.keySet().size() >= 1) {
				it = this.templateInfos.iterator();

				while (it.hasNext()) {
					ti = (TemplateInfo) it.next();
					Table t = (Table) this.tablesMap.get(ti.toString());
					if (t == null) {
						it.remove();
					} else {
						jc = new JavaClass();
						jc.setTable(t);
						ti.setTable(t);
						ti.setJavaClass(jc);
					}
				}

				it = null;
				Iterator var6 = this.templateInfos.iterator();

				while (var6.hasNext()) {
					ti = (TemplateInfo) var6.next();
					this.templateInfosMap.put(ti.toString(), ti);
				}
			} else {
				this.templateInfos.clear();
			}

			it = this.tablesMap.entrySet().iterator();

			while (it.hasNext()) {
				Entry<String, Table> entr = (Entry) it.next();
				if (!this.templateInfosMap.containsKey(entr.getKey())) {
					ti = new TemplateInfo(((Table) entr.getValue()).getSchema(), ((Table) entr.getValue()).getName());
					jc = new JavaClass();
					jc.setTable((Table) entr.getValue());
					ti.setTable((Table) entr.getValue());
					ti.setJavaClass(jc);
					this.templateInfosMap.put(ti.toString(), ti);
				}
			}
		}

		return this.tablesMap;
	}

	public Table getTable(TemplateInfo ti) {
		return (Table) this.tablesMap.get(ti.toString());
	}

	public Map<String, TemplateInfo> getTemplateInfosMap() {
		return this.templateInfosMap;
	}

	public void setTemplateInfosMap(Map<String, TemplateInfo> templateInfosMap) {
		this.templateInfosMap = templateInfosMap;
	}

	public FreeMakerConfig getFreeMakerConfig() {
		return this.freeMakerConfig;
	}

	public void setFreeMakerConfig(FreeMakerConfig freeMakerConfig) {
		this.freeMakerConfig = freeMakerConfig;
	}

	public DatabaseInfo getDatabaseInfo() {
		if (this.databaseInfo == null && this.databaseInfo == null) {
			this.databaseInfo = new DatabaseInfo();
		}

		return this.databaseInfo;
	}

	public void setDatabaseInfo(DatabaseInfo databaseInfo) {
		this.databaseInfo = databaseInfo;
	}
}