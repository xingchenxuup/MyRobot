package com.rabbit.robot.facade.factory.message.team;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rabbit.robot.DO.TeamDO;
import com.rabbit.robot.DO.TeamMemberDO;
import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.facade.factory.message.MessageFacade;
import com.rabbit.robot.helper.SendHelper;
import com.rabbit.robot.mapper.TeamDAO;
import com.rabbit.robot.mapper.TeamMemberDAO;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import java.util.List;

/**
 * @author 邢晨旭
 * @date 2020/11/11
 */
@Order(4)
@Component
public class CancelKaiTuanFacade  implements MessageFacade {

    @Autowired
    private TeamDAO teamDAO;
    @Autowired
    private TeamMemberDAO teamMemberDAO;

    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.GROUP_CANCEL_KAITUAN;
    }

    @Override
    public void execute(Contact sender, Contact group, Message message) {
        String content = message.contentToString();

        if (!this.keyWordVerify(this.get(), content)) {
            return;
        }
        // TODO sender开团权限校验
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
            SendHelper.sendSing(group, at.plus(new PlainText("暂无有效团队,请确认。")));
            return;
        }

        TeamDO updateDO = new TeamDO();
        updateDO.setDeleteYn(1);
        updateDO.setGmtModify(new Date());
        LambdaUpdateWrapper<TeamDO> wrapper2 = Wrappers.lambdaUpdate();
        wrapper2.eq(TeamDO::getId, teamDOList.get(0).getId());
        teamDAO.update(updateDO, wrapper2);
        TeamMemberDO teamMemberDO = new TeamMemberDO();
        teamMemberDO.setDeleteYn(1);
        teamMemberDO.setGmtModify(new Date());
        LambdaUpdateWrapper<TeamMemberDO> wrapper3 = Wrappers.lambdaUpdate();
        wrapper3.eq(TeamMemberDO::getTeamId, group.getId());
        wrapper3.eq(TeamMemberDO::getDeleteYn, 0);
        teamMemberDAO.update(teamMemberDO, wrapper3);
        SendHelper.sendSing(group, at.plus("取消成功。"));
    }


}
