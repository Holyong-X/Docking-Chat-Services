package com.holyong.dockingchatservices.infrastructure.pojo;


import com.holyong.dockingchatservices.infrastructure.im.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage extends Command {

    /**
     * 聊天类型
     */
    private Integer type;

    /**
     * 聊天对象
     */
    private String target;

    /**
     * 内容
     */
    private String content;


}
