package channel;

public class S extends Thread{

	private Sender mySender;
	private int number;
	private int myID;
	private int range;
	
	public S(Sender sender, int id) {
		mySender = sender;
		number = 0;
		myID = id;
		range = 6;
	}

	public void run(){
		while(true){
			mySender.checkIsReady();//s1 or s2 is waiting for it to be their turn to send data
			mySender.updateIsReady(false);//now s1 or s2 can send data, and first sets Sender to be occupied
			mySender.setID(myID);
			mySender.setNumber(number);
			number = (number+1)%range;
			mySender.checkInProgress();
			System.out.println("S" + myID + ".out_ack");
			mySender.updateIsReady(true);
			try {
				sleep(100);//to make the other subsender able to take over the sending. 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
