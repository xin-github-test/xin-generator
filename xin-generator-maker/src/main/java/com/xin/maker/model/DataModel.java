package com.xin.maker.model;

import lombok.Data;

@Data
public class DataModel {
    //全部给上默认值
    private String author = "xin";
    private String outputText = "结果：";
    /**
     * 是否循环
     */
    private boolean loop = true;
}
