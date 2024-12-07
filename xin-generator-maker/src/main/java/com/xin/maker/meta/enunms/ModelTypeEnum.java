package com.xin.maker.meta.enunms;

/**
 * 模型类型枚举
 */
public enum ModelTypeEnum {
    /**
     * 字符串
     */
    STRING("字符串", "String"),
    /**
     * 布尔
     */
    BOOLEAN("布尔", "boolean");

    private final String text;
    private final String value;

    ModelTypeEnum(String text, String value) {
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
