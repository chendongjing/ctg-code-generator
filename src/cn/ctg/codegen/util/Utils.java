package cn.ctg.codegen.util;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Utils {
	public static String getProperty(String origen) {
		return getProperty(origen, 0, 0);
	}

	public static String getProperty(String origen, int start, int end) {
		return getProperty(origen, "CAPITALIZE", start, end);
	}

	public static String getProperty(String origen, String wordStyle, int start, int end) {
		return getProperty(origen, "_", wordStyle, start, end);
	}

	public static String getProperty(String origen, String delim, String wordStyle, int start, int end) {
		String[] temps = split(origen, delim);
		int size = temps.length;
		if (temps.length < 1) {
			temps = new String[] { "___" };
			size = 1;
		}

		StringBuffer sb = new StringBuffer();
		start = Math.max(start, 0);
		start = Math.min(start, size);
		if (end < 0) {
			end += size;
		} else {
			end = size - end;
		}

		end = Math.min(end, size);
		end = Math.max(end, 1);
		int i = start;

		for (int length = end; i < length; ++i) {
			if ("CAPITALIZE".equalsIgnoreCase(wordStyle)) {
				temps[i] = temps[i].toLowerCase();
				if (i == start) {
					sb.append(temps[i]);
				} else {
					sb.append(temps[i].substring(0, 1).toUpperCase() + temps[i].substring(1));
				}
			} else if ("IGNORECASE".equalsIgnoreCase(wordStyle)) {
				sb.append(temps[i]);
			} else if (!"UNCAPITALIZE".equalsIgnoreCase(wordStyle) && !"LOWERCASE".equalsIgnoreCase(wordStyle)
					&& "UPPERCASE".equalsIgnoreCase(wordStyle)) {
				;
			}
		}

		return sb.toString();
	}

	public static boolean isRealEmpty(String value) {
		return isEmpty(value) || value.trim().length() < 1;
	}

	public static boolean isEmpty(String value) {
		return value == null || value.length() < 1;
	}

	public static String strip(String value) {
		return strip(value, "");
	}

	public static String strip(String value, String defaultValue) {
		value = value == null ? defaultValue : value.trim();
		value = value.length() == 0 ? defaultValue : value;
		return value;
	}

	public static String[] split(String inStr, String delim) {
		ArrayList<String> list = new ArrayList();
		StringTokenizer st = new StringTokenizer(inStr, delim);

		while (st.hasMoreTokens()) {
			list.add(st.nextToken());
		}

		return (String[]) list.toArray(new String[list.size()]);
	}

	public static String concatPath(String path, String path2) {
		if (path == null) {
			path = "";
		}

		if (path2 == null) {
			path2 = "";
		}

		path = path.trim();
		path2 = path2.trim();
		if (path.length() > 1 && (!path.endsWith("/") || !path.endsWith("\\\\"))) {
			path = path + File.separator;
		}

		if (path2.startsWith("/")) {
			path2 = path2.substring(1);
		} else if (path2.startsWith("\\\\")) {
			path2 = path2.substring(2);
		}

		return path + path2;
	}

	public static String parsePath(String path) {
		path = strip(path);
		if (File.separator.equals("/")) {
			path = path.replaceAll("\\\\", File.separator);
		} else {
			path = path.replaceAll("/", File.separator + File.separator);
		}

		return path;
	}

	public static String replace(String inString, String target, String replacement) {
		StringBuffer sb = new StringBuffer(512);
		int fromIndex = 0;

		int pos = 0;
		for (int len = target.length(); (pos = inString.indexOf(target, fromIndex)) != -1; fromIndex = pos + len) {
			sb.append(inString.substring(fromIndex, pos)).append(replacement);
		}

		sb.append(inString.substring(fromIndex));
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(getProperty("TD_VOUCHER_TYPE"));
	}
}