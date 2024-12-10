package com.xin.maker.template;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import com.xin.maker.template.enums.FileFilterRangeEnum;
import com.xin.maker.template.enums.FileFilterRuleEnum;
import com.xin.maker.template.model.FileFilterConfig;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;

public class FileFilter {
    /**
     * 对某个文件或目录进行过滤，返回文件列表
     * @param filePath 文件路径
     * @param fileFilterConfigList 文件过滤配置
     * @return 过滤后的文件列表
     */

    public static List<File> doFilter(String filePath, List<FileFilterConfig> fileFilterConfigList) {
        // 根据路径获取所有文件
        List<File> fileList = FileUtil.loopFiles(filePath);
        // 过滤文件
         return fileList.stream()
         .filter(file -> doSingleFileFilter(fileFilterConfigList, file))
         .collect(Collectors.toList());
    }

    /**
     * 单个文件过滤
     * @param fileFilterConfigList 文件过滤配置
     * @param file 文件
     * @return 是否过滤
     */ 
    public static boolean doSingleFileFilter(List<FileFilterConfig> fileFilterConfigList, File file) {
        
        boolean result = true;

        String fileName = file.getName();
        String fileContent = FileUtil.readUtf8String(file);

        if (CollUtil.isEmpty(fileFilterConfigList)) {
            return true;
        }

        for (FileFilterConfig fileFilterConfig : fileFilterConfigList) {
            String range = fileFilterConfig.getRange();
            String rule = fileFilterConfig.getRule();
            String value = fileFilterConfig.getValue();
            
            FileFilterRangeEnum fileFilterRangeEnum = FileFilterRangeEnum.getEnumByValue(range);
            if (fileFilterRangeEnum == null) {
                continue;
            }

            //要过滤的内容
            String content = fileName;
            switch (fileFilterRangeEnum) {
                case FILE_NAME:
                    content = fileName;
                    break;
                case FILE_CONTENT:
                    content = fileContent;
                    break;
                default:
            }

            FileFilterRuleEnum fileFilterRuleEnum = FileFilterRuleEnum.getEnumByValue(rule);
            if (fileFilterRuleEnum == null) {
                continue;
            }

            switch (fileFilterRuleEnum) {
                case CONTAINS:
                    result = content.contains(value);
                    break;
                case STARTS_WITH:
                    result = content.startsWith(value);
                    break;
                case ENDS_WITH:
                    result = content.endsWith(value);
                    break;
                case REGEX:
                    result = content.matches(value);
                    break;
                case EQUALS:
                    result = content.equals(value);
                    break;
                default:
            }
        }

        return result;
    }

}