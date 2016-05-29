package com.server.netty.websocket;


import org.json.JSONException;
import org.json.JSONObject;

import com.server.Utils.XFException;
import com.server.Utils.XFLog;
import com.server.game.manager.BaseConfigManager;
import com.server.game.proto.ProtoDesk;
import com.server.game.proto.ProtoLogin;

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
			//load observer
			ProtoLogin.getInstance();
			ProtoDesk.getInstance();
			//load base config
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
			XFException.logException(e);
		}finally{
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
		
	}
}
