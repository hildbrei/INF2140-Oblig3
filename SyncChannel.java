package channel;

public class SyncChannel<T> implements Channel<T> { 
	private T val=null;
	private int bit = 0;
	private String name;
	private Selectable send_sel;
	private Selectable rec_sel;

	public SyncChannel(String n,Selectable s, Selectable r) throws InterruptedException {
		name=n;
		send_sel = s; 
		rec_sel=r;
		send_sel.updateInternal(true);
		rec_sel.updateInternal(true);
	}

	public synchronized boolean isEmpty(){
		return (val==null);
	}

	public synchronized void send(T v, int b) throws InterruptedException {
		val = v;
		bit = b;
		System.out.println(name+" contains: "+ val+"\n");
		send_sel.updateInternal(isEmpty());
		rec_sel.updateInternal(!isEmpty());
	}

	public synchronized T receive() throws InterruptedException {
		T tmp = val; 
		val = null; 
		rec_sel.updateInternal(!isEmpty());
		send_sel.updateInternal(isEmpty());
		System.out.println(name+" is empty\n");
		return tmp;
	}
}

