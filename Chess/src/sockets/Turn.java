package sockets;

import java.util.Scanner;

import chess.Main;
import chess.Time;

public class Turn implements Runnable {
	
	private MoveMsg moveMsg;
	private SocketTimer socketTimer;
	public Turn(MoveMsg moveMsg) {
		this.moveMsg = moveMsg;
		this.socketTimer = new SocketTimer(moveMsg);
		socketTimer.setMoveMsg(moveMsg);
		
	}
	
	public synchronized String begin() {
		
		System.out.println("turn started");
		String line = "placeholder";
		Thread turnThread = new Thread(this);
		turnThread.start();

		if (!socketTimer.getStarted()) {
			socketTimer.start();
			socketTimer.setStarted(true);
		} else {
			socketTimer.resume();
		}
		//wait for either the turn time to expire or for the turn to be made
		line = moveMsg.getMessage(); //getMessage() has a wait() in it
		System.out.println(line);
		//WILL NEED TO HANDLE ACTUAL CHESS GAME THREAD HERE
		if (!line.equals(SocketTimer.OUT_OF_TIME)) {
			//the thread didn't finish sleeping, so wake it up and pause it
			socketTimer.suspend();
		} else {
			//turn time ran out, so kill the chess game thread 
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

}
