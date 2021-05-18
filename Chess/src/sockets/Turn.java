package sockets;


import chess.Main;
import chess.Time;

public class Turn implements Runnable {
	
	private MoveMsg moveMsg;
	private SocketTimer socketTimer;
	private long timeLeft;
	public Turn(long time) {
		timeLeft = time;
		this.moveMsg = new MoveMsg();
		this.socketTimer = new SocketTimer(moveMsg, time);
		
	}
	
	public synchronized String begin() {
		
		System.out.println("turn started");
		String line = "placeholder";
		Thread turnThread = new Thread(this);
		turnThread.start();

		//-1 indicates that this is an untimed move
		if (timeLeft != -1) {
			
			socketTimer.start();
		} 

		//wait for either the turn time to expire or for the turn to be made
		line = moveMsg.getMessage(); //getMessage() has a wait() in it
		System.out.println(line);
		//WILL NEED TO HANDLE ACTUAL CHESS GAME THREAD HERE
		if (!line.equals(SocketTimer.OUT_OF_TIME)) {
			//the thread didn't finish sleeping, so wake it up and get the remaining time
			//for the next turn
			
			//update the player turn based on the actual game turn
			Main.setWhoseTurnWithChance();
			


			timeLeft = socketTimer.stopTimer();
		} else {
			//reset the clock display
			//turn time ran out, so kill the turn taking thread
			Main.whoseTurn = ChessTeam.THIRD;
			Main.previous = null;
			turnThread.interrupt();
		}
		return line;

		
	}

		

	
	
	public void run() {
		//play the actual chess game
		System.out.println("playing turn!");
		
		Main.whoseTurn = Main.player;
		// must set turn to be true before calling updateChance

		//communicate the turn from Main through moveMsg. It will wait for the outcome from Main
		Main.moveMsg = moveMsg;
	}

	public long getTimeLeft() {
		return timeLeft;
	}

}
