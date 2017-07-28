package it.io.netty.example.myProto.Proto;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
/**
 * 协议体的定义
 * @author 17070680
 *
 * @param <T>
 */
public class Entity<T extends Serializable> implements Serializable{
	
	private static final long serialVersionUID = 10500000000L;
	private Header header;
	private T body;
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public T getBody() {
		return body;
	}
	public void setBody(T body) {
		this.body = body;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "【Entity[header:["+JSON.toJSONString(header)+",body:"+JSON.toJSONString(body)+"]】";
	}
	
	
}
