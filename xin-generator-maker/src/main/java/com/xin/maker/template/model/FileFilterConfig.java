package com.xin.maker.template.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件过滤配置
 */ 
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileFilterConfig {


    /**
     * 过滤范围
     */
    private String range;

    /**
     * 过滤规则
     */
    private String rule;

    /**
     * 过滤值
     */
    private String value;
}
