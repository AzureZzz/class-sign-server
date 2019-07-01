package com.utils;

/**
 * 包含了二十六个字母和十个数字的字符数组
 */
public class AllCharacter {
    public static char[] charArray() {
        int i = 1234567890;
        String s = "qwertyuiopasdfghjklzxcvbnm";
        String S = s.toUpperCase();
        String word = S + i; //只包含大写字母和数字
        char[] c = word.toCharArray();

        return c;
    }
}

