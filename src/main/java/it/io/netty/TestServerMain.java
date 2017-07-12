package it.io.netty;

public class TestServerMain {
	public static void main(String[] args) {

					try {
						new TimeServer().bindPackage(8888);
//						new TimeServer().bind(8888);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

	}
}
