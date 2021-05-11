package sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


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
                      
                      
                      
                      break;
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
        while (gameplay) {
        	ChessTeam nextTurn = this.takeTurns();
        	thirdPlayerTurn();


        	
        }
        
        	
        	
        running = false;

    }
    
    private void thirdPlayerTurn() {
    	//need to call a method in main or something from here to achieve this
    	//must send the move to both of the other players


    	
    }
    
    //Take turns for the normal 2 players
    //returns the next team after the third player's turn
    private ChessTeam takeTurns() {
    	String outcome = "placeholder";
        ChessTeam nextTurn = ChessTeam.BLACK;
        while(!outcome.equals(SocketTimer.OUT_OF_TIME)) {
        	if (nextTurn == ChessTeam.BLACK) {
        		outcome = blackRequestHandler.takeTurn();
        		//inform the other player of the move taken
        		whiteRequestHandler.informOfMove(outcome);
        		nextTurn = ChessTeam.WHITE;
        		
        	}
        	else if (nextTurn == ChessTeam.WHITE) {
        		outcome = whiteRequestHandler.takeTurn();
        		blackRequestHandler.informOfMove(outcome);
        		nextTurn = ChessTeam.BLACK;
        	}
        	
        	

        }
        //should still be the next turn in sequence even if timeout
        return nextTurn;
    	
    }


}