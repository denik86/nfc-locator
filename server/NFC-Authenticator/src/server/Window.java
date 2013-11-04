package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class Window extends JPanel implements Runnable {
	
	JFrame frame;
	
	private UsersDB users;
	private JLabel title;
	private JLabel textUsers;
	private JButton start;
	private JButton addUser;
	private JButton remUser;
	private JButton addAuth;
	private JButton remAuth;
	private JList userList;
	private JList authList;
	private JScrollPane scrollList;
	
	private int port;
	static final int WIDTH = 800;
	static final int HEIGHT = 400;
	
	private boolean isStart;
	static private Listener ls;
	static private Thread listener;
	
	public Window (JFrame frame, UsersDB users, int port) {
		this.frame = frame;
		this.users = users;
		this.port = port;
		isStart = false;
	}

	
	public void run () {
		frame.setSize(WIDTH, HEIGHT);
        JPanel panel = (JPanel) frame.getContentPane();
        panel.setLayout(null);
        frame.setResizable(false);
        
        title = new JLabel("<html><h3>NFC Locator Server</h3></html>");
        title.setBounds(10, 10, (WIDTH/2), 20);
        
        textUsers = new JLabel("<html><i> Users: </i></html> ");
        textUsers.setBounds(10, 50, 70, 20);
        
        start = new JButton("Start Server");
        start.setBounds(150, 70, 120, 20);

        addUser = new JButton("+");
        addUser.setBounds(10,280, 45, 20);
        
        remUser = new JButton("-");
        remUser.setBounds(65,280, 45, 20);
        
        addAuth = new JButton("Authorizations");
        addAuth.setBounds(150, 180, 100, 20);
  
      
		userList = new JList(users.getUsersName());
        userList.setFixedCellWidth(20);
        userList.setFixedCellHeight(20);
        userList.setSelectedIndex(0);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollList = new JScrollPane(userList);
        scrollList.setBounds(10, 70, 100, 200);
        
        authList = new JList(users.getUsersName());
        authList.setFixedCellWidth(20);
        authList.setFixedCellHeight(20);
        authList.setSelectedIndex(0);
        authList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        authList.setBounds(10, 70, 100, 200);
        scrollList = new JScrollPane(userList);
        scrollList.setBounds(10, 70, 100, 200);

        panel.add(title);
        panel.add(textUsers);
        panel.add(new JScrollPane(userList));
        panel.add(start);
        panel.add(addUser);
        panel.add(remUser);
        panel.add(addAuth);
        

        frame.setLocationRelativeTo(null);
        
        start.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		if(!isStart)
        		{
        			ls = new Listener(port);
        			listener = new Thread(ls);
        			listener.start();
        			start.setText("Stop Server");
        			isStart = true;
        		}
        		else
        		{
        			ls.stop();
        			start.setText("Start Server");
        			isStart = false;
        		}
        	}
        });
        
        addUser.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		
        		System.out.println("DEBUG -- Aperta AddUser");
        		JFrame add = new JFrame("Insert a new User");
        		WindowAddUser wau = new WindowAddUser(add);
        		
    	        frame.addWindowListener(new WindowAdapter() {
    		        @Override
    		        public void windowClosing(WindowEvent e) {
    		       
    		                frame.setVisible(false);
    		                System.exit(0);
    		        }
    		        });
        	}
        		
        });
        
        remUser.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		String userRemoved = users.removeUser((String) userList.getSelectedValue());
        		userList.setListData(users.getUsersName());
        		System.out.println("-- Account '" + userRemoved + "' REMOVED!");
        	}
        });
        
        addAuth.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		JFrame newAuth = new JFrame("View and Edit Authorizations");
        		WindowAddAuth waa = new WindowAddAuth(newAuth, (String)userList.getSelectedValue());
        	}
        });
        
        frame.setVisible(true);
	}
	
	class WindowAddUser extends JFrame {
		
		private JFrame frame;
		public WindowAddUser (JFrame fram) {

			this.frame = fram;
			frame.setSize(330, 120);
			frame.setResizable(false);
	        JPanel panel = (JPanel) frame.getContentPane();
	        panel.setLayout(null);
	        JLabel labelUsername = new JLabel("Username:");
	        final JTextField textUsername = new JTextField();
	        JLabel labelPassword = new JLabel("Password:");
	        final JTextField textPassword = new JTextField();
	        final JButton addButton = new JButton("Add");
	        
	        labelUsername.setBounds(10, 10, 100, 20);
	        textUsername.setBounds(90, 10, 200, 20);
	        labelPassword.setBounds(10, 30, 100, 20);
	        textPassword.setBounds(90, 30, 200, 20);
	        addButton.setBounds((frame.getSize().width / 2) - 30, 60 , 60, 20);

	        panel.add(labelUsername);
	        panel.add(textUsername);
	        panel.add(labelPassword);
	        panel.add(textPassword);
	        panel.add(addButton);
	        
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	        frame.setEnabled(true);
	        
	        addButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e)
	        	{
	        		users.addUser(textUsername.getText(), textPassword.getText());
	        		userList.setListData(users.getUsersName());
	        		
	        		frame.setEnabled(false);
	        		frame.setVisible(false);
	        		System.out.println("-- Account '" + textUsername.getText() + "' ADDED!");
	        	}
	        });
	        
	        
		}
	}
	
	class WindowAddAuth extends JFrame {
		
		private JFrame frame;
		public WindowAddAuth (JFrame fram, final String username) {

			this.frame = fram;
			frame.setSize(330, 120);
			frame.setResizable(false);
	        JPanel panel = (JPanel) frame.getContentPane();
	        panel.setLayout(null);
	        JLabel labelAuth = new JLabel("Authorization for " + username +":");
	        final JTextField textAuth = new JTextField();
	        final JButton addButton = new JButton("Add");
	        
	        labelAuth.setBounds(10, 20, 100, 20);
	        textAuth.setBounds(90, 50, 200, 20);
	        addButton.setBounds((frame.getSize().width / 2) - 30, 60 , 60, 20);

	        panel.add(labelAuth);
	        panel.add(textAuth);
	        panel.add(addButton);
	        
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	        frame.setEnabled(true);
	        
	        addButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e)
	        	{
	        		users.addAuth(textAuth.getText(), username);

	        		frame.setEnabled(false);
	        		frame.setVisible(false);
	        		System.out.println("-- Authorization '" + textAuth.getText() + "' ADDED at user " + username);
	        	}
	        });
	        
	        
		}
	}

	
}
