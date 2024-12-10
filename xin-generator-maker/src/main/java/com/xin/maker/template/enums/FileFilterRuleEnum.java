package com.xin.maker.template.enums;

import cn.hutool.core.util.ObjectUtil;

/**
 * 文件过滤规则枚举
 */
public enum FileFilterRuleEnum {
    CONTAINS("包含", "contains"),
    STARTS_WITH("前缀匹配", "startWith"),
    ENDS_WITH("后缀匹配", "endsWith"),
    REGEX("正则", "regex"),
    EQUALS("相等", "equals");

    private String text;

    private String value;

    FileFilterRuleEnum(String   text, String value) {
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
     * @param value
     * @return
     */
    public static FileFilterRuleEnum getEnumByValue(String value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (FileFilterRuleEnum fileFilterRuleEnum : FileFilterRuleEnum.values()) {
            if (fileFilterRuleEnum.value.equals(value)) {
                return fileFilterRuleEnum;
            }
        }
        return null;
    }
}
