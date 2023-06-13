import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class FiveServer extends JFrame implements ActionListener{
	Label lStatus=new Label("��ǰ������:",Label.LEFT);
	TextArea taMessage=new TextArea("",22,50,TextArea.SCROLLBARS_VERTICAL_ONLY);
	Button btServerClose=new Button("�رշ�����");
	ServerSocket ss = null;
	public static final int TCP_PORT = 4801;
	static  int clientNum = 0;
	static  int clientNameNum = 0;
	ArrayList<Client>  clients = new ArrayList<Client>();

	public static void main(String[] args){
		try {
			// ���ñ���ϵͳ���
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		FiveServer fs = new FiveServer();
		fs.startServer();
	}
	
	public FiveServer(){
		super("Java�����������");
		
		btServerClose.addActionListener(this);

		add(lStatus,BorderLayout.NORTH);
		add(taMessage,BorderLayout.CENTER);
		add(btServerClose,BorderLayout.SOUTH);
			
		setLocation(400,100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		setResizable(false);
	}
	public void startServer(){
		try {
			ss = new ServerSocket(TCP_PORT);
			while(true){
				Socket s = ss.accept();			
				clientNum++;
				clientNameNum++;
				Client c = new Client("Player"+clientNameNum, s); 
				clients.add(c);
				lStatus.setText("������" + clientNum);
				String msg = s.getInetAddress().getHostAddress()+ "  Player"+clientNameNum + "\n";
				taMessage.append(msg);
				tellName(c);
				addAllUserToMe(c);
				addMeToAllUser(c);
				new ClientThread(c).start();
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	private void tellName(Client c) {
		DataOutputStream dos=null;
		try{
			dos = new DataOutputStream(c.s.getOutputStream());
			dos.writeUTF(Command.TELLNAME +":" + c.name);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}	
	private void addAllUserToMe(Client c) {
		DataOutputStream dos=null;
		for(int i=0; i<clients.size(); i++){
			if(clients.get(i) != c){
				try {
					dos = new DataOutputStream(c.s.getOutputStream());
					dos.writeUTF( Command.ADD + ":" + clients.get(i).name + ":" +  clients.get(i).state);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private void addMeToAllUser(Client c) {
		DataOutputStream dos=null;
		for(int i=0; i<clients.size(); i++){
			if(clients.get(i) != c){
				try {
					dos = new DataOutputStream(clients.get(i).s.getOutputStream());
					dos.writeUTF( Command.ADD + ":" + c.name + ":ready");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btServerClose)
		{
			System.exit(0);
		}
	}
	class Client{
		String name;
		Socket s;
		String  state; //  1.  ready    2.playing
		Client opponent; //����
		public Client(String name,Socket s) {
			this.name = name;
			this.s = s;
			this.state = "ready";
			this.opponent = null;
		}
	}
	class ClientThread extends Thread{
		private Client c;
		private DataInputStream dis;
		private DataOutputStream dos;
		ClientThread(Client c){
			this.c = c;
		}
		public void run(){
			while(true){
				try {
					dis = new DataInputStream(c.s.getInputStream());
					String msg = dis.readUTF();
					String[] words = msg.split(":");
					if(words[0].equals(Command.JOIN)){
						String opponentName = words[1];
						for(int i=0; i<clients.size(); i++){
							if(clients.get(i).name.equals(opponentName)){
								dos = new DataOutputStream(clients.get(i).s.getOutputStream());
								dos.writeUTF(Command.JOIN + ":" + c.name);
								break;
							}						
						}
					}
					else if(words[0].equals(Command.REFUSE)){
						String opponentName = words[1];
						for(int i=0; i<clients.size(); i++){
							if(clients.get(i).name.equals(opponentName)){
								dos = new DataOutputStream(clients.get(i).s.getOutputStream());
								dos.writeUTF(Command.REFUSE + ":" + c.name);
								break;
							}						
						}
					}
					else if(words[0].equals(Command.AGREE)){
						c.state = "playing";
						String opponentName = words[1];
						int opponentIndex;
						//�����ͻ��໥Ϊ����
						for(int i=0; i<clients.size(); i++){
							if(clients.get(i).name.equals(opponentName)){
								clients.get(i).state = "playing";	
								clients.get(i).opponent = c;
								c.opponent = clients.get(i);
								break;
							}						
						}
						//�ı����пͻ������������ͻ���״̬Ϊplaying
						for(int i=0; i<clients.size(); i++){
							dos = new DataOutputStream(clients.get(i).s.getOutputStream());							
							dos.writeUTF(Command.CHANGE + ":" + c.name + ":playing");
							dos.writeUTF(Command.CHANGE + ":" +opponentName + ":playing");
						}
						int r = (int) (Math.random()*2);  //���������壬����
						if(r==0){
							dos = new DataOutputStream(c.s.getOutputStream());							
							dos.writeUTF(Command.GUESSCOLOR+ ":black:" + opponentName);
							dos = new DataOutputStream(c.opponent.s.getOutputStream());							
							dos.writeUTF(Command.GUESSCOLOR+ ":white:" + c.name);						
						}
						else
						{
							dos = new DataOutputStream(c.s.getOutputStream());							
							dos.writeUTF(Command.GUESSCOLOR+ ":white:" + opponentName);
							dos = new DataOutputStream(c.opponent.s.getOutputStream());							
							dos.writeUTF(Command.GUESSCOLOR+ ":black:" + c.name);													
						}
						taMessage.append(c.name + " playing\n");
						taMessage.append(opponentName + " playing\n");
					}
					else if(words[0].equals(Command.GO)){
						dos = new DataOutputStream(c.opponent.s.getOutputStream());		
						dos.writeUTF(msg);	
						taMessage.append(c.name + " " +msg + "\n");
					}
					else if(words[0].equals(Command.WIN)){
						//���пͻ��˿ͻ��б��У����������ͻ���״̬��Ϊ"ready"
						for(int i=0; i<clients.size(); i++){
							dos = new DataOutputStream(clients.get(i).s.getOutputStream());		
							dos.writeUTF(Command.CHANGE+ ":" + c.name + ":ready");	
							dos.writeUTF(Command.CHANGE+ ":" + c.opponent.name + ":ready");	
						}
						dos = new DataOutputStream(c.s.getOutputStream());		
						dos.writeUTF(Command.TELLRESULT+ ":win");	//���Լ�����ʤ�����������Ϸ
						dos = new DataOutputStream(c.opponent.s.getOutputStream());		
						dos.writeUTF(Command.TELLRESULT+ ":losses");//��Է�����ʧ�����������Ϸ							
						c.state = "ready";
						c.opponent.state = "ready";

						taMessage.append(c.name + " win\n");
						taMessage.append(c.opponent.name + " loss\n");
					}
					else if(words[0].equals(Command.GIVEUP)){
						for(int i=0; i<clients.size(); i++){
							dos = new DataOutputStream(clients.get(i).s.getOutputStream());		
							dos.writeUTF(Command.CHANGE+ ":" + c.name + ":ready");	
							dos.writeUTF(Command.CHANGE+ ":" + c.opponent.name + ":ready");	
						}
						dos = new DataOutputStream(c.s.getOutputStream());		
						dos.writeUTF(Command.TELLRESULT+ ":losses");	
						dos = new DataOutputStream(c.opponent.s.getOutputStream());		
						dos.writeUTF(Command.TELLRESULT+ ":win");	
						c.state = "ready";
						c.opponent.state = "ready";

						taMessage.append(c.name + " loss\n");
						taMessage.append(c.opponent.name + " win\n");
					}
					else if(words[0].equals(Command.QUIT)){
						for(int i=0; i<clients.size(); i++){
							if(clients.get(i)!=c){
								dos = new DataOutputStream(clients.get(i).s.getOutputStream());		
								dos.writeUTF(Command.DELETE+":" + c.name);	
							}						
						}
						clients.remove(c);
						taMessage.append(c.name  + " quit\n");
						clientNum--;
						lStatus.setText("������" + clientNum);
						return;
					}
				}
				catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
		
}