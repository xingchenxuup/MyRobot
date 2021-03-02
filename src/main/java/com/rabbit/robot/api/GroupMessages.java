package com.rabbit.robot.api;

import com.rabbit.robot.DO.GroupMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 邢晨旭
 * @date 2021/03/01
 */
@RestController("api")
public class GroupMessages {

    @PostMapping("GHistory")
    public List<GroupMessage> getMessageHistory(){

        return null;
    }

}
