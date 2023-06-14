import javax.swing.*;
import java.awt.*;

public class PanelChatArea extends JPanel {
    public TextArea mesageArea;

    public PanelChatArea () {
        setLayout (new BorderLayout());
        mesageArea = new TextArea ("", 20, 20, TextArea.SCROLLBARS_VERTICAL_ONLY);
        add (mesageArea, BorderLayout.CENTER);
    }
}
