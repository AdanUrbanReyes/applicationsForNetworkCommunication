//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
public class Player{
	public Socket socket;
	public String name;
	public Score score;
	//public ObjectOutputStream oos;
	//public ObjectInputStream ois;
	public Player(Socket socket){
		this.socket=socket;
/*		try{
			oos=new ObjectOutputStream(this.socket.getOutputStream());
			ois=new ObjectInputStream(this.socket.getInputStream());
		}catch(Exception e){
			System.out.print("\nerror in create player "+e.getMessage());
		}*/
	}
}
