package com.xin.generator;

import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
//16
public class MainGenerator {
    public static void doGenerator(Object model) throws TemplateException, IOException {
        //1.静态文件生成
        String path = System.getProperty("user.dir");
        String inputPath = path + File.separator + "acm-template";
        String outputPath = path + File.separator + "xin-generator-demo-projects";
        StaticGenerator.copyFilesByHutool(inputPath, outputPath);
//        System.out.println("静态文件的路径：path="+path+";"+"inputPath="+inputPath);
        //2.动态文件生成
        // 指定模板文件所在的路径
        String dynamicInputPath = path + File.separator +"xin-generator-basic"+ File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"templates"+File.separator+"MainTemplate.java.ftl";
        String dynamicOutputPath = outputPath + File.separator+"acm-template"+File.separator+"src"+File.separator+"com"+File.separator+"xin"+File.separator+"acm"+File.separator+"MainTemplate.java";

        DynamicGenerator.doGenerator(dynamicInputPath, dynamicOutputPath, model);
    }
}
