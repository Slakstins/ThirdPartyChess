Third Person Chess
========

A GUI based chess game with socket functionality and a twist: if the turn timer of a player runs out, a third party gets to take a turn. In this case, that third party is the host, and the two clients are the ones playing the "normal" game of chess.

Running the Game
----------------

Open a copy of the game on three devices. One device, the host, must enter a valid port number and click "Start Server".
The other two players enter the host's IP address and the socket entered by the host and then click "Start Client".
The host can then press the "Start" button to begin the game for all three players.

Features
--------
A base chess game - created by
Ashish Kedia (ashish1294@gmail.com) and
Adarsh Mohata (amohta163@gmail.com)
as an OOP chess project.

Third player capability - the plan here was that the third player would have win conditions, such as removing all 4 rooks from the game, but we didn't get around to that.

Turn timout and turn timer

Socket communication

Multi-threading utilized in several places

Developing the Project
----------------------

The base project that we started with was not perfect, but that made things much more interesting than if it had been. We attempted to add en passant and castling to the chess logic, but the program was hard coded to only be capable of moving/removing a single piece graphically per turn, so there are some issues with one piece's graphics persisting until the space is refreshed in some way.

Who did what?
----------------------
Seth Lakstins set up the socket connections, timeouts, gui updates, and in game timer display.

Joey Hegg added in chess logic for en passant and castling and pawn promotion. Joey Hegg also handled third party win conditions, as well as refining the existing protocol for third party transitioning.

Links To Repo and Demo
----------------------
Repo: https://github.com/Slakstins/ThirdPartyChess

Demo: https://rose-hulman.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=c25e0d21-6272-404c-98ae-ad2e014569b4

Please let us know if you cannot access these links for whatever reason!
