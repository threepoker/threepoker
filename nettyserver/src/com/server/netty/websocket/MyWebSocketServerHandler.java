package com.server.netty.websocket;

import com.server.game.manager.UserManager;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.json.JSONException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
public class MyWebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
	private static final Logger logger = Logger.getLogger(WebSocketServerHandshaker.class.getName());
	private WebSocketServerHandshaker handshaker;
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 添加
		Global.group.add(ctx.channel());
		System.out.println("客户端与服务端连接开启");
		
	}
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// 移除
		Global.group.remove(ctx.channel());
		UserManager.getInstance().remove(ctx.channel());
		System.out.println("客户端与服务端连接关闭");
	}
	
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	private void handlerWebSocketFrame(ChannelHandlerContext ctx,
			WebSocketFrame frame) throws SQLException, JSONException {
		// 判断是否关闭链路的指令
		if (frame instanceof CloseWebSocketFrame) {
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame
					.retain());
		}
		// 判断是否ping消息
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(
					new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		// 本例程仅支持文本消息，不支持二进制消息
		if (!(frame instanceof TextWebSocketFrame)) {
			System.out.println("本例程仅支持文本消息，不支持二进制消息");
			throw new UnsupportedOperationException(String.format(
					"%s frame types not supported", frame.getClass().getName()));
		}
		String request = ((TextWebSocketFrame) frame).text();
		System.out.println("服务端收到：" + request);
		try {
			MsgManager.getInstance().getMsg(request,ctx.channel());

		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void handleHttpRequest(ChannelHandlerContext ctx,
			FullHttpRequest req) {
		if (!req.getDecoderResult().isSuccess()
				|| (!"websocket".equals(req.headers().get("Upgrade")))) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
					HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			return;
		}
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
				"ws://localhost:7397/websocket", null, false);
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory
					.sendUnsupportedWebSocketVersionResponse(ctx.channel());
		} else {
			handshaker.handshake(ctx.channel(), req);
		}
	}
	private static void sendHttpResponse(ChannelHandlerContext ctx,
			FullHttpRequest req, DefaultFullHttpResponse res) {
		// 返回应答给客户端
		if (res.getStatus().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(),
					CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
		}
		// 如果是非Keep-Alive，关闭连接
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		if (!isKeepAlive(req) || res.getStatus().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}
	private static boolean isKeepAlive(FullHttpRequest req) {
		return false;
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		// TODO Auto-generated method stub
		if (msg instanceof FullHttpRequest) {
			handleHttpRequest(ctx, ((FullHttpRequest) msg));
		} else if (msg instanceof WebSocketFrame) {
			handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}
	
	
//		public void messageReceived(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        fullHttpRequest = (DefaultFullHttpRequest) msg;
//  
//        if (HttpDemoServer.isSSL) {
//            System.out.println("Your session is protected by " +
//                    ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite() +
//                    " cipher suite.\n");
//        }
//        /**
//         * 在服务器端打印请求信息
//         */
//        System.out.println("VERSION: " + fullHttpRequest.getProtocolVersion().text() + "\r\n");
//        System.out.println("REQUEST_URI: " + fullHttpRequest.getUri() + "\r\n\r\n");
//        System.out.println("\r\n\r\n");
//        for (Entry<String, String> entry : fullHttpRequest.headers()) {
//            System.out.println("HEADER: " + entry.getKey() + '=' + entry.getValue() + "\r\n");
//        }
//  
//        /**
//         * 服务器端返回信息
//         */
//        responseContent.setLength(0);
//        responseContent.append("WELCOME TO THE WILD WILD WEB SERVER\r\n");
//        responseContent.append("===================================\r\n");
//  
//        responseContent.append("VERSION: " + fullHttpRequest.getProtocolVersion().text() + "\r\n");
//        responseContent.append("REQUEST_URI: " + fullHttpRequest.getUri() + "\r\n\r\n");
//        responseContent.append("\r\n\r\n");
//        for (Entry<String, String> entry : fullHttpRequest.headers()) {
//            responseContent.append("HEADER: " + entry.getKey() + '=' + entry.getValue() + "\r\n");
//        }
//        responseContent.append("\r\n\r\n");
//        Set<Cookie> cookies;
//        String value = fullHttpRequest.headers().get(COOKIE);
//        if (value == null) {
//            cookies = Collections.emptySet();
//        } else {
//            cookies = CookieDecoder.decode(value);
//        }
//        for (Cookie cookie : cookies) {
//            responseContent.append("COOKIE: " + cookie.toString() + "\r\n");
//        }
//        responseContent.append("\r\n\r\n");
//  
//        if (fullHttpRequest.getMethod().equals(HttpMethod.GET)) {
//            //get请求
//            QueryStringDecoder decoderQuery = new QueryStringDecoder(fullHttpRequest.getUri());
//            Map<String, List<String>> uriAttributes = decoderQuery.parameters();
//            for (Entry<String, List<String>> attr : uriAttributes.entrySet()) {
//                for (String attrVal : attr.getValue()) {
//                    responseContent.append("URI: " + attr.getKey() + '=' + attrVal + "\r\n");
//                }
//            }
//            responseContent.append("\r\n\r\n");
//  
//            responseContent.append("\r\n\r\nEND OF GET CONTENT\r\n");
//            writeResponse(ctx.channel());
//            return;
//        } else if (fullHttpRequest.getMethod().equals(HttpMethod.POST)) {
//            //post请求
//            decoder = new HttpPostRequestDecoder(factory, fullHttpRequest);
//            readingChunks = HttpHeaders.isTransferEncodingChunked(fullHttpRequest);
//            responseContent.append("Is Chunked: " + readingChunks + "\r\n");
//            responseContent.append("IsMultipart: " + decoder.isMultipart() + "\r\n");
//  
//            try {
//                while (decoder.hasNext()) {
//                    InterfaceHttpData data = decoder.next();
//                    if (data != null) {
//                        try {
//                            writeHttpData(data);
//                        } finally {
//                            data.release();
//                        }
//                    }
//                }
//            } catch (EndOfDataDecoderException e1) {
//                responseContent.append("\r\n\r\nEND OF POST CONTENT\r\n\r\n");
//            }
//            writeResponse(ctx.channel());
//            return;
//        } else {
//            System.out.println("discard.......");
//            return;
//        }
//    }
} 