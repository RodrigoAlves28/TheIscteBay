package user;

import java.io.Serializable;

public class FileBlockRequestMessage implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1381748399778212437L;
	private FileDetails details;
	private int offset;
	private int length;
	
	public FileBlockRequestMessage(FileDetails details, int offset, int length) {
		this.details = details;
		this.offset = offset;
		this.length = length;
	}

	public FileDetails getDetails() {
		return details;
	}

	public int getOffset() {
		return offset;
	}

	public int getLength() {
		return length;
	}
	
	public String toString(){
		return details+ " offset:"+offset+" length:"+length;
	}
	
	
	
	

}
