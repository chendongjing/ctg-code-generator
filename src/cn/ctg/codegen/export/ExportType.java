package cn.ctg.codegen.export;

public enum ExportType {
	DB("cn.ctg.codegen.export.DBExporter"), CLASS("cn.ctg.codegen.export.ClassExporter"),
	XML("cn.ctg.codegen.export.XmlExporter"), CUSTOM("cn.ctg.codegen.export.CustomExporter");

	private String className;

	private ExportType(String className) {
		this.className = className;
	}

	public String toString() {
		return String.valueOf(this.className);
	}
}