package user;
import java.util.ArrayDeque;
import java.util.Queue;

public class BlockingQueue<T> {

	private Queue<T> queue = new ArrayDeque<>();
	private int capacidade;

	public BlockingQueue() {
		capacidade = -1;
	}

	public BlockingQueue(int capacidade) {
		this.capacidade = capacidade;
	}

	public synchronized void offer(T e)  {
		while (queue.size() == capacidade)
			try {
				this.wait();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		queue.add(e);
		this.notify();
	}

	public synchronized T poll()  {
		while (queue.isEmpty())
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if (capacidade > 0)
			this.notify();
		return queue.remove();
	}

	public synchronized int size() {
		return queue.size();
	}

	public synchronized void clear() {
		queue.clear();
	}
	
	public synchronized boolean isEmpty(){
		return queue.size()==0;
	}
	
	public String toString(){
		return queue.toString();
	}

}
