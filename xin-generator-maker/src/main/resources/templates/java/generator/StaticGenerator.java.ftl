package ${basePackage}.generator;

import cn.hutool.core.io.FileUtil;

public class StaticGenerator {
    /**
     * 拷贝文件
     * @param inputPath 源路径
     * @param outputPath 目标路径
     */
    public static void copyFilesByHutool(String inputPath, String outputPath) {
        FileUtil.copy(inputPath, outputPath, false);
    }
}
