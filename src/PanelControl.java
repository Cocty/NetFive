import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;

public class PanelControl extends Panel {
	   public Label IPlabel = new Label("������IP:", Label.LEFT);      
	    public TextField inputIP = new TextField ("127.0.0.1", 12);      
	    public Button connectButton = new Button ("��������");      
	    public Button joinGameButton = new Button ("������Ϸ");      
	    public Button cancelGameButton = new Button("������Ϸ");      
 	    public Button exitGameButton = new Button ("�رճ���");    
	    //���캯��������Panel �ĳ�ʼ����    
	    public PanelControl () {        
	    	setLayout (new FlowLayout (FlowLayout.LEFT));        
	    	setBackground (new Color (200,200,200));        
	    	add (IPlabel);       
	    	add (inputIP);       
	    	add (connectButton);      
	    	add (joinGameButton);      
	    	add (cancelGameButton);     
	    	add (exitGameButton);    
	   }  
}
