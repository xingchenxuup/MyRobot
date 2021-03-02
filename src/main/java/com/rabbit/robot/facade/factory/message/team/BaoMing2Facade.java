package com.rabbit.robot.facade.factory.message.team;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rabbit.robot.DO.TeamDO;
import com.rabbit.robot.DO.TeamMemberDO;
import com.rabbit.robot.constants.CommonConstant;
import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.enums.EnumPosition;
import com.rabbit.robot.enums.EnumPositionLocation;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 邢晨旭
 * @date 2020/7/22 下午8:48
 */
@Order(7)
@Component
public class BaoMing2Facade implements MessageFacade {

    @Autowired
    private TeamDAO teamDAO;
    @Autowired
    private TeamMemberDAO teamMemberDAO;

    public static boolean flag = false;

    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.GROUP_BAOMING2;
    }

    @Override
    public void execute(Contact sender, Contact group, Message message) throws InterruptedException {
        String content = message.contentToString();

        if (!this.keyWordVerify(this.get(), content)) {
            return;
        }
        Member member = (Member) sender;
        At at = new At(member.getId());
        if (member.getPermission().getLevel() == 0 && sender.getId() != CommonConstant.ERROR_SEND_QQ) {
            SendHelper.sendSing(group, at.plus(new PlainText("无权限,请联系管理员")));
            return;
        }
        String flagInfo = MessageUtil.getKeybyWord(content, 2);
        if ("关闭".equals(flagInfo) && sender.getId() == CommonConstant.ERROR_SEND_QQ) {
            flag = true;
            SendHelper.sendSing(group, at.plus(new PlainText("插队功能关闭")));
            return;
        }
        if ("开启".equals(flagInfo) && sender.getId() == CommonConstant.ERROR_SEND_QQ) {
            flag = false;
            SendHelper.sendSing(group, at.plus(new PlainText("插队功能开启")));
            return;
        }
        if (flag && sender.getId() != CommonConstant.ERROR_SEND_QQ) {
            SendHelper.sendSing(group, at.plus(new PlainText("无权限,请联系管理员")));
            return;
        }
        // 检查群内是否开团
        LambdaQueryWrapper<TeamDO> wrapper1 = Wrappers.lambdaQuery();
        wrapper1.eq(TeamDO::getGroupId, group.getId());
        wrapper1.eq(TeamDO::getDeleteYn, 0);
        List<TeamDO> teamDOList = teamDAO.selectList(wrapper1);
        if (CollectionUtils.isEmpty(teamDOList)) {
            SendHelper.sendSing(group, at.plus("暂无有效团队,请确认。"));
            return;
        }
        // 检查职业
        String position = MessageUtil.getKeybyWord(content, 2);
        EnumPosition enumPosition = EnumPosition.get(position);
        if (enumPosition == null) {
            SendHelper.sendSing(group, at.plus("职业匹配错误,请输入正确报名格式eg:插队？分山？角色名？位置(几队第几位eg:22)," +
                    "参照以下职业列表:[凌雪,蓬莱,霸刀,长歌,奶歌,分山,铁骨,丐帮,焚影,明尊,田螺,惊羽,毒经,奶毒,藏剑,傲血,铁牢,剑纯,气纯,冰心,奶秀,花间,奶花,易筋,洗髓,衍天]"));
            return;
        }
        // 检查角色名
        String memberName = MessageUtil.getKeybyWord(content, 3);
        if (StringUtils.isBlank(memberName)) {
            SendHelper.sendSing(group, at.plus("请输入正确报名格式eg:插队？分山？角色名？位置(几队第几位eg:22)"));
            return;
        }

        // 报名
        // 获取位置
        Long location = null;
        String locationStr = MessageUtil.getKeybyWord(content, 4);
        try {
            location = Long.valueOf(locationStr);
        } catch (Exception e) {
            SendHelper.sendSing(group, at.plus("请输入正确报名格式eg:报名？分山？角色名？位置(几队第几位eg:22)"));
            return;
        }
        // 检查位置正确性
        if (!GroupMemberUtil.checkLocation(location)) {
            SendHelper.sendSing(group, at.plus("请输入正确队伍位置。"));
            return;
        }
        if (!check(position, location)) {
            SendHelper.sendSing(group, at.plus("奶歌专用位置，勿动"));
            return;
        }
        // 检查位置重复
        LambdaQueryWrapper<TeamMemberDO> wrapper2 = Wrappers.lambdaQuery();
        wrapper2.eq(TeamMemberDO::getTeamId, group.getId());
        wrapper2.eq(TeamMemberDO::getLocation, location);
        wrapper2.eq(TeamMemberDO::getDeleteYn, 0);
        List<TeamMemberDO> teamMemberDOS = teamMemberDAO.selectList(wrapper2);
        if (!CollectionUtils.isEmpty(teamMemberDOS)) {
            SendHelper.sendSing(group, at.plus("该位置已有人报名"));
            return;
        }
        TeamMemberDO insertDO = new TeamMemberDO();
        insertDO.setTeamId(group.getId());
        insertDO.setLocation(location);
        insertDO.setPosition(EnumPositionLocation.get(enumPosition.position).position);
        insertDO.setColor(enumPosition.color);
        insertDO.setSrc(enumPosition.src);
        insertDO.setMemberName(memberName);
        insertDO.setQq(sender.getId());
        insertDO.setType(enumPosition.type);
        insertDO.setIsInsert(1);
        teamMemberDAO.insert(insertDO);
        SendHelper.sendSing(group, at.plus("报名成功。"));
        TimeUnit.SECONDS.sleep(10);
    }

    private Boolean check(String position, Long location) {
        if (location == 45L) {
            return StringUtils.equalsAny(position, "奶歌", "相知");
        }
        return true;
    }


}
