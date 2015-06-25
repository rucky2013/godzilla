package cn.godzilla.common;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

public class StringUtil {
	
	public static boolean isEmpty(String str){
		
		return ("".equals(str)||str == null) ;
	}

	public static String getReqPrameter(HttpServletRequest request, String key) {

		return getReqPrameter(request, key, "");
	}
	
	public static String getReqPrameter(HttpServletRequest request, String key, String defaultValue) {

		String value = request.getParameter(key);
		value = StringUtils.isEmpty(value) ? defaultValue : value;
		return value;

	}
	
	public static String getReqAttribute(HttpServletRequest request, String key) {

		return getReqAttribute(request, key, "");
	}

	public static String getReqAttribute(HttpServletRequest request, String key, String defaultValue) {

		String value = (String) request.getAttribute(key).toString();
		value = StringUtils.isEmpty(value) ? defaultValue : value;
		return value;

	}

	public static String getArrayString(Map<String, String[]> map, String key) {
		return getArrayString(map, key, "");
	}
	
	public static String getArrayString(Map<String, String[]> map, String key, String defaultValue) {
		String value = (map.get(key)[0]==null?"":map.get(key)[0]).toString();
		value = StringUtils.isEmpty(value) ? "" : value;
		return value;
	}
	
	public static String getString(Map<String, Object> map, String key) {
		return getString(map, key, "");
	}
	
	public static String getString(Map<String, Object> map, String key, String defaultValue) {
		String value = (map.get(key)==null?"":map.get(key)).toString();
		value = StringUtils.isEmpty(value) ? "" : value;
		return value;
	}

	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		// 去掉"-"符号
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
	}

	private static final String UNIT = "万千佰拾亿千佰拾万千佰拾元角分";
	private static final String DIGIT = "零壹贰叁肆伍陆柒捌玖";
	private static final double MAX_VALUE = 9999999999999.99D;

	public static String change(double v) {
		if (v < 0 || v > MAX_VALUE) {
			return "参数非法!";
		}
		long l = Math.round(v * 100);
		if (l == 0) {
			return "零元整";
		}
		String strValue = l + "";
		// i用来控制数
		int i = 0;
		// j用来控制单位
		int j = UNIT.length() - strValue.length();
		String rs = "";
		boolean isZero = false;
		for (; i < strValue.length(); i++, j++) {
			char ch = strValue.charAt(i);
			if (ch == '0') {
				isZero = true;
				if (UNIT.charAt(j) == '亿' || UNIT.charAt(j) == '万' || UNIT.charAt(j) == '元') {
					rs = rs + UNIT.charAt(j);
					isZero = false;
				}
			} else {
				if (isZero) {
					rs = rs + "零";
					isZero = false;
				}
				rs = rs + DIGIT.charAt(ch - '0') + UNIT.charAt(j);
			}
		}
		if (!rs.endsWith("分")) {
			rs = rs + "整";
		}
		rs = rs.replaceAll("亿万", "亿");
		return rs;
	}

	public static void main(String[] args) {
		System.out.println(StringUtil.change(12356789.9845));
	}
}
