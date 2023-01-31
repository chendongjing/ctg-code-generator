package cn.ctg.codegen.export;

import java.io.File;
import java.util.Iterator;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ctg.codegen.Application;
import cn.ctg.codegen.database.pojo.Column;
import cn.ctg.codegen.database.pojo.DataType;
import cn.ctg.codegen.database.pojo.ForeignKey;
import cn.ctg.codegen.export.data.Rule;
import cn.ctg.codegen.export.data.TemplateInfo;
import cn.ctg.codegen.export.data.TemplateObject;
import cn.ctg.codegen.export.pojo.JavaClass;
import cn.ctg.codegen.export.pojo.JavaField;
import cn.ctg.codegen.export.ruleengine.RuleEngine;
import cn.ctg.codegen.export.ruleengine.RuleEngineManager;
import cn.ctg.codegen.util.Utils;

public class DBExporter extends GeneralExport {
	private static final Log _log = LogFactory.getLog(DBExporter.class);
	private TemplateInfo templateInfo;
	private JavaClass jc;

	public DBExporter(TemplateInfo templateInfo) {
		super(new TemplateObject());
		if (templateInfo == null) {
			throw new ExporterException("TemplateInfo's Object is null!");
		} else if (Application.getInstance().getTemplateInfosMap() == null) {
			throw new ExporterException("templateInfosMap is null!");
		} else {
			this.templateInfo = templateInfo;
			this.getTemplateObject().setTemplate(templateInfo.getOutput().getTemplate());
		}
	}

	private void initJavaClass() {
		if (this.templateInfo == null) {
			throw new ExporterException("templateInfo is null!");
		} else {
			this.jc = this.templateInfo.getCloneJavaClass();
			this.jc.setPackageName(this.templateInfo.getOutput().getPackageName());
			Rule tableNameRule = null;
			Rule columnNameRule = null;
			tableNameRule = this.templateInfo.getOutput().getTableNameRule();
			columnNameRule = this.templateInfo.getOutput().getColumnNameRule();
			this.convertName(this.jc, tableNameRule, columnNameRule);
			Iterator var3 = this.jc.getTable().getForeignKeys().iterator();

			while (var3.hasNext()) {
				ForeignKey fk = (ForeignKey) var3.next();
				TemplateInfo tInfo = (TemplateInfo) Application.getInstance().getTemplateInfosMap().get(fk.getPkKey());
				if (tInfo != null) {
					JavaClass fkJavaClass = tInfo.getCloneJavaClass();
					this.jc.addFkJavaClass(fkJavaClass);
					Rule tNameRule = tableNameRule;
					Rule cNameRule = columnNameRule;
					if (tInfo.getOutput() != null) {
						fkJavaClass.setPackageName(tInfo.getOutput().getPackageName());
						if (!fkJavaClass.getPackageName().equals(this.jc.getPackageName())) {
							this.jc.addImportClazz(fkJavaClass.getPackageName());
						}

						tNameRule = tInfo.getOutput().getTableNameRule();
						cNameRule = tInfo.getOutput().getColumnNameRule();
					}

					this.convertName(fkJavaClass, tNameRule, cNameRule);
					tInfo = null;
				}
			}

		}
	}

	private void convertName(JavaClass jc, Rule tableNameRule, Rule columnNameRule) {
		try {
			String value = null;
			RuleEngine ruleEngine = RuleEngineManager.getInstance().getRuleEngine();
			value = ruleEngine.process(tableNameRule, new Object[] { jc.getTable().getName() });
			jc.setName(value);
			Iterator var6 = jc.getFields().iterator();

			while (var6.hasNext()) {
				JavaField field = (JavaField) var6.next();
				Column column = field.getColumn();
				value = ruleEngine.process(columnNameRule,
						new Object[] { column.getName(), column.isPkFlag(), column.isFkFlag() });
				field.setName(value);
				DataType dataType = Application.getInstance().getDatabaseInfo().getDataType(field.getColumn());
				field.setDateType(dataType.getJavaDataTypeName());
				if (dataType.getJavaDataType().length() > 0) {
					jc.addPojoImpClass(dataType.getJavaDataType());
				}
			}

		} catch (Exception var10) {
			throw new ExporterException("Convert Name is error on " + this.getClass(), var10);
		}
	}

	private void initOutputFile() {
		String filename = this.templateInfo.getOutput().getFilePattern();
		if (this.templateInfo.getOutput().getFilePattern().indexOf("html-name") != -1) {
			StringBuilder name = new StringBuilder().append(Character.toLowerCase(this.jc.getName().charAt(0)))
					.append(this.jc.getName().substring(1).toString());
			filename = filename.replaceAll("\\{html-name\\}", name.toString());
		} else {
			filename = filename.replaceAll("\\{class-name\\}", this.jc.getName());
		}
		String packageLocation = this.templateInfo.getOutput().getPackageName().replaceAll("\\.", "/");
		if (packageLocation == null || packageLocation.length() == 0) {
			packageLocation = ".";
		}

		filename = filename.replaceAll("\\{package-name\\}", packageLocation);
		int start = filename.lastIndexOf("/");
		int end = filename.lastIndexOf(".");
		if (end <= 0) {
			end = filename.length();
		}

		this.jc.setFileName(filename.substring(start + 1, end));
		String pathname = null;
		Rule pathNameRule = null;
		pathNameRule = this.templateInfo.getOutput().getPathNameRule();
		if (pathNameRule != null) {
			RuleEngine ruleEngine = RuleEngineManager.getInstance().getRuleEngine();
			pathname = ruleEngine.process(pathNameRule, new Object[] { filename });
		}

		if (Utils.isRealEmpty(pathname)) {
			pathname = filename;
		}

		this.getTemplateObject()
				.setOutputFile(new File(this.templateInfo.getOutput().getSubOutputDirectory(), pathname));
	}

	private void initModel() {
		this.getTemplateObject().addContext("pojo", this.jc);
		this.getTemplateObject().addContext("matchContents", this.templateInfo.getOutput().getMatchContents());
		Properties properties = this.templateInfo.getOutput().getProperties();
		if (properties == null) {
			new Properties();
		}

		this.getTemplateObject().addContext("custom", this.templateInfo.getOutput().getProperties());
	}

	public void init() {
		_log.debug("init JavaClass...");
		this.initJavaClass();
		_log.debug("init OutputFile...");
		this.initOutputFile();
		_log.debug("init Model...");
		this.initModel();
	}

	public void destroy() {
		super.destroy();
		this.templateInfo = null;
		this.jc = null;
	}
}