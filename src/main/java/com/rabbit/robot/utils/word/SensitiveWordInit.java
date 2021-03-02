//package com.rabbit.robot.utils.word;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
//import java.util.*;
//
///**
// * @author 邢晨旭
// * @date 2020/12/22
// */
//public class SensitiveWordInit {
//    private static String ENCODING = "utf-8";
//    @SuppressWarnings("rawtypes")
//    private static HashMap sensitiveWordMap;
//
//    SensitiveWordInit() {
//        super();
//    }
//
//
//    @SuppressWarnings("rawtypes")
//    Map initKeyWord() {
//        try {
//            //读取敏感词库
//            Set<String> keyWordSet = readSensitiveWordFile();
//            //将敏感词库加入到HashMap中
//            addSensitiveWordToHashMap(keyWordSet);
//            //spring获取application，然后application.setAttribute("sensitiveWordMap",sensitiveWordMap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return sensitiveWordMap;
//    }
//
//    /**
//     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
//     * 中 = {
//     * isEnd = 0
//     * 国 = {<br>
//     * isEnd = 1
//     * 人 = {isEnd = 0
//     * 民 = {isEnd = 1}
//     * }
//     * 男  = {
//     * isEnd = 0
//     * 人 = {
//     * isEnd = 1
//     * }
//     * }
//     * }
//     * }
//     * 五 = {
//     * isEnd = 0
//     * 星 = {
//     * isEnd = 0
//     * 红 = {
//     * isEnd = 0
//     * 旗 = {
//     * isEnd = 1
//     * }
//     * }
//     * }
//     * }
//     */
//    @SuppressWarnings({"rawtypes", "unchecked"})
//    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
//        sensitiveWordMap = new HashMap(keyWordSet.size());
//        String key = null;
//        Map nowMap = null;
//        Map<String, String> newWorMap = null;
//        //迭代keyWordSet
//        Iterator<String> iterator = keyWordSet.iterator();
//        while (iterator.hasNext()) {
//            key = iterator.next();
//            nowMap = sensitiveWordMap;
//            for (int i = 0; i < key.length(); i++) {
//                //转换成char型
//                char keyChar = key.charAt(i);
//                Object wordMap = nowMap.get(keyChar);
//                //如果存在该key，直接赋值
//                if (wordMap != null) {
//                    nowMap = (Map) wordMap;
//                } else {     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
//                    newWorMap = new HashMap<String, String>(key.length());
//                    //不是最后一个
//                    newWorMap.put("isEnd", "0");
//                    nowMap.put(keyChar, newWorMap);
//                    nowMap = newWorMap;
//                }
//
//                if (i == key.length() - 1) {
//                    //最后一个
//                    nowMap.put("isEnd", "1");
//                }
//            }
//        }
//    }
//
//    /**
//     * 读取敏感词库中的内容，将内容添加到set集合中
//     */
//    @SuppressWarnings("resource")
//    private Set<String> readSensitiveWordFile() throws Exception {
//        Set<String> set = null;
//        //读取文件
//        File file = new File("Constant.SENSITIVEWORD");
//        InputStreamReader read = new InputStreamReader(new FileInputStream(file), ENCODING);
//        try {
//            if (file.isFile() && file.exists()) {
//                set = new HashSet<String>();
//                BufferedReader bufferedReader = new BufferedReader(read);
//                String txt = null;
//                while ((txt = bufferedReader.readLine()) != null) {
//                    set.add(txt);
//                }
//            } else {         //不存在抛出异常信息
//                throw new Exception("敏感词库文件不存在");
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            read.close();     //关闭文件流
//        }
//        return set;
//    }
//}