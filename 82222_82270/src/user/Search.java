package user;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class Search extends Thread {
	
	ObjectOutputStream out;
	ObjectInputStream in;
	private Socket socket;
	private int porto;
	private String address;
	private WordSearchMessage palavra;
	private List<FileDetails> info;
	
	public Search(int porto, String address, WordSearchMessage palavra){
		this.porto=porto;
		this.address=address;
		this.palavra=palavra;
	}
	
	public void connect() throws IOException{
		socket= new Socket(InetAddress.getByName(address),porto);
		out = new ObjectOutputStream(socket.getOutputStream());
		in= new ObjectInputStream(socket.getInputStream());
	}
	
	
	@SuppressWarnings("unchecked")
	public void run(){
		try {
			connect();
			out.writeObject(palavra);
			Object o=in.readObject();
			info=(List<FileDetails>)o;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
			try{
				socket.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
	}
	
	public List<FileDetails> getInfo(){
		return info;
	}
	
	public String getAddress(){
		return address;
	}
	
	public int getPorto(){
		return porto;
	}

}
