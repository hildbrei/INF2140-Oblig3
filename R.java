package channel;

public class R extends Thread{

	Receiver myReceiver;
	int myID;
	public R(Receiver receiver, int id) {
		myReceiver = receiver;
		myID = id;
	}

	public void run(){
		
	}
}
