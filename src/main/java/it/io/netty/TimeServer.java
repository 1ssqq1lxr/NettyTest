package it.io.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;
/**
 * 服务端
 * @author 17070680
 *
 */
public class TimeServer {
	int count = 0;
	
	// 绑定端口（未考虑粘包/拆包）
	public void bind(int port) throws Exception{
			EventLoopGroup bossGroup = new NioEventLoopGroup();
			EventLoopGroup workerGroup = new NioEventLoopGroup();
			try {
				ServerBootstrap bootstrap = new ServerBootstrap();
				bootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						// TODO Auto-generated method stub
						ch.pipeline().addLast(new ChannelHandlerAdapter(){
							// 处理异常
							@Override
							public void exceptionCaught(
									ChannelHandlerContext ctx, Throwable cause)
									throws Exception {
								// TODO Auto-generated method stub
								ctx.close();
							}
							// 开始读取
							@Override
							public void channelRead(ChannelHandlerContext ctx,
									Object msg) throws Exception {
								InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
								String str = remoteAddress.getHostName()+":"+remoteAddress.getPort();
								// TODO Auto-generated method stub
								ByteBuf buf = (ByteBuf)msg;
								int readableBytes = buf.readableBytes();
								byte[] bytes = new byte[readableBytes];
								buf.readBytes(bytes);
								String al = new String(bytes, "utf-8");
								System.out.println("server  accept  "+str+" msg :  " +al);
								
								String msg1 ="你不是我的人是谁的人";
								ByteBuf byteBuf = Unpooled.buffer(msg1.getBytes().length);
								byteBuf.writeBytes(msg1.getBytes());
								ctx.writeAndFlush(byteBuf);
							}
							// 读取完成
							@Override
							public void channelReadComplete(
									ChannelHandlerContext ctx) throws Exception {
								// TODO Auto-generated method stub
								ctx.flush();
							}
									
						});
					}
				});
				//绑定端口等待同步成功
				ChannelFuture sync = bootstrap.bind(port).sync();
				//等待服务端监听端口关闭
				sync.channel().closeFuture().sync();
			} catch (Exception e) {
				// TODO: handle exception
				bossGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
			}
	}
	
	// 绑定端口（考虑粘包/拆包）
	public void bindPackage(int port) throws Exception{
			EventLoopGroup bossGroup = new NioEventLoopGroup();
			EventLoopGroup workerGroup = new NioEventLoopGroup();
			try {
				ServerBootstrap bootstrap = new ServerBootstrap();
				bootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						// 解决粘包 拆包问题
						ch.pipeline().addLast(new LineBasedFrameDecoder(4048));
						ch.pipeline().addLast(new StringDecoder());
//						ByteBuf buf = Unpooled.copiedBuffer("你".getBytes());
//
//						ch.pipeline().addLast(new DelimiterBasedFrameDecoder(4048,buf));
			
		
						// TODO Auto-generated method stub
						ch.pipeline().addLast(new ChannelHandlerAdapter(){
							// 处理异常
							@Override
							public void exceptionCaught(
									ChannelHandlerContext ctx, Throwable cause)
									throws Exception {
								// TODO Auto-generated method stub
								ctx.close();
							}
							// 开始读取
							@Override
							public void channelRead(ChannelHandlerContext ctx,
									Object msg) throws Exception {
								InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
								String str = remoteAddress.getHostName()+":"+remoteAddress.getPort();
								// TODO Auto-generated method stub
								String buf = (String)msg;
						
								System.out.println("server  accept  "+str+" msg :  " +buf+(++count));
								
								String msg1 ="你不是我的人是谁的人"+ System.getProperty("line.separator");
								ByteBuf byteBuf = Unpooled.copiedBuffer(msg1.getBytes());
						        ctx.write(byteBuf);  
							}
//							 读取完成
							@Override
							public void channelReadComplete(
									ChannelHandlerContext ctx) throws Exception {
								// TODO Auto-generated method stub
								ctx.flush();
							}
									
						});
					}
				});
				//绑定端口等待同步成功
				ChannelFuture sync = bootstrap.bind(port).sync();
				//等待服务端监听端口关闭
				sync.channel().closeFuture().sync();
			} catch (Exception e) {
				// TODO: handle exception
				bossGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
			}
	}
}
