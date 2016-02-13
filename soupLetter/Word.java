import java.awt.Font;
import javax.swing.JLabel;
import java.io.Serializable;
public class Word implements Serializable{
	private static final long serialVersionUID=1L;
	public JLabel pista;
	public Letter []letter;
	public String inSpanish;
	public int ri,ci,rf,cf,lettersFounded=0;
	public int seconds=0;
	public Word(String inSpanish){
		this.inSpanish=inSpanish;
		startLetter();
	}
	public void setCoordenadas(int ri,int ci,int rf,int cf){
		this.ri=ri;
		this.ci=ci;
		this.rf=rf;
		this.cf=cf;
	}
	public void startLetter(){
		letter=new Letter[inSpanish.length()];
		int i,size=inSpanish.length();
		for(i=0;i<size;i++)
			letter[i]=new Letter(inSpanish.charAt(i),this);
	}
	public void setPista(String pista){
		this.pista=new JLabel(pista);
		this.pista.setFont(new Font("Serif",Font.PLAIN,17));
	}
	public void founded(int seconds){//apply change went word is founded
		if(pista.getForeground()!=java.awt.Color.red){
			this.seconds=seconds;
			pista.setText(pista.getText()+" "+seconds+" seconds");
			pista.setForeground(java.awt.Color.red);
		}		
	}
	public void notFounded(){//apply change went word not founded
		pista.setText(pista.getText().split(" ")[0]);
		pista.setForeground(java.awt.Color.black);
	}
	public boolean isFound(){
		if(inSpanish.length()==lettersFounded)
			return true;
		return false;
	}
}
