package com.server.netty.websocket;

import com.server.game.baseConfig.BaseConfigManager;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class NettyServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new NettyServer().run();
	}
	public void run(){
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		
		try {
			BaseConfigManager.getInstance().loadAllConfig();
			
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup);
			b.channel(NioServerSocketChannel.class);
			//b.childOption(ChannelOption.SO_KEEPALIVE, true);
			b.childHandler(new ChildChannelHandler());
			System.out.println("服务端开启等待客户端连接 ... ...");
			
			Channel ch = b.bind(8080).sync().channel();
			
			ch.closeFuture().sync();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
		
	}
}
