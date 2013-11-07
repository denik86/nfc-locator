package server;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class Window extends JPanel implements Runnable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JFrame frame;
	
	private UsersDB users;
	private JLabel title;
	private JLabel textUsers;
	private JLabel textAuths;
	private JLabel textAuthsUser;
	private JButton start;
	private JButton addUser;
	private JButton remUser;
	private JButton addAuth;
	private JButton remAuth;
	private JButton addAuthUser;
	private JButton remAuthUser;
	private JList userList;
	private JList authUserList;
	private JList authList;
	private JScrollPane scrollUsers;
	private JScrollPane scrollAuthUser;
	private JScrollPane scrollAuth;
	
	private String qrCodeText;
	
	
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
       
        textAuths = new JLabel("<html><i> Authorizations: </i></html> ");
        textAuths.setBounds(500, 50, 120, 20);
        
        start = new JButton("Start Server");
        start.setBounds(200, 12, 120, 20);

        addUser = new JButton("+");
        addUser.setBounds(10,280, 45, 20);
        
        remUser = new JButton("-");
        remUser.setBounds(65,280, 45, 20);
        
        addAuthUser = new JButton("<html><h5>Add Auth</h5></html>");
        addAuthUser.setBounds(10,310, 100, 20);
        addAuthUser.setMargin(new Insets(1,1,1,1));
        
        remAuthUser = new JButton("<html><h5>Remove Auth</h5></html>");
        remAuthUser.setBounds(150,280, 100, 20);
        remAuthUser.setMargin(new Insets(1,1,1,1));
        
        addAuth = new JButton("+");
        addAuth.setBounds(500, 280, 45, 20);
        addAuth.setMargin(new Insets(1,1,1,1));
        
        remAuth = new JButton("-");
        remAuth.setBounds(555, 280, 45, 20);
        remAuth.setMargin(new Insets(1,1,1,1));
  
      
		userList = new JList(users.getUsers());
        userList.setFixedCellWidth(20);
        userList.setFixedCellHeight(20);
        userList.setSelectedIndex(0);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollUsers = new JScrollPane(userList);
        scrollUsers.setBounds(10, 70, 100, 200);
        
        
        authUserList = new JList(users.getAuthsUsers((String) userList.getSelectedValue()));
        authUserList.setFixedCellWidth(20);
        authUserList.setFixedCellHeight(20);
        authUserList.setSelectedIndex(0);
        authUserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollAuthUser = new JScrollPane(authUserList);
        scrollAuthUser.setBounds(150, 70, 100, 200);
 
        
        authList = new JList(users.getAuths());
        authList.setFixedCellWidth(20);
        authList.setFixedCellHeight(20);
        authList.setSelectedIndex(0);
        authList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollAuth = new JScrollPane(authList);
        scrollAuth.setBounds(500, 70, 100, 200);
        
        if(userList.getSelectedValue() == null)
        	 textAuthsUser = new JLabel ("<html><i><h5>  No User Selected </h5></i></html> ");
        else
        	 textAuthsUser = new JLabel ("<html><i><h5> Authorizations of user : <br> <font color=red>"  +  userList.getSelectedValue() + "</font> </h5></i></html> ");
        textAuthsUser.setBounds(150, 40, 200, 30);

        
        QRCodeWindow qr = new QRCodeWindow();
        qr.setBounds(300, 100, 500, 500);


        
        panel.add(title);
        panel.add(textUsers);
        panel.add(textAuths);
        panel.add(textAuthsUser);
        panel.add(scrollAuth);
        panel.add(scrollAuthUser);
        panel.add(scrollUsers);
        panel.add(start);
        panel.add(addUser);
        panel.add(remUser);
        panel.add(addAuthUser);
        panel.add(remAuthUser);
        panel.add(addAuth);
        panel.add(remAuth);    
        panel.add(qr);

        frame.setLocationRelativeTo(null);
        
        userList.addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent e) {
        		
        		String username = (String) userList.getSelectedValue();
        		textAuthsUser.setText("<html><i><h5> Authorizations of user :<br> <font color=red>"  +  userList.getSelectedValue() + "</font></h5></i></html> ");
        		authUserList.setListData(users.getAuthsUsers(username));
        	}
        });

        start.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		if(!isStart)
        		{
        			ls = new Listener(port, users);
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
        		JFrame add = new JFrame("Insert a new User");
        		new WindowAddUser(add);
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
        		if(userList.getSelectedValue() == null)
        			JOptionPane.showMessageDialog(frame, "No user selected");
        		else
        		{
	        		String userRemoved = users.removeUser((String) userList.getSelectedValue());
	        		userList.setListData(users.getUsers());
	        		System.out.println("-- Account '" + userRemoved + "' REMOVED!");
	        		userList.setSelectedIndex(userList.getLastVisibleIndex());
	        		if(userList.getSelectedValue() == null)
	        			textAuthsUser.setText("<html><i><h5>  No User Selected </h5></i></html> ");
	        			
        		}
        	}
        });
        
        addAuth.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		JFrame newAuth = new JFrame("Add an Authorization");
        		new WindowAddAuth(newAuth);
        	}
        });
        
        remAuth.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		if(users.getAuths().length == 0)
        			JOptionPane.showMessageDialog(frame, "There are no authorizations");
        		else if(authList.getSelectedValue() == null)
        			JOptionPane.showMessageDialog(frame, "No authorization selected");
        		else if(users.getUsersAuth((String) authList.getSelectedValue()).length == 0 || 
        				JOptionPane.showConfirmDialog(null, "There are several users which use this authorization. \n Are you sure to cancel this authorization?", 
        						"Cancel " + authList.getSelectedValue(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
	        	{
		        	users.removeAuth((String) authList.getSelectedValue());
		        	authList.setListData(users.getAuths());
		        	authUserList.setListData(users.getAuthsUsers((String)userList.getSelectedValue()));
	        	}
        	}
        });
        
        addAuthUser.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		if(userList.getSelectedValue() == null)
        			JOptionPane.showMessageDialog(frame, "No user selected");
        		else if(users.getAuths().length == 0 || users.getAuthsNOUser((String) userList.getSelectedValue()).length == 0)
        			JOptionPane.showMessageDialog(frame, "There are no authorizations");
        		else
        		{
	        		JFrame addAuthUser = new JFrame("Add an Authorization to user" + userList.getSelectedValue());
	        		new WindowAddAuthUser(addAuthUser, (String)userList.getSelectedValue());
        		}
        	}
        }); 
        
        remAuthUser.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		if(authUserList.getSelectedValue() == null)
        			JOptionPane.showMessageDialog(frame, "No authorization selected");
        		else
        		{
	        		users.remAuthToUser((String)userList.getSelectedValue(), (String) authUserList.getSelectedValue());
	        		authUserList.setListData(users.getAuthsUsers((String)userList.getSelectedValue()));
        		}
        	}
        });
        
        frame.setVisible(true);
	}
	
	class WindowAddUser extends JFrame {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
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
	        JLabel labelNotice = new JLabel("<html><h5><font color = red>Username must be long max 10 characters</font></h5></html>");
	        final JButton addButton = new JButton("Ok");
	        final JButton cancelButton = new JButton("Cancel");
	        
	        labelUsername.setBounds(10, 10, 100, 20);
	        textUsername.setBounds(90, 10, 200, 20);
	        labelPassword.setBounds(10, 60, 100, 20);
	        textPassword.setBounds(90, 60, 200, 20);
	        labelNotice.setBounds(10, 30, 300, 20);
	        addButton.setBounds(50, 90 , 100, 20);
	        cancelButton.setBounds(160, 90 , 100, 20);

	        panel.add(labelUsername);
	        panel.add(textUsername);
	        panel.add(labelPassword);
	        panel.add(textPassword);
	        panel.add(addButton);
	        panel.add(cancelButton);
	        panel.add(labelNotice);
	        
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	        frame.setEnabled(true);
	        
	        addButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e)
	        	{
	        		if(textUsername.getText().equals(""))
	        			JOptionPane.showMessageDialog(frame, "Username field is empty");
	        		else if(textPassword.getText().equals(""))
        				JOptionPane.showMessageDialog(frame, "Password field is empty");
	        		else if(textUsername.getText().length() > 10)
        				JOptionPane.showMessageDialog(frame, "Username is longer than 10 characters");
	        		else
	        		{
	        			users.addUser(textUsername.getText(), textPassword.getText());
	        			userList.setListData(users.getUsers());
		        		frame.setEnabled(false);
		        		frame.setVisible(false);
		        		userList.setSelectedIndex(userList.getLastVisibleIndex());

	        		}
	        	}
	        });
	        
	        cancelButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e)
	        	{
	        		frame.setEnabled(false);
	        		frame.setVisible(false);
	        	}
	        });
	        
	        
		}
	}
	
	class WindowAddAuth extends JFrame {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JFrame frame;
		public WindowAddAuth (JFrame fram) {

			this.frame = fram;
			frame.setSize(330, 120);
			frame.setResizable(false);
	        JPanel panel = (JPanel) frame.getContentPane();
	        panel.setLayout(null);
	        JLabel labelResource = new JLabel("Resource: ");
	        JLabel labelAddress = new JLabel("Address: ");
	        final JTextField textResource = new JTextField();
	        final JTextField textAddress = new JTextField();
	        final JButton addButton = new JButton("Add");
	        final JButton cancelButton = new JButton("Cancel");
	        
	        labelResource.setBounds(10, 10, 100, 20);
	        textResource.setBounds(90, 10, 200, 20);
	        labelAddress.setBounds(10, 30, 100, 20);
	        textAddress.setBounds(90, 30, 200, 20);
	        addButton.setBounds(50, 60 , 100, 20);
	        cancelButton.setBounds(160, 60 , 100, 20);

	        panel.add(labelResource);
	        panel.add(textResource);
	        panel.add(labelAddress);
	        panel.add(textAddress);
	        panel.add(addButton);
	        panel.add(cancelButton);
	        
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	        frame.setEnabled(true);
	        
	        addButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e)
	        	{
	        		if(textResource.getText().equals(""))
	        			JOptionPane.showMessageDialog(frame, "Resource field is empty");
	        		else if(textAddress.getText().equals(""))
        				JOptionPane.showMessageDialog(frame, "Address field is empty");
	        		else
	        		{
		        		users.addAuth(textResource.getText(), textAddress.getText());
		        		authList.setListData(users.getAuths());
		        		
		        		frame.setEnabled(false);
		        		frame.setVisible(false);
		        		System.out.println("-- Authorization '" + textResource.getText() + " ADDED");
		        		
	        		}
	        	}
	     
	        });
	        
	        cancelButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e)
	        	{
	        		frame.setEnabled(false);
	        		frame.setVisible(false);
	        	}
	        });

		}
	}
	
	class WindowAddAuthUser extends JFrame {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JFrame frame;
		public WindowAddAuthUser (JFrame fram, final String username) {

			final JComboBox auths = new JComboBox(users.getAuthsNOUser(username));
			auths.setBounds(10, 50, 150, 20);
			this.frame = fram;
			frame.setSize(300, 110);
			frame.setResizable(false);
	        JPanel panel = (JPanel) frame.getContentPane();
	        panel.setLayout(null);
	        JLabel labelAuth = new JLabel("<html>Authorization for <font color = red>" + username +"</font>:<html>");
	        final JButton addButton = new JButton("Add");
	        
	        labelAuth.setBounds(10, 20, 300, 20);
	       
	        addButton.setBounds(200, 50 , 70, 20);

	        panel.add(labelAuth);
	      
	        panel.add(addButton);
	        panel.add(auths);
	        
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	        frame.setEnabled(true);
	        
	        addButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e)
	        	{

	        		frame.setEnabled(false);
	        		frame.setVisible(false);
	        		users.addAuthToUser(username, (String) auths.getSelectedItem());
	        		authUserList.setListData(users.getAuthsUsers(username));
	        		System.out.println("-- Authorization '" + auths.getSelectedItem() + "' ADDED at user " + username);
	        	}
	        });

		}
	}

	class Alert extends JFrame
	{
		private static final long serialVersionUID = 1L;
		JFrame frame=new JFrame("Message box appear in JFrame");
		public Alert(String message)
		{
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(400,400);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			
		}
	}

}
