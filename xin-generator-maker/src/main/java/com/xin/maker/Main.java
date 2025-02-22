package com.xin.maker;

import java.io.IOException;

import com.xin.maker.generator.main.GenerateTemplate;
import com.xin.maker.generator.main.MainGenerator;

import com.xin.maker.generator.main.ZipGenerator;
import freemarker.template.TemplateException;
//38
public class Main {
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
//        MainGenerator generateTemplate = new MainGenerator();
        GenerateTemplate generateTemplate = new ZipGenerator();
        generateTemplate.doGenerate();
    }
}
