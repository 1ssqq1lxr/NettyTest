package it.io.netty.example.myProto.heart;

import io.netty.channel.ChannelHandlerContext;
import it.io.netty.example.myProto.Proto.Entity;
import it.io.netty.example.myProto.Proto.Header;

import java.io.Serializable;

/**
 * 发送 ping pong 消息类
 * @author 17070680
 *
 */
public class SendPingPong {
	/**
	 * 发送ping pong
	 */
	public static void sendPing(ChannelHandlerContext ctx) {
		// TODO Auto-generated method stub
		Entity<Serializable> entity = new Entity<Serializable>();
		Header header = new Header();
		header.setBusinessCode("2");
		ctx.writeAndFlush(entity);
	}

}
