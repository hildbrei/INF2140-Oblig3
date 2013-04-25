package channel;

public class Receiver extends Process implements Runnable {

	private Select receiver_select;
	private int send;
	private int receive;
	private String data;
	private int bit;
	private int number;
	UnreliableChannel l;
	UnreliableChannel k;

	public Receiver(String string, Select reciever_select, UnreliableChannel l, UnreliableChannel k) {
		super(string);
		this.receiver_select = reciever_select;
		send = 0;
		receive = 1;
		this.l = l;
		this.k = k;
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
					Selectable s = receiver_select.getList().get(send);
					s.updateExternal(true);
					Selectable r = receiver_select.getList().get(receive);
					r.updateExternal(false);
					sendBit();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else if(choice == receive){
				//receive data and bit
				System.out.println("in choice <recieve> Reciever");
				//we need to update the guard to receiver_select.list.index(receiver)
				try {
					Selectable s = receiver_select.getList().get(send);
					s.updateExternal(false);
					Selectable r = receiver_select.getList().get(receive);
					r.updateExternal(true);
					receiveData();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void sendBit() throws InterruptedException{
		l.send(bit);
	}
	
	private void receiveData() throws InterruptedException{
		data = (String) k.receive();
		String[] dataArr = new String[] {data};
		for(String s: dataArr){
			System.out.println(s);
		}
		int nr = Integer.parseInt(dataArr[0]);
		int b = Integer.parseInt(dataArr[2]);
		if(bit != b){
			number = nr;
			System.out.println("out_msg.data: " + number);
			bit = b;
		}
	}

	public void setStartState() {
		Selectable s = receiver_select.getList().get(send);
		Selectable r = receiver_select.getList().get(send);
		s.updateExternal(false);
		r.updateExternal(true);
	}
}
