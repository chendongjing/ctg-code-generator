package cn.ctg.codegen.export;

import cn.ctg.codegen.export.data.TemplateObject;

public interface IExport {
	void init();

	void destroy();

	void process();

	TemplateObject getTemplateObject();

	void setTemplateObject(TemplateObject var1);
}