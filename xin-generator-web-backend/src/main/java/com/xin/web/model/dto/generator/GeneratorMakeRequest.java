package com.xin.web.model.dto.generator;

import com.xin.maker.meta.Meta;
import lombok.Data;


/**
 * 使用代码生成器请求
 */
@Data
public class GeneratorMakeRequest {
    /**
     * 元信息
     */
    private Meta meta;

    /**
     * 模板文件压缩包路径
     */
    private String zipFilePath;

    private static final long serialVersionUID = 1L;
}
