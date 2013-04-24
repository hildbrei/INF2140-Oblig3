package channel;

public class INF2140assignment3 {

	public static void main(String[] args) throws InterruptedException {
		
		Select sender_select = new Select();
		Select receiver_select = new Select();

		Selectable a = new Selectable();
		Selectable c = new Selectable();
		Selectable b = new Selectable();
		Selectable d = new Selectable();
		

		UnreliableChannel k = new UnreliableChannel("K", a, b);
		UnreliableChannel l = new UnreliableChannel("L", c, d);
		
		Sender sender = new Sender("S", sender_select, 0, 0, l, k);
		sender_select.setProcess(sender);
		Receiver receiver = new Receiver("R", receiver_select, l, k);
		receiver_select.setProcess(receiver);
		
		sender_select.add(0, a);
		receiver_select.add(0, c);
		receiver_select.add(1, b);
		sender_select.add(1, d);

		Thread s_thread = new Thread(sender);
		Thread r_thread = new Thread(receiver);
		Thread k_thread = new Thread(k);
		Thread l_thread = new Thread(l);
		
		s_thread.start();
		r_thread.start();
		k_thread.start();
		l_thread.start();
	}
}
