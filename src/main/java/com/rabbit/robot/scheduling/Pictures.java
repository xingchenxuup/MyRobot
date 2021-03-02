//package com.rabbit.robot.scheduling;
//
//import com.rabbit.robot.DO.Picture;
//import com.rabbit.robot.constants.CommonConstant;
//import com.rabbit.robot.facade.factory.message.ZzFacade;
//import com.rabbit.robot.helper.SendHelper;
//import com.rabbit.robot.service.PictureService;
//import com.rabbit.robot.star.RobotStar;
//import lombok.extern.slf4j.Slf4j;
//import net.mamoe.mirai.message.data.PlainText;
//import org.apache.commons.lang3.StringUtils;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.springframework.beans.factory.annotation.Autowired;;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
///**
// * @author 邢晨旭
// * @date 2020/11/20
// */
//@Slf4j
//@Component
//public class Pictures {
//
//    private static String url = "https://www.siwashi.com/page/";
//    private static String flag1 = "https://www.siwashi.com/heisi/";
//    private static String flag2 = "https://www.siwashi.com/other/";
//    private static String flag3 = "https://www.siwashi.com/baisi/";
//    private static String flag4 = "https://www.siwashi.com/rosi/";
//    private static String flag5 = "https://sw.llcdn.cn/wp-content/uploads/2020/";
//
//    private static int page = 2;
//    private static int end = 78;
//
//    @Autowired
//    PictureService dao;
//
//    @Scheduled(cron = "0 0/10 21 * * ? ")
//    public void demoSchedule() {
//        Set<String> hrefs = new HashSet<>();
//        int x = 0;
//        for (int i = page; i <= end; i++) {
//            if (x > 10) {
//                page = i;
//                SendHelper.sendSing(myself, new PlainText("爬取结束，下一次爬取起始页码" + page));
//                break;
//            }
//            try {
//                Document doc = Jsoup.connect(url + i).get();
//                Elements links = doc.select("a[href]");
//                for (Element src : links) {
//                    //将tagname为img标签的取出，并输出
//                    String href = src.attr("href");
//                    if (StringUtils.containsAny(href, flag1, flag2, flag3, flag4)) {
//                        hrefs.add(href);
//                    }
//                }
//                Set<Picture> result = new HashSet<>();
//                hrefs.forEach(href -> {
//                    try {
//                        List<Picture> pictures = new ArrayList<>();
//                        Document docForHref = Jsoup.connect(href).get();
//                        Elements medias = docForHref.select("img[src]");
//                        for (Element src : medias) {
//                            //将tagname为img标签的取出，并输出
//                            String img = src.attr("src");
//                            if (img.contains(flag5) && img.length() >= 66 && img.length() <= 68) {
//                                Picture p = new Picture();
//                                p.setUrl(img);
//                                pictures.add(p);
//                            }
//                        }
//                        if (pictures.size() > 0) {
//                            pictures.remove(0);
//                            pictures.remove(pictures.size() - 1);
//                        }
//                        result.addAll(pictures);
//                        Thread.sleep(3000);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//                dao.saveBatch(result);
//            } catch (Exception e) {
//                log.error(e.getMessage(), e);
//            }
//            x++;
//            SendHelper.sendSing(myself, new PlainText("爬取第" + i + "页结束"));
//        }
//        ZzFacade.count = dao.count();
//    }
//
//
//}
