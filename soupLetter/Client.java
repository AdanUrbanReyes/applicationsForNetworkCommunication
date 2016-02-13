import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.InetAddress;
import javax.swing.JPanel;
public class Client {
	private Socket socket;
	private int port=4000;
	private String ip="127.0.0.1",name;
	
	private javax.swing.JFrame window;
	private javax.swing.JTextArea messages;
	private javax.swing.JTabbedPane gameContent;
	
	public Client(int port,String ip,String name){
		this.port=port;
		this.ip=ip;
		this.name=name;
		startSocket();
		messages=new javax.swing.JTextArea();
		messages.setEditable(false);//for that not can edit text area 
		messages.setFont(new java.awt.Font("Serif",java.awt.Font.PLAIN,24));
		gameContent=new javax.swing.JTabbedPane();
		startFrame();
	}
	public void closeConnection(){
		try{
			socket.close();
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,"error in close connection "+e.toString());
		}
	}
	public void startFrame(){
		javax.swing.JScrollPane scroll;
		window=new javax.swing.JFrame("soup letter");
		window.setLayout(null);
		window.setSize(900,930);
		window.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);		
		scroll=new javax.swing.JScrollPane(messages);
		scroll.setBounds(0,0,900,200);
		window.add(scroll);
		gameContent.setBounds(0,200,900,700);
		window.add(gameContent);
		window.setVisible(true);
	}
	public boolean startGame(Soup game){
		Score score;
		messages.setText("the game start\nonly have "+game.timeToGame+" seconds\ncome on!!!");
		gameContent.removeAll();
		gameContent.add(game);
		game.putBoardBeautifull();
		game.startTimer();
		try{// espero a que el juego termine y cuando termine send score
			while((score=game.score())==null)
				Thread.sleep(1000);
			sendObject(score);//send score
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,"error geting score "+e.toString());
		}
		messages.setText(String.valueOf(receiveObject()));//recivo quien gano
		if(javax.swing.JOptionPane.showInputDialog(null,"would you like other game? yes/not").charAt(0)=='y'){//ask if like stay or finish game
			sendObject("stay");
			return true;
		}
		//se retira del juego
		sendObject("leave");
		window.dispose();
		closeConnection();
		return false;
	}
	public void sendObject(Object object){
		try{
			ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(object);
			oos.flush();
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,"error sending object "+e.toString());
		}
	}
	public Object receiveObject(){
		Object object=null;
		try{
			ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
			object=ois.readObject();
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,"error receiving object "+e.toString());
		}
		return object;
	}	
	public void waitingGame(){
		Object object;
		boolean flat=true;
		try{
			while(flat){
				//ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
				//object=ois.readObject();
				object=receiveObject();
				if(object instanceof String){
					messages.setText(String.valueOf(object));
				}else{
					if(object instanceof Soup){
						flat=startGame((Soup)object);
					}
				}
				Thread.sleep(70);
			}
		}catch(Exception e){
		   javax.swing.JOptionPane.showMessageDialog(null,"error waiting window \n"+e.toString());
		}
	}
	public boolean wasAccepted(){
		try{
			ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
			Object object=ois.readObject();
			if(object instanceof String){
				String response=String.valueOf(object);
				if(response.startsWith("accepted")){
					messages.setText(response);
					sendObject(name);
					return true;
				}else{
					messages.setText(response);
					return false;
				}
			}
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,"error question was accepted "+e.toString());
		}
		return false;
	}
	private boolean startSocket(){
		try{
			socket=new Socket(InetAddress.getByName(ip),port);
			System.out.print("\nsocket client wake up in port: "+port+" ip: "+ip);
			return true;
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,"error starting socket client");
		}
		return false;
	}
	public static void main(String []args){
		//int port=Integer.parseInt(javax.swing.JOptionPane.showInputDialog(null,"enter port server"));
		//String ip=javax.swing.JOptionPane.showInputDialog(null,"enter ip server");
		String name=javax.swing.JOptionPane.showInputDialog(null,"what is your name?");
		//Client c=new Client(port,ip,name);
		Client c=new Client(4000,"127.0.0.1",name);
		if(c.wasAccepted()){
			c.waitingGame();
		}
	}	
}
