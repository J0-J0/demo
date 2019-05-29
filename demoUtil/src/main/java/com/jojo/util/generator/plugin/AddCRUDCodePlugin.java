package com.jojo.util.generator.plugin;

import java.io.IOException;
import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import com.jojo.util.generator.JavaCodeCRUDGenerator;

import freemarker.template.TemplateException;

public class AddCRUDCodePlugin extends PluginAdapter {

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		String fullyQualifiedName = topLevelClass.getType().getFullyQualifiedNameWithoutTypeParameters();
//		List<Field> fieldList = topLevelClass.getFields();
		try {
			JavaCodeCRUDGenerator.generate(fullyQualifiedName, null);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return true;
	}

}
