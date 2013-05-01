package channel;

public class Receiver extends Process implements Runnable {

	private Select receiver_select;
	private int send;
	private int receive;
	private String data;
	private int bit;
	private int number;
	private int sID;
	UnreliableChannel<String> l;
	UnreliableChannel<String> k;
	private Selectable b;
	private Selectable c;
	String outData;//the data to be sent to R1 or R2
	

	public Receiver(String string, Select reciever_select, UnreliableChannel<String> l, UnreliableChannel<String> k, Selectable b, Selectable c) {		
		super(string);
		this.receiver_select = reciever_select;
		send = 0;
		receive = 1;
		this.l = l;
		this.k = k;
		bit = 1;
		this.b = b;
		this.c = c;
		outData = null;
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
	/**
	 * Sends both bit and the id of the subsender. 
	 */
	private void sendBit() throws InterruptedException{
		l.send(bit + "," + sID);
	}
	
	private void receiveData() throws InterruptedException{
		data = k.receive();
		if(data == null){
			System.out.println("blææ R");
			
		}
		int nr = (int)(data.charAt(0)-48);
		int b = (int)(data.charAt(2) - 48);
		int id = (int)(data.charAt(4) - 48);
		
		if(bit != b){
			number = nr;
			sID = id;
			System.out.println("R"+sID + ".out_msg.data: " + number);
			updateOutData(sID + "," + number);
			bit = b;	
		}
	}

	public synchronized void updateOutData(String od) {
		outData = od;	
		notifyAll();
	}
	
	public synchronized int checkOutData(R r){
		while(outData == null){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		int rID = (int) (outData.charAt(0) - 48);
		if(r.getID() == rID){
			int outNr = (int)(outData.charAt(2) - 48);
			return outNr;
		}
		else return -1;
	}



	public void setStartState() {
		b.updateExternal(true);
		c.updateExternal(false);
		
	}



	public void sendInAck(int id) {
		System.out.println("R"+id+".in_ack");
	}
}
