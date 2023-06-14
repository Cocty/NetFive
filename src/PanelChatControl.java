import javax.swing.*;
import java.awt.*;

public class PanelChatControl extends  JPanel{
    public JTextField input = new JTextField("", 25);
    public JButton sendButton = new JButton("·¢ËÍ");
    public PanelChatControl() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(new Color(200, 200, 200));
        add(input);
        add(sendButton);
    }
}
