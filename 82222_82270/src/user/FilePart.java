package user;

import java.io.Serializable;

public class FilePart implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5154860311476086457L;
	private byte[] fileBlock;
	private int offset;
	private int length;
	
	public FilePart(byte[] fileBlock, int offset, int length){
		this.fileBlock=fileBlock;
		this.offset=offset;
		this.length=length;
	}
	
	public byte[] getFileBlock(){
		return fileBlock;
	}
	
	public int getOffset(){
		return offset;
	}
	
	public int getLength(){
		return length;
	}

}
