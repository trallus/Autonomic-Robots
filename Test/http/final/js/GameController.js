/**
 * GameController - contorls game network communication
 * @class GameController
 * @author Florian Krüllke
 * @version 0.1
 */

var GameController = {
	//Main
	/**
	 * main of GameController sets up a game
	 * @method mainGC
	 * @param {Controller} controller - controller-object to access user interactivity
	 */
	thisObj :  this,
	mainGC : function(controller) {

		
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
			$("body").append(controller.name);
			var robot = serverRobot();
			//console.log(robot);
			controller.setNextRobot(robot, function () { controller.joinBattleQuery( function(json) {
					gui.setStart();
					//console.log(json);
					
					//Intervall Gameloop
					intervallCounter = 30;
					var i = j = 0;
					var size;
					intervall = window.setInterval(function() {
					
						//get game situation in 1/sec up to 20x
						$.ajax({
							url : "serverRequest/game-getGameSituation"
						}).done(function(json) {

							position = json;
							
							//checking for new gameSituation & size of server robot list size to gui robot list size
							if (position.gameSituation && position.gameSituation[controller.name]) {
								
								
								//var me  = Object.keys(position.gameSituation[controller.name]);
								//console.log(Object.keys(position.gameSituation).length);
								
								/*
								
								size = Object.keys(position.gameSituation[controller.name]).length;
								console.log(position.gameSituation);
								if (size > gui.getRobotNum()/2) {
									
											gui.newRobot(Object.getOwnPropertyNames(position.gameSituation)[0]);
								}
								*/
								
								
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
							if (Object.keys(position.gameSituation).length == 0) {
								
								window.clearInterval(intervall);
								gui.clear();
								//controller.setRobotStats();
								controller.endGame(gui.getScore());
								win = gui.getWinner();
								//console.log(win);
								if(win == true) window.alert("V I C T O R Y");
								else if(win == false) window.alert("D E F E A T");
								else window.alert("D R A W");;
								delete thisObj;
								//controller.loadHome();
								/*$.ajax({
									url : "home.html"
								}).done(function(html) {
									$("#content").html(html).promise().done(function() {
										regisButtons();
									});
								});*/
							}
							
						});
						//end game after 30x
						//Intervall next robot
						/*
						if(intervallCounter%10 == 0) {
							controller.getRobot();
							robot = serverRobot();
							controller.setNextRobot(robot, function () {});
							console.log(robot);
						}
						*/
						//SERVERPOINT -> auskommentieren Zeile 65 & 72
						//kommentiert es aus und probierts
						//bzw, das Spiel muss dann serverseitig irgendwie enden

						//if ( ! obj.position && ! obj.position.gameSituation.name) {
						
						}, 300);
					});
				}
			);
		}
		//Set next serverRobot
		/**
		 * get roboSet array to fill next robot values
		 * @return r - JSON robot
		 */
		function serverRobot() {
			roboSet = controller.roboSet;
			//var roboSet = set;
			r = {
				weaponPrototype : {
					range : roboSet[0],
					rateOfFire : roboSet[1],
					damage : roboSet[2]
				},
				armor : roboSet[3],
				enginePower : roboSet[4],
				behaviour : roboSet[5]
			};
			return r;
		};
		//Get robot values set by the user
		/**
		 * get robot
		 * @return roboSet - array of robot values
		 */
		function getRobot () {
			roboSet = [];
			roboSet[0] = $("#range-setter").value;
			roboSet[1] = $("#rateOfFire-setter").value;
			roboSet[2] = $("#damage-setter").value;
			roboSet[3] = $("#armor-setter").value;
			roboSet[4] = $("#enginePower-setter").value;
			roboSet[5] = $("#behavior-setter").value;
			return roboSet;
		}
	}
};