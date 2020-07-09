package main;

import java.io.IOException;

import javax.swing.SwingUtilities;

import user.IG;
import user.User;
import user.UserServer;

public class TheIscteBay {
	
	public static void createAndShowGUI(User user){
		IG ig=new IG();
		ig.start(user);
	}

	public static void main(String[] args) {
		try {
			User user = new User(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3]);
			user.connectToServer();
			SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				createAndShowGUI(user);
			}
		});
			new UserServer(user).startServing();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
