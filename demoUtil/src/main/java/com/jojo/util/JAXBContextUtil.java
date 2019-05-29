package com.jojo.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 这个类的意义主要是减少newInstance的开销，方便转换类与字符串
 * 
 * @author jgy
 *
 */
public class JAXBContextUtil {

	private static ConcurrentHashMap<String, JAXBContext> contextMap = new ConcurrentHashMap<String, JAXBContext>();

	private static Logger logger = LoggerFactory.getLogger(JAXBContextUtil.class);

	/**
	 * 
	 * @param classOfT
	 * @return
	 */
	public static <T> JAXBContext getContext(Class<T> classOfT) {
		String className = classOfT.getName();
		JAXBContext context = contextMap.get(className);
		if (context != null) {
			return context;
		}

		try {
			context = JAXBContext.newInstance(classOfT);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		if (context != null) {
			contextMap.put(className, context);
		}

		return context;
	}

	/**
	 * 对象转xml
	 * 
	 * @param object
	 * @param classOfT
	 * @return
	 */
	public static <T> String beanToXML(T object, Class<T> classOfT) {
		String xmlString = "";
		JAXBContext context = getContext(classOfT);
		try {
			Marshaller m = context.createMarshaller();
			StringWriter writer = new StringWriter();
			m.marshal(object, writer);
			xmlString = writer.toString();
		} catch (JAXBException e) {
			logger.error("生成XML失败", e);
		}
		return xmlString;
	}

	/**
	 * 
	 * @param xmlString
	 * @param classOfT
	 * @return
	 */
	public static <T> T xmlToBean(String xmlString, Class<T> classOfT) {
		T result = null;
		JAXBContext context = JAXBContextUtil.getContext(classOfT);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream in = new ByteArrayInputStream(xmlString.getBytes());
			unmarshaller.unmarshal(in);
			@SuppressWarnings("unchecked")
			T tempResult = (T) unmarshaller.unmarshal(in);
			result = tempResult;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return result;
	}
}
