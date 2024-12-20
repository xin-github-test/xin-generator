package com.xin.maker.generator.file;

import cn.hutool.core.io.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

//14
public class DynamicFileGenerator {
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

        //若是文件不存在则创建文件
        if (!FileUtil.exist(outputPath)) {
            FileUtil.touch(outputPath);
        }

        //生成指定文件
        Writer out = new OutputStreamWriter(new FileOutputStream(outputPath), StandardCharsets.UTF_8);
        
        template.process(model, out);

        out.close();
    }
}
