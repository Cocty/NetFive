import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FiveClient extends JFrame {
    PanelBoard board;
    PanelUserList userList;
    PanelMessage message;
    PanelTiming timing;
    PanelControl control;
    PanelChatArea chatArea;
    PanelChatControl panelChatControl;
    String myname;
    String opname;
    public boolean isConnected = false;
    Communication c = null;

    public static void main(String[] args) {
        try {
            // ���ñ���ϵͳ���
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        FiveClient fc = new FiveClient();
    }

    public FiveClient() {
        super("������ͻ���");
        board = new PanelBoard(this);
        this.add(board, BorderLayout.WEST);

        timing = new PanelTiming();
        userList = new PanelUserList();
        message = new PanelMessage();
        Panel east = new Panel();
        east.setLayout(new BorderLayout());
        east.add(userList, BorderLayout.CENTER);
        east.add(message, BorderLayout.SOUTH);
        east.add(timing, BorderLayout.NORTH);
        this.add(east, BorderLayout.EAST);

        chatArea = new PanelChatArea();//������
        Panel center = new Panel();
        center.setLayout(new BorderLayout());
        panelChatControl = new PanelChatControl();
        center.add(chatArea, BorderLayout.NORTH);
        center.add(panelChatControl, BorderLayout.CENTER);
        this.add(center, BorderLayout.CENTER);


        control = new PanelControl();
        this.add(control, BorderLayout.SOUTH);

        ActionMonitor monitor = new ActionMonitor();
        control.exitGameButton.addActionListener(monitor);
        control.connectButton.addActionListener(monitor);
        control.joinGameButton.addActionListener(monitor);
        control.cancelGameButton.addActionListener(monitor);
        panelChatControl.sendButton.addActionListener(monitor);

        control.exitGameButton.setEnabled(true);
        control.connectButton.setEnabled(true);
        control.joinGameButton.setEnabled(false);
        control.cancelGameButton.setEnabled(false);

        this.setLocation(300, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        this.setResizable(false);
        this.setVisible(true);
    }

    class ActionMonitor implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == control.exitGameButton) {
                if (isConnected) {
                    c.disConnect();
                }
                System.exit(0);
            } else if (e.getSource() == control.connectButton) {
                connect();
            } else if (e.getSource() == control.cancelGameButton) {
                c.giveup();
            } else if (e.getSource() == control.joinGameButton) {
                String select = userList.userList.getSelectedItem();
                if (select == null) {
                    message.mesageArea.append("��ѡ��һ������" + "\n");
                    return;
                }
                if (!select.endsWith("ready")) {
                    message.mesageArea.append("��ѡ��һ���Ѿ�Ready�Ķ���" + "\n");
                    return;
                }
                if (select.startsWith(myname)) {
                    message.mesageArea.append("����ѡ�Լ���Ϊ����" + "\n");
                    return;
                }
//                System.out.println(select);
                int index = select.lastIndexOf(":");
                String name = select.substring(0, index);
                join(name);
            } else if (e.getSource() == panelChatControl.sendButton) {
                String select = userList.userList.getSelectedItem();
                if (select == null) {
                    message.mesageArea.append("��ѡ��һ�����" + "\n");
                    return;
                }
                if (select.startsWith(myname)) {
                    message.mesageArea.append("����ѡ�Լ���Ϊ�������" + "\n");
                    return;
                }
                int index = select.lastIndexOf(":");
                String name = select.substring(0, index);
                String content = panelChatControl.input.getText();
                chatArea.mesageArea.append("���"+name+"˵��"+content+"\n");
                send(name,content);
                panelChatControl.input.setText("");
            }
        }
    }

    public void join(String opponentName) {
        c.join(opponentName);
    }
    public void send(String opponentName,String content){
        c.send(opponentName,content);
    }

    public void connect() {
        c = new Communication(this);
        String ip = control.inputIP.getText();
        if (c.connect(ip, FiveServer.TCP_PORT)) {
            message.mesageArea.append("������" + "\n");
            isConnected = true;
            control.exitGameButton.setEnabled(true);
            control.connectButton.setEnabled(false);
            control.joinGameButton.setEnabled(true);
            control.cancelGameButton.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "����ʧ�ܣ����Ժ����ԣ�");
        }


    }
}
