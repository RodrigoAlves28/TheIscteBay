package user;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.util.ArrayList;

public class DealWithClient extends Thread {

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private User user;
	private File[] files;
	private ThreadPool threadpool;

	public DealWithClient(Socket socket, User user,ThreadPool threadpool) {
		this.socket = socket;
		this.user = user;
		this.threadpool=threadpool;
		files = new File(this.user.getPasta()).listFiles();
	}

	public void run() {
		try {
			doConnections(socket);
			serve();
		} catch(EOFException e){
			System.out.println("No more to read");
		} catch (ClassNotFoundException |IOException e) {
			e.printStackTrace();
		}finally{
			try{
				socket.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	public File getFile(FileDetails f) {
		for (File file : files)
			if (f.equals(new FileDetails(file.getName(), file.length())))
				return file;
		return null;
	}

	private byte[] getFileBlock(FileBlockRequestMessage request) throws IOException {
		byte[] fileBlock = new byte[request.getLength()];
		byte[] fileContents = Files.readAllBytes(getFile(request.getDetails()).toPath());
		System.arraycopy(fileContents, request.getOffset(), fileBlock, 0, fileBlock.length);
		return fileBlock;

	}

	private void serve() throws ClassNotFoundException, IOException {
		while (true) {
			ArrayList<FileDetails> info = new ArrayList<FileDetails>();
			Object o = in.readObject();
			if (o instanceof WordSearchMessage) {
				String s = ((WordSearchMessage) o).getPalavra();
				for (File f : files) {
					if (f.getName().contains(s)) {
						FileDetails f1 = new FileDetails(f.getName(), f.length());
						info.add(f1);
					}
				}
				out.writeObject(info);
			} else if (o instanceof FileBlockRequestMessage) {
				FileBlockRequestMessage fileBlockRequest = (FileBlockRequestMessage) o;
				HandleFileBlockRequest handler=new HandleFileBlockRequest(fileBlockRequest);
				threadpool.submit(handler);
				
			}
		}

	}

	private void doConnections(Socket socket) throws IOException {
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());

	}

	private class HandleFileBlockRequest implements Runnable {

		private FileBlockRequestMessage fileBlockRequest;

		public HandleFileBlockRequest(FileBlockRequestMessage fileBlockRequest) {
			this.fileBlockRequest = fileBlockRequest;

		}

		@Override
		public void run() {
			byte[] fileBlock;
			try {
				fileBlock = getFileBlock(fileBlockRequest);
				FilePart filepart = new FilePart(fileBlock, fileBlockRequest.getOffset(), fileBlockRequest.getLength());
				synchronized (out) {
					out.writeObject(filepart);
				}
			} catch (SocketException e) {
				System.out.println("Timeout");
			} catch (IOException e){
				e.printStackTrace();
			}

		}

	}

}
