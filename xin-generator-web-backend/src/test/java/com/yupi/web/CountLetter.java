package com.yupi.web;

import java.util.HashMap;
import java.util.Stack;

public class CountLetter {
    public static void main(String[] args) {
        String str1 = "X2Y3XZ";
        String str2 = "Z3X(XY)2";
        String str3 = "Z4(Y2(XZ2)3)2X2";
        countStr(str1);
        countStr(str2);
        countStr(str3);
    }
    public static String[] initStr(String str) {

        String newStr = new String();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isDigit(c) || c == '(' || c == ')') {
                newStr = newStr + c;
                continue;
            }
            if (i + 1 < str.length() && Character.isDigit(str.charAt(i + 1))) {
                newStr = newStr + c;
            } else {
                newStr = newStr + c + "1";
            }
        }

        System.out.println("初始化之后的字符串(ProMax)：" + newStr);

        String[] strArray = new String[newStr.length()];
        int ArrayIndex = 0;

        for (int i = 0; i < newStr.length(); i++) {
            char c = newStr.charAt(i);
            if (Character.isLetter(c) || c == '(' || c == ')') {
                strArray[ArrayIndex] = String.valueOf(c);
                ArrayIndex++;
                continue;
            }
            if (Character.isDigit(c)) {
                String Num = String.valueOf(c);
                if (i == newStr.length() - 1) {
                    strArray[ArrayIndex] = Num;
                    break;
                }
                for (int j = i + 1; j < newStr.length(); j++) {
                    char c1 = newStr.charAt(j);
                    if (Character.isDigit(c1)) {
                        Num += c1;
                        i = j;
                    } else {
                        strArray[ArrayIndex] = Num;
                        ArrayIndex++;
                        break;
                    }
                    if(j==newStr.length()-1){
                        strArray[ArrayIndex] = Num;
                    }
                }
            }
        }
        return strArray;
    }
    //计算结果
    public static void countStr(String str) {

        String[] strArray = initStr(str);
        HashMap<Character, Integer> map = new HashMap<>();
        Stack<String> stack = new Stack<>();
        for (int i = 0; i < strArray.length; i += 2) {
            String s = strArray[i];
            if (s == null) {
                break;
            }
            char c = s.charAt(0);
            if (c == '(') {
                stack.push(s);
                HashMap<Character, Integer> mapStack = new HashMap<>();
                for (int j = i + 1; j < strArray.length; j++) {
                    String s2 = strArray[j];
                    if (")".equals(s2)) {
                        String peek = "";
                        do {
                            String value = stack.pop();
                            String key = stack.pop();
                            if (!mapStack.containsKey(key)) {
                                mapStack.put(key.charAt(0), Integer.valueOf(value));
                            } else {
                                mapStack.put(key.charAt(0), mapStack.get(key) + Integer.valueOf(value));
                            }
                            peek = stack.peek();
                        } while (!"(".equals(peek));
                        stack.pop();
                        Integer NumValue = Integer.valueOf(strArray[j + 1]);
                        j = j + 1;
                        mapStack.entrySet().stream().forEach(entry -> {
                            mapStack.put(entry.getKey(), entry.getValue() * NumValue);
                        });
                        if (stack.empty()) {
                            System.out.println("栈Map：");
                            mapStack.entrySet().stream().forEach(entry -> System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue()));
                            i = j + 1 - 2;
                            mapStack.entrySet().stream().forEach((entry) -> {
                                Character key = entry.getKey();
                                Integer value = entry.getValue();
                                if (map.containsKey(key)) {
                                    map.put(key, map.get(key) + value);
                                } else {
                                    map.put(key, value);
                                }
                            });
                            break;
                        }
                    } else {
                        stack.push(s2);
                    }
                }
            } else {
                Integer value = 1;
                value = Integer.valueOf(strArray[i + 1]);
                if (map.containsKey(c)) {
                    map.put(c, map.get(c) + value);
                } else {
                    map.put(c, value);
                }
            }
        }
        System.out.println("统计Map：");
        map.entrySet().
                stream().
                forEach(entry -> System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue()));
    }

}