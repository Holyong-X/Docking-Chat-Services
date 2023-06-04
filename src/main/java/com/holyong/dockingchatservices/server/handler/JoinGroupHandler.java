package com.holyong.dockingchatservices.server.handler;

import com.holyong.dockingchatservices.infrastructure.im.Command;
import com.holyong.dockingchatservices.infrastructure.im.Result;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Map;

public class JoinGroupHandler {

    public static void execute(ChannelHandlerContext ctx, ChannelGroup group) {
        group.add(ctx.channel());
        ctx.channel().writeAndFlush(Result.success("加如系统群聊成功"));
    }
}
