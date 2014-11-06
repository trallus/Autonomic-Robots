function swp(controller) {
	var obj = this;
	this.robot
	this.position = false;
	this.serverRobo = function() {
		//controller.getRobot();
		roboSet = controller.roboSet;
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
		obj.robot = r;
		console.log(obj.robot);

	};
	obj.serverRobo();

	function getNextServerFrame() {
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
					});
					
					//end game after 20x
					var name = controller.name;
					//console.log(name);
					//intervallCounter--;

					//SERVERPOINT -> auskommentieren Zeile 65 & 72
					//kommentiert es aus und probierts
					//bzw, das Spiel muss dann serverseitig irgendwie enden
					
					if ( ! obj.position && ! obj.position.gameSituation.name) {
					//if (intervallCounter == 0) {
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

		/*
		 $.ajax({
		 url: "serverFrameRequest",
		 data: "player1",
		 error: function ( info, b, c ) {
		 console.log(info);
		 console.log(b);
		 console.log(c);
		 }
		 }).done ( function ( json ) {
		 obj.position = json;
		 console.log("Server-Client latenz im ms: " + (json.time - new Date().getTime()));
		 getNextServerFrame();
		 }).fail ( function ( info ) {
		 console.log(info);
		 });

		 $.ajax({
		 url: "/",
		 error: function ( info, b, c ) {
		 console.log(info);
		 console.log(b);
		 console.log(c);
		 }
		 }).done ( function ( json ) {
		 }).fail ( function ( info ) {
		 console.log(info);
		 });
		 */
	}

	getNextServerFrame();
}