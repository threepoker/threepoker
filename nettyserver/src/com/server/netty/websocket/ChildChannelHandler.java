package com.server.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
public class ChildChannelHandler extends ChannelInitializer<SocketChannel>{
	@Override
	protected void initChannel(SocketChannel e)  {				
		e.pipeline().addLast("http-codec",new HttpServerCodec());
		e.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));
		e.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
		

		e.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));
		
		e.pipeline().addLast("handler",new MyWebSocketServerHandler());
	}
} 
