package sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import sockets.RequestHandler;

import chess.Main;


public class Server extends Thread
{
    private ServerSocket serverSocket;
    private int port;
    private boolean running = false;

    private RequestHandler blackRequestHandler = null;
    private RequestHandler whiteRequestHandler = null;
    public Server( int port )
    {
        this.port = port;
        System.out.println( "Start server on port: " + port );
		if (port < 1024 || port > 49151) {
			System.out.println("portnum must be between 1024 and 49151");
			return;
		}
        this.startServer();
        //NEED TO SHUT DOWN AT SOME POINT


        this.stopServer();
    }

    public void startServer()
    {
        try
        {
            serverSocket = new ServerSocket( port );
            this.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void stopServer()
    {
        running = false;
        this.interrupt();
    }
    
    public void getConnections() {
    	
        Socket blackSocket = null;
        Socket whiteSocket = null;
        while( running )
        {
            try
            {
                System.out.println( "Listening for a connection" );

                // Call accept() to receive the next connection
                if (blackSocket == null) {
                	  blackSocket = serverSocket.accept();
                      // Pass the socket to the RequestHandler thread for processing
                      blackRequestHandler = new RequestHandler( blackSocket );
                      blackRequestHandler.setChessTeam(ChessTeam.BLACK);
                      blackRequestHandler.sendGreetings(ChessTeam.BLACK);
                      
                      //BREAK FOR TESTING
                      
                      
                      
                }
                else if (whiteSocket == null) {
                	  whiteSocket = serverSocket.accept();
                      // Pass the socket to the RequestHandler thread for processing
                      whiteRequestHandler = new RequestHandler( whiteSocket );
                      whiteRequestHandler.setChessTeam(ChessTeam.WHITE);
                      whiteRequestHandler.sendGreetings(ChessTeam.WHITE);
                } else {
                	System.out.println("players full");
                	break;
                }
              
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run()
    {
        running = true;
        //obtain connections -- handle disconnects here if time
        this.getConnections();
        //connect turn logic here!
        boolean gameplay = true;
        //set team that goes first. Use black for now for testing
        ChessTeam nextTurn = ChessTeam.WHITE;
        while (gameplay) {
        	nextTurn = this.takeTurns(nextTurn);
        	thirdPlayerTurn();


        	
        }
        
        	
        	
        running = false;

    }
    
    private void thirdPlayerTurn() {
    	//need to call a method in main or something from here to achieve this
    	Main.myTurn = true;

    	//must send the move to both of the other players


    	
    }
    
    //make the move in the format "x,y" in main
    public static void makeClick(String outcome) {
    	String[] coords = new String[4];
		coords = outcome.split(",");
		int x1 = Integer.parseInt(coords[0]);
		int y1 = Integer.parseInt(coords[1]);
		int x2 = Integer.parseInt(coords[2]);
		int y2 = Integer.parseInt(coords[3]);
		System.out.println("moveMabye: " + x1 + ", " + y1 + " to " + x2 + ", " + y2);
		Main.moveMaybe(x1, y1, true);
		Main.moveMaybe(x2, y2, true);
    }
    
    //Take turns for the normal 2 players
    //returns the next team after the third player's turn
    private ChessTeam takeTurns(ChessTeam nextTurn) {
    	String outcome = "placeholder";
        while(!outcome.equals(SocketTimer.OUT_OF_TIME)) {
        	if (nextTurn == ChessTeam.BLACK) {
        		outcome = blackRequestHandler.takeTurn();

        		// make the received click in MAIN
        		makeClick(outcome);
        	
        		//inform the other player of the move taken
        		whiteRequestHandler.informOfClick("move:" + outcome);
        		nextTurn = ChessTeam.WHITE;
        		
        	}
        	else if (nextTurn == ChessTeam.WHITE) {
        		outcome = whiteRequestHandler.takeTurn();
        		// make the received move in MAIN
        		makeClick(outcome);
        		blackRequestHandler.informOfClick("move:" + outcome);
        		nextTurn = ChessTeam.BLACK;
        	}
        	
        	

        }
        //should still be the next turn in sequence even if timeout
        return nextTurn;
    	
    }


}