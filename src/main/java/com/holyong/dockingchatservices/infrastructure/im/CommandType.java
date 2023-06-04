package com.holyong.dockingchatservices.infrastructure.im;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandType {

    //TODO
    //如果枚举类型过多，后面使用hashmap去存储


    /**
     * 建立连接
     */
    CONNECTION(101),

    /**
     * 私聊
     */
    CHAT(102),

    /**
     * 加如群聊
     */
    JOIN_GROUP(103),

    /**
     * 不支持类型
     */
    ERROR(-1);

    private Integer code;


    public static CommandType match(Integer code) {
        for (CommandType value : CommandType.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }

        return ERROR;
    }
}
