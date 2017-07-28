package it.io.netty.example.myProto;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import it.io.netty.example.myProto.Handles.ClientHandlerAdapter;
import it.io.netty.example.myProto.Handles.ServerHandlerAdapter;
import it.io.netty.example.myProto.code.MyDecode;
import it.io.netty.example.myProto.code.MyEncode;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 服务端代码
 * @author 17070680
 *
 */
public class Server  extends AbstractBaseServer {

	private ScheduledExecutorService executorService;
	public Server() {
		this.init();
        executorService = Executors.newScheduledThreadPool(2);
    }

    public Server(int port) {
        this.port = port;
        executorService = Executors.newScheduledThreadPool(2);
    }

    public void start() {
        b.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                    	ch.pipeline().addLast(new MyDecode());
                    	ch.pipeline().addLast(new MyEncode());
        				ch.pipeline().addLast(new  ServerHandlerAdapter());
        				ch.pipeline().addLast(new IdleStateHandler(20, 30, 40,TimeUnit.SECONDS));
                    }
                }) .childOption(ChannelOption.SO_KEEPALIVE, true);;

        try {
            cf = b.bind().sync();
            InetSocketAddress addr = (InetSocketAddress) cf.channel().localAddress();
            logger.info("WebSocketServer start success, port is:{}", addr.getPort());


        } catch (InterruptedException e) {
            logger.error("WebSocketServer start fail,", e);
        }
    }

    @Override
    public void shutdown() {
        if (executorService != null) {
            executorService.shutdown();
        }
        super.shutdown();
    }
    public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}
}
