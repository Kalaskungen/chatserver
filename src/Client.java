import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;

@SuppressWarnings("serial")
public class Client extends JFrame implements KeyListener, Runnable {
	private boolean connected = false;
	public String name = "Unknown";
	public Client c;
	public Thread activity = new Thread(this);
	JTextArea textArea = new JTextArea();
	JScrollPane scrollPane = new JScrollPane(textArea);
	JTextField textField = new JTextField();
	static Socket socket;
	DataInputStream inStream;// =

	public Client() throws IOException {
		setTitle("Client");
		textArea.setEditable(false);
		add(textArea, BorderLayout.CENTER);
		add(scrollPane, BorderLayout.NORTH);
		add(textField, BorderLayout.SOUTH);
		setSize(500, 400);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		textField.addKeyListener(this);

	}

	private void sendMessage(String message) throws UnknownHostException,
			IOException {
		DataOutputStream outStream = new DataOutputStream(
				socket.getOutputStream());
		outStream.writeUTF(message + "\n");

	}

	private boolean checkCommand(String input, String regex) {

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}

	}

	private void commandHandler(String input) throws UnknownHostException,
			IOException {
		if (checkCommand(input, "/connect .*")) {
			String tmpAddress = input.split("(/connect )")[1];
			connectToServer(tmpAddress);
		} else if (checkCommand(input, "/disconnect")) {

		} else if (checkCommand(input, "/name \\b\\w*\\b")) {
			String tmpName = name;
			name = input.split("(/name )")[1];
			if(connected)
				sendMessage(tmpName+" is now known as: "+name);
			else
			textArea.append("You are now known as: "+name+"\n");
		} else {
			if (connected)
				sendMessage(name+"\n  " + input);
			else
				textArea.append("Not connected to a server\n");
		}
	}

	private void connectToServer(String address) throws UnknownHostException,
			IOException {
		socket = new Socket(address, 1337);
		inStream = new DataInputStream(socket.getInputStream());
		activity.start();
		connected = true;
		sendMessage(name+" has connected");
	}

	public static void main(String[] args) throws IOException {
		new Client();

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		//
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			try {
				commandHandler(textField.getText());
				textField.setText("");
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		try {
			inStream = new DataInputStream(socket.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (true) {
			try {
				String str = inStream.readUTF();
				textArea.append(str);

			} catch (IOException e) {
				break;
			}
		}
		try {
			socket.close();
		} catch (IOException e) {
		}

	}

}
