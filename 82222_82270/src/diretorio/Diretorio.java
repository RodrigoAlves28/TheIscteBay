package diretorio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Diretorio {

	private ServerSocket ss;
	private int porto;
	private ArrayList<String[]> users = new ArrayList<>();

	public Diretorio(int porto) {
		this.porto = porto;
	}

	public void startServing() throws IOException {
		ss = new ServerSocket(porto);

		try {
			while (true) {
				Socket socket = ss.accept();
				System.out.println("Conexão aceite: " + socket);
				new DealWithUser(socket,this).start();
			}
		} finally {
			ss.close();
		}
	}
	
	public synchronized void addUser(String[] s){
		users.add(s);
	}
	
	public synchronized ArrayList<String[]> getUsers() {
		return users;
	}


	public static void main(String[] args) {
		try {
			new Diretorio(Integer.parseInt(args[0])).startServing();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
