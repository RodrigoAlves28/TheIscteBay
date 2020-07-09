package user;

import java.io.Serializable;

public class WordSearchMessage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3794235401783980448L;
	private String palavra;
	
	
	public WordSearchMessage(String palavra){
		this.palavra=palavra;
	}
	
	public String getPalavra(){
		return palavra;
	}
	
	

}
