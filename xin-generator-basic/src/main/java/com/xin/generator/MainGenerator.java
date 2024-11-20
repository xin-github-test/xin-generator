package com.xin.generator;

import com.xin.model.MainTemplateConfig;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public class MainGenerator {
    public static void main(String[] args) throws TemplateException, IOException {
        //1.静态文件生成
        String path = System.getProperty("user.dir");
        String inputPath = "xin-generator-demo-project" + File.separator + "acm-template";
        String outputPath = path;
        StaticGenerator.copyFilesByHutool(inputPath, outputPath);

        //2.动态文件生成
        // 指定模板文件所在的路径
        String dynamicOutputPath = path + File.separator +"xin-generator-basic"+File.separator+"acm-template/src/com/yupi/acm/MainTemplate.java";
        String dynamicInputPath = path + File.separator+"src/main/resources/templates/MainTemplate.java.ftl";

        //数据模型
        MainTemplateConfig templateConfig = new MainTemplateConfig();
        templateConfig.setAuthor("xin");
        templateConfig.setOutputText("xin的输出：");
        templateConfig.setLoop(false);

        DynamicGenerator.doGenerator(dynamicInputPath, dynamicOutputPath, templateConfig);
    }
}
