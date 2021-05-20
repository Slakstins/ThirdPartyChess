package sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestHandler 
{
	private ChessTeam chessTeam;
    private Socket socket;
    BufferedReader in;
    PrintWriter out;
    RequestHandler( Socket socket )
    {
    	 // Get input and output streams
        this.socket = socket;
        try {
			in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
			out = new PrintWriter( socket.getOutputStream() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

    }
    
    public void sendLine(String line) {
    	System.out.println("sending... " + line);
		out.println(line);
		out.flush();
    	System.out.println("sent: " + line);
    }
    
    private String receiveLine() {
    	System.out.println("receiving...");
    	String line = null;;
		try {
			line = in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (line == null) {
			System.out.println("error receiving line");
		}
    	System.out.println("received: " + line);

    	return line;
    }
    
    public void sendGreetings(ChessTeam team) {
    	
		//HAND SHAKE
		// Send message to client
    	if (team == ChessTeam.BLACK) {
    		sendLine("black");
    	}
    	if (team == ChessTeam.WHITE) {
    		sendLine("white");
    	}
    	
    	if(team == ChessTeam.THIRD) {
    		sendLine("third");
    	}
		//receive message from client
		String line = receiveLine();
		System.out.println("received: " + line);
		
		
    }
    
    public void informOfClick(String outcome) {
    	sendLine(outcome);
    	//need to do more here?
    }
    
    public String takeTurn() {
    	System.out.println("starting turn: " + chessTeam.name());
		sendLine("your turn");
		String line = receiveLine();
		return line;
    	
    }
    
    
    public void closeConnections() {
    	
        try {
			in.close();
            out.close();
            socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println( "Connection closed" );
    }

	public ChessTeam getChessTeam() {
		return chessTeam;
	}

	public void setChessTeam(ChessTeam chessTeam) {
		this.chessTeam = chessTeam;
	}


}