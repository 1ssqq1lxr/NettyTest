package websocket;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;

public class WebSocket 
{
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
						ch.pipeline().addLast("http-codec",new HttpServerCodec());		
						ch.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));
						ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
						// TODO Auto-generated method stub
						ch.pipeline().addLast(new SimpleChannelInboundHandler<Object>() {

							@Override
							public void channelReadComplete(
									ChannelHandlerContext ctx) throws Exception {
								// TODO Auto-generated method stub
								ctx.flush();
							}

							@Override
							protected void messageReceived(
									ChannelHandlerContext ctx, Object msg)
									throws Exception {
								// TODO Auto-generated method stub
								
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
