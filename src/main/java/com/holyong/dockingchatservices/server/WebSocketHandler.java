package com.holyong.dockingchatservices.server;


import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson2.JSON;
import com.holyong.dockingchatservices.infrastructure.im.Command;
import com.holyong.dockingchatservices.infrastructure.im.CommandType;
import com.holyong.dockingchatservices.infrastructure.im.Result;
import com.holyong.dockingchatservices.server.handler.ChatTypeHandler;
import com.holyong.dockingchatservices.server.handler.ConnectionHandler;
import com.holyong.dockingchatservices.server.handler.JoinGroupHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义处理类
 * TextWebSocketFrame: websocket数据是帧的形式处理
 */
@Component
@ChannelHandler.Sharable //设置通道共享
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    /**
     * 使用数据库实现  暂时使用map替代
     */
    public static final Map<String, Channel> userChannel = new ConcurrentHashMap<>();
    /**
     * 使用数据库实现群聊
     */
    public static final ChannelGroup GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static List<Channel> channelList = new ArrayList<>();

    /**
     * 通道就绪事件
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {


//        super.channelActive(ctx);

        //当有新的客户端连接的时候, 将通道放入集合
        Channel channel = ctx.channel();
        channelList.add(channel);
        System.out.println("[Server]:" + channel.remoteAddress().toString().substring(1) + "在线.");
    }

    /**
     * 通道未就绪--channel下线
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        super.channelInactive(ctx);
        Channel channel = ctx.channel();
        //当有客户端断开连接的时候,就移除对应的通道
        channelList.remove(channel);
        System.out.println("[Server]:" + channel.remoteAddress().toString().substring(1) + "下线.");
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        try {
            Command command = JSON.parseObject(textWebSocketFrame.text(), Command.class);
            switch (CommandType.match(command.getCode())) {
                case CONNECTION -> ConnectionHandler.execute(ctx, command, userChannel);
                case CHAT -> ChatTypeHandler.execute(ctx, textWebSocketFrame, userChannel, GROUP);
                case JOIN_GROUP -> JoinGroupHandler.execute(ctx, GROUP);
                default -> ctx.channel().writeAndFlush(Result.fail("不支持的CODE"));
            }
        } catch (Exception e) {
            ctx.channel().writeAndFlush(Result.fail(e.getMessage()));
            throw new RuntimeException(e);
        }


//        String msg = textWebSocketFrame.text();
//        System.out.println("msg:" + msg);
//        //当前发送消息的通道, 当前发送的客户端连接
//        Channel channel = channelHandlerContext.channel();
//        for (Channel channel1 : channelList) {
//            //排除自身通道
//            if (channel != channel1) {
//                channel1.writeAndFlush(new TextWebSocketFrame(msg));
//            }
//        }
    }


    /**
     * 异常事件处理
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);

        cause.printStackTrace();
        Channel channel = ctx.channel();
        //移除集合
        channelList.remove(channel);
        System.out.println("[Server]:" + channel.remoteAddress().toString().substring(1) + "异常.");

    }


}
