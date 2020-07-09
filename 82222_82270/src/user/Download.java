package user;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

public class Download {
	
	private static final int BLOCKSIZE = 1024;
	private static final int TIMEOUT = 15000;
	
	private FileDetails fileInfo;
	private ArrayList<UserInfo> users;
	private BlockingQueue<FileBlockRequestMessage> blockList = new BlockingQueue<FileBlockRequestMessage>();
	private int fileSize;
	private int numberOfBlocks;
	private int lastBlockSize;
	private byte[] file;
	private int contador = 0;
	private String pasta;
	private ArrayList<StartDownload> start = new ArrayList<StartDownload>();
	private JProgressBar bar;
	private long tempoInicial = System.currentTimeMillis();

	public Download(FileDetails fileInfo, String pasta, JProgressBar bar) {
		this.pasta = pasta;
		this.fileInfo = fileInfo;
		this.bar = bar;
		bar.setMinimum(0);
		users = fileInfo.getUsers();
		fileSize = (int) fileInfo.getTamanho();
		bar.setMaximum(fileSize);
		file = new byte[fileSize];
		numberOfBlocks = fileSize / BLOCKSIZE;
		lastBlockSize = fileSize % BLOCKSIZE;
		if (lastBlockSize > 0)
			numberOfBlocks++;
		startDownload();
		writeFile();

	}

	public void startDownload() {
		new Thread(new Runnable() {
			public void run() {
				System.out.println("Comecei");
				getBlockList();
				for (UserInfo u : users)
					start.add(new StartDownload(u));
				for (StartDownload s : start)
					s.start();

			}
		}).start();
	}

	public void getBlockList() {
		for (int i = 0; i != numberOfBlocks; i++) {
			if (lastBlockSize > 0) {
				if (i != numberOfBlocks - 1)
					blockList.offer(new FileBlockRequestMessage(fileInfo, i * BLOCKSIZE, BLOCKSIZE));
				else
					blockList.offer(new FileBlockRequestMessage(fileInfo, i * BLOCKSIZE, lastBlockSize));
			} else
				blockList.offer(new FileBlockRequestMessage(fileInfo, i * BLOCKSIZE, BLOCKSIZE));

		}
	}

	public synchronized void addToArray(FilePart filepart) {
		System.arraycopy(filepart.getFileBlock(), 0, file, filepart.getOffset(), filepart.getLength());
		contador += filepart.getLength();
		bar.setValue(contador);
		if (contador == file.length)
			notifyAll();

	}

	public void writeFile() {
		new Thread(new Runnable() {
			public void run() {
				synchronized (file) {
					write();
				}
			}
		}).start();

	}

	public synchronized void write() {
		while (contador != file.length)
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		try {
			Files.write(Paths.get(pasta + "/" + fileInfo.getNome()), file);
			System.out.println("Escrevi");
		} catch (IOException e) {
			e.printStackTrace();
		}
		String s = "";
		for (UserInfo u : users)
			s += "Fornecedor" + u.toString() + ":" + u.getPartesDescarregadas() + "\n";
		long tempoTotal = (System.currentTimeMillis() - tempoInicial) / 1000;
		users.clear();
		start.clear();
		JOptionPane.showMessageDialog(null, "Descarga completa\n" + s + "Tempo decorrido:" + tempoTotal + "s");

	}

	private class StartDownload extends Thread {
		private UserInfo user;
		private ObjectOutputStream out;
		private ObjectInputStream in;
		private Socket socket;
		private FileBlockRequestMessage fileBlockRequest;
		private FilePart filepart;

		public StartDownload(UserInfo user) {
			this.user = user;
		}

		public void connect() throws UnknownHostException, IOException {
			socket = new Socket(InetAddress.getByName(user.getAddress()), user.getPorto());
			socket.setSoTimeout(TIMEOUT);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		}

		public void serve() throws IOException, ClassNotFoundException {
			while (!blockList.isEmpty() || contador!=fileSize) {
				fileBlockRequest = blockList.poll();
				synchronized(out){
				out.writeObject(fileBlockRequest);
				}
				Object o = in.readObject();
				filepart = (FilePart) o;
				user.addParteDescarregada(1);
				addToArray(filepart);
			}

		}

		public void run() {
			try {
				connect();
				serve();
			}catch(SocketTimeoutException e){
				blockList.offer(fileBlockRequest);
			}catch(SocketException e){
				blockList.offer(fileBlockRequest);
			}catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();

			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
