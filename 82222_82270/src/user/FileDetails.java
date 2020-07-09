package user;

import java.io.Serializable;
import java.util.ArrayList;

public class FileDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7443264148354693635L;
	private String nome;
	private long tamanho;
	private ArrayList<UserInfo>users=new ArrayList<UserInfo>();

	public FileDetails(String nome, long tamanho) {
		this.nome = nome;
		this.tamanho = tamanho;
	}

	public String getNome() {
		return nome;
	}

	public long getTamanho() {
		return tamanho;
	}

	public String toString() {
		return "Nome: " + nome + ", Tamanho: " + tamanho;

	}

	public boolean equals(Object f) {
		boolean same = false;
		if (f instanceof FileDetails) {
			same = ((FileDetails) f).getNome().equals(this.nome) && ((FileDetails) f).getTamanho() == this.tamanho;
		}
		return same;
	}
	
	public void addUser(UserInfo user){
		users.add(user);
	}
	
	public ArrayList<UserInfo> getUsers(){
		return users;
	}

}
