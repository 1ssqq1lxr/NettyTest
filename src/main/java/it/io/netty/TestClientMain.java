package it.io.netty;

public class TestClientMain {
	public static void main(String[] args) {

				// TODO Auto-generated method stub
				try {
					new TimeClient().connectPackage(8888, "127.0.0.1");;
//					new TimeClient().connect(8888, "127.0.0.1");;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
}
