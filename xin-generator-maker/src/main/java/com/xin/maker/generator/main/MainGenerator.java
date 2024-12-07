package com.xin.maker.generator.main;

import freemarker.template.TemplateException;

import java.io.IOException;
//29 8分s
public class MainGenerator extends GenerateTemplate{
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        MainGenerator mainGenerator = new MainGenerator();
        mainGenerator.doGenerate();
    }

}
