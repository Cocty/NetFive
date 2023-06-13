import java.awt.BorderLayout;
import java.awt.List;
import java.awt.Panel;

public class PanelUserList extends Panel {
	public List userList = new List(6);      
	public PanelUserList () {       
		setLayout (new BorderLayout());       
		add (userList, BorderLayout.CENTER);    
	}  

}
