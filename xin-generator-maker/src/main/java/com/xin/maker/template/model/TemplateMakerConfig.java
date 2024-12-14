package com.xin.maker.template.model;

import com.xin.maker.meta.Meta;

import lombok.Data;

/**
 * 模板制作配置
 */
@Data
public class TemplateMakerConfig {
    private Long id;

    private Meta meta = new Meta(); 

    private String originProjectPath;

    private TemplateMakerFileConfig fileConfig = new TemplateMakerFileConfig();

    private TemplateMakerModelConfig modelConfig = new TemplateMakerModelConfig();
}
