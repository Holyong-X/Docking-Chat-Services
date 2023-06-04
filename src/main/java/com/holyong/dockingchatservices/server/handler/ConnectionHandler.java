package com.holyong.dockingchatservices.server.handler;

import com.alibaba.fastjson.JSON;
import com.holyong.dockingchatservices.infrastructure.im.Command;
import com.holyong.dockingchatservices.infrastructure.im.Result;
import com.holyong.dockingchatservices.server.NettyWebSocketServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class ConnectionHandler {


    /**
     * 保存映射关系
     */
    public static void execute(ChannelHandlerContext ctx, Command command, Map<String, Channel> userChannel) {

        //判断用户是否已经在线
        if (userChannel.containsKey(command.getNickName())) {
            ctx.channel().writeAndFlush(Result.fail("用户已经在线"));
        }

        userChannel.put(command.getNickName(), ctx.channel());
        ctx.channel().writeAndFlush(Result.success("与服务器建立连接成功"));
        ctx.channel().writeAndFlush(Result.success(JSON.toJSONString(userChannel.keySet())));
    }


}
