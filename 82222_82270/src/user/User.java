package user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

public class User extends Thread {

	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;
	private String address;
	private int portDir, porto;
	private String palavra;
	private String pasta;
	private ArrayList<UserInfo> users = new ArrayList<>();
	private ArrayList<FileDetails> ficheirosEncontrados = new ArrayList<>();
	private DefaultListModel<FileDetails> search = new DefaultListModel<FileDetails>();

	public User(String address, int portDir, int porto, String pasta) {
		this.address = address;
		this.portDir = portDir;
		this.porto = porto;
		this.pasta = pasta;
	}

	public void connectToServer() throws IOException {
		socket = new Socket(InetAddress.getByName(address), portDir);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		out.println("INSC " + address + " " + porto);
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						String s = in.readLine();
						if (!s.equals("END")) {
							String[] tokens = s.split(" ");
							users.add(new UserInfo(tokens[1], Integer.parseInt(tokens[2])));
						} else
							procura();

					} catch (IOException | NumberFormatException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	public FileDetails addToFicheirosEncontrados(FileDetails f) {
		for (FileDetails f1 : ficheirosEncontrados)
			if (f.equals(f1))
				return f1;
		ficheirosEncontrados.add(f);
		return f;
	}
	
	public ArrayList<FileDetails> ficheirosExistentes(){
		File[] files=new File(getPasta()).listFiles();
		ArrayList<FileDetails> filesExistentes=new ArrayList<FileDetails>();
		for(File f:files)
			if(ficheirosEncontrados.contains(new FileDetails(f.getName(),f.length())))
				filesExistentes.add(new FileDetails(f.getName(),f.length()));
		return filesExistentes;
	}

	public void consultaUsers(String w) {
		out.println("CLT ");
		palavra = w;

	}

	public void procura() throws IOException, InterruptedException {
		if (palavra.isEmpty())
			System.out.println("Pesquisa vazia");
		else {
			FileDetails file;
			ArrayList<Search> search1 = new ArrayList<Search>();
			for (UserInfo u : users) {
				if (u.getPorto() != getPorto() && u.getAddress() != getAddress())
					search1.add(new Search(u.getPorto(), u.getAddress(), new WordSearchMessage(palavra)));
			}
			for (Search t : search1)
				t.start();
			for (Search a : search1)
				a.join();
			for (Search s1 : search1)
				for (FileDetails f1 : s1.getInfo()) {
					file = addToFicheirosEncontrados(f1);
					file.addUser(new UserInfo(s1.getAddress(), s1.getPorto()));
				}
			ficheirosEncontrados.removeAll(ficheirosExistentes());
			System.out.println(ficheirosEncontrados);

			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						search.removeAllElements();
						for (FileDetails f : ficheirosEncontrados)
							search.addElement(f);
					}

				});
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

			ficheirosEncontrados.clear();
			users.clear();
		}
	}

	public int getPorto() {
		return porto;
	}

	public String getPasta() {
		return pasta;
	}

	public String getAddress() {
		return address;
	}

	public ArrayList<FileDetails> getFiles() {
		return ficheirosEncontrados;
	}

	public DefaultListModel<FileDetails> getSearch() {
		return search;
	}


}
