package channel;

public class Sender extends Process implements Runnable {

	private Select sender_select;
	private UnreliableChannel <String> l;
	private UnreliableChannel <String> k;
	private final int send;
	private final int receive;
	private int number;//the data to be sent
	private int bit;//bit to be sent with data (number)
	private int choice;//the place in ArrayList (Selectable objects)
	private boolean ack;//if received ack or not
	private Selectable a;
	private Selectable d;
	
	

	public Sender(String string, Select sender_select, int nr, int b, UnreliableChannel<String> l, UnreliableChannel<String> k, Selectable a, Selectable d) {
		super(string);
		this.l = l;
		this.k = k;
		this.sender_select = sender_select;
		send = 1;
		receive = 0;
		number = nr;
		System.out.println("in_msg.data: " + number);
		bit = b;
		ack = false;
		
		this.a = a;
		this.d = d;
		
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
					a.updateExternal(true);
					d.updateExternal(true);
					
					Thread.sleep(1000);
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
					a.updateExternal(true);
					d.updateExternal(true);
					Thread.sleep(100);
				
					if(bitReceived == bit){
						System.out.println("out_ack: " + bit);
						bit = (bit+1)%2;
						number = (number+1)%9;
						System.out.println("in_msg.data: " + number);
					}
					Thread.sleep(1000);
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
		String b = l.receive();
		if(b == null){
			System.out.println("blææ Sender");
			
		}
		
		return Integer.parseInt(b);
	}


	public void setStartState() {
		a.updateExternal(true);
		d.updateExternal(false);
	}
}
