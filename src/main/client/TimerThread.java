package main.client;

public class TimerThread extends Thread {
	FiveClient fc;
	private int myTotalTime;
	private int opTotalTime;
	
	public TimerThread(FiveClient fc, int totalTime) {
		this.fc = fc;
		this.myTotalTime = totalTime;
		this.opTotalTime = totalTime;
	}

	public void run(){
		fc.timing.setMyTime(myTotalTime);
		fc.timing.setOpTime(opTotalTime);
		while(fc.board.isGamming){
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(fc.board.isGoing){	//如果我方的棋盘正在下棋
				myTotalTime--;
				fc.timing.setMyTime(myTotalTime);
				if(myTotalTime<=0){
					fc.c.giveup();
					break;
				}
			}
			else{
				opTotalTime--;
				fc.timing.setOpTime(opTotalTime);
				if(opTotalTime<=0)
					break;
			}
		}	
	}
}
