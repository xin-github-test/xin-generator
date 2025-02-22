package com.xin.maker.generator.main;

public class ZipGenerator extends GenerateTemplate{

    @Override
    protected String buildDist(String outputPath, String sourceCopyDestPath, String shellOutputPath, String jarPath) {
        String distPath = super.buildDist(outputPath, sourceCopyDestPath, shellOutputPath, jarPath);
        return super.buildZip(distPath);
    }
}
