package channel;

public interface Channel<T> {
	public void send(T v, int bit) throws InterruptedException;
	public T receive() throws InterruptedException;
}
