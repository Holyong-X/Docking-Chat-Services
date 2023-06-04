package com.holyong.dockingchatservices.server.handler;

import com.alibaba.fastjson.JSON;
import com.holyong.dockingchatservices.infrastructure.im.Command;
import com.holyong.dockingchatservices.infrastructure.im.Result;
import com.holyong.dockingchatservices.infrastructure.pojo.ChatMessage;
import com.holyong.dockingchatservices.infrastructure.pojo.MessageType;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.internal.StringUtil;

import java.util.Map;

public class ChatTypeHandler {

    public static void execute(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame, Map<String, Channel> userChannel, ChannelGroup group) {
        try {
            ChatMessage chatMessage = JSON.parseObject(textWebSocketFrame.text(), ChatMessage.class);
            switch (MessageType.match(chatMessage.getType())) {

                //支持私聊消息
                case PRIVATE -> {
                    if (StringUtil.isNullOrEmpty(chatMessage.getTarget())) {
                        ctx.channel().writeAndFlush(Result.fail("消息发送失败，发送消息前请指定发送对象"));
                        return;
                    }
                    //从数据库获取用户发送对象
                    Channel channel = userChannel.get(chatMessage.getTarget());
                    if (channel == null || !channel.isActive()) {
                        ctx.channel().writeAndFlush(Result.fail("消息发送失败" + chatMessage.getTarget() + "不在线"));
                    } else {
                        //TODO 使用chatgpt去回复消息

                        ctx.channel().writeAndFlush(Result.success("私聊消息(" + chatMessage.getNickName() + ")", chatMessage.getContent()));
                    }
                }
                //支持群聊消息   会发送给已经注册进来的所有用户
                case GROUP ->
                        group.writeAndFlush(Result.success("群消息(" + chatMessage.getNickName() + ")", chatMessage.getContent()));

                default -> ctx.channel().writeAndFlush(Result.fail("不支持的消息类型"));
            }
        } catch (Exception e) {
            ctx.channel().writeAndFlush(Result.fail("消息发送格式错误"));
//            throw new RuntimeException(e);
        }
    }

}
