package cn.ctg.codegen.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ctg.codegen.Application;
import cn.ctg.codegen.export.data.Output;
import cn.ctg.codegen.export.data.Rule;
import cn.ctg.codegen.export.data.TemplateInfo;
import cn.ctg.codegen.util.Utils;

public class ProjectInfo extends XmlReader {
	private static final Log _log = LogFactory.getLog(ProjectInfo.class);
	private Map<String, String> templatesMap = null;
	private Map<String, Rule> rulesMap = new HashMap();
	private Map<String, Output> outpustMap = new HashMap();
	private List<Output> originOutputList = new ArrayList();
	private List<TemplateInfo> templateInfoList = new ArrayList();
	private String dbName = null;

	public ProjectInfo(String xmlConfigFile) {
		super(xmlConfigFile);
	}

	public void subLoad() {
		try {
			this.loadFreeMakerConfig();
			this.loadTemplates();
			this.loadRule();
			this.loadInputs();
			if (!Utils.isRealEmpty(this.dbName)) {
				this.loadDBInfo();
			}

			this.templatesMap = null;
			this.rulesMap = null;
			this.outpustMap = null;
			this.originOutputList = null;
			this.templateInfoList = null;
			this.dbName = null;
		} catch (Exception var2) {
			_log.warn(var2);
		}

	}

	private void loadFreeMakerConfig() {
		_log.debug("--------------------- configuration ---------------------");
		String[] args = new String[] { "[@key]=value" };
		List<HierarchicalConfiguration> hcList = this.getConfig().configurationsAt("configuration.prop");
		FreeMakerConfig freeMakerConfig = (FreeMakerConfig) this.parseXmlList2Object(hcList, FreeMakerConfig.class,
				args);
		freeMakerConfig.setTemplateDir(this.formatMsg(freeMakerConfig.getTemplateDir()));
		args = null;
		hcList = null;
		Application.getInstance().setFreeMakerConfig(freeMakerConfig);
	}

	private void loadTemplates() {
		_log.debug("--------------------- Templates ---------------------");
		String[] args = new String[] { "[@name]=value" };
		List<HierarchicalConfiguration> hcList = this.getConfig().configurationsAt("templates.template");
		this.templatesMap = this.parseXmlList2Map(hcList, args);
		args = null;
		hcList = null;
	}

	private void loadRule() {
		_log.debug("--------------------- Rule ---------------------");
		String[] args = new String[] { "[@name]", "[@type]", "{value}=value" };
		List<HierarchicalConfiguration> hcList = this.getConfig().configurationsAt("rules.rule");
		List<Object> temp = this.parseXmlList2List(hcList, Rule.class, args);
		Iterator var4 = temp.iterator();

		while (var4.hasNext()) {
			Object o = var4.next();
			Rule rule = (Rule) o;
			this.rulesMap.put(rule.getName(), rule);
		}

		args = null;
		hcList = null;
		temp = null;
	}

