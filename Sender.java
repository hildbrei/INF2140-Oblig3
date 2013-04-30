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
	private Selectable a;
	private Selectable d;
	private boolean isReady;//is telling if the sender is ready to receive a in_msg.data or not
							//(it may happen that S is occupied with a number from S1 when S2 wants to send in a number)
	private boolean inProgress;//is telling if the sender has received in_msg.data and not acked yet. (used in S to wait)
	

	public Sender(String string, Select sender_select, int b, UnreliableChannel<String> l, UnreliableChannel<String> k, Selectable a, Selectable d) {
		super(string);
		this.l = l;
		this.k = k;
		this.sender_select = sender_select;
		send = 1;
		receive = 0;
		bit = b;		
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
						/**
						 * Start from here! out_ack and in_msg.data is supposed to be done from S1 and S2.
						 */
						/*System.out.println("out_ack: " + bit);
						bit = (bit+1)%2;
						number = (number+1)%9;
						System.out.println("in_msg.data: " + number); Now, we are suppose to do this from S1 or S2*/
						inProgress = false;
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
	
	public synchronized boolean checkIsReady(){
		return isReady;
	}
	
	public synchronized void updateIsReady(boolean b){
		isReady = b;
	}
	
	public synchronized void setNumber(int nr){
		number = nr;
		System.out.println("in_msg.data: " + number);
		inProgress = true;
		
	}


	public synchronized boolean checkInProgress() {
		return inProgress;
	}
}
