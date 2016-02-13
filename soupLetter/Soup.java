import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Insets;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.InputEvent;
import java.util.LinkedList;
import java.io.Serializable;
public class Soup extends JPanel implements MouseListener,Serializable,Runnable{
	private static final long serialVersionUID=1L;
	public static int timeToGame=60;
	private JLabel timel;
	private int row,column,badButtonsPushed=0,second=0;
	private LinkedList <Word> words;
	private JButton [][]board;
	/*private Runnable timer=new Runnable(){
		public void run(){
			try{
				second=0;
				while(second<=timeToGame){
					timel.setText("time: "+second+" seconds");
					Thread.sleep(1000);
					second++;
				}
				endGame();
			}catch(Exception e){
				javax.swing.JOptionPane.showMessageDialog(null,"error in thread timer "+e.getMessage());
			}
		}
	};*/
	public Soup(int row,int column,LinkedList <Word> words){
		super(null);
		startLabelTimer();
		this.row=row;
		this.column=column;
		this.words=words;
		board=new JButton[row][column];
		fillBoard();
		putBoardBeautifull();
		setPanelBoard();
		setPistas();
	}
	@Override
	public void mousePressed(MouseEvent e){;}
	@Override
	public void mouseReleased(MouseEvent e){;}
	@Override
	public void mouseExited(MouseEvent e){;}
	@Override
	public void mouseEntered(MouseEvent e){;}
	@Override
	public void mouseClicked(MouseEvent e){
		Object object=e.getSource();
		if((e.getModifiers()&InputEvent.BUTTON1_MASK)==InputEvent.BUTTON1_MASK){//click izquierdo
			if(object instanceof Letter){
				Letter temp=(Letter)object;
				temp.updateStateLeft();
				checkFoundedWord(temp,true);
			}else{
				if(object instanceof JButton){
					JButton temp=(JButton)object;
					if(temp.getBackground()==java.awt.Color.gray){
					   temp.setBackground(java.awt.Color.pink);
					   badButtonsPushed++;
					}
				}
			}
		}else{//click derecho
			if(object instanceof Letter){
				Letter temp=(Letter)object;
				temp.updateStateRigth();
				checkFoundedWord(temp,false);
			}else{
				JButton temp=(JButton)object;
				if(temp.getBackground()==java.awt.Color.pink){
					temp.setBackground(java.awt.Color.gray);
					if((--badButtonsPushed)==0){
						checkFoundedWord();
					}
				}
			}
		}
		
	}
	@Override
	public void run(){
		try{
			second=0;
			while(second<=timeToGame){
				timel.setText("time: "+second+" seconds");
				Thread.sleep(1000);
				second++;
			}
			endGame();
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,"error in thread timer "+e.getMessage());
		}
	}	
	public void checkFoundedWord(){
		int i,size=words.size();
		for(i=0;i<size;i++){
			if(words.get(i).isFound())
				words.get(i).founded(second);
		}		
	}
	public void checkFoundedWord(Letter letter,boolean flat){//recived letter pushed and a flat if flat is true then left click else rigth click
		int i,size=letter.words.size();
		Word word;
		for(i=0;i<size;i++){
			word=letter.words.get(i);//get word that have letter received
			if(flat&&word.isFound()&&badButtonsPushed==0){//if was lert click and word is founded and ningun bad button was pushed
				word.founded(second);//apply change de word founded is change color to red label pistas and put seconts that tardo in found this word
			}else{//not found 
				word.notFounded();//apply change the word not founded is change color to black label 
			}
		}
	}
	public void startTimer(){
		Thread time=new Thread(this);
		time.start();
	}
	public void startLabelTimer(){
		timel=new JLabel("");
		timel.setFont(new Font("Serif",Font.PLAIN,17));
		timel.setForeground(Color.blue);
		timel.setBounds(650,0,250,70);
		add(timel);			
	}
	public void endGame(){
		int r,c;
		for(r=0;r<row;r++){
			for(c=0;c<column;c++)
				board[r][c].removeMouseListener(this);
		}
	}
	public Score score(){
		if(second<=timeToGame)
			return null;
		int i,size=words.size();
		Word word;
		Score score=new Score();
		for(i=0;i<size;i++){//ciclo with reccorro all list word
			word=words.get(i);
			if(word.isFound()){//if word is found
				score.wordsFounded++;//aumento words founded
				score.totalSeconds+=word.seconds;//aumento seconts
				if(word.inSpanish.length()>score.wordMoreLarge.length()){//pregunto si la palabra encontrada es mas larga de la que ya esta en la variable wordMoreLarge
					score.wordMoreLarge=word.inSpanish;//corono as word more large la que se acaba de encontrar
					score.secondsWordMoreLarge=word.seconds;//set seconts that tardo en found this word
				}else{
					if(word.inSpanish.length()==score.wordMoreLarge.length()){//ask if size of word founded is same that size of wordMoreLarge
						if(word.seconds<score.secondsWordMoreLarge){//if secons that found word is less that seconts that wordMareLarge
							score.wordMoreLarge=word.inSpanish;//corono as word more large la que se acaba de encontrar
							score.secondsWordMoreLarge=word.seconds;//set seconts that tardo en found this word						
						}
					}
				}
			}
		}
		return score;
	}
	public void setPistas(){
		JPanel pistas=new JPanel(new GridLayout(0,1));
		JScrollPane scroll=new JScrollPane(pistas);
		Word temp;
		int i,size=words.size();
		for(i=0;i<size;i++){
			temp=words.get(i);
			temp.setPista(temp.inSpanish);
			pistas.add(temp.pista);
			pistas.revalidate();
		}
		scroll.setBounds(650,70,250,580);
		this.add(scroll);
	}
	public int[] howPutWord(int ri,int ci,int rf,int cf){
		if(ci==cf){//set in vertical
			if(ri<rf)//vertical down
				return (new int[]{0,1});
			else//vertical up
				return (new int[]{0,-1});
		}else{
			if(ri==rf){//para ponerse en horisontal
				if(ci<cf)//hacia la derecha
					return (new int[]{1,0});
				else//hacia la izquierda
					return (new int[]{-1,0});
		   }else{//diagonal
				if(ri>rf){//diagonal hacia arriba
					if(ci<cf)//diagonal hacia arriba derecha
						return (new int[]{1,-1});
					else //diagonal hacia arriba izquierda
						return (new int[]{-1,-1});
				}else{//diagonal acia abajo
					if(ci<cf)//diagonal hacia abajo derecha
						return (new int[]{1,1});
					else //diagonal hacia abajo izquierda
						return (new int[]{-1,1});
				}
			}
		}
		//return (new int[]{0,0});
	}
	public void setPanelBoard(){
		JPanel board=new JPanel(new GridLayout(row,column));
		int r,c;
		for(r=0;r<row;r++){
			for(c=0;c<column;c++){
				board.add(this.board[r][c]);
			}
		}
		board.setBounds(0,0,650,650);
		add(board);
	}
	public void setWordInBoard(Word word,int r,int c,int fc,int fr){//word to put in board,ri and ci row and column where start word, fc,fr flags
		/*
		  horizontal rigth fc=1 fr=0 
		  horizontal left fc=-1 fr=0
		  vertical up fc=0 fr=-1
		  vertical down fc=0 fr=1
		  diagonal rigth up fc=1 fr=-1
		  diagonal rigth down fc=1 fr=1
		  diagonal left up fc=-1 fr=-1
		  diagonal left down fc=-1 fr=1
		*/
		int i,j,size=word.inSpanish.length();
		Letter aux;
		   for(i=0;i<size;i++,c+=fc,r+=fr){
			  if((aux=(Letter)board[r][c])!=null){//checa si esta ocupado por Letter falta q cheque por cualquier componente
				aux.addWordToList(word);
				 continue ;
			  }
			  word.letter[i].addMouseListener(this);
			  word.letter[i].setCoordenadas(r,c);
				board[r][c]=word.letter[i];
		 }
	}
	public void putBoardBeautifull(){
		int r,c;
		for(r=0;r<row;r++){
			for(c=0;c<column;c++){
				board[r][c].setBackground(java.awt.Color.gray);//color del boton
				board[r][c].setForeground(java.awt.Color.green);//color del texto en el boton
				board[r][c].setMargin(new Insets(0,0,0,0));//margen del texto
				board[r][c].setFont(new Font("Serif",Font.PLAIN,17));//tipo de letra
			}
		}
	}
	public void fillBoard(){//first put words then put letters with a random
		String abc="abcdefghijklmnopqrstuvwxyz";		
		int []howPut;
		Word word;
		int r,c,i,size=words.size();
		for(i=0;i<size;i++){
			word=words.get(i);
			howPut=howPutWord(word.ri,word.ci,word.rf,word.cf);
			setWordInBoard(word,word.ri,word.ci,howPut[0],howPut[1]);
		}
		for(r=0;r<row;r++){
			for(c=0;c<column;c++){
				if(board[r][c]==null){//the row and column dont was ocupada
					board[r][c]=new JButton(""+abc.charAt(((int)(Math.random()*26))));
					board[r][c].addMouseListener(this);
				}
			}
		}
	}
}
