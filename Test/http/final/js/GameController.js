/**
 * GameController - contorls game network communication
 * @class GameController
 * @author Florian Krüllke
 * @version 0.1
 */
var GameController = {
	/**
    * relate onjekt to himself as an variable to delete it later
    * @type GrameControler (this)
    * @memberof GameController
    */
	thisObj :  this,
	//Main
	/**
	 * main of GameController sets up a game
	 * @method mainGC
	 * @param {Controller} controller - controller-object to access user interactivity
	 */
	mainGC : function(controller, robot) {
		/**
	    * create a new frameContoller
	    * @type FrameControler
	    * @memberof GameController
	    */
		var frameControler = new FrameControler();
		/**
	    * create a new GUI
	    * @type GUI
	    * @memberof GameController
	    */
		var gui = new GUI(frameControler, controller.name);
		//get into battle queue & create gameSituatuion
		getNextServerFrame();
		//relate GUI to the frame controller
		frameControler.setGUI(gui);
		frameControler.start();
		//set the game to pause via key "p" pressed
		$("body").keydown(function(event) {
			if (event.keyCode == 80) {
				frameControler.pauseButton();
				gui.on = false;
			}
		});
		//Get next server frame
		/**
		 * start game request, joining batlle queue
		 */
		function getNextServerFrame() {
			//$("body").append(controller.name);
			controller.setNextRobot(robot, function () { controller.joinBattleQuery( function(json) {
					gui.setStart();
					//Intervall Gameloop
					intervallCounter = 30;
					var i = 0;
					var size;
					intervall = window.setInterval(function() {
						//get game situation in 1/sec up to 20x
						$.ajax({
							url : "serverRequest/game-getGameSituation"
						}).done(function(json) {
							position = json;
							//checking for new gameSituation & size of server robot list size to gui robot list size
							if (position.gameSituation && position.gameSituation[controller.name]) {
								//setting new positions for each robot by robot.id in gui > allRobots[]
								var target;
								var shot = [];
								for (var names in position.gameSituation) {
									for (var keys in position.gameSituation[names]) {
										if (position.gameSituation[names][keys].shotTarget) {
											
											shot[0] = position.gameSituation[names][keys].position;
											shot[1] = position.gameSituation[names][keys].shotTarget;
										}
										if(position.gameSituation[names][keys].id > i){
											i = position.gameSituation[names][keys].id;
											
											if (i%2 == 1) gui.newRobot(i ,names);
										}
										gui.setBot(position.gameSituation[names][keys].id, position.gameSituation[names][keys].position, position.gameSituation[names][keys].hitPoints, shot);
									}
								}
							}
							//ending a game if gameSituation is empty
							if (Object.keys(position.gameSituation).length == 0) {
								window.clearInterval(intervall);
								gui.clear();
								controller.endGame(gui.getScore(), gui.getWinner());
								delete thisObj;
							}
						});}, 300);
					});
				}
			);
		}
	}
};