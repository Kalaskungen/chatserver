import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class ClientHandler implements Runnable{

	public Thread activity = new Thread(this);
	Socket socket;
	DataInputStream inStream;
	DataOutputStream outStream;
	
	public ClientHandler(Socket s) throws IOException{
		socket = s;
		inStream = new DataInputStream(socket.getInputStream());
		activity.start();
	}
	public void run() {
		
		while (true){
			try{
				String str = inStream.readUTF();
				Server.textArea.append(str);
				Server.sendToAll(str);
				
			}
			catch(IOException e){
				Server.clientList.remove(this);
				break;
			}
			
		}
		try {socket.close();}
		catch(IOException e){}
	}
	void sendMessage(String s){
		try {
			outStream = new DataOutputStream(socket.getOutputStream());
			outStream.writeUTF(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
