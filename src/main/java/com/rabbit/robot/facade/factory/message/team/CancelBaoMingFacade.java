package com.rabbit.robot.facade.factory.message.team;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rabbit.robot.DO.TeamDO;
import com.rabbit.robot.DO.TeamMemberDO;
import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.enums.EnumPositionType;
import com.rabbit.robot.facade.factory.message.MessageFacade;
import com.rabbit.robot.helper.SendHelper;
import com.rabbit.robot.mapper.TeamDAO;
import com.rabbit.robot.mapper.TeamMemberDAO;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import org.springframework.beans.factory.annotation.Autowired;;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author 邢晨旭
 * @date 2020/11/11
 */
@Order(3)
@Component
public class CancelBaoMingFacade implements MessageFacade {

    @Autowired
    private TeamDAO teamDAO;
    @Autowired
    private TeamMemberDAO teamMemberDAO;

    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.GROUP_CANCEL_BAOMING;
    }

    @Override
    public void execute(Contact sender, Contact group, Message message) {
        String content = message.contentToString();

        if (!this.keyWordVerify(this.get(), content)) {
            return;
        }
        At at = new At(sender.getId());

        // 群内是否开团
        LambdaQueryWrapper<TeamDO> wrapper1 = Wrappers.lambdaQuery();
        wrapper1.eq(TeamDO::getGroupId, group.getId());
        wrapper1.eq(TeamDO::getDeleteYn, 0);
        List<TeamDO> teamDOList = teamDAO.selectList(wrapper1);
        if (CollectionUtils.isEmpty(teamDOList)) {
            SendHelper.sendSing(group, at.plus("暂无有效团队,请确认。"));
            return;
        }
//        // 获取位置
//        Long location = null;
//        String locationStr = MessageUtil.getKeybyWord(content, 2);
//        try {
//            location = Long.valueOf(locationStr);
//        } catch (Exception e) {
//            SendHelper.sendSing(group, at.plus("请输入正确队伍位置。"));
//            return;
//        }
//        // 位置合法性
//        if (!GroupMemberUtil.checkLocation(location)) {
//            SendHelper.sendSing(group, at.plus("请输入正确队伍位置。"));
//            return;
//        }
        // 查询位置报名情况
        LambdaQueryWrapper<TeamMemberDO> wrapper2 = Wrappers.lambdaQuery();
        wrapper2.eq(TeamMemberDO::getTeamId, group.getId());
        wrapper2.eq(TeamMemberDO::getQq, sender.getId());
        wrapper2.eq(TeamMemberDO::getDeleteYn, 0);
        wrapper2.eq(TeamMemberDO::getIsInsert, 0);
        wrapper2.ne(TeamMemberDO::getType, EnumPositionType.LAO_BAN.type);
//        if (CollectionUtils.isEmpty(teamMemberDOS)) {
//            SendHelper.sendSing(group, at.plus("该位置暂无人报名,请确认。"));
//            return;
//        }
        // TODO 取消权限校验
        if (teamMemberDAO.selectCount(wrapper2) == 0) {
            SendHelper.sendSing(group, at.plus("未查到报名信息,老板取消请联系管理"));
            return;
        }
        TeamMemberDO updateDO = new TeamMemberDO();
        updateDO.setDeleteYn(1);
        LambdaUpdateWrapper<TeamMemberDO> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(TeamMemberDO::getTeamId, group.getId());
        wrapper.eq(TeamMemberDO::getQq, sender.getId());
        wrapper.eq(TeamMemberDO::getDeleteYn, 0);
        wrapper.eq(TeamMemberDO::getIsInsert, 0);
        teamMemberDAO.update(updateDO, wrapper);
        SendHelper.sendSing(group, at.plus("取消报名成功。"));
    }
}
