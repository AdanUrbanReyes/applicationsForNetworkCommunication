import javax.swing.JFileChooser;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.util.LinkedList;
public class StreamServer {
	private static final int maximumPlayers=2,timeToGame=60;//timeToGame is the senconds have players for finished game
	private ServerSocket socket;
	private LinkedList <Player> players;
	private File []file;//files where estaran words for the soup game
	private int port=4000,indexFile,playersConnected=0;
	private MulticastServer ms;
	private Runnable atiende=new Runnable(){
		public void run(){
			sendGame();
			receiveWordsFound();
			whosWon();
			playerStaysOrLeave();		
		}
	};
	public StreamServer(int port,MulticastServer ms){
		this.ms=ms;
		this.port=port;
		selectFiles();
		startServerSocket();
	}
	public void atiende(){
		Thread thread=new Thread(atiende);
		thread.start();
	}
	public void startServerSocket(){
		try{
			socket=new ServerSocket(port);
			socket.setReuseAddress(true);
			System.out.print("\nserver socket running in port "+port);
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,"error startarting server socket \n"+e.getMessage());
		}
	}
	public void sendObject(Socket client,Object object){
		try{
			ObjectOutputStream oos=new ObjectOutputStream(client.getOutputStream());
			oos.writeObject(object);
			oos.flush();
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,"error sending object "+e.toString());
		}
	}
	public void sendClientsConnected(int playersConnected,int forConnected){//recieve number of players connected and faltantes
		int i,size=players.size();
		String message="accepted\n"+playersConnected+"players connected\nwaiting for "+forConnected+" players";
		System.out.print("\nsize of players in send client connected ="+size);
		for(i=0;i<size;i++){
			sendObject(players.get(i).socket,message);
		}
	}	
	public void rejectedClient(Socket client){
	  String freeServer="rejected\nplease try connect to\n"+ms.freeServer();//ms.freeServer();
	  sendObject(client,freeServer);
	}	
	public void sendGame(){
		Soup soup=new Soup(16,16,loadWords());
		soup.timeToGame=timeToGame;//put time to game
		int i,size=players.size();
		for(i=0;i<size;i++){
			sendObject(players.get(i).socket,soup);
		}
	}
	public Object receiveObject(Socket client){
		Object object=null;
		try{
			ObjectInputStream ois=new ObjectInputStream(client.getInputStream());
			object=ois.readObject();
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,"error receiving object "+e.toString());
		}
		return object;
	}
	public void receiveClients(){
		Socket client;
		playersConnected=0;
		players=new LinkedList<Player>();
		ms.startAnnounce(port);
		try{
			while(true){
				System.out.print("\nwaiting for client");
				client=socket.accept();
				System.out.print("\nclient connect with ip:"+client.getInetAddress().getHostAddress()+" port:"+client.getPort());
				if((playersConnected++)<maximumPlayers){
					players.add(new Player(client));
					sendClientsConnected(playersConnected,maximumPlayers-playersConnected);					
					players.getLast().name=String.valueOf(receiveObject(client));
					if(playersConnected==maximumPlayers){
						ms.stopAnnounce();
						atiende();
					}
				}else{
					rejectedClient(client);
				}
				Thread.sleep(500);
			}
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,"error receiving client \n"+e.toString());
		}
	}
	public void receiveWordsFound(){
		int i;
		String []split;
		String score;
		try{
			for(i=0;i<players.size();i++)
				players.get(i).score=(Score)receiveObject(players.get(i).socket);
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,"error receiving scores "+e.getMessage());
		}
	}
	public void whosWon(){
		int i;
		String names="";
		LinkedList<Player> playersFoundMoreWords=whoFoundMoreWords(players);
		if(playersFoundMoreWords.size()>1){
			LinkedList<Player> playersFoundWordMoreLarge=whoFoundWordMoreLarge(playersFoundMoreWords);
			for(i=0;i<playersFoundWordMoreLarge.size();i++)
				names+=playersFoundWordMoreLarge.get(i).name;
			for(i=0;i<players.size();i++)
				sendObject(players.get(i).socket,names+" win at found word more large in less time");
		}else{
			for(i=0;i<players.size();i++)
				sendObject(players.get(i).socket,playersFoundMoreWords.get(0).name+" win at found the more number of words"); 
		}
	}
	public LinkedList<Player> whoFoundMoreWords(LinkedList<Player> players){
		int i,record=0,size=players.size();
		Player player;
		LinkedList<Player> playersFoundMoreWords=new LinkedList<Player>();
		for(i=0;i<size;i++){
			player=players.get(i);
			if(record<player.score.wordsFounded){
				record=player.score.wordsFounded;
				emptyList(playersFoundMoreWords);
				playersFoundMoreWords.add(player);
			}else{
				if(record==player.score.wordsFounded)
					playersFoundMoreWords.add(player);
			}
		}
		return playersFoundMoreWords;
	}
	public LinkedList<Player> whoFoundWordMoreLarge(LinkedList<Player> players){
		int i,recordLength=0,recordSeconds=timeToGame+180,size=players.size();
		Player player;
		LinkedList<Player> playersFoundWordMoreLarge=new LinkedList<Player>();
		for(i=0;i<size;i++){
			player=players.get(i);
			if(recordLength<player.score.wordMoreLarge.length()){
				recordLength=player.score.wordMoreLarge.length();
				recordSeconds=player.score.secondsWordMoreLarge;
				emptyList(playersFoundWordMoreLarge);
				playersFoundWordMoreLarge.add(player);
			}else{
				if(recordLength==player.score.wordMoreLarge.length()){
					if(recordSeconds>player.score.secondsWordMoreLarge){
						recordSeconds=player.score.secondsWordMoreLarge;
						emptyList(playersFoundWordMoreLarge);
						playersFoundWordMoreLarge.add(player);					
					}else{
						if(recordSeconds==player.score.secondsWordMoreLarge)
							playersFoundWordMoreLarge.add(player);
					}
				}
			}
		}
		return playersFoundWordMoreLarge;
	}
	public void playerStaysOrLeave(){
		int i,playersRemoved=0;
		String stayLeave;
		for(i=0;i<players.size();i++){
				stayLeave=String.valueOf(receiveObject(players.get(i).socket));
				if(stayLeave.equals("leave")){//the client leave
					playersRemoved++;//aumento the players removed
					players.remove(i--);//remove the list player retirado and como lo remuevo nesesito restarle a i uno para que no se salte uno
				}
		}	
		if(playersRemoved==0){//if any player was removed then atiende 
			atiende();
		}
		else{
			ms.startAnnounce(port);
			playersConnected-=playersRemoved;
			sendClientsConnected(playersConnected,maximumPlayers-playersConnected);					
		}
	}
	public void emptyList(LinkedList list){
		while(list.size()>0)
			list.remove(0);
	}
	public void selectFiles(){
		JFileChooser selector=new JFileChooser();
		selector.setMultiSelectionEnabled(true);
		if(selector.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
			file=selector.getSelectedFiles();		
	}	
	public LinkedList<Word> loadWords(){
		if(indexFile==file.length)
			indexFile=0;
		//int random=(int)Math.random()*file.length;//random in
		//System.out.print("\nfile.length="+file.length+" random="+Math.random()*file.length);
		LinkedList<Word> words=new LinkedList<Word>();
		String line;
		String []split,rci,rcf;//rci cordenadas iniciales rcf condenadas finales
		Word word;
		try{
			FileReader lector=new FileReader(file[indexFile++]);
			BufferedReader bufer=new BufferedReader(lector);
			while((line=bufer.readLine())!=null){
				split=line.split(",");
				rci=split[1].split("-");
				rcf=split[2].split("-");
				word=new Word(split[0]);
				word.setCoordenadas(Integer.parseInt(rci[0]),Integer.parseInt(rci[1]),Integer.parseInt(rcf[0]),Integer.parseInt(rcf[1]));
				words.add(word);
			}
		}catch(Exception e){
			System.out.print("\nerror loading words "+e.getMessage());
		}
		return words;
	}
	public static void main(String []args){
		int port=Integer.parseInt(javax.swing.JOptionPane.showInputDialog(null,"enter port flujo"));
		int portMulticast=Integer.parseInt(javax.swing.JOptionPane.showInputDialog(null,"enter port multicast"));
		String ipGroup=javax.swing.JOptionPane.showInputDialog(null,"enter ip group");
		MulticastServer ms=new MulticastServer(portMulticast,ipGroup);
		StreamServer ss=new StreamServer(port,ms);
		//ss.sendGame();
		ss.receiveClients();
	}
}
