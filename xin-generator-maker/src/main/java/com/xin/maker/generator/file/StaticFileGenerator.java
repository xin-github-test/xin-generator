package com.xin.maker.generator.file;

import cn.hutool.core.io.FileUtil;

public class StaticFileGenerator {
    /**
     * 拷贝文件
     * @param inputPath 源路径
     * @param outputPath 目标路径
     */
    public static void copyFilesByHutool(String inputPath, String outputPath) {
        FileUtil.copy(inputPath, outputPath, false);
    }
}
