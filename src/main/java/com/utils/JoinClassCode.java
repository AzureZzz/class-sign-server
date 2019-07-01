package com.utils;

import java.util.Random;

public class JoinClassCode {

    public static String joinCode() {

        char[] c = AllCharacter.charArray();//获取包含26个字母大写和数字的字符数组

        Random rd = new Random();
        String code = "";
        for (int k = 0; k < 6; k++) {
            int index = rd.nextInt(c.length);//随机获取数组长度作为索引
            code += c[index];//循环添加到字符串后面
        }
        return code;
    }

}
