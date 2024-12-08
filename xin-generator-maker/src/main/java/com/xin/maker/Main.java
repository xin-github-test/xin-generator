package com.xin.maker;

import java.io.IOException;

import com.xin.maker.generator.main.MainGenerator;

import freemarker.template.TemplateException;
//38
public class Main {
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        MainGenerator mainGenerator = new MainGenerator();
        mainGenerator.doGenerate();
    }
}
