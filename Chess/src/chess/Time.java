package chess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

import sockets.ChessTeam;
import sockets.SocketTimer;


/**
 * This is the Time Class.
 * It contains all the required variables and functions related to the timer on the Main GUI
 * It uses a Timer Class
 *
 */

public class Time
{
	//in MS - implementation may change
    private JLabel label;
    static Timer whiteCountdownTimer;
    static Timer blackCountdownTimer;
    static int whiteTimerem;
    static int blackTimerem;
    public Time(JLabel passedLabel)
    {
       whiteCountdownTimer = new Timer(1000, new CountdownTimerListener());
       blackCountdownTimer = new Timer(1000, new CountdownTimerListener());

       this.label = passedLabel;
       whiteTimerem=(int) (SocketTimer.TURNTIME / 1000);
       blackTimerem=(int) (SocketTimer.TURNTIME / 1000);
    }
    
    public static void setTimersPaused() {
    		((CountdownTimerListener)whiteCountdownTimer.getActionListeners()[0]).setText("timer paused");
    		((CountdownTimerListener)blackCountdownTimer.getActionListeners()[0]).setText("timer paused");
    		whiteCountdownTimer.stop();
    		blackCountdownTimer.stop();
    	
    }
    
    //A function that starts the timer
    public static void update()
    {
    	whiteCountdownTimer.stop();
    	blackCountdownTimer.stop();
    	//start the correct timer
    	//if it's the third player's turn start no timers
    	System.out.println("time updating!");
    	System.out.println(Main.whoseTurn.name());
    	if (Main.whoseTurn == ChessTeam.THIRD) {
    		setTimersPaused();
    		return;
    	}
    	else if (Main.whoseTurn  == ChessTeam.BLACK) {
			blackCountdownTimer.start();
		}
		else if (Main.whoseTurn == ChessTeam.WHITE) {
			whiteCountdownTimer.start();
		}
    }
    
    //A function that resets the timer
    public static void resetWhite()
    {
    	whiteTimerem= (int) (SocketTimer.TURNTIME / 1000);
    }

    //A function that resets the timer
    public static void resetBlack()
    {
    	blackTimerem= (int) (SocketTimer.TURNTIME / 1000);
    }

    
    
    //A function that is called after every second. It updates the timer and takes other necessary actions
    class CountdownTimerListener implements ActionListener
    {
    	public void setText(String string) {
    		label.setText(string);
    	}
        public void actionPerformed(ActionEvent e)
        {
         long timeleft = 0;
       	 int min,sec;
       	 if (Main.chance == 0) {
       		 timeleft = whiteTimerem;
       	 }
       	 else if (Main.chance == 1) {
       		 timeleft = blackTimerem;
       	 }

       	 if (timeleft > 0) {
           	min= (int) (timeleft/60);
           	sec=(int)(timeleft%60);
            label.setText(String.valueOf(min)+":"+(sec>=10?String.valueOf(sec):"0"+String.valueOf(sec)));
       	    if (Main.chance == 0) {
       		    whiteTimerem--;
       	    }
       	    else if (Main.chance == 1) {
       		    blackTimerem--;
       	    }

         }
       	 else
       	 {
		   label.setText("Time's up!");
		   if (Main.chance == 0) {
			   System.out.println("resetting white timer");
			   resetWhite();
		   }
		   else if (Main.chance == 1) {
			   System.out.println("resetting black timer");
			   resetBlack();
		   }
		   // change the turn to the third player
		   if (Main.player != ChessTeam.THIRD) {
			   Main.whoseTurn = ChessTeam.THIRD;
			   Time.update();
		   }
		   //start();
       	 }
    }
 }
}