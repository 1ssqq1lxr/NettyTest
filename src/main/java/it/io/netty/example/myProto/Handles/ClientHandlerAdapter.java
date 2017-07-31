package it.io.netty.example.myProto.Handles;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import it.io.netty.example.myProto.Client;
import it.io.netty.example.myProto.Proto.Entity;
import it.io.netty.example.myProto.Proto.Header;
import it.io.netty.example.myProto.Proto.Person;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandlerAdapter extends SimpleChannelInboundHandler<Entity<Serializable>> {
  	
	private  Client client1;
	public ClientHandlerAdapter(Client client1) {
		super();
		this.client1 = client1;
	}
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Entity<Serializable> msg)
			throws Exception {
		// TODO Auto-generated method stub
		logger.info("success accept server, port is:{}", msg.toString());
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		Entity<Serializable> entity = new Entity<Serializable>();
		Header header = new Header();
		Person p = new Person();
		p.setAge(22);
		p.setName("lisi");
		header.setBusinessCode("2222");
		entity.setHeader(header);
		entity.setBody( p);
		ctx.channel().writeAndFlush(entity);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		client1.connect();
		super.channelInactive(ctx);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		// TODO Auto-generated method stub
		sendHeat(ctx);
		ctx.fireUserEventTriggered(evt);
	}
	@Override
	public void exceptionCaught(
			ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		ctx.close();
	}
	private void sendHeat(ChannelHandlerContext ctx){
		 Entity<Serializable>  entity= new  Entity<Serializable> ();
		 Header header = new Header();
		 header.setSerialNo(2);
		 entity.setHeader(header);
		 ctx.writeAndFlush(entity);
	}
	
	
}
