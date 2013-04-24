package channel;

public class INF2140assignment3 {

	public static void main(String[] args) throws InterruptedException {

		Select sender_select = new Select();
		Select reciever_select = new Select();

		Selectable a = new Selectable();
		sender_select.add(a);
		Selectable b = new Selectable();
		reciever_select.add(b);
		Selectable c = new Selectable();
		reciever_select.add(c);
		Selectable d = new Selectable();
		sender_select.add(d);

		Sender sender = new Sender("S", sender_select);
		Reciever reciever = new Reciever("R", reciever_select);

		UnreliableChannel k = new UnreliableChannel("K", a, b);
		UnreliableChannel l = new UnreliableChannel("L", c, d);

	}

}
