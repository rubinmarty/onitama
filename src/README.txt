=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: _______
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 	All piece positions are stored in a 2D array inside the GameData object. The array is called boardData. That
  	array is then wrapped in an object to make it read-only before it is passed to the players.
  		A 2D array is a good way of storing piece positions because lookup/insertion are both incredibly quick. The size
  	of the board never changes, so you don't have to worry about the inability to resize an array.

  2.	Whenever you play a game, each move from the game (as well as the game's initial state) is automatically stored in
  	a file called "myReplay.txt". After playing a particularly thrilling game, click the Previous Game Replay button in the main
  	menu to watch a re-enactment. Because the data is stored in a file, you can replay old games even after closing and re-opening
  	the application.

  3.	Many types in this game use a subtyping / inheritance model. For example, Soul.java is an interface that describes the
  	methods any computer-controlled player must have. Both the AI Objects and the Replay Objects implement this interface,
  	so that the game can call [soulInstance].getAction and be passed a move without having to know anything about the type of player
  	it's asking for a move from.
  		Other types of inheritance include Move.RelativeMove.java, which extends Move.java, which extends Vector.java. Additionally,
  	various custom JComponents extend either JComponent of JPanel. For example, RotatingButton.java extends Button.java which extends
  	JPanel.

  4.	My game allows you to choose whether each player will be controlled by a Human, an EasyAI, or a MediumAI. The EasyAI more or
  	less calculates every possible move it could make and picks one at random. The MediumAI, on the other hand, will always take a winning
  	move if one is available. When one isn't, it's also able to prefer moves that involve capturing enemy pieces over ones that don't. Failing
  	that, it tends to play fairly defensively and not over-extend.


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  
  Board - A custom JComponent used for drawing the gameboard portion of the game-state.
  Board.Tile - A custom JComponent used to represent a single tile on the gameboard.
  Button - A custom JComponent similar to JButton.
  Hand - A custom JComponent used for holding the MoveCards.
  MoveCard - A custom JComponent used to represent the move cards a player holds.
  RotatingButton - A custom JComponent. Like a button, but it rotates through multiple states.
  
  EasyRobotSoul - The AI behind the easy computer.
  FileSoul - The AI behind replaying a game from a file.
  Game - Main file that launches the application.
  GameData - Stores all the relevent board-state, and updates it when requested by the engine.
  GameEngine - Makes the game tick, prompts players for moves when relevant and ends the game when a player wins.
  GameField - Responsible for holding all the various JComponents that represent game-state.
  MediumRobotSoul - The AI behind the medium computer.
  MoveSet - Actually a list, an object that holds all the possible Moves of a particular card.
  Piece - Object that represents a piece in the game.
  PieceType - Enum of the two possible types of peices.
  Play - An object that totally describes a play a player might make on his turn. Gets validated and run by the engine when submitted.
  Player - Represents one of the players in the game.
  PlayerType - Represents the types of players. Passed to the engine when initializing a game.
  PlaySubmitter - An object the engine can hand to a player to allow them to submit plays. The gameboard also holds one to
  	allow to plays submitted through the GUI.
  Soul - Interface for implementing AIs.
  Vector - Simple class used to represent spaces on the board as well as translations from one space to another. Has an x field and a y field.
  


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  
  The hardest part was probably getting used to using Swing. Figuring out how to make JComponents layout nicely was a challenge.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

  There's definitely a good separation of functionality - basically whenever I had one class responsibile for two different things, I
  cut it in half. Private state is somewhat encapsulated but there are definitely some public fields that I ought to refactor to make private
  (doing so would require a large overhaul). 

========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.

  https://docs.oracle.com/javase/tutorial/uiswing/layout/visual.html
