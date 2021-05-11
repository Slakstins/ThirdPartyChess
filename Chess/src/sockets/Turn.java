package sockets;

import java.util.Scanner;

import chess.Time;

public class Turn implements Runnable {
	
	private MoveMsg moveMsg;
	private Client client;
	public Turn(MoveMsg moveMsg, Client client, SocketTimer socketTimer) {
		this.moveMsg = moveMsg;
		this.client = client;
		socketTimer.setMoveMsg(moveMsg);
		
	}
	
	public synchronized void begin() {
		
		String line = "placeholder";
		SocketTimer socketTimer = new SocketTimer(moveMsg);
		Thread turnThread = new Thread(this);
		turnThread.start();

		socketTimer.start();
		//wait for either the turn time to expire or for the turn to be made
		line = moveMsg.getMessage(); //getMessage() has a wait() in it
		System.out.println(line);
		//WILL NEED TO HANDLE ACTUAL CHESS GAME THREAD HERE
		if (!line.equals(SocketTimer.OUT_OF_TIME)) {
			//the thread didn't finish sleeping, so wake it up and kill it
			socketTimer.interrupt();
		} else {
			//turn time ran out, so kill the chess game thread
			turnThread.interrupt();
		}
		client.sendLine(line);
		client.sendLine("exit");

		
	}

		

	
	
	public void run() {
		//play the actual chess game
		
		
		
		
		//replace all of this. 
		System.out.println("made it!");
		Scanner scanner = new Scanner(System.in);
		String line = "placeholder";
		while (!line.equals("exit")) {
			line = scanner.nextLine();
			moveMsg.setMessage("Move made: " + line); 
		}
		scanner.close();

		
	}

}
