package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Window extends JPanel implements Runnable {
	
	static JFrame frame;
	static private Users users;
	static boolean stop = false;
	static final int WIDTH = 300;
	static final int HEIGHT = 300;
	
	public Window (JFrame frame, Users users) {
		this.frame = frame;
		this.users = users;
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
        final String[] names = {"prova1", "prova2"};
        JComboBox userCB = new JComboBox(names);
        JPanel visual = this;
        title.setBounds((WIDTH/2-100), 10, (WIDTH/2), 20);
        userList.setBounds(0, 20, 100, 100);
        userCB.setBounds(100, 20, 100, 20);
        panel.add(title);
        panel.add(userList);
        panel.add(userCB);
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
