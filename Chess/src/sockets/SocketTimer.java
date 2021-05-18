package sockets;

import chess.Main;
import chess.Time;

public class SocketTimer extends Thread{
	public static long TURNTIME = 20000;
	public long timeSlept;
	private MoveMsg moveMsg;
	public static final String OUT_OF_TIME = "TIME ran out!";
	public static long WHITETIMELEFT = TURNTIME;
	public static long BLACKTIMELEFT = TURNTIME;
	public SocketTimer(MoveMsg moveMsg, long time) {
		timeSlept = time;
		this.moveMsg = moveMsg;
	}
	
	
	public void run() {
		try {
			//start the turn timer
			// do the sleep in 1000 intervals so that turn time can be saved for the next turn
			while (timeSlept < TURNTIME) {
				Thread.sleep(TURNTIME / (TURNTIME / 1000));
				timeSlept += TURNTIME / (TURNTIME / 1000);
			}
		   moveMsg.setMessage(OUT_OF_TIME);
		} catch (InterruptedException e) {
			//NOTE, IF THERE ARE EVER WEIRD BUGS with inconsistent state of moveMsg,
			//this may be the culprit. Interrupting can be glitchy supposedly - better
			//way of interrupting sleeping thread?
			// TODO Auto-generated catch block
			Thread.currentThread().interrupt();
		}
		
	}
	
	
	public long stopTimer() {
		this.interrupt();
		return timeSlept;
		
	}

	
}
