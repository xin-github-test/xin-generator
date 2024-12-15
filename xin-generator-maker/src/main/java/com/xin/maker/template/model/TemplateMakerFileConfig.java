package com.xin.maker.template.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class TemplateMakerFileConfig {

    private List<FileInfoConfig> files;

    private FileGroupConfig fileGroupConfig;


    @Data
    @NoArgsConstructor
    public static class FileInfoConfig {
        private String path;

        private String condition;

        private List<FileFilterConfig> filterConfigList;
    }

    @Data
    @NoArgsConstructor
    public static class FileGroupConfig {
        private String condition;
        
        private String groupKey;

        private String groupName;

    }
}
