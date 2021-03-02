package com.rabbit.robot.facade.factory;

import com.rabbit.robot.helper.SendHelper;
import com.rabbit.robot.star.RobotStar;
import com.rabbit.robot.utils.DateUtil;
import com.rabbit.robot.utils.MessageUtil;
import net.mamoe.mirai.IMirai;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.internal.MiraiImpl;
import net.mamoe.mirai.message.data.FlashImage;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static com.rabbit.robot.star.RobotStar.myself;

/**
 * @author 邢晨旭
 * @date 2020/11/16
 */
@Component
public class ImageFacade {

    public void image(GroupMessageEvent event) {
        Image image = event.getMessage().get(Image.Key);
        FlashImage flashImage = event.getMessage().get(FlashImage.Key);
        if (image == null && flashImage == null) {
            return;
        }
        try {
            Group group = event.getGroup();
            Member sender = event.getSender();
            String userName = StringUtils.isEmpty(sender.getNameCard()) ? sender.getNick() : sender.getNameCard();
            String path = "C:\\Users\\Administrator\\Desktop\\images\\"+ group.getId() + "\\"+DateUtil.getDay()+"\\" + sender.getId() + "\\";
            String fileName =  event.getTime() + ".png";
            String imgUrl = "";
            if (image != null) {
                //保存到本地
                imgUrl = MiraiImpl.INSTANCE.queryImageUrl(RobotStar.bot,image);
            }
            if (flashImage != null) {
                image = flashImage.getImage();
                imgUrl = MiraiImpl.INSTANCE.queryImageUrl(RobotStar.bot,image);
                FlashImageFacade.flashImage(group, sender, userName, flashImage);
            }
            if(!"".equals(imgUrl)){
                MessageUtil.downloadPicture(imgUrl, path, fileName);
            }
        } catch (Exception e) {
            SendHelper.sendSing(myself, new PlainText("图片保存出错," + e));
        }
    }

}
