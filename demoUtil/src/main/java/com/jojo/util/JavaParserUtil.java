package com.jojo.util;

import java.io.File;
import java.util.List;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.google.common.base.Joiner;

public class JavaParserUtil {
	public static void main(String[] args) throws Exception {

		CompilationUnit compilationUnit = StaticJavaParser
				.parse(new File("C:\\WorkSpace\\ecplise\\BMS\\src\\main\\mmlib\\com\\jiatu\\mmlib\\service\\impl\\interlib\\api\\InterlibReaderApi.java"));

		List<MethodDeclaration> methodDeclarationList = compilationUnit.findAll(MethodDeclaration.class);
		for (MethodDeclaration methodDeclaration : methodDeclarationList) {
			StringBuilder fullMethodName = getFullMethodName(methodDeclaration);
			fullMethodName.append(" {");
			System.out.println(fullMethodName);
			System.out.println(" return InterlibApi.VERSION_DEPRECATED_ERROR_RESPONSE;");

			// 代码块
			BlockStmt blockStmt = methodDeclaration.getBody().get();
			String bodyCode = blockStmt.toString();
			String[] arr = bodyCode.split("\n");
			for (int i = 1; i < arr.length - 1; i++) {
				System.out.println("//"+arr[i]);
			}
			System.out.println("}");
			
			
			System.out.println("\n\n\n");
		}

	}


	private static StringBuilder getFullMethodName(MethodDeclaration methodDeclaration) {
		StringBuilder fullMethodName = new StringBuilder();
		Joiner blankJoiner = Joiner.on(" ");

		// 方法名前缀，包括访问权限，静态非静态之类的信息
		NodeList<Modifier> modifierList = methodDeclaration.getModifiers();
		blankJoiner.appendTo(fullMethodName, modifierList);

		// 返回类型
		String returnType = methodDeclaration.getTypeAsString();
		fullMethodName.append(" ").append(returnType);

		// 方法名
		String methodSimpleName = methodDeclaration.getName().asString();
		fullMethodName.append(" ").append(methodSimpleName);

		// 参数
		NodeList<Parameter> parameterList = methodDeclaration.getParameters();
		fullMethodName.append("(");
		Joiner.on(", ").appendTo(fullMethodName, parameterList);
		fullMethodName.append(")");
		return fullMethodName;
	}
}
