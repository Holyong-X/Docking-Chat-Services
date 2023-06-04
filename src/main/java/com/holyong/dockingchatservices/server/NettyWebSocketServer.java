package com.holyong.dockingchatservices.server;


import com.holyong.dockingchatservices.config.NettyConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@Component
public class NettyWebSocketServer implements Runnable {

    public final Map<String, Channel> userChannel = new ConcurrentHashMap<>();
    @Autowired
    NettyConfig nettyConfig;
    @Autowired
    private WebSocketChannelInit webSocketChannelInit;
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    /**
     * 资源关闭--在容器销毁是关闭
     */
    @PreDestroy
    public void close() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Override
    public void run() {

        try {
            //1.创建服务端启动助手
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //2.设置线程组
            serverBootstrap.group(bossGroup, workerGroup);
            //3.设置参数
            serverBootstrap
                    .channel(NioServerSocketChannel.class)
                    .localAddress(nettyConfig.getPort())
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(webSocketChannelInit);

            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}
