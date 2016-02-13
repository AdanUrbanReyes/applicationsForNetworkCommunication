import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.io.Serializable;
import java.util.LinkedList;
import javax.swing.JButton;
public class Letter extends JButton implements Serializable{
	private static final long serialVersionUID=1L;
	public LinkedList <Word> words=new LinkedList<Word>();
	public int row,column;
	public Letter(char letter,Word word){
		super(""+letter);
		setBackground(Color.gray);
		setForeground(Color.green);
		setMargin(new Insets(0,0,0,0));
		setFont(new Font("Serif",Font.PLAIN,17));
		this.words.add(word);
	}
	public void setCoordenadas(int row,int column){
		this.row=row;
		this.column=column;
	}   
	public void addWordToList(Word word){
		words.add(word);
	}
	public void updateStateRigth(){//is went user rigth click
		if(this.getBackground()==java.awt.Color.pink){//then no avia clickeado this button
			this.setBackground(java.awt.Color.gray);
			int i,size=words.size();
			for(i=0;i<size;i++)
				words.get(i).lettersFounded--;
		}
	}
	public void updateStateLeft(){//is went user left click
		if(this.getBackground()==java.awt.Color.gray){//then no avia clickeado this button
			this.setBackground(java.awt.Color.pink);
			int i,size=words.size();
			for(i=0;i<size;i++)
				words.get(i).lettersFounded++;
		}
	}	
}
