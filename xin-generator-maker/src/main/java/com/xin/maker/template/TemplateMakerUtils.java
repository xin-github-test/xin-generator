package com.xin.maker.template;

import cn.hutool.core.util.StrUtil;
import com.xin.maker.meta.Meta;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 模板制作工具类
 */
public class TemplateMakerUtils {
    /**
     * 文件列表去重 （从未分组的文件列表中移除组内同名文件）
     * @param fileInfoList
     * @return
     */
    public static List<Meta.FileConfig.FileInfo> removeGroupFilesFromRoot(List<Meta.FileConfig.FileInfo> fileInfoList) {
        //先获取所有分组信息
        List<Meta.FileConfig.FileInfo> groupFileInfoList = fileInfoList.stream()
                .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                .collect(Collectors.toList());
        //获取所有分组内的文件列表
        List<Meta.FileConfig.FileInfo> groupInnerFileInfoList = groupFileInfoList.stream()
                .flatMap(fileInfo -> fileInfo.getFiles().stream())
                .collect(Collectors.toList());
        //获取所有分组内文件的输入路径的集合
        Set<String> fileInputPathSet = groupInnerFileInfoList.stream()
                .map(fileInfo -> fileInfo.getInputPath().toLowerCase())
                .collect(Collectors.toSet());
        //移除所有在集合内的外层文件
        return fileInfoList.stream()
                .filter(fileInfo -> !fileInputPathSet.contains(fileInfo.getInputPath() == null ? null : fileInfo.getInputPath().toLowerCase()))
                .collect(Collectors.toList());
    }
    /**
     * 模型列表去重 （从未分组的模型列表中移除组内同名模型参数）
     * @param modelInfoList
     * @return
     */
    public static List<Meta.ModelConfig.ModelInfo> removeGroupModelsFromRoot(List<Meta.ModelConfig.ModelInfo> modelInfoList) {
        //先获取所有分组信息
        List<Meta.ModelConfig.ModelInfo> groupModelInfoList = modelInfoList.stream()
                .filter(modelInfo -> StrUtil.isNotBlank(modelInfo.getGroupKey()))
                .collect(Collectors.toList());
        //获取所有分组内的模型列表
        List<Meta.ModelConfig.ModelInfo> groupInnerModelInfoList = groupModelInfoList.stream()
                .flatMap(modelInfo -> modelInfo.getModels().stream())
                .collect(Collectors.toList());
        //获取所有分组内模型的参数名称的集合
        Set<String> modelFieldNameSet = groupInnerModelInfoList.stream()
                .map(modelInfo -> modelInfo.getFieldName().toLowerCase())
                .collect(Collectors.toSet());
        //移除所有在集合内的外层模型
        return modelInfoList.stream()
                .filter(modelInfo -> !modelFieldNameSet.contains(modelInfo.getFieldName() == null ? null : modelInfo.getFieldName().toLowerCase()))
                .collect(Collectors.toList());
    }

}
