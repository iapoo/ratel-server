package org.ivipa.ratel.system.common.utils;

public class SystemUtils {


    public static boolean IsValidPassword(String password) {
        if(password == null || password.length() < 128) {
            return false;
        }
        return true;
    }
    public static boolean IsValidPasswordOld(String password) {
        if (password == null) {
            return false;
        }
        if (password.length() < 8) {
            return false;
        }
        boolean specialChar = false;
        boolean upperChar = false;
        boolean lowerChar = false;
        boolean digitChar = false;
        int charTypeCount = 0;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (c == '_' || c == '!' || c == '#' || c == '$') {
                specialChar = true;
            }
            if (c == '0' || (c >= '1' && c <= '9') ) {
                digitChar = true;
            }
            if (c >= 'a' && c <= 'z') {
                lowerChar = true;
            }
            if (c >= 'A' && c <= 'Z') {
                upperChar = true;
            }
            if(!(c == '_' || c == '!' || c == '#' || c == '$' || (c == '0' || (c >= '1' && c <= '9')) || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') )) {
                return false;
            }
        }
        if(specialChar) {
            charTypeCount ++;
        }
        if(digitChar) {
            charTypeCount ++;
        }
        if(lowerChar) {
            charTypeCount ++;
        }
        if(upperChar) {
            charTypeCount ++;
        }
        if(charTypeCount < 3) {
            return false;
        }
        return true;

    }
}
