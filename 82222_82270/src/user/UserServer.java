package user;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class UserServer {
	
	private static final int THREAD_LIMIT=5;
	private ServerSocket ss;
	private User user;
	private ThreadPool threadpool;
	

	public UserServer(User user) {
		this.user=user;
	}

	public void startServing() throws IOException {
		ss=new ServerSocket(user.getPorto());
		threadpool=new ThreadPool(THREAD_LIMIT);
		try {
			while(true) {
				Socket socket = ss.accept();
				System.out.println("Conexão aceite: " + socket);
				new DealWithClient(socket,user,threadpool).start();
			} 
			}finally {
				try {
					ss.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
