package com.xin.maker.meta.enunms;
/**
 * 文件类型枚举
 */
public enum FileTypeEnum {
    /**
     * 目录
     */
    DIR("目录", "dir"),
    /**
     * 文件
     */
    FILE("文件", "file"),
    /**
     * 文件组
     */
    GROUP("文件组", "group");

    private final String text;
    private final String value;

    FileTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }
}
