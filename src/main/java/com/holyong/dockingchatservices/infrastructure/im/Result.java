package com.holyong.dockingchatservices.infrastructure.im;

import com.alibaba.fastjson.JSON;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private String name;

    private LocalDateTime time;

    private String message;

    public static TextWebSocketFrame fail(String message) {
        return new TextWebSocketFrame(JSON.toJSONString(new Result("系统消息", LocalDateTime.now(), message)));
    }

    public static TextWebSocketFrame success(String message) {
        return new TextWebSocketFrame(JSON.toJSONString(new Result("系统消息", LocalDateTime.now(), message)));
    }

    public static TextWebSocketFrame success(String name, String message) {
        return new TextWebSocketFrame(JSON.toJSONString(new Result(name, LocalDateTime.now(), message)));
    }
}
