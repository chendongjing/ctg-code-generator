package cn.ctg.codegen.export;

public class ExporterException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ExporterException() {
	}

	public ExporterException(String message) {
		super(message);
	}

	public ExporterException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExporterException(Throwable cause) {
		super(cause);
	}
}