package com.xin.maker.model;

import lombok.Data;

@Data
public class DataModel {
    /**
     * 核心模板
     */ 
    private MainTemplate mainTemplate = new MainTemplate();
    /**
     * 是否需要git文件
     */
    private boolean needGit = true;
    /**
     * 是否循环
     */
    private boolean loop = true;

    @Data
    public static class MainTemplate {
        //全部给上默认值
        private String author = "xin";
        private String outputText = "结果：";
    }
}
