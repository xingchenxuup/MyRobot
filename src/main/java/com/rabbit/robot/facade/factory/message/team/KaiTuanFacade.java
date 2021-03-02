package com.rabbit.robot.facade.factory.message.team;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rabbit.robot.DO.TeamDO;
import com.rabbit.robot.constants.CommonConstant;
import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.facade.factory.message.MessageFacade;
import com.rabbit.robot.helper.SendHelper;
import com.rabbit.robot.mapper.TeamDAO;
import com.rabbit.robot.utils.GroupMemberUtil;
import com.rabbit.robot.utils.MessageUtil;
import gui.ava.html.image.generator.HtmlImageGenerator;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.xhtmlrenderer.swing.Java2DRenderer;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import java.util.List;

import static com.rabbit.robot.utils.MessageUtil.initImage;

/**
 * @author 邢晨旭
 * @date 2020/7/22 下午4:53
 */
@Order(5)
@Component
public class KaiTuanFacade  implements MessageFacade {

    @Autowired
    private TeamDAO teamDAO;

    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.GROUP_KAITUAN;
    }

    @Override
    public void execute(Contact sender, Contact group, Message message) {
        String content = message.contentToString();

        if (!this.keyWordVerify(this.get(), content)) {
            return;
        }
        Member member = (Member) sender;
        At at = new At(member.getId());
        if (member.getPermission().getLevel() == 0 && sender.getId() != 251238841L) {
            SendHelper.sendSing(group, at.plus(new PlainText("无权限,请联系管理员")));
            return;
        }
        LambdaQueryWrapper<TeamDO> wrapper1 = Wrappers.lambdaQuery();
        wrapper1.eq(TeamDO::getGroupId, group.getId());
        wrapper1.eq(TeamDO::getDeleteYn, 0);
        List<TeamDO> teamDOList = teamDAO.selectList(wrapper1);
        if (!CollectionUtils.isEmpty(teamDOList)) {
            SendHelper.sendSing(group, at.plus(new PlainText("请勿重复开团。")));
            return;
        }

        String teamName = MessageUtil.getKeybyWord(content, 2);
        if (StringUtils.isBlank(teamName)) {
            SendHelper.sendSing(group, at.plus(new PlainText("请输入团名。")));
            return;
        }

        TeamDO insetDO = new TeamDO();
        insetDO.setGroupId(group.getId());
        insetDO.setTeamName(teamName);
        insetDO.setGmtCreate(new Date());
        teamDAO.insert(insetDO);

        HtmlImageGenerator generator = new HtmlImageGenerator();
        generator.loadHtml(GroupMemberUtil.replaceInit(teamName, CommonConstant.htmlStr));
        Image image = group.uploadImage(initImage(generator.getBufferedImage()));
        SendHelper.sendSing(group, at.plus("开团成功。").plus(image));
    }

}
