package com.jojo.springmvc.util.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * 增删改查生成
 */
public class JavaCodeCRUDGenerator {

    /**
     * 项目文件根目录
     */
    private static final String DEFAULT_PROJECT_DIRECTORY = System.getProperty("user.dir");

    /**
     * 分割完整类名的正则
     */
    private static final String CLASSNAME_SPLIT_REGEX = "\\.";

    /**
     * 默认的模板文件目录
     */
    public static final String TEMPLATE_FILE_DIRECTORY = "demoUtil/src/main/resources/generatorTemplate";

    /**
     * 默认的controller模板文件名
     */
    public static final String CONTROLLER_TEMPLATE_NAME = "controllerApi.ftlh";

    /**
     * 默认的jsp模板文件名
     */
    public static final String JSP_TEMPLATE_NAME = "jsp.ftlh";

    /**
     * 默认的service模板文件名
     */
    public static final String SERVICE_TEMPLATE_NAME = "service.ftlh";

    /**
     * 默认的serviceImpl模板文件名
     */
    public static final String SERVICEIMPL_TEMPLATE_NAME = "serviceImpl.ftlh";

    public static void generate(Class<?> clazz) throws IOException, TemplateException {
        generate(clazz.getName(), Arrays.asList(clazz.getFields()));
    }

    /**
     * 调用此方法生成代码，使用默认的模板目录与模板文件名
     *
     * @param fullyQualifiedName 全限定类名
     * @param fieldList          此参数以后再扩展
     * @throws IOException
     * @throws TemplateException
     */
    public static void generate(String fullyQualifiedName, List<Field> fieldList)
            throws IOException, TemplateException {

        generate(fullyQualifiedName, fieldList, TEMPLATE_FILE_DIRECTORY, CONTROLLER_TEMPLATE_NAME,
                SERVICE_TEMPLATE_NAME, SERVICEIMPL_TEMPLATE_NAME, JSP_TEMPLATE_NAME);

    }

    /**
     * 调用次方法生成代码，需要传入模板目录以及模板文件名，缺少哪个文件名，则哪个文件不再生成
     *
     * @param fullyQualifiedName
     * @param fieldList
     * @param templateFileDirectory
     * @param controllerTemplateName
     * @param serviceTemplateName
     * @param serviceimplTemplateName
     * @param jspTemplateName
     * @throws IOException
     * @throws TemplateException
     */
    public static void generate(String fullyQualifiedName, List<Field> fieldList, String templateFileDirectory,
                                String controllerTemplateName, String serviceTemplateName, String serviceimplTemplateName,
                                String jspTemplateName) throws IOException, TemplateException {
        String[] nameArr = StringUtils.split(fullyQualifiedName, CLASSNAME_SPLIT_REGEX);
        if (ArrayUtils.isEmpty(nameArr)) {
            return;
        }

        Map<String, String> param = extractTemplateParam(fullyQualifiedName, nameArr);

        // 新建freemarker的Configuration对象
        Configuration configuration = getConfiguration(templateFileDirectory);

        // 生成文件名
        int length = nameArr.length;
        String classSimpleName = nameArr[length - 1];
        // service文件名
        StringBuffer serviceBasePath = buildBasePath(nameArr, "demoTaskServiceProvider");
        StringBuffer serviceFileName = new StringBuffer(serviceBasePath).append("service").append(File.separator);
        serviceFileName.append(classSimpleName).append("Service.java");
        // serviceImpl文件名
        StringBuffer serviceImplFileName = new StringBuffer(serviceBasePath).append("service").append(File.separator);
        serviceImplFileName.append("impl").append(File.separator).append(classSimpleName).append("ServiceImpl.java");
        // 创建文件
        createFileByTemplate(configuration, serviceTemplateName, serviceFileName.toString(), param);
        createFileByTemplate(configuration, serviceimplTemplateName, serviceImplFileName.toString(), param);

//        // controller文件名
//        StringBuffer controllerFileName = new StringBuffer(basePath).append("controller").append(File.separator);
//        controllerFileName.append(classSimpleName).append("Controller.java");
//        // jsp文件名
//        StringBuffer jspFileName = new StringBuffer(DEFAULT_PROJECT_DIRECTORY).append(File.separator);
//        jspFileName.append("src").append(File.separator).append("main").append(File.separator);
//        jspFileName.append("webapp").append(File.separator).append("WEB-INF").append(File.separator);
//        jspFileName.append(classSimpleName).append(File.separator).append("list.jsp");
//        createFileByTemplate(configuration, controllerTemplateName, controllerFileName.toString(), param);
//        createFileByTemplate(configuration, jspTemplateName, jspFileName.toString(), param);
    }

    /**
     * 只考虑规范的全限定类名，即最后一个是类的名字，倒数第二个是实体类的包名。
     *
     * @param nameArr
     * @param projectName
     * @return
     */
    private static StringBuffer buildBasePath(String[] nameArr, String projectName) {
        if (StringUtils.isBlank(projectName)) {
            return new StringBuffer();
        }
        StringBuffer basePath = new StringBuffer(DEFAULT_PROJECT_DIRECTORY);
        basePath.append(File.separator).append(projectName);
        basePath.append(File.separator).append("src").append(File.separator);
        basePath.append("main").append(File.separator).append("java");
        for (int i = 0; i < nameArr.length - 3; i++) {// 就这个地方每个项目不一样
            basePath.append(File.separator).append(nameArr[i]);
        }
        basePath.append(File.separator);
        return basePath;
    }

    private static Map<String, String> extractTemplateParam(String fullyQualifiedName, String[] nameArr) {
        Map<String, String> param = Maps.newHashMap();

        param.put("className", fullyQualifiedName); // 全限定类名

        int length = nameArr.length;
        String classSimpleName = nameArr[length - 1];
        param.put("classSimpleName", classSimpleName);// 类名
        param.put("uncapitalizedClassSimpleName", StringUtils.uncapitalize(classSimpleName));// 首字母小写的类名

        StringBuffer basePackagName = new StringBuffer();
        for (int i = 0; i < nameArr.length - 2; i++) { // 去掉类名，以及实体类的包名
            basePackagName.append(nameArr[i]).append(".");
        }
        param.put("basePackagName", basePackagName.toString());// 最后面带个点的

        return param;
    }

    /**
     * 根据模板创建文件
     *
     * @param configuration
     * @param templateName
     * @param filePath
     * @param param
     * @throws IOException
     * @throws TemplateException
     */
    private static void createFileByTemplate(Configuration configuration, String templateName, String filePath,
                                             Map<String, String> param) throws IOException, TemplateException {
        if (StringUtils.isAnyBlank(templateName, filePath)) {
            return;
        }

        File controllerFile = new File(filePath);
        Files.createParentDirs(controllerFile);
        if (!controllerFile.exists()) {
            controllerFile.createNewFile();
        } else {
            System.out.println(filePath + "   文件已存在，将被覆盖");
        }

        BufferedWriter out = Files.newWriter(controllerFile, Charsets.UTF_8);

        Template controllerTemplate = configuration.getTemplate(templateName);
        controllerTemplate.process(param, out);
        System.out.println(filePath + "    done.");
    }

    /**
     * @param templateFileDirectory 模板文件目录
     * @return
     * @throws IOException
     */
    private static Configuration getConfiguration(String templateFileDirectory) throws IOException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
        // 设置模板文件的目录
        configuration.setDirectoryForTemplateLoading(new File(templateFileDirectory));
        // 编码
        configuration.setDefaultEncoding("UTF-8");
        // 异常处理
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return configuration;
    }

}
