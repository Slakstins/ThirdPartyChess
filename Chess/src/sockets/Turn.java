package sockets;

import java.util.Scanner;

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

			timeLeft = socketTimer.stopTimer();
		} else {
			//turn time ran out, so kill the turn taking thread
			Main.previous = null;
			turnThread.interrupt();
		}
		return line;

		
	}

		

	
	
	public void run() {
		//play the actual chess game
		System.out.println("playing turn!");
		
		Main.myTurn = true;

		//communicate the turn from Main through moveMsg. It will wait for the outcome from Main
		Main.moveMsg = moveMsg;
	}

	public long getTimeLeft() {
		return timeLeft;
	}

}
