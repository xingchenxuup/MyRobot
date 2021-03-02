package com.rabbit.robot.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 邢晨旭
 * @date 2020/11/20
 */
@Slf4j
@Component
public class KaiFuJianKong {

//    private static String url = "https://www.siwashi.com/";
//    private static String url2 = "https://www.siwashi.com/heisi/54075.html";
//
//    @Scheduled(cron = "0/10 10 7 ? * 2,5,6 *")
//    public void demoSchedule() {
//
//    }
//0 0/10 20 * * ?
//    public static void main(String[] args) {
//        try {
//            Document doc = Jsoup.connect(url2).get();
//            Elements links = doc.select("a[href]");
//            //将含src的元素取出 例如：src="s.gif"
//            Elements media = doc.select("img[src]");
//            for (Element src : media) {
//                //将tagname为img标签的取出，并输出
//                System.out.println(src.attr("src"));
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//    }

    public static void main(String[] args) {
        System.out.println("https://sw.llcdn.cn/wp-content/uploads/2020/11/2e3d804398666cd.webp".length());
    }
}
