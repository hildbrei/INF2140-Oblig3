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
	private S s1;
	private S s2;
	private int id;


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
		isReady = true;
	}


	public void run() {
		while(true){
			try {
				choice = sender_select.choose();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			id = getID();
			if(choice == send && id!= 0){
				System.out.println("in choice <send> Sender");
				try {
					
					sendData(id);
					a.updateExternal(true);
					d.updateExternal(true);
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else if(choice == receive && id != 0){
				System.out.println("in choice <recieve> Sender");
				try {
					String b = l.receive();
					if(b == null){
						System.out.println("blææ Sender");	
					}
					int bitReceived = (int)(b.charAt(0) - 48);
					int rID = (int)(b.charAt(2) - 48);
					a.updateExternal(true);
					d.updateExternal(true);
					Thread.sleep(100);
					if(bitReceived == bit && rID == id){
						bit = (bit+1)%2;
						updateInProgress(false);// same as out_ack to s1 or s2. 
						if(id == 1){
							id++;
						}
						else id--;
					}
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private synchronized void updateInProgress(boolean b) {
		inProgress = false;
		notifyAll();
	}


	private int getID() {
		return id;
	}

	public synchronized void setID(int i){
		id = i;
	}


	private void sendData(int id) throws InterruptedException{
		k.send(number + "," + bit + "," + id);
	}

	public void setStartState() {
		a.updateExternal(true);
		d.updateExternal(false);
	}

	public synchronized void checkIsReady(){
		while(!isReady){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void updateIsReady(boolean b){
		isReady = b;
		notifyAll();
	}

	public synchronized void setNumber(int nr){
		number = nr;
		System.out.println("in_msg.data: " + number);
		inProgress = true;

	}


	public synchronized void checkInProgress() {
		while(inProgress){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	public void setSubSenders(S s1, S s2) {
		this.s1 = s1;
		this.s2 = s2;
	}
}
