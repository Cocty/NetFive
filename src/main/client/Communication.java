package main.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Communication {
	FiveClient fc;
	Socket s;
	private DataInputStream dis;
	private DataOutputStream dos;
	TimerThread tt = null;
	
	public Communication(FiveClient fc) {
		this.fc = fc;
	}

	public boolean connect(String IP, int port) {
		try {
			s = new Socket(IP,port);
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
			new ReceaveThread(s).start();			
			return true;
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	public void join(String opponentName) {
		try {
			dos.writeUTF(Command.JOIN + ":" + opponentName);			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	public void send(String opponentName,String content) {
		try {
			dos.writeUTF(Command.SEND + ":" + opponentName+":"+content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void go(int col, int row) {
		try {
			String msg = Command.GO+ ":" + col + ":" + row;
			dos.writeUTF(msg);			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public void wins() {
		try {
			dos.writeUTF(Command.WIN);			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}	
	public void giveup() {
		try {
			dos.writeUTF(Command.GIVEUP);		
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void disConnect() {
		try {
			dos.writeUTF(Command.QUIT);			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	class ReceaveThread extends Thread{//接收线程，接收服务器发送的信息，然后进行解析
		Socket s;
		private DataInputStream dis;
		private DataOutputStream dos;
		String  msg;
		public ReceaveThread(Socket s){
			this.s = s;
		}
		public void run(){
			while(true){
				try {
					dis = new DataInputStream(s.getInputStream());
					dos = new DataOutputStream(s.getOutputStream());
					msg = dis.readUTF();
					String[] words = msg.split(":");
					if(words[0].equals(Command.TELLNAME)){
						fc.myname = words[1];
						fc.userList.userList.add(fc.myname + ":ready");
						fc.timing.setMyName(fc.myname);
						fc.message.mesageArea.append("My name: " + fc.myname + "\n");
						fc.setTitle("五子棋客户端-"+fc.myname);
					}
					
					else if(words[0].equals(Command.ADD)){
						fc.userList.userList.add(words[1]+ ":" + words[2]);
						fc.message.mesageArea.append(words[1]+ ":" + words[2] + "\n");
					}
					else if(words[0].equals(Command.JOIN)){
						String name = words[1];
						TimeDialog d = new TimeDialog();  
						int select = d.showDialog(fc, name + "  邀请你下棋，是否接受?", 100);  
						if(select == 0){
							dos.writeUTF(Command.AGREE + ":" + name);
						}
						else{
							dos.writeUTF(Command.REFUSE + ":" + name);		
						} 
					}
					else if(words[0].equals(Command.REFUSE)){
						String name = words[1];
						JOptionPane.showMessageDialog(fc, name + "  拒绝了您的邀请！");
									
					}
					else if(words[0].equals(Command.CHANGE)){
    					String name = words[1];
    					String state = words[2];
    					for(int i=0; i<fc.userList.userList.getItemCount(); i++){
    						if(fc.userList.userList.getItem(i).startsWith(name)){
    							fc.userList.userList.replaceItem(name + ":" +state, i);
    						}
    					}
    					fc.message.mesageArea.append(name + " " + state + "\n");
    				}
					else if(words[0].equals(Command.GUESSCOLOR)){
						String color = words[1];
						String oppName = words[2];
						fc.board.isGamming = true;
						fc.opname = oppName;
						fc.timing.setOpName(oppName);
						if(color.equals("black")){    //黑棋
							fc.timing.setMyIcon("black");
							fc.timing.setOpIcon("white");
							fc.board.isBlack = true;
							fc.board.isGoing = true;
						}
						else if(color.equals("white")){ //白棋
							fc.timing.setMyIcon("white");
							fc.timing.setOpIcon("black");
							fc.board.isBlack = false;
							fc.board.isGoing = false;
						}
						fc.control.joinGameButton.setEnabled(false);
						fc.control.cancelGameButton.setEnabled(true);
						fc.control.exitGameButton.setEnabled(false);
						
						tt = new TimerThread(fc,120);
						tt.start();
						
						fc.message.mesageArea.append("My color is " + color + "\n");
					}
					else if(words[0].equals(Command.GO)){
						int  col = Integer.parseInt(words[1]);
						int  row = Integer.parseInt(words[2]);
						fc.board.addOpponentChess(col,row);
					}
					else if(words[0].equals(Command.TELLRESULT)){
						if(words[1].equals("win"))
							fc.board.winsGame();
						else
							fc.board.lossesGame();
						fc.control.joinGameButton.setEnabled(true);
						fc.control.cancelGameButton.setEnabled(false);
						fc.control.exitGameButton.setEnabled(true);
					}
					else if(words[0].equals(Command.DELETE)){
						for(int i=0; i<fc.userList.userList.getItemCount();i++){
							String name = fc.userList.userList.getItem(i);
							if(name.startsWith(words[1])){
								fc.userList.userList.remove(i);
							}
						}
						fc.message.mesageArea.append(words[1] + " disconnected" + "\n");
					} else if (words[0].equals(Command.SEND)) {
						String name = words[1];
						String content = words[2];
						fc.chatArea.mesageArea.append(name+"对你说："+content+"\n");
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