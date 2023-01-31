package cn.ctg.codegen.util;

public class TemplateMethod {
	public boolean startByUppercase(String str) {
		boolean result = false;
		if (!Utils.isEmpty(str)) {
			result = Character.isUpperCase(str.charAt(0));
		}

		return result;
	}

	public String toUnicode(String uniStr) {
		StringBuffer ret = new StringBuffer();
		if (uniStr == null) {
			return null;
		} else {
			int maxLoop = uniStr.length();

			for (int i = 0; i < maxLoop; ++i) {
				char character = uniStr.charAt(i);
				if (character <= 127) {
					ret.append(character);
				} else {
					ret.append("\\u");
					String hexStr = Integer.toHexString(character);
					int zeroCount = 4 - hexStr.length();

					for (int j = 0; j < zeroCount; ++j) {
						ret.append('0');
					}

					ret.append(hexStr);
				}
			}

			return ret.toString();
		}
	}
}