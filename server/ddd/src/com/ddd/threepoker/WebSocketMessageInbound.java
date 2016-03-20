package com.ddd.threepoker;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

import com.ddd.threepoker.turncard.TurnCard;
import com.ddd.threepoker.utils.CardUtils;

public class WebSocketMessageInbound extends MessageInbound {

	//当前连接的用户名称
	private final String user;

	public WebSocketMessageInbound(String user) {
		this.user = user;
	}

	public String getUser() {
		return this.user;
	}

	//建立连接的触发的事件
	@Override
	protected void onOpen(WsOutbound outbound) {
		
		CardUtils cardUtils = new CardUtils();
		cardUtils.reSetcards_();
		ArrayList<Integer> cardsArrayList = null;
		System.out.println();
		for (int j = 0; j < 1; j++) {
			if (null != cardsArrayList) {
				cardsArrayList.clear();
			}
			cardsArrayList = cardUtils.getRandomCards();
			cardUtils.sortCardsSmallBig(cardsArrayList);
			for (int i = 0; i < cardsArrayList.size(); i++) {
				System.out.print(cardsArrayList.get(i)+"  ");
			}
			System.out.println();
		}
		
		
//		for (int k = 0; k < 999; k++) {
//			cardUtils.reSetcards_();
//			int num = 0;
//			for (int j = 0; j < 20; j++) {
//				if (null != cardsArrayList) {
//					cardsArrayList.clear();
//				}
//				cardsArrayList = cardUtils.getSZCards();
//				if (cardsArrayList.size() == 3) {
//					num++;
//				}
//			}
//			System.out.println("num = "+num);
//		}
		
		
		System.out.println("user : onOpen" );
		// 触发连接事件，在连接池中添加连接
		JSONObject result = new JSONObject();
		result.put("type", "user_join");
		result.put("user", this.user);
		for (int i = 0; i < cardsArrayList.size(); i++) {
			result.put("card"+i,cardsArrayList.get(i).toString());
		}
		
		//向所有在线用户推送当前用户上线的消息
		WebSocketMessageInboundPool.sendMessage(result.toString());
		
		
		result.element("type", "get_online_user");
		result.element("list", WebSocketMessageInboundPool.getOnlineUser());
		//向连接池添加当前的连接对象
		WebSocketMessageInboundPool.addMessageInbound(this);
		//向当前连接发送当前在线用户的列表
		WebSocketMessageInboundPool.sendMessageToUser(this.user, result.toString());
	}

	@Override
	protected void onClose(int status) {
		System.out.println("user : onClose user = "+this.user );
		// 触发关闭事件，在连接池中移除连接
		WebSocketMessageInboundPool.removeMessageInbound(this);
		JSONObject result = new JSONObject();
		result.element("type", "user_leave");
		result.element("user", this.user);
		//向在线用户发送当前用户退出的消息
		WebSocketMessageInboundPool.sendMessage(result.toString());
	}

	@Override
	protected void onBinaryMessage(ByteBuffer message) throws IOException {
		throw new UnsupportedOperationException("Binary message not supported.");
	}

	//客户端发送消息到服务器时触发事件
	@Override
	protected void onTextMessage(CharBuffer message) throws IOException {
		//向所有在线用户发送消息
		WebSocketMessageInboundPool.onMessage(message.toString());
	}
}
