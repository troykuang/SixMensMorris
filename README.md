# 3 Men's Morris #

	This project implements the game 3 Men's Morris in Java. The user interface is implemented using JavaFX and the Scenebuilder. 

	It was decomposed into the MVC modules (Model, View, Controller).


**********************************************************************************************

## Module Decomposition ##

	Controller Module
		Relays between the logic in the model and the graphical User interface created by the View.
	Model Module
		Contains the logic for the game. Uses the Point and Board classes to store game variables and states and check logic of game play actions.
	Point Module
		“INSERT INFO”
	Board Module
		“INSERT INFO”
	View Module
		Creates the Stage for JavaFX GUI. Integrates the fxml files with overlayed dynamically created objects such as the board specified through the Model. 
	titleScreen.fxml
		The game’s title screen which includes accessing colour choice, new game, and load game. Such input choices are dealt with in the Controller.
	gameScreen.fxml
		This is the fxml file that is added to the Stage created in the View. It is utilized when the player wants to start a new game.
	gameBoard.fxml
		This is the fxml file added to the Stage created in the View when the player wants to load a game and set all the pieces. 
	application.css
		This is a module that effects design aesthetics for the objects in the fxml files. 


**********************************************************************************************

Created in Collaboration.

Collaborators:

Troy Kuang
Rebecca Tran

**********************************************************************************************

Natasha DeCoste
