package user;

public class ThreadPool {
	private Worker[] workers;
	private BlockingQueue<Runnable> queue;

	public ThreadPool(int n) {
		queue = new BlockingQueue<Runnable>();
		workers = new Worker[n];

		for (int i = 0; i < n; i++) {
			workers[i] = new Worker(queue);
			workers[i].start();
		}

	}

	public void submit(Runnable task) {
		queue.offer(task);

	}

}
