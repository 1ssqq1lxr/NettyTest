package it.io.netty.example;

import java.util.Date;  

import io.netty.buffer.ByteBuf;  
import io.netty.buffer.Unpooled;  
import io.netty.channel.ChannelHandlerAdapter;  
import io.netty.channel.ChannelHandlerContext;  
  
/** 
 * @author Administrator 
 * 
 */  
public class TimeServerHandler extends ChannelHandlerAdapter {  
  
    private int counter;  
    
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
        String body = (String) msg;  
        System.out.println("The time server receive order:" + body + ";the counter is:" + (++counter));  
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString()  
                : "BAD ORDER";  
        currentTime = currentTime + System.getProperty("line.separator");  
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());  
        ctx.write(resp);  
    }  
  
    public void channelReadComplete(ChannelHandlerContext ctx) {  
        ctx.flush();  
    }  
  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {  
        ctx.close();  
    }  
  
} 