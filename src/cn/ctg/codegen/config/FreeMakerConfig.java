package cn.ctg.codegen.config;

public class FreeMakerConfig {
	private String defaultEncoding;
	private String outputEncoding;
	private String templateDir;

	public String getDefaultEncoding() {
		return this.defaultEncoding;
	}

	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	public String getOutputEncoding() {
		return this.outputEncoding;
	}

	public void setOutputEncoding(String outputEncoding) {
		this.outputEncoding = outputEncoding;
	}

	public String getTemplateDir() {
		return this.templateDir;
	}

	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}

	public String toString() {
		return "[defaultEncoding:" + this.defaultEncoding + ", outputEncoding:" + this.outputEncoding + ", templateDir:"
				+ this.templateDir + "]";
	}
}