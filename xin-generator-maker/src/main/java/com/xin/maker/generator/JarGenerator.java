package com.xin.maker.generator;

import java.io.*;

public class JarGenerator {
    public static void main(String[] args) throws IOException, InterruptedException {
        doGenerate("");
    }

    public static void doGenerate(String projectDir) throws IOException, InterruptedException {
        //执行 maven 打包命令(系统不一样，打包的命令也不一样)
        String winMavenCommand = "mvn.cmd clean package -DskipTests=true";
        String otherMavenCommand = "mvn clean package -DskipTests=true";
        String mavenCommand = winMavenCommand;

        ProcessBuilder processBuilder = new ProcessBuilder(mavenCommand.split(" "));
        processBuilder.directory(new File(projectDir));

        Process process = processBuilder.start();

        //读取命令的输出
        InputStream inputStream = process.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }

        int exitCode = process.waitFor();
        System.out.println("命令执行结束，退出码:"+exitCode);

    }
}
