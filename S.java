package channel;

public class S extends Thread{
	
	Sender mySender;
	int number;
	public S(Sender sender) {
		mySender = sender;
		number = 0;
	}

	public void run(){
		while(mySender.checkIsReady() == false){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mySender.updateIsReady(false);
			mySender.setNumber(number);
			while(mySender.checkInProgress() == true){
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mySender.updateIsReady(true);
				notify();//is it supposed to be notifyAll() here?
			}
		}
	}
}
