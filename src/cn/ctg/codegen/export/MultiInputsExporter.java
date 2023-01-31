package cn.ctg.codegen.export;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ctg.codegen.Application;

public class MultiInputsExporter {
	private static final Log _log = LogFactory.getLog(MultiInputsExporter.class);
	private String lineSeparator = System.getProperty("line.separator");
	private static MultiInputsExporter mie = null;
	private final ReentrantLock lock = new ReentrantLock();
	private HashMap<File, StringBuffer> map = new HashMap();

	public static MultiInputsExporter getInstance() {
		if (mie == null) {
			mie = new MultiInputsExporter();
		}

		return mie;
	}

	public void exprot() {
		if (this.map.size() >= 1) {
			Iterator var1 = this.map.entrySet().iterator();

			while (var1.hasNext()) {
				Entry<File, StringBuffer> e = (Entry) var1.next();
				this.createFile(((StringBuffer) e.getValue()).toString(), (File) e.getKey());
			}

			this.map.clear();
			this.map = null;
		}
	}

	public void add(String content, File destination, List<String> matchContents) {
		this.lock.lock();

		try {
			String fileContent = this.readForFile(destination);
			int pos = -1;

			String matchStr;
			for (Iterator var6 = matchContents.iterator(); var6.hasNext(); pos += matchStr.length()) {
				matchStr = (String) var6.next();
				if (pos == -1) {
					pos = fileContent.lastIndexOf(matchStr);
				} else {
					pos = fileContent.indexOf(matchStr, pos);
				}

				if (pos == -1) {
					break;
				}
			}

			_log.debug("existContent:\n" + fileContent);
			_log.debug("pos:" + pos);
			if (pos == -1) {
				content = content + "\n" + fileContent;
			} else {
				int end = pos + 1;
				_log.debug("end:" + end);
				_log.debug("firstContent:\n" + fileContent.substring(0, end));
				_log.debug("middleContent:\n" + content);
				_log.debug("lastContent:\n" + fileContent.substring(end));
				if (fileContent.length() >= end + 2 && fileContent.substring(end, end + 2).equals("\r\n")) {
					end += 2;
				} else if (fileContent.length() >= end + 1 && fileContent.substring(end, end + 1).equals("\r")) {
					++end;
				} else if (fileContent.length() >= end + 1 && fileContent.substring(end, end + 1).equals("\n")) {
					++end;
				}

				content = fileContent.substring(0, end) + "\n" + content + "\n" + fileContent.substring(end);
			}

			content = content.replaceAll("\r", "").replaceAll("\n\n\n\n", "\n\n").replaceAll("\n\n\n", "\n\n");
			this.map.put(destination, new StringBuffer(content));
		} finally {
			this.lock.unlock();
		}

	}

	private void createFile(String content, File destination) {
		BufferedWriter fw = null;

		try {
			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destination, false),
					Application.getInstance().getFreeMakerConfig().getOutputEncoding()));
			fw.write(content.replaceAll("\r", "").replaceAll("\n\n\n\n", "\n\n").replaceAll("\n\n\n", "\n\n")
					.replaceAll("\n", this.lineSeparator));
		} catch (IOException var12) {
			throw new ExporterException("Error when writing to " + destination.getAbsolutePath(), var12);
		} finally {
			if (fw != null) {
				try {
					fw.flush();
					fw.close();
				} catch (IOException var11) {
					_log.warn("Exception while createFile: " + destination, var11);
				}
			}

		}

	}

	private String readForFile(File destination) {
		StringBuffer sb = (StringBuffer) this.map.get(destination);
		if (sb == null) {
			sb = new StringBuffer(1024);
			BufferedReader in = null;
			if (destination.exists() && destination.isFile()) {
				try {
					in = new BufferedReader(new InputStreamReader(new FileInputStream(destination),
							Application.getInstance().getFreeMakerConfig().getOutputEncoding()));
					String temp = null;

					while ((temp = in.readLine()) != null) {
						sb.append(temp + "\n");
					}

					in.close();
				} catch (Exception var13) {
					;
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (IOException var12) {
							;
						}
					}

				}
			}

			this.map.put(destination, sb);
		}

		return sb.toString();
	}
}