package channel;
import java.util.Random;

public class UnreliableChannel<T> implements Runnable,Channel<T>  {
	final static int maxError = 5; // The channel cannot lose or duplicate more than maxError times continuously
	private T val=null;
	private int bit;
	private SyncChannel<T> ch1, ch2;
	private int numberOfRepetitionAndLost = 0;


	public UnreliableChannel(String name, Selectable send_sel, Selectable rec_sel) throws InterruptedException {
		Selectable s_int=new Selectable();//internal selectable
		Selectable r_int=new Selectable();//internal selectable
		send_sel.updateInternal(true);
		rec_sel.updateInternal(true);
		ch1 = new SyncChannel<T>(name+" left",send_sel,r_int);//channel a or c?
		ch2 = new SyncChannel<T>(name+" right",s_int,rec_sel);//channel b or d?
	}

	/*
	 * After sending, notify run-method
	 */
	public synchronized void send(T v) throws InterruptedException {
		ch1.send(v);
		System.out.println(v + " er mottatt i U ch");
		notifyAll();
	}
	

	/*
	 * After receiving, notify run-method
	 */
	public synchronized T receive() throws InterruptedException {
		T v = ch2.receive();
		notifyAll();
		return v;
	}

	/*
	 * Internal methods used by run-method
	 */
	private synchronized void int_send(T v) throws InterruptedException { 
		ch2.send(v); 
	}
	private synchronized T int_receive() throws InterruptedException { 
		return ch1.receive(); 
	}

	@Override
	public synchronized void run() {
		try{ 
			while (true){
				while (ch1.isEmpty()||!ch2.isEmpty()){ 
					wait(); 
				} // Wait for something to do
				val=int_receive();
				while (numberOfRepetitionAndLost < maxError){
					Random random = new Random();
					int randomInt = random.nextInt(3);	// randomInt gets the values 0, 1 and 2 which 
					int safeInt = 0;
					// correspond to losing, duplicating or properly sending messages		
					switch (safeInt) {
					case 0: // Send normally
						int_send(val);
						numberOfRepetitionAndLost= maxError; //now, you will not continue in while-loop
						break;
					case 1: // Lose the message, i.e, the message is not delivered
						numberOfRepetitionAndLost++;
						break;
					case 2: // Duplicate the message
						int_send(val);
						numberOfRepetitionAndLost++;
						break;
					}
					while (!ch2.isEmpty()){ 
						wait();
					} // if message is sent, wait for receiver before looping
				}
				numberOfRepetitionAndLost = 0; // reset for next message
			}
		} catch (Exception e){}
	}
	
	public synchronized boolean isEmpty(){
		return (val == null);
	}
}

