import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

/**
 * @author Kais
 *
 */
public class UI {
	private static JFrame frameCC;
	private static JButton login;
	private static JButton exit;
	private static JTextArea username;
	private static JTextArea password;
	private static String user;
	private static String pass;
	private static String usertype;
	private static boolean authenticated;
	private static int count = 0;

	/**
	 * @param args
	 * @throws IOException 
	 */
	//handles the creation of the JFrame and
    //all it's components
    private static void createLoginFrame() throws IOException
    {
        
        JFrame guiFrame = new JFrame();
        
      //This will center the JFrame in the middle of the screen
        guiFrame.setLocationRelativeTo(null);
        guiFrame.setVisible(true);

        JPanel userPanel = new JPanel();
        userPanel.setLayout(new GridLayout(2,2));
        
        JLabel usernameLbl = new JLabel("Username:");
        JLabel passwordLbl = new JLabel("Password:");
        JTextField username = new JTextField();
        JPasswordField passwordFld = new JPasswordField();
        
        userPanel.add(usernameLbl);
        userPanel.add(username);
        userPanel.add(passwordLbl);
        userPanel.add(passwordFld);
        
        //As the JOptionPane accepts an object as the message
        //it allows us to use any component we like - in this case 
        //a JPanel containing the dialog components we want
        JOptionPane.showConfirmDialog(guiFrame, userPanel, "Enter your password:"
                            ,JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        pass = new String(passwordFld.getPassword());
        user = username.getText();
        
        authenticated = authenticate(user, pass);
        
        System.out.println(user);
        System.out.println(pass);
        
        if (authenticated) {
        	System.out.println("The user has been authenticated");
        	System.out.println("The user is a(n): " + usertype);
            guiFrame.dispose();
            
            createUserFrame();
        }
        else {
        	count++;
        	if(count != 5) {
        		System.out.println("You have failed to authenticate");
        		System.out.println("You have " + (5-count) + " trie(s) remaining, after that you will be locked out");
        		guiFrame.dispose();
        		createLoginFrame();
        	}
        	else {
        		System.out.println("You have failed to authenticate 5 times, please contact the system admin for help, you will no longer be able to login from this IP");
        		guiFrame.dispose();
        	}
        	
        }
    }
    
    private static void createUserFrame() throws FileNotFoundException, IOException {
    	JFrame guiFrame = new JFrame();
    	JButton newApp = new JButton("New Application");
    	JButton viewApp = new JButton("View Current Application");
    	JButton editApp = new JButton("Edit Application");
    	JButton submitApp = new JButton("Submit Application");
    	
    	

        
    	ArrayList<Object> application = ApplicationDB.getApplicationbyName(user);
    	
    	System.out.println(application);
    	
    	if (application == null) {
    		Container pane = guiFrame.getContentPane();
    		pane.setLayout(new GridLayout(1,4));
    		pane.add(newApp);
    		pane.add(viewApp);
    		pane.add(editApp);
    		pane.add(submitApp);
		
    		guiFrame.setSize(1000, 100);
    		//This will center the JFrame in the middle of the screen
    		guiFrame.setLocationRelativeTo(null);
    		guiFrame.setVisible(true);
    	}
    	else {
    		Container pane = guiFrame.getContentPane();
    		pane.setLayout(new GridLayout(1,4));
    		pane.add(newApp);
    		pane.add(viewApp);
    		pane.add(editApp);
    		pane.add(submitApp);
		
    		guiFrame.setSize(1000, 100);
    		//This will center the JFrame in the middle of the screen
    		guiFrame.setLocationRelativeTo(null);
    		guiFrame.setVisible(true);
    		newApp.setEnabled(false);
    		if(!application.get(1).equals(ApplicationState.OPEN.toString())) {
    			submitApp.setEnabled(false);
    			editApp.setEnabled(false);
    		}
    	}
    }

    private static boolean authenticate(String username, String password) throws IOException {
    	BufferedReader br = new BufferedReader(new FileReader((System.getProperty("user.dir") + "\\Database\\Users.txt")));

    	String line;
    	String user = null;
    	String pass = null;
    	int index = 0;

    	while ((line = br.readLine()) != null) {
    		while(true) {
    			for(int i = 0; i < line.length(); i++) {
    				if(line.charAt(i) == '$') {
    					user = line.substring(0, i);
    					index = i+1;
    					break;
    				}
    			}
    			if(!user.equals(username))
    				break;
    			for(int i = index; i <line.length(); i++) {
    				if(line.charAt(i) == '$') {
    					pass = line.substring(index, i);
    					index = i+1;
    					break;
    				}
    			}
    			if(!pass.equals(password))
    				break;
    			usertype = line.substring(index, line.length());
    			return true;
    		}
    	}
    	return false;
    }

	public static void main(String[] args) {
		frameCC = new JFrame("Hello, welcome to Carleton Central");
		login = new JButton("Login");
		exit = new JButton("Exit");

		//This will center the JFrame in the middle of the screen
		frameCC.setLocationRelativeTo(null);
		frameCC.setVisible(true);
		
		login.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				frameCC.dispose();
				try {
					createLoginFrame();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		exit.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		Container pane = frameCC.getContentPane();
		pane.setLayout(new GridLayout(1,2));
		pane.add(login);
		pane.add(exit);
		
		frameCC.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameCC.setSize(300, 200);
		frameCC.setVisible(true);
	}

}