package com.iy.api.common.util;

import com.iy.api.common.annotation.Sensitive;
import com.iy.api.common.enums.SensitiveType;

import java.lang.reflect.Field;

public class SensitiveUtils {

    private SensitiveUtils() {
    }

    public static void desensitize(Object obj) {
        if (obj == null) {
            return;
        }
        
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        
        for (Field field : fields) {
            field.setAccessible(true);
            
            if (field.isAnnotationPresent(Sensitive.class)) {
                Sensitive sensitive = field.getAnnotation(Sensitive.class);
                
                try {
                    Object value = field.get(obj);
                    if (value != null && value instanceof String) {
                        String result = desensitizeValue((String) value, sensitive);
                        field.set(obj, result);
                    }
                } catch (IllegalAccessException e) {
                    // ignore
                }
            }
        }
    }

    private static String desensitizeValue(String value, Sensitive sensitive) {
        SensitiveType type = sensitive.type();
        
        switch (type) {
            case PHONE:
                return maskPhone(value);
            case EMAIL:
                return maskEmail(value);
            case ID_CARD:
                return maskIdCard(value);
            case BANK_CARD:
                return maskBankCard(value);
            case PASSWORD:
                return maskPassword(value);
            case NAME:
                return maskName(value);
            case ADDRESS:
                return maskAddress(value);
            case CUSTOM:
            default:
                return maskCustom(value, sensitive.start(), sensitive.end(), sensitive.mask());
        }
    }

    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 11) {
            return phone;
        }
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        int atIndex = email.indexOf("@");
        String prefix = email.substring(0, atIndex);
        String suffix = email.substring(atIndex);
        
        if (prefix.length() <= 2) {
            return prefix.charAt(0) + "****" + suffix;
        }
        return prefix.substring(0, 2) + "****" + suffix;
    }

    public static String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 18) {
            return idCard;
        }
        return idCard.substring(0, 4) + "**********" + idCard.substring(14);
    }

    public static String maskBankCard(String bankCard) {
        if (bankCard == null || bankCard.length() < 16) {
            return bankCard;
        }
        return bankCard.substring(0, 4) + "********" + bankCard.substring(12);
    }

    public static String maskPassword(String password) {
        if (password == null) {
            return null;
        }
        return "******";
    }

    public static String maskName(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        
        if (name.length() == 1) {
            return name;
        }
        
        if (name.length() == 2) {
            return name.charAt(0) + "*";
        }
        
        return name.charAt(0) + "*".repeat(name.length() - 1);
    }

    public static String maskAddress(String address) {
        if (address == null || address.length() <= 6) {
            return address;
        }
        return address.substring(0, 6) + "****";
    }

    public static String maskCustom(String value, int start, int end, String mask) {
        if (value == null) {
            return null;
        }
        
        int length = value.length();
        if (start >= length) {
            return value;
        }
        
        int actualEnd = Math.min(end, length);
        if (start >= actualEnd) {
            return value;
        }
        
        StringBuilder sb = new StringBuilder(value);
        for (int i = start; i < actualEnd; i++) {
            sb.setCharAt(i, mask.charAt(0));
        }
        return sb.toString();
    }
}