	private void loadInputs() {
		_log.debug("--------------------- inputs ---------------------");
		String[] args = null;
		List<HierarchicalConfiguration> xmlInputList = this.getConfig().configurationsAt("inputs.input");
		Iterator var3 = xmlInputList.iterator();

		while (var3.hasNext()) {
			HierarchicalConfiguration xmlInput = (HierarchicalConfiguration) var3.next();
			_log.debug("--------------------- inputData ---------------------");
			_log.debug("--------------------- custom ---------------------");
			List<HierarchicalConfiguration> xmlCustomList = xmlInput.configurationsAt("custom.prop");
			args = new String[] { "[@key]=value" };
			Map<String, String> xmlCustomMap = this.parseXmlList2Map(xmlCustomList, args);
			Properties custom = new Properties();
			List<String> unFormatMsgs = new ArrayList();
			Iterator var9 = xmlCustomMap.entrySet().iterator();

			Entry it;
			String rootOutputDirectory;
			String[] value;
			while (var9.hasNext()) {
				it = (Entry) var9.next();
				rootOutputDirectory = (String) it.getKey();
				value = this.formatMsgFlag((String) it.getValue());
				if (value[1] == null) {
					this.valuesMap.put(rootOutputDirectory, value[0]);
					custom.put(rootOutputDirectory, value[0]);
				} else {
					unFormatMsgs.add(rootOutputDirectory);
				}
			}

			int count = 0;
			Iterator objIt = unFormatMsgs.iterator();

			while (objIt.hasNext()) {
				rootOutputDirectory = (String) objIt.next();
				value = this.formatMsgFlag((String) xmlCustomMap.get(rootOutputDirectory));
				if (value[1] == null) {
					this.valuesMap.put(rootOutputDirectory, value[0]);
					custom.put(rootOutputDirectory, value[0]);
					objIt.remove();
				} else {
					if (count > 10) {
						custom.put(rootOutputDirectory, value[0]);
						objIt.remove();
					}

					++count;
				}
			}

			objIt = null;
			unFormatMsgs = null;
			xmlCustomList = null;
			xmlCustomMap = null;
			rootOutputDirectory = this.formatMsg(xmlInput.getString("outputs[@outputDir]"));
			_log.debug("outputDir:" + rootOutputDirectory);
			List<HierarchicalConfiguration> xmlOutputList = xmlInput.configurationsAt("outputs.output");
			_log.debug("--------------------- outputs ---------------------");
			Iterator sourceType = xmlOutputList.iterator();

			HierarchicalConfiguration xmlOutput;
			Output output;
			String tempStr;
			Map valueMap;
			Iterator tempList;
			Entry entr;
			while (sourceType.hasNext()) {
				xmlOutput = (HierarchicalConfiguration) sourceType.next();
				_log.debug("--------------------- output ---------------------");
				output = null;
				tempStr = null;
				args = new String[] { "[@name]", "[@subOutputDir]", "[@template]", "[@package-name]", "[@extends]" };
				valueMap = this.parseXml2Map(xmlOutput, args);
				tempStr = (String) valueMap.get("extends");
				Output xmlRuleList;
				if (!Utils.isRealEmpty(tempStr)) {
					xmlRuleList = (Output) this.outpustMap.get(tempStr);
					if (xmlRuleList != null) {
						output = (Output) xmlRuleList.clone();
					}
				}

				if (output == null) {
					output = new Output();
				}

				this.originOutputList.add(output);
				tempStr = (String) valueMap.get("name");
				if (!Utils.isRealEmpty(tempStr)) {
					this.outpustMap.put(tempStr, output);
				}

				output.setProperties(custom);
				output.setName((String) valueMap.get("name"));
				tempStr = (String) valueMap.get("subOutputDir");
				if (!Utils.isEmpty(tempStr)) {
					output.setSubOutputDirectory(Utils.concatPath(rootOutputDirectory, this.formatMsg(tempStr)));
				}

				tempStr = (String) valueMap.get("template");
				if (!Utils.isEmpty(tempStr)) {
					tempStr = (String) this.templatesMap.get(tempStr);
					output.setTemplate(tempStr);
				}

				tempStr = (String) valueMap.get("package-name");
				if (!Utils.isEmpty(tempStr)) {
					output.setPackageName(this.formatMsg(tempStr));
				}

				tempStr = (String) valueMap.get("extends");
				if (!Utils.isEmpty(tempStr)) {
					output.setExtend(tempStr);
				}

				List<HierarchicalConfiguration> xmlRuleList2 = xmlOutput.configurationsAt("rule.prop");
				args = new String[] { "[@key]=value" };
				Map<String, String> xmlRulesMap = this.parseXmlList2Map(xmlRuleList2, args);
				tempList = xmlRulesMap.entrySet().iterator();

				while (tempList.hasNext()) {
					entr = (Entry) tempList.next();
					if ("tableName".equals(entr.getKey())) {
						output.setTableNameRule((Rule) this.rulesMap.get(entr.getValue()));
					} else if ("columnName".equals(entr.getKey())) {
						output.setColumnNameRule((Rule) this.rulesMap.get(entr.getValue()));
					} else if ("pathName".equals(entr.getKey())) {
						output.setPathNameRule((Rule) this.rulesMap.get(entr.getValue()));
					}
				}

				tempStr = xmlOutput.getString("filePattern");
				_log.debug("filePattern:" + tempStr);
				if (!Utils.isEmpty(tempStr)) {
					output.setFilePattern(tempStr);
				}

				List<String> tempList2 = this.split(xmlOutput.getString("insert"), ",");
				if (tempList2 != null && tempList2.size() > 0) {
					output.setMatchContents(tempList2);
				}

				tempStr = xmlOutput.getString("insert[@position]", "");
				if (!Utils.isEmpty(tempStr)) {
					output.setPosition(tempStr);
				}

				tempList2 = null;
				xmlRuleList2 = null;
				xmlRulesMap = null;
				valueMap = null;
				args = null;
				tempStr = null;
				_log.debug("insert[position:" + output.getPosition() + "]:" + this.parseMsg(output.getMatchContents()));
			}

			rootOutputDirectory = null;
			value = null;
			_log.debug("--------------------- source ---------------------");
			String sourceType2 = xmlInput.getString("source[@type]");
			if ("db".equals(sourceType2)) {
				this.dbName = xmlInput.getString("source.db[@name]");
				custom.put("dbName", this.dbName);
				List<HierarchicalConfiguration> xmlDb = xmlInput.configurationsAt("source." + sourceType2 + ".prop");
				args = new String[] { "[@key]=value" };
				Map<String, String> dbMap = this.parseXmlList2Map(xmlDb, args);
				List<String> dbSchemas = this.split((String) dbMap.get("schema"), ",");
				List<String> dbTables = this.split((String) dbMap.get("table"), ",");
				int size = dbTables.size();
				int i = 0;

				for (int len = size - dbSchemas.size(); i < len; ++i) {
					dbSchemas.add(dbSchemas.get(dbSchemas.size() - 1));
				}

				while (dbSchemas.size() > size) {
					dbSchemas.remove(dbSchemas.size() - 1);
				}

				_log.debug("databaseSchema:" + this.parseMsg(dbSchemas));
				_log.debug("databaseTable:" + this.parseMsg(dbTables));
				i = 0;

				while (true) {
					if (i >= size) {
						tempStr = null;
						valueMap = null;
						output = null;
						xmlOutput = null;
						args = null;
						break;
					}

					String schema = ((String) dbSchemas.get(i)).trim();
					String tableName = ((String) dbTables.get(i)).trim();
					if (schema.length() >= 1 && tableName.length() >= 1) {
						Iterator var22 = this.originOutputList.iterator();

						while (var22.hasNext()) {
							Output op = (Output) var22.next();
							Output output2 = (Output) op.clone();
							TemplateInfo ti = new TemplateInfo(schema, tableName);
							ti.setOutput(output2);
							this.templateInfoList.add(ti);
						}

						tempList = null;
						entr = null;
					}

					++i;
				}
			}

			sourceType2 = null;
			this.outpustMap.clear();
			this.originOutputList.clear();
			this.clearValueMap();
		}

		args = null;
		xmlInputList = null;
		Application.getInstance().setTemplateInfos(this.templateInfoList);
	}

	private void loadDBInfo() {
		File file = new File(this.getXmlConfigFile());
		String xmlConfigFile = Utils.concatPath(file.getParent(), "dbConfig.xml");
		file = null;
		XmlReader dbXml = new DbConfigInfo(xmlConfigFile, this.dbName);
		dbXml.load();
		dbXml = null;
	}

	public static void main(String[] args) {
		String config = "project.xml";
		String xmlConfigFile = Application.getInstance().getFile(config);
		XmlReader xr = new ProjectInfo(xmlConfigFile);
		xr.load();
		System.out.println(xr);
	}
}