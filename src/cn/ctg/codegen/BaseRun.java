package cn.ctg.codegen;
import java.awt.Component;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ctg.codegen.config.FreeMakerConfig;
import cn.ctg.codegen.config.ProjectInfo;
import cn.ctg.codegen.config.XmlReader;
import cn.ctg.codegen.export.ExportType;
import cn.ctg.codegen.export.IExport;
import cn.ctg.codegen.export.MultiInputsExporter;
import cn.ctg.codegen.export.TemplateProducer;

public abstract class BaseRun {
	private static final Log _log = LogFactory.getLog(BaseRun.class);
	protected Application app = null;
	protected MultiInputsExporter multiInputsExporter = null;
	private FreeMakerConfig freeMakerConfig = null;
	private ExportType exportType = null;
	private List<Object> datas = null;

	public ExportType getExportType() {
		return this.exportType;
	}

	public void setExportType(ExportType exportType) {
		this.exportType = exportType;
	}

	public List<Object> getDatas() {
		return this.datas;
	}

	public void setDatas(List<Object> datas) {
		this.datas = datas;
	}

	private void _init() {
		this.multiInputsExporter = MultiInputsExporter.getInstance();

		try {
			this.app = Application.getInstance();
		} catch (Exception var2) {
			JOptionPane.showMessageDialog((Component) null, var2, "初始化错误", 0);
			System.exit(-1);
		}

		_log.debug("load project.xml start ...");
		XmlReader xr = new ProjectInfo(this.app.getFile("project.xml"));
		xr.load();
		_log.debug("load project.xml finished.");
		this.freeMakerConfig = this.app.getFreeMakerConfig();
	}

	public abstract void init();

	public void start() {
		this._init();
		_log.info("user.dir:" + this.app.getUserDir());
		_log.info("root.dir:" + this.app.getRootPath());
		_log.info("xml.path:" + this.app.getFile("project.xml"));
		this.init();
		TemplateProducer.getInstance().init(this.freeMakerConfig);
		this.execute();
	}

	private void execute() {
		String clazz = this.getExportType().toString();
		if (this.datas != null && this.datas.size() > 0) {
			ExecutorService threadPool = Executors.newFixedThreadPool(1);
			Iterator var3 = this.datas.iterator();

			while (var3.hasNext()) {
				Object data = var3.next();
				if (data != null) {
					threadPool.execute(new ThreadTask(this, data, clazz));
				}
			}

			Thread monitor = new Thread(new Monitor(this, this.multiInputsExporter, threadPool));
			monitor.start();
			threadPool.shutdown();
			threadPool = null;
		}

	}

	class ThreadTask implements Runnable {
		private static final long serialVersionUID = 0L;
		private Object data;
		private String clazz;

		ThreadTask(BaseRun this$0, Object data, String clazz) {
			this.data = data;
			this.clazz = clazz;
		}

		public void run() {
			BaseRun._log.debug(Thread.currentThread().getName() + ": start!");
			BaseRun._log.debug("Export Class is:" + this.clazz);
			try {
				Class cls = Class.forName(this.clazz);
				int index = -1;
				Constructor[] cons = cls.getDeclaredConstructors();
				for (int i = 0; i < cons.length; i++) {
					Class[] paramTypes = cons[i].getParameterTypes();
					if (paramTypes.length == 1) {
						index = i;
						break;
					}
				}
				IExport export = (IExport) cons[index].newInstance(new Object[] { this.data });
				export.process();
				Thread.sleep(200L);
			} catch (Exception e) {
				e.printStackTrace();
			}
			BaseRun._log.debug(Thread.currentThread().getName() + ": end!");
			this.data = null;
		}
	}

	class Monitor implements Runnable {
		private ExecutorService threadPool = null;
		private MultiInputsExporter multiInputsExporter = null;

		public Monitor(BaseRun this$0, MultiInputsExporter multiInputsExporter, ExecutorService threadPool) {
			this.multiInputsExporter = multiInputsExporter;
			this.threadPool = threadPool;
		}

		public void run() {
			while ((this.threadPool != null) && (!this.threadPool.isTerminated())) {
				try {
					Thread.sleep(100L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (this.multiInputsExporter != null) {
				this.multiInputsExporter.exprot();
			}
		}
	}

}