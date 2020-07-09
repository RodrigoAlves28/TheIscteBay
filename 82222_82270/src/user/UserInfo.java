package user;

import java.io.Serializable;

public class UserInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4510017766863768483L;
	private String address;
	private int porto;
	private int partesDescarregadas;
	
	public UserInfo(String address, int porto){
		this.address=address;
		this.porto=porto;
	}
	
	public String getAddress(){
		return address;
	}
	
	public int getPorto(){
		return porto;
	}
	
	public void setAddress(String address){
		this.address=address;
	}
	
	public void setPorto(int porto){
		this.porto=porto;
	}
	
	public String toString(){
		return "[endereço=/"+address + ", porto="+ porto+"]";
	}
	
	public void addParteDescarregada(int parte){
		partesDescarregadas+=parte;
	}
	
	public int getPartesDescarregadas(){
		return partesDescarregadas;
	}

}
