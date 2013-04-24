package channel;

public class Sender implements Runnable {

	private String name;
	private Select sender_select;
	private int send;
	private int recieve;


	public Sender(String string, Select sender_select) {
		name = string;
		this.sender_select = sender_select;
		send = 0;
		recieve = 1;

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
			}
			else if(choice == recieve){
				//recieve data and bit
			}
		}
	}
}
