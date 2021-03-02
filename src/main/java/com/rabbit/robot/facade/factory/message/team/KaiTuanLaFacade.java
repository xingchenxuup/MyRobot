package com.rabbit.robot.facade.factory.message.team;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.rabbit.robot.DO.TeamDO;
import com.rabbit.robot.DO.TeamMemberDO;
import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.facade.factory.message.MessageFacade;
import com.rabbit.robot.helper.SendHelper;
import com.rabbit.robot.mapper.TeamDAO;
import com.rabbit.robot.mapper.TeamMemberDAO;
import com.rabbit.robot.utils.GroupMemberUtil;
import gui.ava.html.image.generator.HtmlImageGenerator;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

import static com.rabbit.robot.utils.MessageUtil.initImage;

/**
 * @author 邢晨旭
 * @date 2020/11/11
 */
@Slf4j
@Order(7)
@Component
public class KaiTuanLaFacade implements MessageFacade {

    @Autowired
    private TeamDAO teamDAO;
    @Autowired
    private TeamMemberDAO teamMemberDAO;

    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.KAI_ZU_LA;
    }

    @Override
    public void execute(Contact sender, Contact group, Message message) {
        String content = message.contentToString();

        if (!this.keyWordVerify(this.get(), content)) {
            return;
        }
        At at = new At(sender.getId());
        log.info("sender->{}", sender);
        // 查询群内团队
        LambdaQueryWrapper<TeamDO> wrapper1 = Wrappers.lambdaQuery();
        wrapper1.eq(TeamDO::getGroupId, group.getId());
        wrapper1.eq(TeamDO::getDeleteYn, 0);
        List<TeamDO> teamDOList = teamDAO.selectList(wrapper1);
        if (CollectionUtils.isEmpty(teamDOList)) {
            SendHelper.sendSing(group, at.plus("群内暂无有效团队,请确认。"));
            return;
        }
        // 查询团队成员
        LambdaQueryWrapper<TeamMemberDO> wrapper2 = Wrappers.lambdaQuery();
        wrapper2.eq(TeamMemberDO::getTeamId, group.getId());
        wrapper2.eq(TeamMemberDO::getDeleteYn, 0);
        List<TeamMemberDO> teamMembers = teamMemberDAO.selectList(wrapper2);
        List<At> list = teamMembers.stream().map(tm -> new At(tm.getQq())).distinct().collect(Collectors.toList());
        List<List<At>> atList = Lists.partition(list, 13);
        // 渲染HTML
        String htmlStr = GroupMemberUtil.replaceMember(teamDOList.get(0).getTeamName(), teamMembers);
        HtmlImageGenerator generator = new HtmlImageGenerator();
        generator.loadHtml(GroupMemberUtil.replaceInit("", htmlStr));
        Image image = group.uploadImage(initImage(generator.getBufferedImage()));
        atList.forEach(ats -> {
            SendHelper.sendSing(group, new PlainText("兄弟们开组了！").plus(ats));
        });
        SendHelper.sendSing(group, new PlainText("团队详情如下:").plus(image));
    }

}
