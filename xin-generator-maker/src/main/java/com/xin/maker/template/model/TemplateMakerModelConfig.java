package com.xin.maker.template.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class TemplateMakerModelConfig {

    private List<ModelInfoConfig> models;

    private ModelGroupConfig modelGroupConfig;


    @Data
    @NoArgsConstructor
    public static class ModelInfoConfig {
        private String fieldName;

        private String type;

        private String description;

        private String defaultValue;

        private String abbr;
        //用于替换哪些文本
        private String replaceText;
    }

    @Data
    @NoArgsConstructor
    public static class ModelGroupConfig {
        private String condition;
        
        private String groupKey;

        private String groupName;

        private String type;

        private String description;

    }
}
