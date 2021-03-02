package com.rabbit.robot.DO;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 邢晨旭
 * @date 2021/02/26
 */
@Data
@TableName("group_message")
public class GroupMessage {

    @TableId
    private Long Id;
    /**
     * 群号
     */
    private Long groupId;
    /**
     * 群名片
     */
    private String nameCard;
    /**
     * 群头衔
     */
    private String specialTitle;
    /**
     * qq号
     */
    private Long senderNum;
    /**
     * qq昵称
     */
    private String nickName;
    /**
     * 消息发送时间
     */
    private Integer messageTime;
    /**
     * 消息内容
     */
    private String messageInfo;
}
