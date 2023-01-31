package cn.ctg.codegen.export;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleDate;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ctg.codegen.config.FreeMakerConfig;
import cn.ctg.codegen.export.data.TemplateObject;
import cn.ctg.codegen.util.TemplateMethod;
import cn.ctg.codegen.util.Utils;

public class TemplateProducer {
	private static final Log _log = LogFactory.getLog(TemplateProducer.class);
	private static String lineSeparator = System.getProperty("line.separator");
	private static final String ENCODING = "UTF-8";
	private static Configuration config = null;
	private static TemplateProducer _instance = null;

	public static TemplateProducer getInstance() {
		if (_instance == null) {
			_instance = new TemplateProducer();
			config = new Configuration();
		}

		return _instance;
	}

	public void init(FreeMakerConfig freeMakerConfig) {
		String defaultEncoding = Utils.strip(freeMakerConfig.getDefaultEncoding(), "UTF-8");
		String outputEncoding = Utils.strip(freeMakerConfig.getOutputEncoding(), "UTF-8");
		File templatePath = new File(freeMakerConfig.getTemplateDir());

		try {
			config.setDefaultEncoding(defaultEncoding);
			config.setOutputEncoding(outputEncoding);
			config.setTemplateLoader(new FileTemplateLoader(templatePath));
		} catch (Exception var6) {
			_log.warn("Problems with templatepath " + templatePath.getAbsolutePath(), var6);
			throw new ExporterException("Problems with templatepath " + templatePath.getAbsolutePath(), var6);
		}
	}

	public void produce(TemplateObject to) {
		Map<String, Object> context = to.getContext();
		String templateName = to.getTemplate();
		File destination = to.getOutputFile();
		_log.debug("template:" + templateName);
		_log.debug("outputFile:" + destination.getAbsolutePath());
		String tempResult = this.produceToString(context, templateName);
		_log.debug("Converted content: " + tempResult);
		if (tempResult.trim().length() < 1) {
			_log.warn("Generated output is empty. Skipped creation for file " + destination);
		} else {
			try {
				this.createFile(tempResult, destination, context);
				_log.info("Writing " + templateName + " to " + destination.getAbsolutePath());
			} catch (Exception var7) {
				_log.warn("Writing " + templateName + " to " + destination.getAbsolutePath() + " failure!", var7);
				throw new ExporterException("Error while writing result to file", var7);
			}
		}
	}

	private String produceToString(Map<String, Object> context, String templateName) {
		StringWriter tempWriter = null;
		BufferedWriter bw = null;
		TemplateModel model = null;
		String result = null;

		try {
			tempWriter = new StringWriter();
			bw = new BufferedWriter(tempWriter);
			Template template = config.getTemplate(templateName);
			model = this.createModel(context);
			template.process(model, bw);
			bw.flush();
			result = tempWriter.toString();
			bw.close();
		} catch (IOException var17) {
			_log.warn("Error while processing template " + templateName, var17);
			throw new ExporterException("Error while processing template " + templateName, var17);
		} catch (TemplateException var18) {
			_log.warn("Error while processing template " + templateName, var18);
			throw new ExporterException("Error while processing template " + templateName, var18);
		} catch (Exception var19) {
			_log.warn("Error while processing template " + templateName, var19);
			throw new ExporterException("Error while processing template " + templateName, var19);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException var16) {
					;
				}
			}

			this.removeModel(model, context);
		}

		return result;
	}

	private TemplateModel createModel(Map<String, Object> context) {
		SimpleHash model = new SimpleHash(ObjectWrapper.BEANS_WRAPPER);
		Iterator var3 = context.entrySet().iterator();

		while (var3.hasNext()) {
			Entry<String, Object> e = (Entry) var3.next();
			model.put((String) e.getKey(), e.getValue());
		}

		model.put("now", new SimpleDate(new Date(), 3));
		model.put("datetime", new SimpleDate(new Date(), 3));
		model.put("date", new SimpleDate(new Date(), 2));
		model.put("time", new SimpleDate(new Date(), 1));
		model.put("func", new TemplateMethod());
		return model;
	}

	private void removeModel(TemplateModel model, Map<String, Object> context) {
		if (model != null) {
			SimpleHash model2 = null;
			model2 = (SimpleHash) model;
			Iterator var4 = context.entrySet().iterator();

			while (var4.hasNext()) {
				Entry<String, Object> e = (Entry) var4.next();
				model2.remove((String) e.getKey());
			}

			model2.remove("now");
			model2.remove("func");
			model2 = null;
		}
	}

	private void ensureExistence(File destination) {
		if (!destination.exists()) {
			File dir = destination.getAbsoluteFile().getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}

		}
	}

	private void createFile(String content, File destination, Map<String, Object> context) {
		BufferedWriter fw = null;

		try {
			while (!destination.getParentFile().exists()) {
				this.ensureExistence(destination);

				try {
					Thread.sleep(50L);
				} catch (InterruptedException var17) {
					var17.printStackTrace();
				}
			}

			boolean append = false;
			if (destination.getName().endsWith(".txt")) {
				append = true;
			}

			List matchContents = (List) context.get("matchContents");
			if (matchContents == null || matchContents.size() <= 0) {
				context.clear();
				fw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(destination.getAbsolutePath(), append), config.getOutputEncoding()));
				fw.write(content.replaceAll("\r", "").replaceAll("\n", lineSeparator));
				return;
			}

			_log.debug("MatchContentsSize:" + matchContents.size());
			_log.debug("MatchContents:" + matchContents);
			MultiInputsExporter.getInstance().add(content, destination, matchContents);
			context.clear();
		} catch (IOException var18) {
			throw new ExporterException("Error when writing to " + destination.getAbsolutePath(), var18);
		} finally {
			if (fw != null) {
				try {
					fw.flush();
					fw.close();
				} catch (IOException var16) {
					_log.warn("Exception while createFile: " + destination, var16);
				}
			}

		}

	}
}