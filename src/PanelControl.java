import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.FlowLayout;

public class PanelControl extends JPanel {
	public JLabel IPlabel = new JLabel("服务器IP:");
	public JTextField inputIP = new JTextField("localhost", 12);
	public JButton connectButton = new JButton("连接主机");
	public JButton joinGameButton = new JButton("加入游戏");
	public JButton cancelGameButton = new JButton("认输");
	public JButton exitGameButton = new JButton("关闭程序");

	// 构造函数，负责Panel的初始布局
	public PanelControl() {
		setLayout(new FlowLayout(FlowLayout.CENTER));
		setBackground(new Color(200, 200, 200));
		add(IPlabel);
		add(inputIP);
		add(connectButton);
		add(joinGameButton);
		add(cancelGameButton);
		add(exitGameButton);
	}
}

