package com.rabbit.robot.facade.factory.message.team;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rabbit.robot.DO.TeamDO;
import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.facade.factory.message.MessageFacade;
import com.rabbit.robot.helper.SendHelper;
import com.rabbit.robot.mapper.TeamDAO;
import com.rabbit.robot.utils.MessageUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import java.util.List;

/**
 * @author 邢晨旭
 * @date 2020/7/22 下午4:53
 */
@Order(6)
@Component
public class UpdateTuanFacade implements MessageFacade {

    @Autowired
    private TeamDAO teamDAO;

    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.GROUP_UPDATE_TUAN;
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
        if (CollectionUtils.isEmpty(teamDOList)) {
            SendHelper.sendSing(group, at.plus(new PlainText("当前无有效团队,请确认")));
            return;
        }
        String newTeamName = MessageUtil.getKeybyWord(content, 2);
        if (StringUtils.isBlank(newTeamName)) {
            SendHelper.sendSing(group, at.plus(new PlainText("请输入团名。")));
            return;
        }

        TeamDO update = new TeamDO();
        update.setTeamName(newTeamName);
        update.setGmtModify(new Date());
        LambdaQueryWrapper<TeamDO> wrapper2 = Wrappers.lambdaQuery();
        wrapper2.eq(TeamDO::getGroupId, group.getId());
        wrapper2.eq(TeamDO::getDeleteYn, 0);
        teamDAO.update(update, wrapper2);
        SendHelper.sendSing(group, at.plus("团名已修改为：" + newTeamName));
    }

}
