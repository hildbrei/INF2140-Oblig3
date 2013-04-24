package channel;

public class Receiver implements Runnable {

	private String name;
	private Select receiver_select;
	private int send;
	private int receive;
	private String data;
	private int bit;
	private int value;
	UnreliableChannel l;
	UnreliableChannel k;

	public Receiver(String string, Select reciever_select, UnreliableChannel l, UnreliableChannel k) {

		name = string;
		this.receiver_select = reciever_select;
		send = 0;
		receive = 1;
		this.l = l;
		this.k = k;
	}

	public void run() {
		while(true){
			int choice = -1;
			try {
				choice = receiver_select.choose();	
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(choice == send){
				//send data and bit
				System.out.println("in choice <send> Reciever");
				try {
					l.send(bit);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else if(choice == receive){
				//receive data and bit
				System.out.println("in choice <recieve> Reciever");
				try {
					data = (String) k.receive();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
