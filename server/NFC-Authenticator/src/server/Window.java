package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Window extends JPanel implements Runnable {
	
	static JFrame frame;
	static private Users users;
	static private int port;
	static boolean stop = false;
	static final int WIDTH = 300;
	static final int HEIGHT = 300;
	private boolean isStart;
	static private Listener ls;
	static private Thread listener;
	
	public Window (JFrame frame, Users users, int port) {
		this.frame = frame;
		this.users = users;
		this.port = port;
		isStart = false;
	}
	
	public void stop () {
		stop = true;
	}
	
	public void run () {
		frame.setSize(WIDTH, HEIGHT);
        JPanel panel = (JPanel) frame.getContentPane();
        panel.setLayout(null);
        JLabel title = new JLabel("<html>NFC Locator Server</html>");
        JLabel userList = new JLabel();
        final String[] names = users.getAccounts();
        JComboBox userCB = new JComboBox(names);
        final JButton startServer = new JButton("Start");
        JPanel visual = this;
        title.setBounds((WIDTH/2-100), 10, (WIDTH/2), 20);
        userList.setBounds(0, 20, 100, 100);
        userCB.setBounds(20, 50, 100, 20);
        startServer.setBounds(150, 50, 100, 20);
        panel.add(title);
        panel.add(userList);
        panel.add(userCB);
        panel.add(startServer);
        frame.setLocationRelativeTo(null);
        
        userCB.addActionListener(new ActionListener()
        {
                public void actionPerformed(ActionEvent e) {
                        JComboBox cb = (JComboBox)e.getSource();
                        String selected = (String)cb.getSelectedItem();
                        ArrayList<String> auth = users.getAuth(selected);
                        int result = 0;
                        for (int i=0; i<names.length; i++) {
                                if (selected.equals(names[i])) {
                                        result = i;
                                        break;
                                }
                        }
                        
                }
        });
        
        startServer.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		if(!isStart)
        		{
        			ls = new Listener(port);
        			listener = new Thread(ls);
        			listener.start();
        			startServer.setText("Stop");
        			isStart = true;
        		}
        		else
        		{
        			ls.stop();
        			startServer.setText("Start");
        			isStart = false;
        		}
        	}
        });
        

        
        // update user list
        /*String[] userAccounts = users.getAccounts();
        String userString = "<html>Users:<br>";
        for(int i=0; i < userAccounts.length; i++) {
        	userString += userAccounts[i] + "<br>";
        }
        userString += "</html>";
        userList.setText(userString);*/
        
        frame.setVisible(true);
		while (!stop) {
			// aggiorna la finestra
		}
	}

}
