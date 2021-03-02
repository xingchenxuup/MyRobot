package com.rabbit.robot.facade.factory.message.team;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rabbit.robot.DO.TeamDO;
import com.rabbit.robot.DO.TeamMemberDO;
import com.rabbit.robot.enums.*;
import com.rabbit.robot.facade.factory.message.MessageFacade;
import com.rabbit.robot.helper.SendHelper;
import com.rabbit.robot.mapper.TeamDAO;
import com.rabbit.robot.mapper.TeamMemberDAO;
import com.rabbit.robot.utils.JxTeamAlgorithm;
import com.rabbit.robot.utils.MessageUtil;
import com.rabbit.robot.utils.MyException;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author 邢晨旭
 * @date 2020/7/22 下午8:48
 */
@Order(2)
@Component
@Slf4j
public class BaoMingFacade implements MessageFacade {

    @Autowired
    private TeamDAO teamDAO;
    @Autowired
    private TeamMemberDAO teamMemberDAO;

    @Autowired
    JxTeamAlgorithm jxTeamAlgorithm;

    @Override
    public EnumKeyWord get() {
        return EnumKeyWord.GROUP_BAOMING;
    }

    @Override
    public void execute(Contact sender, Contact group, Message message) throws InterruptedException {
        String content = message.contentToString();

        if (!this.keyWordVerify(this.get(), content)) {
            return;
        }
        At at = new At(sender.getId());
        // 检查群内是否开团
        LambdaQueryWrapper<TeamDO> wrapper1 = Wrappers.lambdaQuery();
        wrapper1.eq(TeamDO::getGroupId, group.getId());
        wrapper1.eq(TeamDO::getDeleteYn, 0);
        List<TeamDO> teamDOList = teamDAO.selectList(wrapper1);
        if (CollectionUtils.isEmpty(teamDOList)) {
            SendHelper.sendSing(group, at.plus("暂无有效团队,请确认。"));
            return;
        }
        String position = MessageUtil.getKeybyWord(content, 2);
        EnumPosition enumPosition = EnumPosition.get(position);

        if (!EnumPositionType.LAO_BAN.type.equals(enumPosition.type)) {
            LambdaQueryWrapper<TeamMemberDO> wrapper2 = Wrappers.lambdaQuery();
            wrapper2.eq(TeamMemberDO::getQq, sender.getId());
            wrapper2.eq(TeamMemberDO::getTeamId, group.getId());
            wrapper2.eq(TeamMemberDO::getDeleteYn, 0);
            wrapper2.ne(TeamMemberDO::getType, EnumPositionType.LAO_BAN.type);
            if (teamMemberDAO.selectCount(wrapper2) > 0) {
                SendHelper.sendSing(group, at.plus("请勿重复报名"));
                return;
            }
        }
        // 检查职业
//        String position = MessageUtil.getKeybyWord(content, 2);
//        EnumPosition enumPosition = EnumPosition.get(position);
        if (enumPosition == null) {
            SendHelper.sendSing(group, at.plus("职业匹配错误,请输入正确报名格式eg:报名？分山？角色名？,报名 老板 职业？" +
                    "参照以下职业列表:[凌雪,蓬莱,霸刀,长歌,奶歌,分山,铁骨,丐帮,焚影,明尊,田螺,惊羽,毒经,奶毒,藏剑,傲血,铁牢,剑纯,气纯,冰心,奶秀,花间,奶花,易筋,洗髓,衍天]"));
            return;
        }
        // 检查角色名
        String memberName = MessageUtil.getKeybyWord(content, 3);
        if (StringUtils.isBlank(memberName)) {
            SendHelper.sendSing(group, at.plus("请输入正确报名格式eg:报名？分山？角色名？,报名 老板 职业？"));
            return;
        }
        if (EnumPositionType.LAO_BAN.type.equals(enumPosition.type)) {
            EnumPositionLaoBan laoban = EnumPositionLaoBan.get(memberName);
            if (laoban == null) {
                SendHelper.sendSing(group, at.plus("请输入正确报名格式eg:报名？分山？角色名？,报名 老板 职业？"));
                return;
            }
            LambdaQueryWrapper<TeamMemberDO> wrapper3 = Wrappers.lambdaQuery();
            wrapper3.eq(TeamMemberDO::getTeamId, group.getId());
            wrapper3.eq(TeamMemberDO::getDeleteYn, 0);
            wrapper3.eq(TeamMemberDO::getType, EnumPositionType.LAO_BAN.type);
            wrapper3.likeRight(TeamMemberDO::getMemberName, laoban.position);
            if (teamMemberDAO.selectCount(wrapper3) > 0) {
                SendHelper.sendSing(group, at.plus("已有" + laoban.position + "老板"));
                return;
            }
        }
//        // 获取位置
//        Long location = null;
//        String locationStr = MessageUtil.getKeybyWord(content, 4);
//        try {
//            location = Long.valueOf(locationStr);
//        } catch (Exception e) {
//            SendHelper.sendSing(group, at.plus("请输入正确报名格式eg:报名？分山？角色名？位置(几队第几位eg:22)"));
//            return;
//        }
//        // 检查位置正确性
//        if (!GroupMemberUtil.checkLocation(location)) {
//            SendHelper.sendSing(group, at.plus("请输入正确队伍位置。"));
//            return;
//        }
//        // 检查位置重复
//        LambdaQueryWrapper<TeamMemberDO> wrapper2 = Wrappers.lambdaQuery();
//        wrapper2.eq(TeamMemberDO::getTeamId, teamDOList.get(0).getId());
//        wrapper2.eq(TeamMemberDO::getLocation, location);
//        wrapper2.eq(TeamMemberDO::getDeleteYn, 0);
//        List<TeamMemberDO> teamMemberDOS = teamMemberDAO.selectList(wrapper2);
//        if (!CollectionUtils.isEmpty(teamMemberDOS)) {
//            SendHelper.sendSing(group, at.plus("报名位置重复,请重新选择或联系团长调整。"));
//            return;
//        }
        // 报名
        Long location = null;
        try {
            assert EnumPositionLocation.get(enumPosition.position) != null;
            boolean result = jxTeamAlgorithm.apply(group.getId(), EnumPositionLocation.get(enumPosition.position).position);
            if (!result) {
                throw new MyException("算法校验失败");
            }
            location = giveLocation(group.getId(), enumPosition.type, EnumPositionLocation.get(enumPosition.position).position);
        } catch (MyException e) {
            SendHelper.sendSing(group, at.plus("该职业已满,请重新选择或联系团长调整。"));
            return;
        }
        TeamMemberDO insertDO = new TeamMemberDO();
        insertDO.setTeamId(group.getId());
        insertDO.setLocation(location);
        insertDO.setPosition(EnumPositionLocation.get(enumPosition.position).position);
        insertDO.setColor(enumPosition.color);
        insertDO.setSrc(enumPosition.src);
        insertDO.setMemberName(memberName);
        if (EnumPositionType.LAO_BAN.type.equals(enumPosition.type)) {
            String name = StringUtils.isEmpty(((Member) sender).getNameCard()) ? ((Member) sender).getNick() : ((Member) sender).getNameCard();
            insertDO.setMemberName(EnumPositionLaoBan.get(memberName).position + "老板:" + name);
        }
        insertDO.setQq(sender.getId());
        insertDO.setType(enumPosition.type);
        teamMemberDAO.insert(insertDO);
        SendHelper.sendSing(group, at.plus("报名成功。"));
        TimeUnit.SECONDS.sleep(1);
    }

