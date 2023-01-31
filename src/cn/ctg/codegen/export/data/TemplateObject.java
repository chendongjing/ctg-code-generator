package cn.ctg.codegen.export.data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TemplateObject {
	private Map<String, Object> context;
	private String template;
	private File outputFile;

	public TemplateObject() {
		this((String) null, (File) null, (Map) null);
	}

	public TemplateObject(String template, File outputFile, Map<String, Object> context) {
		this.context = new HashMap();
		this.setTemplate(template);
		this.setOutputFile(outputFile);
		this.setContext(context);
	}

	public Map<String, Object> getContext() {
		return this.context;
	}

	public void setContext(Map<String, Object> context) {
		if (context == null) {
			if (this.context != null) {
				return;
			}

			context = new HashMap();
		}

		this.context = (Map) context;
	}

	public void addContext(String key, Object value) {
		this.setContext((Map) null);
		this.context.put(key, value);
	}

	public String getTemplate() {
		return this.template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public File getOutputFile() {
		return this.outputFile;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	public void clear() {
		if (this.context != null) {
			this.context.clear();
		}

		this.template = null;
		this.outputFile = null;
	}
}