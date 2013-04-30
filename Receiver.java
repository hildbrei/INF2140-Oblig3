package channel;

public class Receiver extends Process implements Runnable {

	private Select receiver_select;
	private int send;
	private int receive;
	private String data;
	private int bit;
	private int number;
	UnreliableChannel<String> l;
	UnreliableChannel<String> k;
	private Selectable b;
	private Selectable c;
	private boolean ack;//to check when to send from receiver
	

	public Receiver(String string, Select reciever_select, UnreliableChannel<String> l, UnreliableChannel<String> k, Selectable b, Selectable c) {
		
		
		
		super(string);
		this.receiver_select = reciever_select;
		send = 0;
		receive = 1;
		this.l = l;
		this.k = k;
		bit = 1;
		ack = false;
		this.b = b;
		this.c = c;
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
				//we need to update the guard to receiver_select.list.index(send)
				try {
					sendBit();
					b.updateExternal(true);
					c.updateExternal(false);
					
					Thread.sleep(1000);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else if(choice == receive){
				//receive data and bit
				System.out.println("in choice <recieve> Reciever");
				//we need to update the right guard to receiver_select.list.index(receiver)
				try {
					
					receiveData();
					b.updateExternal(true);
					c.updateExternal(true);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void sendBit() throws InterruptedException{
		l.send("" +bit);
	}
	
	private void receiveData() throws InterruptedException{
		data = k.receive();
		if(data == null){
			System.out.println("blææ R");
			
		}
		System.out.println(data + " is received at " + getName());
		int nr = (int)(data.charAt(0)-48);
		int b = (int)(data.charAt(2) - 48);
		
		if(bit != b){
			number = nr;
			System.out.println("out_msg.data: " + number);
			bit = b;	
			ack = true;
		}
	}

	public void setStartState() {
		b.updateExternal(true);
		c.updateExternal(false);
		
	}
}
