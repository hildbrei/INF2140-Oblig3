package channel;

public class Sender implements Runnable {

	private String name;
	private Select sender_select;
	private UnreliableChannel l;
	private UnreliableChannel k;
	private int send;
	private int receive;
	private int value;
	private int bit;
	private String data;


	public Sender(String string, Select sender_select, int val, int b, UnreliableChannel l, UnreliableChannel k) {
		this.l = l;
		this.k = k;
		name = string;
		this.sender_select = sender_select;
		send = 0;
		receive = 1;
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
				//send data(value and bit together in a String)
				System.out.println("in choice <send> Sender");
				try {
					k.send(data);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else if(choice == receive){
				//recieve data (value and bit together in a String)
				System.out.println("in choice <recieve> Sender");
				try {
					String bit = (String) l.receive();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
