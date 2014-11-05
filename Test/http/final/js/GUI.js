function GUI(frameControler, server, controller) {
	var obj = this;
	var canvas, physic, ctx, x, data, $select, color, robo, e, selectedItem;
	var count = 1;
	var robotNum = 1;
	this.context;
	var colors = [];
	var allRobots = [];
	var RIP = [];
	//default values
	var counter = setInterval(timer, 1000);
	var start = new Date();
	var	canvas = document.getElementById('scene');
	//correct top/left navi bar offset
	
	var html = document.body.parentNode;
	htmlTop = html.offsetTop;
	htmlLeft = html.offsetLeft;
	//correct mouse position to the canavs
	stylePaddingLeft = parseInt(document.defaultView.getComputedStyle(canvas, null)['paddingLeft'], 10) || 0;
	stylePaddingTop = parseInt(document.defaultView.getComputedStyle(canvas, null)['paddingTop'], 10) || 0;
	styleBorderLeft = parseInt(document.defaultView.getComputedStyle(canvas, null)['borderLeftWidth'], 10) || 0;
	styleBorderTop = parseInt(document.defaultView.getComputedStyle(canvas, null)['borderTopWidth'], 10) || 0;
	
	//Construct
	//setting up the default attributes
	//add physics and gui function draw to framecontroller
	function construct() {
		canvas.width = 600;
		canvas.height = 423;
		obj.context = canvas.getContext("2d");
		frameControler.addOnNewFrame(draw);
		physic = new CollisionControler(allRobots);
		frameControler.addOnNewFrame(physic.onFrame);
	};
	
	//create a new robot
	this.newRobot = function(id/*, pos*/) {
		var id = robotNum - 1;
		//server.serverRobo(controller.getRobot);
		//console.log(controller.getRobot());
		//server.serverRobo(controller.getRobot());
		//console.log(server.serverRobo(controller.roboSet));
		
		
		//
		//create two robots one for user, one for enemy
		//each robot knows the server and get an id
		
		//robo = new Robot(frameControler, server, id + 'enemy');
		//enemyRobots.push(robo);
		//allRobots.push(robo);
		robo = new Robot(frameControler, server, id /*, pos*/ );
		
		
		
		//push robot into robot list
		allRobots.push(robo);
	};
	
	//global ticker	
	function timer() {
		count--;
		if (server.position.gameSituation == undefined) {
		} else {
			var name = Object.getOwnPropertyNames (server.position.gameSituation)[0];
			//represent the server number of robot
			var size = Object.keys(server.position.gameSituation[name]).length;
			//size triggers the point of robot creation for the gui
			if (size == robotNum) {
			/*
				var gameSituation = server.position.gameSituation;
				//check for changes!!!
				console.log(server.position.gameSituation);
				for (var key in gameSituation) {
					var robots = gameSituation[key];
					for (var robot in robots) {
						//here check for robot id, changes
						//setRobot if new id new robt()
					}
				}
				*/
				//console.log(server.position.gameSituation);
				//create a new robot
				obj.newRobot();
				robotNum++;
				count = 10;
			}
			if (size > 1) {
				clearInterval(counter);
			}
		}
		//requestAnimFrame(timer);
		//timer();
	}
	
	//WORK
	//
	//set a robot
	function changeRobotProp(serverRobot) {
		for (var robot in allRobots) {
			if (robot.id == serverRobot.id) {
				robot.serPosition(serverRobot.position);
				return;
			}
		}

		//Robot erstellt sich aus den Eigenschaften von serverRobot
		allRobots.push(new Robot(serverRobot));
	}

	//todo: 2 Listen, not in gameSituation List -> tod
	//show middle of map
	function fillSelect() {
		var data = [{
			'1' : 'Robot ' + (robotNum - 1)
		}];
		$select = $('#robots');
		$.each(data, function(i, val) {
			$select.append($('<option />', {
				value : (i),
				text : val[i + 1]
			}));
		});

	};
	
	//Gui Utility
	//
	//mouse on canvas detection (using robot.hitTest)
	//setting request animation frame for different browser
	window.requestAnimFrame = (function() {
		return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame ||
		function(callback) {
			window.setTimeout(callback, 1000 / 60);
		};})();
	
	//return a set of x & y coordinates
	function getMouse(e, canvas) {
		var element = canvas, offsetX = 0, offsetY = 0, mx, my;

		// Compute the total offset. It's possible to cache this if you want
		if (element.offsetParent !== undefined) {
			do {
				offsetX += element.offsetLeft;
				offsetY += element.offsetTop;
			} while ((element = element.offsetParent));
		}

		// Add padding and border style widths to offset
		// Also add the <html> offsets in case there's a position:fixed bar (like the stumbleupon bar)
		// This part is not strictly necessary, it depends on your styling
		offsetX += stylePaddingLeft + styleBorderLeft + htmlLeft;
		offsetY += stylePaddingTop + styleBorderTop + htmlTop;

		mx = e.pageX - offsetX;
		my = e.pageY - offsetY;

		//return a simple javascript object with x and y defined
		return {
			x : mx,
			y : my
		};
	};
	//detect mouse over robot
	canvas.onmousedown = function(e) {
		var pt = getMouse(e, canvas);
		console.log(pt);
		for (var i = 0, l = allRobots.length; i < l; i++) {
			if (allRobots[i].hitTest(pt.x, pt.y)) {
				allRobots[i].color = "#ffffff";
				console.log("AUSGEWAEHLTER ROBOT: " + i);
				//allRobots.push({id: i});
				//console.log(allRobots[i]);
				console.log(pt);
				s = document.createElement("article");
				s.setAttribute("class", "pushSelection");
				var e = document.createElement("P");
				e.innerHTML="robot " + i + " selected";
				s.appendChild(e);
				document.getElementById("gameSelect").appendChild(s);
			}
			// Set the color to red to demonstrate the "hit"
		}
	};
	//END mouse detection

	function msToTime(s) {

		function addZ(n) {
			return (n < 10 ? '0' : '') + n;
		}

		var ms = s % 1000;
		s = (s - ms) / 1000;
		var secs = s % 60;
		s = (s - secs) / 60;
		var mins = s % 60;
		var hrs = (s - mins) / 60;

		return addZ(hrs) + ':' + addZ(mins) + ':' + addZ(secs);
		//'.' + ms;
	}

	function draw() {
		canvas.width = canvas.width;
		canvas.height = canvas.height;
		x = canvas.getContext("2d");
		x.beginPath();
		x.moveTo(canvas.width/2, 0);
		x.lineTo(canvas.width/2, canvas.height);
		x.lineWidth = 1;
		
		// set drawing style
        x.strokeStyle = "#0f0";
		x.moveTo(0, canvas.height/2);
		x.lineTo(canvas.width, canvas.height/2);
        //x.strokeStyle = "#0f0";
        
        // actually start drawing
        x.stroke();
        x.strokeStyle = "#000";
		
		var time = parseInt((new Date().getTime() - start), 10);

		x.font = "13px Verdana";
		x.fillStyle = '#FFFFFF';
		x.fillText('Playtime:  ' + msToTime(time), 469, 10);
		x.fillText('New Robot in:   ' + count, 0, 10);
		//x.fillText('Count of Robots:   ' + (robotNum - 1), 15, 120);
		
		/*//e = document.getElementById("#robots");
		e = $("#robots").children(":selected").text();
		if (e != '') {
			//selectedItem = e.options[e.selectedIndex].text;

			x.fillText('Selected:     ' + e, 15, 165);
			e = '';
		}
		//x.fillStyle = color;
		//console.log(color);
		//x.fillText('LAST ROBOT COLOR', 15, 360);
		*/

	}

	construct();
}