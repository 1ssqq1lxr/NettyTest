package it.io.netty.example.myProto.Handles;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import it.io.netty.example.myProto.Proto.Entity;
import it.io.netty.example.myProto.Proto.Header;
import it.io.netty.example.myProto.Proto.Person;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHandlerAdapter  extends SimpleChannelInboundHandler<Entity<Serializable>> {
  	
	private static List<Channel> channels = new ArrayList<Channel>();
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private int num ;
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Entity<Serializable> msg)
			throws Exception {
		
		// TODO Auto-generated method stub
		if(msg.getHeader().getSerialNo()!=2){
			logger.info("success accept client, port is:{}", msg.toString());	
			Entity<Serializable> entity = new Entity<Serializable>();
			Header header = new Header();
			Person p = new Person();
			p.setAge(11);
			p.setName("zhangsan");
			header.setBusinessCode("11111");
			entity.setHeader(header);
	 		entity.setBody( p);
			ctx.channel().writeAndFlush(entity);
		}
		else{
			logger.info("=========收到心跳检测==========={}", msg.toString());	
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("===========================连接成功");
//		ctx.close();
		super.channelActive(ctx);
	}


	@Override
	public void exceptionCaught(
			ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
	}
	
}
