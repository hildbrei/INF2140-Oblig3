package channel;

public class Sender extends Process implements Runnable {

	private Select sender_select;
	private UnreliableChannel l;
	private UnreliableChannel k;
	private int send;
	private int receive;
	private int number;
	private int bit;
	private int choice;
	private boolean ack;


	public Sender(String string, Select sender_select, int nr, int b, UnreliableChannel l, UnreliableChannel k) {
		super(string);
		this.l = l;
		this.k = k;
		this.sender_select = sender_select;
		send = 0;
		receive = 1;
		number = nr;
		System.out.println("in_msg.data: " + number);
		bit = b;
		ack = false;
	}


	public void run() {
		while(true){
			try {
				choice = sender_select.choose();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(choice == send){
				//send data(value and bit together in a String)
				System.out.println("in choice <send> Sender");
				//we must update the guards for sender_select.list.index(send)
				try {
					sendData();
					Selectable s = sender_select.getList().get(send);
					s.updateExternal(k.isEmpty());
					Selectable r = sender_select.getList().get(receive);
					r.updateExternal(!k.isEmpty());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			else if(choice == receive){
				//receive bit (bit as a String)
				System.out.println("in choice <recieve> Sender");
				//we must update the guards for sender_select.list.index(receive)
				try {
					int bitReceived = receiveBit();
					Selectable s = sender_select.getList().get(send);
					s.updateExternal(l.isEmpty());
					Selectable r = sender_select.getList().get(receive);
					r.updateExternal(!l.isEmpty());
					if(bitReceived == bit){
						System.out.println("out_ack: " + bit);
						bit = (bit+1)%2;
						number = (number+1)%3;
						System.out.println("in_msg.data: " + number);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void sendData() throws InterruptedException{
		k.send(number + "," + bit);
	}
	
	private int receiveBit() throws InterruptedException{
		String b = (String) l.receive();
		return Integer.parseInt(b);
	}


	public void setStartState() {
		Selectable s = sender_select.getList().get(send);
		Selectable r = sender_select.getList().get(send);
		s.updateExternal(true);
		r.updateExternal(true);
	}
}
