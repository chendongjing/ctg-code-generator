package cn.ctg.codegen.export;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ctg.codegen.export.data.TemplateObject;

public abstract class AbstractExport implements IExport {
	private static final Log _log = LogFactory.getLog(AbstractExport.class);
	private TemplateObject templateObject;

	public void process() {
		this.init();
		if (this.templateObject == null) {
			_log.warn("TemplateObject is null!");
		} else {
			TemplateProducer.getInstance().produce(this.templateObject);
			this.destroy();
		}
	}

	public void destroy() {
		if (this.templateObject != null) {
			this.templateObject.clear();
		}
	}

	public TemplateObject getTemplateObject() {
		return this.templateObject;
	}

	public void setTemplateObject(TemplateObject templateObject) {
		this.templateObject = templateObject;
	}
}