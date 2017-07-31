package it.io.netty.example.myProto;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import it.io.netty.example.myProto.Handles.ClientHandlerAdapter;
import it.io.netty.example.myProto.code.MyDecode;
import it.io.netty.example.myProto.code.MyEncode;
import it.io.netty.example.myProto.heart.ChannelConnect;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 客户端代码
 * @author 17070680
 *
 */
public class Client extends AbstractBaseClient{
	private ScheduledExecutorService executorService;
	public Client() {
		this.init();
		executorService = Executors.newScheduledThreadPool(2);
	}

	public Client(int port) {
		this.init();
		this.port = port;
		executorService = Executors.newScheduledThreadPool(2);
	}
	public void start() {
		// TODO Auto-generated method stub
		b.group(clientGroup)
		.channel(NioSocketChannel.class)
		.option(ChannelOption.TCP_NODELAY, true)
		.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new MyEncode());
				ch.pipeline().addLast(new MyDecode());
				ch.pipeline().addLast(new  ClientHandlerAdapter(Client.this));
				ch.pipeline().addLast(new IdleStateHandler(20, 30, 60,TimeUnit.SECONDS));

				//检测链路是否读空闲    	
			}
		}) .option(ChannelOption.SO_KEEPALIVE, true);;
		this.connect();
	}

	@Override
	public  ChannelFuture connect() {
		ChannelFuture sync = b.connect("127.0.0.1",8099);
		sync.addListener(new ChannelFutureListener(){

			public void operationComplete(ChannelFuture future)
					throws Exception {
				// TODO Auto-generated method stub
				if (future.isSuccess()) {
					Channel  channel = future.channel();
					System.out.println("Connect to server successfully!"+channel.toString());
				} else {
					System.out.println("Failed to connect to server, try connect after 10s");
					executorService.execute(new ChannelConnect(Client.this));
				}
			}
		});
		return sync;
	}
	public static void main(String[] args) {
		Client client = new Client(11113);
		client.start();
	}

}
