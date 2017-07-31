package it.io.netty.example.myProto.code;

import java.io.Serializable;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import it.io.netty.example.myProto.Proto.Entity;
/**
 * 编码
 * @author 17070680
 *
 */
public class MyEncode extends MessageToByteEncoder<Entity<Serializable>> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Entity<Serializable> msg,
			ByteBuf out) throws Exception {
		// TODO Auto-generated method stub
		int serialNo = msg.getHeader().getSerialNo();
		out.writeInt(serialNo);
		out.writeInt(msg.getHeader().getVersion());
		boolean s =msg.getBody()!=null&&serialNo!=2;
		if(msg.getBody()!=null&&serialNo!=2){
			byte[] objectToByte = ByteObjConverter.objectToByte(msg.getBody());
			out.writeInt(objectToByte.length);
			out.writeBytes(objectToByte);
		}
		else{
			out.writeInt(0);
		}
	}

}
