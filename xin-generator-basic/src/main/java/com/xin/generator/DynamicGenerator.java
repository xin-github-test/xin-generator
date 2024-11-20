package com.xin.generator;

import com.xin.model.MainTemplateConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

//14
public class DynamicGenerator {
    public static void main(String[] args) throws IOException, TemplateException {
        // 指定模板文件所在的路径
        String projectPath = System.getProperty("user.dir") + File.separator + "xin-generator-basic";
        String outputPath = projectPath + File.separator + "MainTemplate.java";
        String inputPaht = projectPath + File.separator+"src/main/resources/templates/MainTemplate.java.ftl";

        //数据模型
        MainTemplateConfig templateConfig = new MainTemplateConfig();
        templateConfig.setAuthor("xin");
        templateConfig.setOutputText("xin的输出：");
        templateConfig.setLoop(false);

        doGenerator(inputPaht, outputPath, templateConfig);
    }

    public static void doGenerator(String inputPath, String outputPath, Object model) throws IOException, TemplateException {
        // new 出 Configuration 对象，参数为 FreeMarker 版本号
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        // 指定模板文件所在的路径
        File inputFile = new File(inputPath);
        configuration.setDirectoryForTemplateLoading(inputFile.getParentFile());

        // 设置模板文件使用的字符集
        configuration.setDefaultEncoding("utf-8");
        configuration.setNumberFormat("0.######");
        // 创建模板对象，加载指定模板
        Template template = configuration.getTemplate(inputFile.getName());


        //生成指定文件
        Writer out = new FileWriter(outputPath);
        template.process(model, out);

        out.close();
    }
}
