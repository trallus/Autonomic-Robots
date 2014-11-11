var GameController = {
	/**
	 * vars roboSet[]
	 * 
	 * server() to timer calls gui.newBot and on get receive => roboServerList 
	 */
    main : function (controller) {
		
        var frameControler = new FrameControler();
        var server = new swp(controller);
        //server.serverRobo(controller.roboSet);
        var gui = new GUI ( frameControler, server, controller.name );
        
        
        
        frameControler.setGUI ( gui );
        frameControler.start();

        $("body").keydown(function(event) {
            if(event.keyCode == 80){
                frameControler.pauseButton();
                gui.on = false;
            }
            
        });
        
    
    
    getNextServerFrame : function () {
		//prototype robot (set new Robot() give back) - SET ROBOT FORM
		/*
		var robot = {
		weaponPrototype : {
		range : 100,
		rateOfFire : 30,
		damage : 10
		},
		//color: controller.colors[0],
		armor : 100,
		enginePower : 100,
		behaviour : "gibts noch nicht"
		};
		*/
		//obj.serverRobo();

		$.ajax({
			type : "POST",
			data : JSON.stringify(obj.robot/*GameController.allRobots*/ ),
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
					
						obj.position = json;
						var i = 0;
		
				        if (obj.position.gameSituation) {
				        	for (var names in obj.position.gameSituation) {
				        		for (var keys in obj.position.gameSituation[names]) {
				        			i++;
				        			obj.allRobots[i-1] = obj.position.gameSituation[names][keys].position;
				        			//console.log(obj.allRobots);
				        			}
				        		}
				        }
				        //console.log(obj.position);
						
					});
					
					//end game after 20x
					var name = controller.name;
					//console.log(name);
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
								//startDebug();
							});

						});

					}
				}, 1000);
			});
		});
	}
}
    


