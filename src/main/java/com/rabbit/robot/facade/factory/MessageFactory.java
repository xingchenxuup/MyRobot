package com.rabbit.robot.facade.factory;

import com.rabbit.robot.enums.EnumKeyWord;
import com.rabbit.robot.facade.factory.message.MessageFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 邢晨旭
 * @date 2020/7/22 下午1:03
 */
@Slf4j
@Component
public class MessageFactory {

    @Autowired
    private List<MessageFacade> messageFacades;

    public MessageFacade get(EnumKeyWord keyWord) {

        for (MessageFacade messageFacade : messageFacades) {
            if (messageFacade.get().equals(keyWord)) {
                log.info("MessageFactory->{}",messageFacade.getClass().getName());
                return messageFacade;
            }
        }
        return null;
    }


}
