var GameController = {
	
	//main control of the game
	main : function(controller) {

		var frameControler = new FrameControler();
		var gui = new GUI(frameControler, controller.name);
		
		//get into battle queue & create gameSituatuion
		getNextServerFrame();

		frameControler.setGUI(gui);
		frameControler.start();

		$("body").keydown(function(event) {
			if (event.keyCode == 80) {
				frameControler.pauseButton();
				gui.on = false;
			}

		});
		//setting up the game situation after joining battle queue
		function getNextServerFrame() {
			var position;
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
					console.log(json);
					var intervallCounter = 20;
					var intervall = window.setInterval(function() {
						//get game situation in 1/sec up to 20x
						$.ajax({
							url : "serverRequest/game-getGameSituation"
						}).done(function(json) {

							position = json;
	
							//var i = 0;
							//checking for new gameSituation & size of server robot list size to gui robot list size
							if (position.gameSituation && position.gameSituation[controller.name]) {
								var size = Object.keys(position.gameSituation[controller.name]).length;
								if (size > gui.getRobotNum()/2) {
										//for(var i = 0; i < size; i++){
											gui.newRobot(Object.getOwnPropertyNames(position.gameSituation)[0]);
											//console.log("hopp");
										//}
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
		//get roboSet array to fill next robot values
		function serverRobot() {
			//controller.getRobot();
			var roboSet = controller.roboSet;
			var r = {
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
		function getRobot () {
			var roboSet = [];
			roboSet[0] = $("#range-setter").val();
			roboSet[1] = $("#rateOfFire-setter").val();
			roboSet[2] = $("#damage-setter").val();
			roboSet[3] = $("#armor-setter").val();
			roboSet[4] = $("#enginePower-setter").val();
			roboSet[5] = $("#behavior-setter").val();
			return roboSet;
		}
	}
};