package channel;

public class INF2140assignment3 {

	public static void main(String[] args) throws InterruptedException {
		
		Select sender_select = new Select();
		Select receiver_select = new Select();

		Selectable a = new Selectable();
		Selectable c = new Selectable();
		Selectable b = new Selectable();
		Selectable d = new Selectable();
		

		UnreliableChannel<String> k = new UnreliableChannel<String>("K", a, b);
		UnreliableChannel<String> l = new UnreliableChannel<String>("L", c, d);
		
		Sender sender = new Sender("S", sender_select, 0, l, k, a, d);
		sender_select.setProcess(sender);
		Receiver receiver = new Receiver("R", receiver_select, l, k, b, c);
		receiver_select.setProcess(receiver);
		
		sender_select.add(0, d);
		receiver_select.add(0, c);
		receiver_select.add(1, b);		
		sender_select.add(1, a);
		
		sender.setStartState();//first time send should happen, not receive
		receiver.setStartState();//first time receive should happen, not send
		
		S s1 = new S(sender, 1);
		S s2 = new S(sender, 2);
		
		R r1 = new R(receiver, 1);
		R r2 = new R(receiver, 2);
			
		Thread s_thread = new Thread(sender);
		Thread k_thread = new Thread(k);
		Thread r_thread = new Thread(receiver);
		Thread l_thread = new Thread(l);
		
		s1.start();
		s2.start();
		
		k_thread.start();
		l_thread.start();
		Thread.sleep(100);
		s_thread.start();
		Thread.sleep(100);
		r_thread.start();
		
		r1.start();
		r2.start();		
	}
}
