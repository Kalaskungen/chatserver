import java.awt.BorderLayout;
import java.io.IOException;
import javax.swing.*;
import java.net.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Server extends JFrame{

	public static Server s;
	static JTextArea textArea = new JTextArea();
	JScrollPane scrollPane = new JScrollPane(textArea);
	static ArrayList<ClientHandler> clientList = new ArrayList<ClientHandler>(); 
	
	
	public Server() throws IOException{
		setTitle("Server");
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		add(textArea, BorderLayout.CENTER);
		add(scrollPane,	BorderLayout.NORTH);
		setSize(500,400);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public static void main(String[] args) throws IOException{
		s = new Server();
		
		ServerSocket listenerSocket = new ServerSocket(1337);
		while(true){
			Socket clientSocket = listenerSocket.accept();
			textArea.append(clientSocket.getInetAddress().getHostName()+" connected\n");
			clientList.add(new ClientHandler(clientSocket));

		}
	}
	public static void  sendToAll(String s){
		for(ClientHandler c : clientList){
			c.sendMessage(s);
			//textArea.append(c.toString());
		}
		
	}


}
