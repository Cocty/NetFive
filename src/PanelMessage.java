import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.TextArea;

public class PanelMessage extends JPanel {
	public TextArea mesageArea;

	public PanelMessage () {        
		setLayout (new BorderLayout());   
		mesageArea = new TextArea ("", 6, 20, TextArea.SCROLLBARS_VERTICAL_ONLY);
		add (mesageArea, BorderLayout.CENTER);     
	}  
}
