package com.xin.web.model.dto.generator;

import lombok.Data;

import java.util.Map;

/**
 * 使用代码生成器请求
 */
@Data
public class GeneratorUseRequest {
    /**
     * 生成器id
     */
    private Long id;

    /**
     * 数据模型
     */
    private Map<String,Object> dataModel;

    private static final long serialVersionUID = 1L;
}
