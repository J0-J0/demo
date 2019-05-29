package com.jojo.util.generator.plugin;

import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.PrimitiveTypeWrapper;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * mybatis generator插件
 * 把表里的字段名字都添加到静态变量，方便程序的引用
 * 
 * @author chenyue
 * @date 2017-12-18 15:00
 */
public class AddStaticPropPlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		CommentGenerator commentGenerator = context.getCommentGenerator();
		// 获得所有的字段
		List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();
		for (IntrospectedColumn introspectedColumn : introspectedColumns) {
			String property = introspectedColumn.getJavaProperty();
			Field fieldFinalProp = new Field();
			fieldFinalProp.setName(property);
			fieldFinalProp.setStatic(true);
			fieldFinalProp.setFinal(true);
			fieldFinalProp.setVisibility(JavaVisibility.PUBLIC);
			fieldFinalProp.setType(PrimitiveTypeWrapper.getStringInstance());
			fieldFinalProp.setName("PROP_" + underScoreName(introspectedColumn.getJavaProperty()));
			fieldFinalProp.setInitializationString("\"" + introspectedColumn.getJavaProperty() + "\"");
			commentGenerator.addFieldComment(fieldFinalProp, introspectedTable);
			// 添加属性
			topLevelClass.addField(fieldFinalProp);
		}
		return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
	}

	private String underScoreName(String name) {
		StringBuilder result = new StringBuilder();
		if ((name != null) && (name.length() > 0)) {
			result.append(name.substring(0, 1).toUpperCase());
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				if ((s.equals(s.toUpperCase())) && (!Character.isDigit(s.charAt(0)))) {
					result.append("_");
				}
				result.append(s.toUpperCase());
			}
		}
		return result.toString();
	}

}
