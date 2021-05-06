package sockets;

public class SocketTimer extends Thread{
	public static int TURNTIME = 5000;
	private MoveMsg moveMsg;
	public static final String OUT_OF_TIME = "TIME ran out!";
	public SocketTimer(MoveMsg moveMsg) {
		this.moveMsg = moveMsg;
	}
	
	
	public void run() {
		try {
			//start the turn timer
			Thread.sleep(TURNTIME);
		} catch (InterruptedException e) {
			//NOTE, IF THERE ARE EVER WEIRD BUGS with inconsistent state of moveMsg,
			//this may be the culprit. Interrupting can be glitchy supposedly - better
			//way of interrupting sleeping thread?
			// TODO Auto-generated catch block
			Thread.currentThread().interrupt();
		}
		moveMsg.setMessage(OUT_OF_TIME);
		
	}

}
