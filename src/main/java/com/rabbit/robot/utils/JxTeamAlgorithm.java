package com.rabbit.robot.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rabbit.robot.DO.TeamMemberDO;
import com.rabbit.robot.enums.EnumPosition;
import com.rabbit.robot.enums.EnumPositionType;
import com.rabbit.robot.mapper.TeamMemberDAO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 邢晨旭
 * @date 2020/11/16
 */
@Component
@Slf4j
public class JxTeamAlgorithm {

    @Autowired
    TeamMemberDAO dao;

    @Autowired
    TeamMemberDAO teamMemberDAO;
    public static Map<Long, List<TeamMemberDO>> map = new HashMap<>();

    public Boolean apply(Long group, String position) {
        mapInit(group);
        EnumPosition enumPosition = EnumPosition.get(position);
        assert enumPosition != null;
        EnumPositionType enumPositionType = EnumPositionType.get(enumPosition.type);
        Integer num1 = getNum(group, enumPosition.type, null);
        Integer num2 = getNum(group, enumPosition.type, position);
        Integer num3 = getNum(group, null, null);
        Integer numForType = EnumPositionType.getNum(enumPositionType);
        Integer numForPosition = EnumPosition.getNum(enumPosition);
        log.info("算法校验{},type数量->{},心法数量->{},团队总数->{}",group,num1,num2,num3);
        log.info("算法校验{},type上线->{},心法上限->{}",group,numForType,numForPosition);
        if (num3 >= 25) {
            return false;
        }
        if (num1 >= numForType || num2 >= numForPosition) {
            return false;
        }
        if (enumPosition.type.equals(0) && w(group, position) && num1 >= 8) {
            return false;
        }
        if (enumPosition.type.equals(2) && n(group, position) && num1 >= 3) {
            return false;
        }
        //
        return true;
    }


    private void mapInit(Long group) {
        if (CollectionUtils.isEmpty(map.get(group))) {
            LambdaQueryWrapper<TeamMemberDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(TeamMemberDO::getDeleteYn, 0);
            wrapper.eq(TeamMemberDO::getTeamId, group);
            wrapper.orderByAsc(TeamMemberDO::getType);
            map.put(group, dao.selectList(wrapper));
        }
    }

    private void mapRefresh(Long group) {
        LambdaQueryWrapper<TeamMemberDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TeamMemberDO::getDeleteYn, 0);
        wrapper.eq(TeamMemberDO::getTeamId, group);
        map.put(group, dao.selectList(wrapper));
    }


    private Integer getNum(Long group, Integer type, String position) {

        LambdaQueryWrapper<TeamMemberDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TeamMemberDO::getTeamId, group);
        wrapper.eq(TeamMemberDO::getDeleteYn, 0);
        if (type == null) {
            return teamMemberDAO.selectCount(wrapper);
        }
        if (StringUtils.isEmpty(position)) {
            wrapper.eq(TeamMemberDO::getType, type);
        } else {
            wrapper.eq(TeamMemberDO::getPosition, position);
        }
        return teamMemberDAO.selectCount(wrapper);
    }

    private Boolean w(Long group, String position) {
        if ("傲血 傲雪 丐帮 剑纯 鲸鱼".contains(position)) {
            return false;
        }
        LambdaQueryWrapper<TeamMemberDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TeamMemberDO::getTeamId, group);
        wrapper.eq(TeamMemberDO::getDeleteYn, 0);
        wrapper.eq(TeamMemberDO::getType, 0);
        wrapper.in(TeamMemberDO::getPosition, "傲血", "傲雪", "藏剑", "丐帮", "剑纯","鲸鱼","惊羽");
        return teamMemberDAO.selectCount(wrapper) < 2;
    }

    private Boolean n(Long group, String position) {
        if ("奶歌 歌奶 相知".contains(position)) {
            return false;
        }
        LambdaQueryWrapper<TeamMemberDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TeamMemberDO::getTeamId, group);
        wrapper.eq(TeamMemberDO::getDeleteYn, 0);
        wrapper.eq(TeamMemberDO::getType, 2);
        wrapper.in(TeamMemberDO::getPosition, "奶歌", "相知");
        return teamMemberDAO.selectCount(wrapper) == 0;
    }

}
