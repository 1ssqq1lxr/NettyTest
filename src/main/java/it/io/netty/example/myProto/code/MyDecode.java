package it.io.netty.example.myProto.code;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import it.io.netty.example.myProto.Proto.Entity;
import it.io.netty.example.myProto.Proto.Header;

import java.io.Serializable;
import java.util.List;
/**
 * 解码
 * @author 17070680
 *
 */
public class MyDecode extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		
		Entity<Serializable> entity = new Entity<Serializable>();
		Header header = new Header();
		entity.setHeader(header);
		// TODO Auto-generated method stub
		int readerIndex = in.readerIndex(); //初始化读index
		int serialNo= in.readInt();// SerialNo
		if(serialNo!=(2>>8|156)&&serialNo!=2){ //协议标识错误，关闭链路 
			ctx.close();
			return ;
		}
		int version = in.readInt();
		int bodyLength = in.readInt();
		header.setSerialNo(serialNo);
		header.setVersion(version);
		if(bodyLength>0&&serialNo!=2){
			//			if(bodyLength)
			if(in.readableBytes()<bodyLength){
			    in.readerIndex(readerIndex);  //刷新指针
	            return;  
			}
			Serializable byteToObject = (Serializable) ByteObjConverter.byteToObject(ByteObjConverter.read(in));
			entity.setBody(byteToObject);
		}
		out.add(entity);
	}

}
