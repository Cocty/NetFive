import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.FlowLayout;

public class PanelControl extends JPanel {
	public JLabel IPlabel = new JLabel("������IP:");
	public JTextField inputIP = new JTextField("localhost", 12);
	public JButton connectButton = new JButton("��������");
	public JButton joinGameButton = new JButton("������Ϸ");
	public JButton cancelGameButton = new JButton("����");
	public JButton exitGameButton = new JButton("�رճ���");

	// ���캯��������Panel�ĳ�ʼ����
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

