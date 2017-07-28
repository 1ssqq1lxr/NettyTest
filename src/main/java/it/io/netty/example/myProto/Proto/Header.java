package it.io.netty.example.myProto.Proto;

import java.io.Serializable;
/**
 *  头部信息
 * @author 17070680
 *
 */
public class Header implements Serializable{
	/**
	 * 
	 */
	private   long serialVersionUID = 100000000L;
	private   int  serialNo=2>>8|156;
	private int version =1;
	private int bodyLength;
	private String businessCode;//1 普通消息 2ping pong消息 
	
	public  void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getBodyLength() {
		return bodyLength;
	}
	public void setBodyLength(int bodyLength) {
		this.bodyLength = bodyLength;
	}
	public String getBusinessCode() {
		return businessCode;
	}
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
	public int getSerialNo() {
		return serialNo;
	}
	
	public static void main(String[] args) {
		System.out.println();
	}
	
	
}
