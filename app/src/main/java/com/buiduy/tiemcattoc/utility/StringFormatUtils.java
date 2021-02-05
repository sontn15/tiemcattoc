package com.buiduy.tiemcattoc.utility;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringFormatUtils {


    public static String convertUTF8ToString(String value) {
        try {
            String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").replace("Đ", "D")
                    .replace("đ", "d").toLowerCase();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
