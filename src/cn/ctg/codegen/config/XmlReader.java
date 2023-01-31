package cn.ctg.codegen.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.ctg.codegen.Application;
import cn.ctg.codegen.util.Utils;

public abstract class XmlReader {
	private static final Log _log = LogFactory.getLog(XmlReader.class);
	private static String VALUE = "value";
	private String xmlConfigFile;
	private XMLConfiguration config = null;
	protected Map<String, String> valuesMap = new HashMap();
	private String root = Application.getInstance().getRootPath();
	private static Pattern pattern = Pattern.compile("\\{(.*?)\\}");

	public XmlReader(String xmlConfigFile) {
		this.xmlConfigFile = xmlConfigFile;
	}

	public void load() {
		try {
			XMLConfiguration.setDefaultListDelimiter('|');
			this.config = new XMLConfiguration(this.xmlConfigFile);
			this.clearValueMap();
			this.subLoad();
			this.config.clear();
			this.config = null;
		} catch (ConfigurationException var2) {
			_log.warn(var2);
		}

	}

	protected void clearValueMap() {
		this.valuesMap.clear();
		this.valuesMap.put("root", this.root);
	}

	public abstract void subLoad();

	public XMLConfiguration getConfig() {
		return this.config;
	}

	public String getXmlConfigFile() {
		return this.xmlConfigFile;
	}

	protected Map<String, String> parseXml2Map(HierarchicalConfiguration xmlProperty, String[] args) {
		HashMap result = new HashMap();

		try {
			this.setValue(xmlProperty, args, result, true);
		} catch (Exception var5) {
			_log.warn(var5);
		}

		return result;
	}

	protected Map<String, String> parseXmlList2Map(List<HierarchicalConfiguration> hcList, String[] args) {
		HashMap result = new HashMap();

		try {
			Iterator var4 = hcList.iterator();

			while (var4.hasNext()) {
				HierarchicalConfiguration xmlProperty = (HierarchicalConfiguration) var4.next();
				this.setValue(xmlProperty, args, result, false);
			}
		} catch (Exception var6) {
			_log.warn(var6);
		}

		return result;
	}

	protected Object parseXmlList2Object(List<HierarchicalConfiguration> hcList, Class descClazz, String[] args) {
		Object result = null;

		try {
			result = descClazz.newInstance();
			Iterator var5 = hcList.iterator();

			while (var5.hasNext()) {
				HierarchicalConfiguration xmlProperty = (HierarchicalConfiguration) var5.next();
				this.setValue(xmlProperty, args, result, false);
			}
		} catch (Exception var7) {
			_log.warn(var7);
		}

		return result;
	}

	protected List<Object> parseXmlList2List(List<HierarchicalConfiguration> hcList, Class descClazz, String[] args) {
		ArrayList result = new ArrayList();

		try {
			Iterator var5 = hcList.iterator();

			while (var5.hasNext()) {
				HierarchicalConfiguration xmlProperty = (HierarchicalConfiguration) var5.next();
				Object bean = descClazz.newInstance();
				this.setValue(xmlProperty, args, bean, true);
				result.add(bean);
			}
		} catch (Exception var8) {
			_log.warn(var8);
		}

		return result;
	}

	private void setValue(HierarchicalConfiguration xmlProperty, String[] args, Object bean, boolean innerAdd)
			throws Exception {
		String name = null;
		String value = null;
		String[] var7 = args;
		int var8 = args.length;

		for (int var9 = 0; var9 < var8; ++var9) {
			String attr = var7[var9];
			String[] tempValues = this.parseValue(xmlProperty, attr);
			name = tempValues[0];
			value = tempValues[1];
			tempValues = null;
			if (innerAdd) {
				if (Map.class.isInstance(bean)) {
					((Map) bean).put(name, value);
				} else {
					BeanUtils.setProperty(bean, name, value);
				}
			}
		}

		if (!innerAdd) {
			if (Map.class.isInstance(bean)) {
				((Map) bean).put(name, value);
			} else {
				BeanUtils.setProperty(bean, name, value);
			}
		}

		name = null;
		value = null;
	}

	private String[] parseValue(HierarchicalConfiguration xmlProperty, String keyValue) {
		String[] result = new String[2];
		String[] t = this.getKeyValue(keyValue);
		result[0] = t[0];
		if (result[0] == null) {
			result[0] = this.getString(xmlProperty, t[1]);
		}

		result[1] = t[2];
		if (result[1] == null) {
			result[1] = this.getString(xmlProperty, t[3]);
		}

		t = null;
		return result;
	}

	private String getString(HierarchicalConfiguration xmlProperty, String keyName) {
		String value = null;
		if (VALUE.equals(keyName)) {
			Object o = xmlProperty.getRoot().getValue();
			value = o == null ? null : o.toString();
		} else {
			value = xmlProperty.getString(keyName);
		}

		return value;
	}

	private String[] getKeyValue(String str) {
		String[] result = new String[4];
		String[] temp = str.split("=");
		int len = temp.length;
		int i = 0;

		for (byte loop = 2; i < loop; ++i) {
			String t = temp[i >= len ? i - 1 : i];
			int index = i * 2;
			if (t.startsWith("{") && t.endsWith("}")) {
				result[index] = t.replaceAll("\\{", "").replaceAll("}", "");
			} else if (i == 0) {
				if (t.indexOf("@") != -1 && len == 1) {
					result[index] = t.replaceAll("\\[@", "").replaceAll("]", "");
				} else {
					result[index + 1] = t;
				}
			} else {
				result[index + 1] = t;
			}
		}

		return result;
	}

	protected List<String> split(String str, String delimiter) {
		List<String> result = null;
		if (str != null) {
			String[] temp = str.split(delimiter);
			result = new ArrayList();
			result.addAll(Arrays.asList(temp));
		}

		return result;
	}

	protected String parseMsg(List<String> list) {
		String result = "";
		if (list != null && list.size() > 0) {
			result = list.toString();
			result = result.substring(1, result.length() - 1);
		}

		return result;
	}

	protected String formatMsg(String origin) {
		return this.formatMsgFlag(origin)[0];
	}

	protected String[] formatMsgFlag(String origin) {
		String[] result = new String[] { Utils.strip(origin).trim(), null };
		HashSet<String> replaceKeys = new HashSet();
		if (!Utils.isRealEmpty(result[0])) {
			Matcher matcher = pattern.matcher(result[0]);

			while (matcher.find()) {
				replaceKeys.add(matcher.group(0));
			}

			matcher = null;
			Iterator var5 = replaceKeys.iterator();

			while (var5.hasNext()) {
				String target = (String) var5.next();
				String replacement = (String) this.valuesMap.get(target.substring(1, target.length() - 1));
				if (replacement == null) {
					result[1] = "";
				} else {
					result[0] = Utils.replace(result[0], target, Utils.strip(replacement));
				}
			}
		}

		return result;
	}
}