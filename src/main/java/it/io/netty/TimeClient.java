package it.io.netty;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
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
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
/**
 * 客户端
 * @author 17070680
 *
 */
public class TimeClient {
	
		//	（未考虑粘包/拆包）
		public void connect(int port,String ip) {
			// TODO Auto-generated method stub
			EventLoopGroup bossGroup = new NioEventLoopGroup();
			try {
				Bootstrap bootstrap = new Bootstrap();
				bootstrap.group(bossGroup)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
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
								// TODO Auto-generated method stub
								InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
								String str = remoteAddress.getHostName()+":"+remoteAddress.getPort();
								// TODO Auto-generated method stub
								ByteBuf buf = (ByteBuf)msg;
								int readableBytes = buf.readableBytes();
								byte[] bytes = new byte[readableBytes];
								buf.readBytes(bytes);
								String al = new String(bytes, "utf-8");
								System.out.println("client  accept  "+str+" msg :  " +al);
							}
							// 连接成功后写入数据
							@Override
							public void channelActive(ChannelHandlerContext ctx)
									throws Exception {
								// TODO Auto-generated method stub
								String msg ="我是你的人哦";
								ByteBuf byteBuf = Unpooled.buffer(msg.getBytes().length);
								byteBuf.writeBytes(msg.getBytes());
								ctx.writeAndFlush(byteBuf);
							}
									
						});
					}
				});
				//绑定端口等待同步成功
				ChannelFuture sync = bootstrap.connect(ip, port).sync();
				//等待服务端监听端口关闭
				sync.channel().closeFuture().sync();
			} catch (Exception e) {
				// TODO: handle exception
				bossGroup.shutdownGracefully();
			}
		}
		
		
		//	（考虑粘包/拆包）
		public void connectPackage(int port,String ip) {
			// TODO Auto-generated method stub
			EventLoopGroup bossGroup = new NioEventLoopGroup();
			try {
				Bootstrap bootstrap = new Bootstrap();
				bootstrap.group(bossGroup)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						// TODO Auto-generated method stub
						// 解决粘包 拆包问题
				
//						ByteBuf buf = Unpooled.buffer("你".getBytes().length);
//						buf.writeBytes("你".getBytes());
//						ch.pipeline().addLast(new DelimiterBasedFrameDecoder(4048,buf));
						ch.pipeline().addLast(new LineBasedFrameDecoder(4048));
						ch.pipeline().addLast(new StringDecoder());
		
					
			
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
								// TODO Auto-generated method stub
						
								InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
								String str = remoteAddress.getHostName()+":"+remoteAddress.getPort();
								// TODO Auto-generated method stub
								String buf = (String)msg;
								int count =0;
								System.out.println("client  accept  "+str+" msg :  " +buf+(++count));
							}
							// 连接成功后写入数据
							@Override
							public void channelActive(ChannelHandlerContext ctx)
									throws Exception {
								// TODO Auto-generated method stub
								String msg ="我是你的人哦"+ System.getProperty("line.separator");
								ByteBuf byteBuf  =null;;
								for(int i=0;i<100;i++){
									byteBuf  =Unpooled.buffer(msg.getBytes().length);
									byteBuf.writeBytes(msg.getBytes());
									ctx.writeAndFlush(byteBuf);
//									Thread.sleep(1000);
								}
							}
									
						});
						ch.pipeline().addLast(new StringDecoder());
					}
					
				});
				//绑定端口等待同步成功
				ChannelFuture sync = bootstrap.connect(ip, port).sync();
				//等待服务端监听端口关闭
				sync.channel().closeFuture().sync();
			} catch (Exception e) {
				// TODO: handle exception
				bossGroup.shutdownGracefully();
			}
		}
}
