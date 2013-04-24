package channel;

public class Reciever implements Runnable {

	private String name;
	private Select reciever_select;
	private int send;
	private int recieve;

	public Reciever(String string, Select reciever_select) {

		name = string;
		this.reciever_select = reciever_select;
		send = 1;
		recieve = 0;
	}

	public void run() {

	}
}
