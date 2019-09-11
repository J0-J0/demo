package com.jojo.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	
	private static Logger logger = LoggerFactory.getLogger(RegexUtil.class);
	
	
	/**
	 * 取url中的文件名
	 * 
	 * @param url
	 * @return
	 */
	public static String getLastPartOfUrl(String url) {
		if (StringUtils.isBlank(url)) {
			logger.error("URL为空");
			return null;
		}
		int questionMark = url.indexOf("?");
		if (questionMark != -1) {
			url = url.substring(0, url.indexOf("?"));
		}
		String[] arr = url.split("/");
		if (ArrayUtils.isEmpty(arr)) {
			logger.error("该URL不合法或非restful风格");
			return null;
		}
		return arr[arr.length - 1];
	}

	/**
	 * 取url中文件后缀名
	 * 
	 * @param url
	 * @return
	 */
	public static String getSuffixFromUrl(String url) {
		if (StringUtils.isBlank(url)) {
			logger.error("URL为空");
			return null;
		}
		int questionMark = url.indexOf("?");
		if (questionMark != -1) {
			url = url.substring(0, url.indexOf("?"));
		}
		String[] arr = url.split("\\.");
		if (ArrayUtils.isEmpty(arr)) {
			logger.error("该URL中不包含文件名");
			return null;
		}
		return arr[arr.length - 1];
	}
	
	
	/**
	 * 根据正则表达式过滤字符串
	 * @param rawData
	 * @param regex
	 * @return
	 */
	public static List<String> getMatchedString(String rawData, String... regex) {
		if (ArrayUtils.isEmpty(regex) || StringUtils.isBlank(rawData)) {
			System.err.println("必填项不能为空");
		}
		List<String> resultList = Lists.newArrayList();
		// 开始匹配
		for (String reg : regex) {
			Pattern p = Pattern.compile(reg);
			Matcher m = p.matcher(rawData);
			while (m.find()) {
				String str = m.group();
				System.out.println("根据正则表达式" + reg + "找到目标" + str);
				resultList.add(str);
			}
		}
		System.out.println("总计：" + resultList.size() + "个");
		return resultList;
	}
	
}
