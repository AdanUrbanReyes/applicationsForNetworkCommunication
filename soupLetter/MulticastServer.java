import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.InetAddress;
public class MulticastServer {
	private MulticastSocket socket;
	private int portAnnounce=4000,port = 5000;
	private String ip = "230.1.1.1";
	private boolean isAnnounce=true;
   private Runnable announce=new Runnable(){
	 public void run(){
		 byte []buffer=new byte[5];
		 String message=""+portAnnounce;
		 try{
			DatagramPacket packet=new DatagramPacket(buffer,buffer.length,InetAddress.getByName(ip),port);
			packet.setData(message.getBytes());
			packet.setLength(message.length());
			while(isAnnounce){
			   socket.send(packet);
			   Thread.sleep(1000);
			}
		 }catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,"error in announce "+e.getMessage());
		 }
	 }  
   };
	public MulticastServer(int port, String ip) {
		this.port = port;
		this.ip = ip;
		startMulticastSocket(7);
	}
	private boolean startMulticastSocket(int ttl) {
		try {
			socket = new MulticastSocket(port);
			socket.setReuseAddress(true);
			socket.setTimeToLive(ttl);
			socket.joinGroup(InetAddress.getByName(ip));
			System.out.print("\nserver multicast running in port " + port + " joined group " + ip);
			return true;
		} catch (Exception e) {
			javax.swing.JOptionPane.showMessageDialog(null, "error starting server multicast " + e.toString());
		}
		return false;
	}

	public void startAnnounce(int port) {//received ip and port to announce
		portAnnounce=port;
		isAnnounce=true;
 		Thread thread = new Thread(announce);
		thread.start();
	}
	public void stopAnnounce(){
		isAnnounce=false;
	}
	public String freeServer(){
		String message,freeServer = "any server is free";
		byte []buffer=new byte[5];
		DatagramPacket packet=new DatagramPacket(buffer,buffer.length);
		int second=0;
		try{
			socket.receive(packet);//recivo the pakcet
			message=new String(packet.getData()).trim();//obtengo el mensaje
			freeServer=message+":"+packet.getAddress().getHostAddress();
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null, "error in listen server availables " + e.toString());
		}
		return freeServer;
	}
}
