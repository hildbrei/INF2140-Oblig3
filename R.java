package channel;

public class R extends Thread{

	Receiver myReceiver;
	int myID;
	int receivedNr;
	public R(Receiver receiver, int id) {
		myReceiver = receiver;
		myID = id;
		receivedNr = -1;
	}

	public void run(){
		while(true){
			int nr = myReceiver.checkOutData(this);
			if(nr != -1){
				System.out.println("R"+myID + ".output.data:" + nr);
				myReceiver.sendInAck(myID);
			}
			myReceiver.updateOutData(null);
		}
	}

	public void sendOutputData(int number) {
		receivedNr = number;
		System.out.println(myID + ".output.data:"+receivedNr);
	}
	
	public int getID(){
		return myID;
	}
}
