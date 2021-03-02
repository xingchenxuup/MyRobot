package com.rabbit.robot.utils;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.utils.ExternalResource;
import org.apache.commons.lang3.StringUtils;
import org.xhtmlrenderer.swing.Java2DRenderer;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 邢晨旭
 * @date 2020/4/9 下午4:52
 */
@Slf4j
public class MessageUtil {

    /**
     * 系统分隔符替换
     *
     * @param content
     * @return
     */
    public static String separator(String content) {
        return content.replace("？", "?");
    }

    /**
     * 关键词分割
     *
     * @param content
     * @return
     */
    public static List<String> split(String content) {
        List<String> keyList = Arrays.stream(separator(content).split("\\?")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        if (keyList.size() <= 1) {
            keyList = Arrays.stream(separator(content).split(" ")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        }
        return keyList;
    }

    /**
     * 获取一级关键词
     *
     * @param content
     * @return
     */
    public static String getKeybyWord(String content, Integer level) {
        if (StringUtils.isBlank(content)) {
            return "";
        }
        List<String> split = split(content);
        if (split.isEmpty() || split.size() < level) {
            return "";
        }
        return split.get(level - 1);
    }

    public static Boolean isNum(String num) {
        try {
            Integer.parseInt(num);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static BufferedImage convertXhtmlToImage(String html, Long groupId, int widthImage, int heightImage) {
        //将html转成文件
        String inputFilename = "team" + groupId;
        FileWriter writer = null;
        Java2DRenderer renderer = null;
        try {
            writer = new FileWriter(inputFilename);
            writer.write(html);
            writer.flush();
            writer.close();
            //将xhtml文件转成图片
            File f = new File(inputFilename);
            renderer = new Java2DRenderer(f, widthImage, heightImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert renderer != null;
        return renderer.getImage();
    }

    public static ExternalResource initImage(String url) {
        try {
            return ExternalResource.create(new URL(url).openStream());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static ExternalResource initImage(BufferedImage bufferedImage) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ExternalResource.create(os.toByteArray());
    }

    /**
     * 将InputStream写入本地文件
     *
     * @param destination 写入本地目录
     * @param input       输入流
     * @throws IOException IOException
     */
    public static void writeToLocal(String destination, InputStream input)
            throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        input.close();
        downloadFile.close();
    }

    public static void downloadPicture(String urlList, String path, String fileName) {
        URL url = null;
        try {
            File dir = new File(path);
            // 判断目录是否存在
            if (!dir.exists()) {
                //多层目录需要调用mkdirs
                dir.mkdirs();
            }
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path + fileName));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            BASE64Encoder encoder = new BASE64Encoder();
            String encode = encoder.encode(buffer);//返回Base64编码过的字节数组字符串
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
