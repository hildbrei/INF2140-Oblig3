package channel;

public class Sender implements Runnable {

	private String name;
	private Select sender_select;
	private int send;
	private int recieve;
	private int value;
	private int bit;


	public Sender(String string, Select sender_select, int val, int b) {
		name = string;
		this.sender_select = sender_select;
		send = 0;
		recieve = 1;
		value = val;
		bit = b;
	}


	public void run() {
		while(true){
			int choice = -1;
			try {
				choice = sender_select.choose();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(choice == send){
				//send data and bit
				System.out.println("in choice <send> Sender");
				break;
			}
			else if(choice == recieve){
				//recieve data and bit
				System.out.println("in choice <recieve> Sender");
				break;
			}
		}
	}
}
