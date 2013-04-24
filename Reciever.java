package channel;

public class Reciever implements Runnable {

	private String name;
	private Select reciever_select;
	private int send;
	private int recieve;
	private int value;
	private int bit;

	public Reciever(String string, Select reciever_select) {

		name = string;
		this.reciever_select = reciever_select;
		send = 1;
		recieve = 0;
	}

	public void run() {
		while(true){
			int choice = -1;
			try {
				choice = reciever_select.choose();	
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(choice == send){
				//send data and bit
				System.out.println("in choice <send> Reciever");
				break;
			}
			else if(choice == recieve){
				//recieve data and bit
				System.out.println("in choice <recieve> Reciever");
				break;
			}
		}
	}
}
