package user;

public class Worker extends Thread {

	private BlockingQueue<Runnable> queue;
	
	public Worker(BlockingQueue<Runnable> queue) {
		this.queue = queue;
	}

	public void run() {
		Runnable task;
		while (true) {
			task = queue.poll();
			task.run();
			

		}
	}

}
