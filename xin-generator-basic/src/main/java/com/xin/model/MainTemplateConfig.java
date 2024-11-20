package com.xin.model;

import lombok.Data;

/**
 * 明确需求
 * 1. 在代码开头增加作者@Author注释（增加代码）
 * 2. 修改程序输出的信息提示（替换代码）
 * 3. 将循环读取输入改为单次读取（可选代码）
 */
@Data
public class MainTemplateConfig {
    //全部给上默认值
    private String author = "xin";
    private String outputText = "结果：";
    /**
     * 是否循环
     */
    private boolean loop = true;
}
