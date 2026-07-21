package com.iy.api.common.util;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class IdGenUtils {

    private static final AtomicLong SEQ = new AtomicLong(0);
    private static final Random RANDOM = new Random();
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    
    private static final String NUMBERS = "0123456789";
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String ALPHANUM = NUMBERS + LETTERS;

    private IdGenUtils() {
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static String uuidWithoutHyphen() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String uuidShort() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18);
    }

    public static long numericId() {
        String timestamp = LocalDateTime.now().format(DATETIME_FORMATTER);
        long sequence = SEQ.incrementAndGet() % 1000;
        return Long.parseLong(timestamp + String.format("%03d", sequence));
    }

    public static String numericIdStr() {
        String timestamp = LocalDateTime.now().format(DATETIME_FORMATTER);
        long sequence = SEQ.incrementAndGet() % 1000;
        return timestamp + String.format("%03d", sequence);
    }

    public static String numericIdStr(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive");
        }
        
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())));
        }
        return sb.toString();
    }

    public static String randomString(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive");
        }
        
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUM.charAt(RANDOM.nextInt(ALPHANUM.length())));
        }
        return sb.toString();
    }

    public static String randomString(int length, boolean uppercase) {
        String result = randomString(length);
        return uppercase ? result.toUpperCase() : result.toLowerCase();
    }

    public static String randomLetters(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive");
        }
        
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));
        }
        return sb.toString();
    }

    public static String randomLetters(int length, boolean uppercase) {
        String result = randomLetters(length);
        return uppercase ? result.toUpperCase() : result.toLowerCase();
    }

    public static String timestampId() {
        return LocalDateTime.now().format(DATETIME_FORMATTER);
    }

    public static String timestampId(String prefix) {
        return prefix + LocalDateTime.now().format(DATETIME_FORMATTER);
    }

    public static String orderId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = numericIdStr(4);
        return timestamp + random;
    }

    public static String orderId(String prefix) {
        return prefix + orderId();
    }

    public static String phoneCode() {
        return numericIdStr(6);
    }

    public static String phoneCode(int length) {
        return numericIdStr(length);
    }

    public static String verificationCode() {
        return numericIdStr(6);
    }

    public static String verificationCode(int length) {
        return numericIdStr(length);
    }

    public static String generateId(String prefix, int length) {
        if (length <= (prefix != null ? prefix.length() : 0)) {
            throw new IllegalArgumentException("Length must be greater than prefix length");
        }
        
        int randomLength = length - (prefix != null ? prefix.length() : 0);
        StringBuilder sb = new StringBuilder(length);
        if (prefix != null) {
            sb.append(prefix);
        }
        for (int i = 0; i < randomLength; i++) {
            sb.append(ALPHANUM.charAt(RANDOM.nextInt(ALPHANUM.length())));
        }
        return sb.toString();
    }

    public static String generateNumericId(String prefix, int length) {
        if (length <= (prefix != null ? prefix.length() : 0)) {
            throw new IllegalArgumentException("Length must be greater than prefix length");
        }
        
        int randomLength = length - (prefix != null ? prefix.length() : 0);
        StringBuilder sb = new StringBuilder(length);
        if (prefix != null) {
            sb.append(prefix);
        }
        for (int i = 0; i < randomLength; i++) {
            sb.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())));
        }
        return sb.toString();
    }

    public static long snowflakeId() {
        return IdUtil.getSnowflake(1).nextId();
    }

    public static void main(String[] args) {
        // 生成UUID
        String uid = IdGenUtils.uuid();

// 生成10位纯数字ID
        String numId = IdGenUtils.numericIdStr(10);

// 生成带前缀的订单号
        String orderNo = IdGenUtils.orderId("ORD");

// 生成6位验证码
        String code = IdGenUtils.verificationCode();

// 生成分布式雪花ID
        long snowflake = IdGenUtils.snowflakeId();

        System.out.println("UUID: " + uid);
        System.out.println("10位纯数字ID: " + numId);
        System.out.println("带前缀的订单号: " + orderNo);
        System.out.println("6位验证码: " + code);
        System.out.println("分布式雪花ID: " + snowflake);
    }
}