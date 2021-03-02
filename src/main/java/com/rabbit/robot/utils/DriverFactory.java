//package com.rabbit.robot.utils;
//
//import lombok.extern.slf4j.Slf4j;
//import org.openqa.selenium.*;
//import org.openqa.selenium.phantomjs.PhantomJSDriver;
//import org.openqa.selenium.phantomjs.PhantomJSDriverService;
//import org.openqa.selenium.remote.DesiredCapabilities;
//import org.springframework.stereotype.Component;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author 邢晨旭
// * @date 2021/01/07
// */
//@Component
//@Slf4j
//public class DriverFactory {
//
//    public static PhantomJSDriver getDriver() {
//        DesiredCapabilities dcaps = new DesiredCapabilities();
//        //ssl证书支持
//        dcaps.setCapability("acceptSslCerts", true);
//        //截屏支持
//        dcaps.setCapability("takesScreenshot", true);
//        //css搜索支持
//        dcaps.setCapability("cssSelectorsEnabled", true);
//        dcaps.setCapability("phantomjs.page.settings.XSSAuditingEnabled", true);
//        dcaps.setCapability("phantomjs.page.settings.webSecurityEnabled", false);
//        dcaps.setCapability("phantomjs.page.settings.localToRemoteUrlAccessEnabled", true);
//        dcaps.setCapability("phantomjs.page.settings.XSSAuditingEnabled", true);
//
//        dcaps.setCapability("phantomjs.page.settings.loadImages", false);
//        //js支持
//        dcaps.setJavascriptEnabled(true);
//        //驱动支持
//        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "C:\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
//        //dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,systemProps.getPhantomjsPath());
//        //dcaps.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
//        //dcaps.setCapability("phantomjs.page.customHeaders.User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:50.0) Gecko/20100101 　　Firefox/50.0");
//        dcaps.setCapability("ignoreProtectedModeSettings", true);
////        org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
////        proxy.setProxyType(org.openqa.selenium.Proxy.ProxyType.MANUAL);
////        proxy.setHttpProxy("http://180.155.128.87:47593/");
////        dcaps.setCapability(CapabilityType.PROXY, proxy);
//        //创建无界面浏览器对象
//        PhantomJSDriver driver = new PhantomJSDriver(dcaps);
//        driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
//        driver.manage().timeouts().setScriptTimeout(120, TimeUnit.SECONDS);
//        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//        driver.manage().deleteAllCookies();
//        driver.manage().window().setSize(new Dimension(1920, 1080));
//        //创建无界面浏览器对象
//        return driver;
//
//    }
//
//    public static BufferedImage captureElement(File screenshot, WebElement element) {
//        BufferedImage img = null;
//        try {
//            img = ImageIO.read(screenshot);
//            int width = element.getSize().getWidth();
//            int height = element.getSize().getHeight();
//            //获取指定元素的坐标
//            Point point = element.getLocation();
//            //从元素左上角坐标开始，按照元素的高宽对img进行裁剪为符合需要的图片
//            return img.getSubimage(point.getX(), point.getY(), width, height);
//        } catch (IOException e) {
//            log.error("网页截图出错{}", e.getMessage());
//            return null;
//        }
//
//    }
//
//
//    public static void main(String[] args) throws InterruptedException, IOException {
//
//        PhantomJSDriver driver = DriverFactory.getDriver();
//        driver.get("https://jx3.xoyo.com/announce/");
//
//        //WebElement announce = driver.findElementByCssSelector("[class='allnews_list_ul clearfix']");
//
//        WebElement announce = driver.findElements(By.partialLinkText("版本更新内容公告")).get(0);
//        announce.click();
////        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
////        String windowHandle = driver.getWindowHandle();
////        driver.switchTo().window(windowHandle);
//        String FirstHandle = driver.getWindowHandle();     //首先得到最先的窗口 权柄
//        for(String winHandle : driver.getWindowHandles()) {    //得到浏览器所有窗口的权柄为Set集合，遍历
//            if (winHandle.equals(FirstHandle)) {				//如果为 最先的窗口 权柄跳出
//                continue;
//            }
//            driver.switchTo().window(winHandle);             //如果不为 最先的窗口 权柄，将 新窗口的操作权柄  给 driver
//            System.out.println(driver.getCurrentUrl());     //打印是否为新窗口
//            break;
//        }
////        driver.findElementByCssSelector("[class=\"hd_web_box\"]");
////        driver.getScreenshotAs(OutputType.FILE);
//        BufferedImage bufferedImage = captureElement(driver.getScreenshotAs(OutputType.FILE), driver.findElementByCssSelector("[class='detail_con']"));
//        File outputfile = new File("image.jpg");
//        ImageIO.write(bufferedImage, "jpg", outputfile);
//        driver.quit();
//    }
//
//    //attr-href="/announce/" 点击
//    //class="xfe-group-41 news_info_box"
//    //ul display: block; 第一个li
//}
