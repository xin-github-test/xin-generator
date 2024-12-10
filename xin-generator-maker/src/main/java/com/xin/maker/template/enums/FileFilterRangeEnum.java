package com.xin.maker.template.enums;

import cn.hutool.core.util.ObjectUtil;

/**
 * 文件过滤范围枚举
 */
public enum FileFilterRangeEnum {
    FILE_NAME("文件名", "fileName"),
    FILE_CONTENT("文件内容", "fileContent");

    private String text;

    private String value;

    FileFilterRangeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    /**
     * 根据value获取枚举
     */
    public static FileFilterRangeEnum getEnumByValue(String value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (FileFilterRangeEnum fileFilterRangeEnum : FileFilterRangeEnum.values()) {
            if (fileFilterRangeEnum.value.equals(value)) {
                return fileFilterRangeEnum;
            }
        }
        return null;
    }
}

