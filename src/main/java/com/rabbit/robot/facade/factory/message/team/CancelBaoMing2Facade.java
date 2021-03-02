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
import com.rabbit.robot.utils.GroupMemberUtil;
import com.rabbit.robot.utils.MessageUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * @author 邢晨旭
 * @date 2020/11/11
 */
@Order(3)
@Component
public class CancelBaoMing2Facade  implements MessageFacade {

    @Autowired
    private TeamDAO teamDAO;
    @Autowired
    private TeamMemberDAO teamMemberDAO;

    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.GROUP_CANCEL_BAOMING2;
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
        String flagInfo = MessageUtil.getKeybyWord(content, 2);
        if (BaoMing2Facade.flag && sender.getId() != 251238841L) {
            SendHelper.sendSing(group, at.plus(new PlainText("无权限,请联系管理员")));
            return;
        }
        // 群内是否开团
        LambdaQueryWrapper<TeamDO> wrapper1 = Wrappers.lambdaQuery();
        wrapper1.eq(TeamDO::getGroupId, group.getId());
        wrapper1.eq(TeamDO::getDeleteYn, 0);
        List<TeamDO> teamDOList = teamDAO.selectList(wrapper1);
        if (CollectionUtils.isEmpty(teamDOList)) {
            SendHelper.sendSing(group, at.plus("暂无有效团队,请确认。"));
            return;
        }
        // 获取位置
        Long location = null;
        String locationStr = MessageUtil.getKeybyWord(content, 2);
        try {
            location = Long.valueOf(locationStr);
        } catch (Exception e) {
            SendHelper.sendSing(group, at.plus("请输入正确队伍位置。"));
            return;
        }
        // 位置合法性
        if (!GroupMemberUtil.checkLocation(location)) {
            SendHelper.sendSing(group, at.plus("请输入正确队伍位置。"));
            return;
        }
        // 查询位置报名情况
        LambdaQueryWrapper<TeamMemberDO> wrapper2 = Wrappers.lambdaQuery();
        wrapper2.eq(TeamMemberDO::getTeamId, group.getId());
        wrapper2.eq(TeamMemberDO::getLocation, location);
        wrapper2.eq(TeamMemberDO::getDeleteYn, 0);
        if (teamMemberDAO.selectCount(wrapper2) == 0) {
            SendHelper.sendSing(group, at.plus("未查到报名信息"));
            return;
        }
        TeamMemberDO updateDO = new TeamMemberDO();
        updateDO.setDeleteYn(1);
        LambdaUpdateWrapper<TeamMemberDO> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(TeamMemberDO::getTeamId, group.getId());
        wrapper.eq(TeamMemberDO::getLocation, location);
        wrapper.eq(TeamMemberDO::getDeleteYn, 0);
        teamMemberDAO.update(updateDO, wrapper);
        SendHelper.sendSing(group, at.plus("取消插队成功。"));
    }
}
