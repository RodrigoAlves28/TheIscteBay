package diretorio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class DealWithUser extends Thread {

	private Socket socket;
	private Diretorio dir;
	private BufferedReader in;
	private PrintWriter out;

	public DealWithUser(Socket socket, Diretorio dir) {
		super();
		this.socket = socket;
		this.dir = dir;
	}

	public void run() {
		try {
			doConnections(socket);
			serve();
		} catch (IOException e) {
			ArrayList<String[]> temp = new ArrayList<>();
			for (String[] s : dir.getUsers()) {
				if (s[3].equals(Integer.toString(socket.getPort()))) {
					temp.add(s);
					System.out.println("Utilizador "+s[1]+" "+s[2] + " saiu");
				}
			}

			dir.getUsers().removeAll(temp);

		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void serve() throws IOException {
		while (true) {
			String s = in.readLine();
			String[] tokens = s.split(" ");
			String[] users = new String[4];
			for (int i = 0; i != tokens.length; i++)
				users[i] = tokens[i];
			users[3] = Integer.toString(socket.getPort());
			switch (tokens[0]) {
			case "INSC":
				dir.addUser(users);
				break;
			case "CLT":
				for (String[] s1 : dir.getUsers())
					out.println("CLT " + s1[1] + " " + s1[2]);
				out.println("END");
				break;
			}
		}

	}

	public void doConnections(Socket socket) throws IOException {
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
	}

}