    private Long giveLocation(Long group, Integer type, String position) throws MyException {
        log.info("开始分配位置{},{},{}", group, type, position);
        LambdaQueryWrapper<TeamMemberDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TeamMemberDO::getTeamId, group);
        wrapper.eq(TeamMemberDO::getDeleteYn, 0);
//        wrapper.eq(TeamMemberDO::getType,type);
        List<TeamMemberDO> teamMembers = teamMemberDAO.selectList(wrapper);
        Long location = Objects.requireNonNull(EnumPositionLocation.get(position)).location;
        // 检查位置
        AtomicReference<TeamMemberDO> teamMemberDO = new AtomicReference<>();
        teamMembers.forEach(teamMember -> {
            if (position.equals(teamMember.getPosition()) || location.equals(teamMember.getLocation())) {
                teamMemberDO.set(teamMember);
            }
        });
        List<TeamMemberDO> list = new ArrayList<>();
        if (teamMemberDO.get() == null) {
            return location;
        } else {
            switch (type) {
                case 0:
                case 4:
                    list = teamMembers.stream().filter(t -> t.getLocation() > 20L).collect(Collectors.toList());
                    break;
                case 1:
                    list = teamMembers.stream().filter(t -> t.getLocation() > 40L).collect(Collectors.toList());
                    break;
                case 3:
                    list = teamMembers.stream().filter(t -> t.getLocation() > 50L).collect(Collectors.toList());
                    break;
                case 2:
                    list = teamMembers.stream().filter(t -> t.getLocation() > 52L).collect(Collectors.toList());
                    break;
                default:
            }
        }

        return get(list, type).longValue();
    }

    private Integer get(List<TeamMemberDO> list, Integer type) throws MyException {
        int flag = 21;
        int end = 26;
        if (type == 1) {
            flag = 41;
            end = 45;
        }
        if (type == 2) {
            flag = 53;
            end = 56;
        }
        if (type == 3) {
            flag = 51;
            end = 53;
        }
        if (type == 4) {
            flag = 23;
        }
        List<Long> locations = list.stream().map(TeamMemberDO::getLocation).collect(Collectors.toList());
        System.out.println("过滤的位置" + locations);
        for (Integer i = flag; i < end; i++) {
            System.out.println(i);
            if (locations.contains(i.longValue())) {
                continue;
            }
            return i;
        }
        if (type == 2 || type == 3) {
            throw new MyException("无有效位置");
        }
        System.out.println("二队满了");
        LambdaQueryWrapper<TeamMemberDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TeamMemberDO::getTeamId, list.get(0).getTeamId());
        wrapper.eq(TeamMemberDO::getDeleteYn, 0);
        wrapper.eq(TeamMemberDO::getType, type);
        if (type == 0) {
            wrapper.between(TeamMemberDO::getLocation, 10L, 16L);
        }
        if (type == 1) {
            wrapper.between(TeamMemberDO::getLocation, 30L, 36L);
        }
        List<TeamMemberDO> teamMembers = teamMemberDAO.selectList(wrapper);
        List<Long> locations2 = teamMembers.stream().map(TeamMemberDO::getLocation).collect(Collectors.toList());
        int flag2 = 11;
        int end2 = 16;
        if (type == 1) {
            flag2 = 31;
            end2 = 35;
        }
        for (Integer i = flag2; i < end2; i++) {
            System.out.println(i);
            if (locations2.contains(i.longValue())) {
                continue;
            }
            return i;
        }
        throw new MyException("无有效位置");
    }

    public static void main(String[] args) {
        EnumPositionLocation laoban = EnumPositionLocation.get("憨批");
        System.out.println(laoban == null);
    }
}
