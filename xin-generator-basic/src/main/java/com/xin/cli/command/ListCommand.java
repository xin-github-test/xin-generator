package com.xin.cli.command;

import cn.hutool.core.io.FileUtil;
import picocli.CommandLine;

import java.io.File;
import java.util.List;

@CommandLine.Command(name = "list", mixinStandardHelpOptions = true)
public class ListCommand implements Runnable{
    @Override
    public void run() {
        String path = System.getProperty("user.dir");
        // 整个项目的根路径
//        File parentFile = new File(path).getParentFile();
        //输入路径(后面的路径有问题)
        path = path + File.separator + "xin-generator-demo-projects";
        String inputPath = new File(path, "acm-template").getAbsolutePath();
        List<File> files = FileUtil.loopFiles(inputPath);
        for (File file : files) {
            System.out.println(file);
        }
    }
}
