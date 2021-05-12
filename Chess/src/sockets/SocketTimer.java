package sockets;

public class SocketTimer extends Thread{
	public static int TURNTIME = 1000000;
	public long timeSlept;
	private MoveMsg moveMsg;
	public static final String OUT_OF_TIME = "TIME ran out!";
	private boolean started;
	public SocketTimer(MoveMsg moveMsg) {
		timeSlept = 0;
		this.moveMsg = moveMsg;
	}
	
	
	public void run() {
		try {
			//start the turn timer
			// do the sleep in 100 intervals so that turn time can be saved for the next turn
			while (timeSlept < TURNTIME) {
				Thread.sleep(TURNTIME / 100);
				timeSlept += TURNTIME / 100;
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
	public void setMoveMsg(MoveMsg moveMsg) {
		this.moveMsg = moveMsg;
	}

	public void setStarted(boolean bool) {
		this.started = bool;
	}
	
	public boolean getStarted() {
		return started;
	}
}
