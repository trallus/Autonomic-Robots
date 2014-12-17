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
			//send first robot settings
			$.ajax({
				type : "POST",
				data : JSON.stringify(serverRobot()/*getting actual selection for robot values*/),
				dataType : "json",
				url : "serverRequest/game-setNextRobot"
			}).done(function(json) {
				console.log(json);
				//join BattleQuery
				$.ajax({
					url : "serverRequest/game-joinBattleQuery"
				}).done(function(json) {
					gui.setStart();
					console.log(json);
					roboSet = [];
					var i = 0;
					$(".setNext input").each(function(){roboSet[i]=this.value;i++;});
					console.log(roboSet);
					intervallCounter = 100;
					intervall = window.setInterval(function() {
					
						//get game situation in 1/sec up to 20x
						$.ajax({
							url : "serverRequest/game-getGameSituation"
						}).done(function(json) {

							position = json;
	
							//checking for new gameSituation & size of server robot list size to gui robot list size
							if (position.gameSituation && position.gameSituation[controller.name]) {
								size = Object.keys(position.gameSituation[controller.name]).length;
								if (size > gui.getRobotNum()/2) {
											gui.newRobot(Object.getOwnPropertyNames(position.gameSituation)[0]);
								}
								//setting new positions for each robot by robot.id in gui > allRobots[]
								for (var names in position.gameSituation) {
									for (var keys in position.gameSituation[names]) {
										gui.setBot(position.gameSituation[names][keys].id, position.gameSituation[names][keys].position);
									}
								}
							}
						});
						//end game after 20x
						intervallCounter--;

						//SERVERPOINT -> auskommentieren Zeile 65 & 72
						//kommentiert es aus und probierts
						//bzw, das Spiel muss dann serverseitig irgendwie enden

						//if ( ! obj.position && ! obj.position.gameSituation.name) {
						if (intervallCounter == 0) {
							window.clearInterval(intervall);
							$.ajax({
								url : "home.html"
							}).done(function(html) {
								$("#content").html(html).promise().done(function() {
									regisButtons();
									$("#setNext").hide();
									controller.logOut();
									controller.overlay('JUST REFRESH THE PAGE');
								});
							});
						}
					}, 1000);
				});
			});
		}
		//Set next serverRobot
		/**
		 * get roboSet array to fill next robot values
		 * @return r - JSON robot
		 */
		function serverRobot() {
			//controller.getRobot();
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