package channel;

public class INF2140assignment3 {

	public static void main(String[] args) throws InterruptedException {

		Select sender_select = new Select();
		Select receiver_select = new Select();

		Selectable a = new Selectable();
		sender_select.add(0, a);
		Selectable b = new Selectable();
		receiver_select.add(1, b);
		Selectable c = new Selectable();
		receiver_select.add(0, c);
		Selectable d = new Selectable();
		sender_select.add(1, d);

		UnreliableChannel k = new UnreliableChannel("K", a, b);
		UnreliableChannel l = new UnreliableChannel("L", c, d);
		
		Sender sender = new Sender("S", sender_select, 0, 0, l, k);
		Receiver reciever = new Receiver("R", receiver_select, l, k);

		Thread s_thread = new Thread(sender);
		Thread r_thread = new Thread(reciever);
		Thread k_thread = new Thread(k);
		Thread l_thread = new Thread(l);
		
		s_thread.start();
		r_thread.start();
		k_thread.start();
		l_thread.start();
	}
}
