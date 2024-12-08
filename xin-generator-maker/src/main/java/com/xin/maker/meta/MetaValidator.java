package com.xin.maker.meta;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;

import cn.hutool.core.util.StrUtil;

import java.nio.file.Paths;

import java.util.List;
import java.util.stream.Collectors;

import com.xin.maker.meta.Meta.ModelConfig.ModelInfo;
import com.xin.maker.meta.enunms.FileGenerateEnum;
import com.xin.maker.meta.enunms.FileTypeEnum;
import com.xin.maker.meta.enunms.ModelTypeEnum;
//31
public class MetaValidator {

    public static void doValidAndFill(Meta meta) {
        //基础信息校验和默认值
        validAndFillMetaRoot(meta);

        validAndFillFileConfig(meta);

        validAndFillModelConfig(meta);

    }

    private static void validAndFillModelConfig(Meta meta) {
        Meta.ModelConfig modelConfig = meta.getModelConfig();
        if (modelConfig == null) {
            return;
        }
        List<ModelInfo> models = modelConfig.getModels();
        if (!CollUtil.isNotEmpty(models)) {
            return;
        }
        for (ModelInfo model : models) {
            //为group不校验
            String groupKey = model.getGroupKey();
            if (StrUtil.isNotEmpty(groupKey)) {
                // 生成中间参数
                List<ModelInfo> subModelInfoList = model.getModels();
                String allArgsStr = subModelInfoList.stream()
                                .map(subModelInfo -> {return String.format("\"--%s\"", subModelInfo.getFieldName());})
                                .collect(Collectors.joining(","));
                model.setAllArgsStr(allArgsStr);
                continue;
            }
            String fieldName = model.getFieldName();
            if (StrUtil.isBlank(fieldName)) {
                throw new MetaException("未填写 fieldName ");
            }
            String modelInfoType = model.getType();
            if (StrUtil.isBlank(modelInfoType)) {
                model.setType(ModelTypeEnum.STRING.getValue());
            }
        }
    }

    private static void validAndFillFileConfig(Meta meta) {
        Meta.FileConfig fileConfig = meta.getFileConfig();
        if (fileConfig == null) {
            return;
        }
        String sourceRootPath = fileConfig.getSourceRootPath();
        if (StrUtil.isBlank(sourceRootPath)) {
            throw new MetaException("未填写 sourceRootPath ");
        }

        String defaultInputRootPath = ".source/" +
                FileUtil.getLastPathEle(Paths.get(sourceRootPath)).getFileName().toString();
        String inputRootPath = fileConfig.getInputRootPath();
        if (StrUtil.isEmpty(inputRootPath)) {
            fileConfig.setInputRootPath(defaultInputRootPath);
        }

        String outputRootPath = fileConfig.getOutputRootPath();
        String defaultOutPutRootPath = "generated";
        if (StrUtil.isEmpty(outputRootPath)) {
            fileConfig.setOutputRootPath(defaultOutPutRootPath);
        }
        String fileConfigType = fileConfig.getType();
        String defaultType = FileTypeEnum.DIR.getValue();
        if (StrUtil.isEmpty(fileConfigType)) {
            fileConfig.setType(defaultType);
        }

        List<Meta.FileConfig.FileInfo> files = fileConfig.getFiles();
        if (CollUtil.isEmpty(files)) {
            return;
        }
        for (Meta.FileConfig.FileInfo file : files) {
            String type = file.getType();
            if (FileTypeEnum.GROUP.getValue().equals(type)) {
                continue;
            }
            // inputPath 必填
            String inputPath = file.getInputPath();
            if (StrUtil.isBlank(inputPath)) {
                throw new MetaException("未填写 inputPath ");
            }
            //outputPath:默认等于inputPath
            String outputPath = file.getOutputPath();
            if (StrUtil.isEmpty(outputPath)) {
                file.setOutputPath(inputPath);
            }
            //type ：默认 inputPath有后缀（.java）则为 file ,否则为 dir
            if (StrUtil.isBlank(type)) {
                //获取文件后缀
                if (StrUtil.isBlank(FileUtil.getSuffix(inputPath))) {
                    file.setType(FileTypeEnum.DIR.getValue());
                }else {
                    file.setType(FileTypeEnum.FILE.getValue());
                }
            }
            //generateType : 文件的结尾非 ftl 则为static,否则为 dynamic
            String generateType = file.getGenerateType();
            if (StrUtil.isBlank(generateType)) {
                // 如果文件后缀为.ftl，则为dynamic生成
                if (inputPath.endsWith(".ftl")) {
                    file.setGenerateType(FileGenerateEnum.DYNAMIC.getValue());
                } else {
                    file.setGenerateType(FileGenerateEnum.STATIC.getValue());
                }
            }

        }

    }

    private static void validAndFillMetaRoot(Meta meta) {
        String name = StrUtil.blankToDefault(meta.getName(), "xin-generator");
        String description = StrUtil.emptyToDefault(meta.getDescription(), "xin的代码生成器");
        String basePackage = StrUtil.blankToDefault(meta.getBasePackage(), "com.xin");
        String version = StrUtil.emptyToDefault(meta.getVersion(), "1.0");
        String author = StrUtil.emptyToDefault(meta.getAuthor(), "xin");
        String createTime = StrUtil.emptyToDefault(meta.getCreateTime(), DateUtil.now());

        meta.setName(name);
        meta.setDescription(description);
        meta.setBasePackage(basePackage);
        meta.setVersion(version);
        meta.setAuthor(author);
        meta.setCreateTime(createTime);
    }
}
