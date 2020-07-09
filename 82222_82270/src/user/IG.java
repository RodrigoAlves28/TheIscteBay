package user;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class IG {

	private JScrollPane scroll;
	private JFrame frame;
	private JTextField text;
	private JButton procurar, descarregar;
	private JProgressBar progresso;
	private User user;
	private JList<FileDetails> resultados;

	

	private void addFrameContent() {
		frame = new JFrame("TheISCTEBay");
		frame.setLayout(new BorderLayout());
		JPanel topo = new JPanel();
		topo.setLayout(new GridLayout(1, 2));
		text = new JTextField();
		text.setEditable(true);
		text.setPreferredSize(new Dimension(100, 20));
		procurar = new JButton("procurar");
		topo.add(new JLabel("Texto a procurar: "));
		topo.add(text);
		topo.add(procurar);

		JPanel right = new JPanel();
		right.setLayout(new GridLayout(2, 1));
		descarregar = new JButton("descarregar");
		progresso = new JProgressBar();
		right.add(descarregar);
		right.add(progresso);
		resultados = new JList<FileDetails>(user.getSearch());
		scroll = new JScrollPane(resultados);

		frame.add(right, BorderLayout.EAST);
		frame.add(topo, BorderLayout.NORTH);
		frame.add(scroll, BorderLayout.CENTER);

		frame.setVisible(true);
		
		resultados.addListSelectionListener(new ListSelectionListener(){
			private int previous=-1;
			public void valueChanged(ListSelectionEvent e){
				if (resultados.getSelectedIndex() != -1
						&& previous != resultados.getSelectedIndex()) {
					System.out.println(resultados.getSelectedValue()
							.getUsers());
				}
				previous = resultados.getSelectedIndex();
			}
		});

		procurar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				user.consultaUsers(text.getText());
				scroll.repaint();
				
			}
		});
		
		descarregar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				progresso.setValue(0);
				new Download(resultados.getSelectedValue(),user.getPasta(), progresso);
			}
		});
	}

	public User getUser() {
		return user;
	}

	public void start(User user) {
		this.user = user;
		addFrameContent();
		frame.setPreferredSize(new Dimension(400, 200));
		frame.pack();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}

}
