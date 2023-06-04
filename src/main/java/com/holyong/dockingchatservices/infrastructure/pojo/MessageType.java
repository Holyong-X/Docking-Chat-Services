package com.holyong.dockingchatservices.infrastructure.pojo;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {

    /**
     * 私聊
     */
    PRIVATE(1),

    /**
     * 群聊
     */
    GROUP(2),

    /**
     * 不支持类型
     */
    ERROR(-1);

    private Integer type;


    public static MessageType match(Integer type) {

        for (MessageType value : MessageType.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }

        return ERROR;
    }
}
