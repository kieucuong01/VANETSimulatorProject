package vanetsim.gui.controlpanels;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TrustAuthenticationPanel {

	private JFrame frame;
	private JButton btnInitalize;
	private JTextArea textArea;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrustAuthenticationPanel window = new TrustAuthenticationPanel();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TrustAuthenticationPanel() {
		initialize();
		//setActionForView();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnInitalize = new JButton("New button");

		btnInitalize.setBounds(171, 6, 117, 29);
		frame.getContentPane().add(btnInitalize);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(6, 43, 427, 229);
		//frame.getContentPane().add(textArea);
		
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setBounds(16, 264, 412, -213);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(scroll);

	}
	
	private void setActionForView() {
		btnInitalize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 textArea.setText("asd asd asd as das  asd asd asd asd asasd asd asd "
				 		+ " sadasd  asd asd asd asd asd asd asdasd asd"
				 		+ "a sd asd asd as dsd"
				 		+ "as"
				 		+ " dadasd"
				 		+ "a"
				 		+ "s d"
				 		+ "a sd"
				 		+ "a sdas d"
				 		+ ""
				 		+ "as d"
				 		+ "asd"
				 		+ "as"
				 		+ "d "
				 		+ "asdd"
				 		+ " asd asd"
				 		+ ""
				 		+ ""
				 		+ "as d");
			}
		});
	}
}
