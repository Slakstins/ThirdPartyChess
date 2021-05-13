package sockets;

import chess.Main;

public class MoveMsg {
	private String message = null; 
	public MoveMsg() {
	}
	public synchronized String getMessage() {
		try {
			System.out.println("before wait");
			wait();
			System.out.println("after wait");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			try {
				wait();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return message;
	}
	public synchronized void setMessage(String message) {
		this.message = message;
		System.out.println("message is: " + message);
		notify();
		Main.myTurn = false;
	}
	
	

}
