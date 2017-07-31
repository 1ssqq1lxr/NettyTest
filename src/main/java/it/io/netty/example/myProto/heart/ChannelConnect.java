package it.io.netty.example.myProto.heart;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import it.io.netty.example.myProto.Client;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 断电重连 如果一个线程连接成功后 自动关闭所有线程
 * @author 17070680
 *
 */
public class ChannelConnect implements Runnable{
	Client client ;
	
  	public ChannelConnect(Client client) {
		super();
		this.client = client;

	}

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public void run() {
		// TODO Auto-generated method stub
			boolean open=true;
			// TODO Auto-generated method stub
			try {
				// TODO Auto-generated method stub
				Thread.sleep(20000);
				logger.info("重连中");
				client.connect();
				
			} catch (Exception e) {
				// TODO: handle exception
				logger.error( e.getMessage());
			}
	}

}
