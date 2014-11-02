function GUI(frameControler, server) {
	var obj = this;
	var canvas;
	var ctx;
	this.context;
	var x;
	var start;
	var count = 10;
	var counter = setInterval(timer, 1000);
	var robotNum = 1;
	var data;
	var $select;
	var color;
	var robo;
	var e;
	var selectedItem;
	var allRobots = [];
	var physic;

	//mouse on canvas detection (using robot.hitTest)
	//setting request animation frame for different browser
	window.requestAnimFrame = (function() {
		return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame ||
		function(callback) {
			window.setTimeout(callback, 1000 / 60);
		};
	})();
	//correct top/left navi bar offset
	var html = document.body.parentNode;
	htmlTop = html.offsetTop;
	htmlLeft = html.offsetLeft;
	canvas = document.getElementById('scene');
	//correct mouse position to the canavs
	stylePaddingLeft = parseInt(document.defaultView.getComputedStyle(canvas, null)['paddingLeft'], 10) || 0;
	stylePaddingTop = parseInt(document.defaultView.getComputedStyle(canvas, null)['paddingTop'], 10) || 0;
	styleBorderLeft = parseInt(document.defaultView.getComputedStyle(canvas, null)['borderLeftWidth'], 10) || 0;
	styleBorderTop = parseInt(document.defaultView.getComputedStyle(canvas, null)['borderTopWidth'], 10) || 0;
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
	/*canvas.onmousemove*/
	//detect mouse over robot
	canvas.onmousedown = function(e) {
		var pt = getMouse(e, canvas);
		console.log(pt);
		for (var i = 0, l = allRobots.length; i < l; i++) {
			if (allRobots[i].hitTest(pt.x, pt.y)) {
				allRobots[i].color = "#ffffff";
				console.log("AUSGEWAEHLTER ROBOT: " + i);
			}
			// Set the color to red to demonstrate the "hit"
		}
	};
	//END mouse detection
	
	
	//setting up the default attributes
	function construct() {
		

		//start = new Date();
		//ctx = document.getElementById('info');
		//ctx.width = 200;
		//ctx.height = 400;
		//x = ctx.getContext('2d');

		canvas.width = 600;
		canvas.height = 423;
		obj.context = canvas.getContext("2d");
		//fillSelect();

		frameControler.addOnNewFrame(draw);

		physic = new CollisionControler(allRobots);
		frameControler.addOnNewFrame(physic.onFrame);
	};
	
	//create a new robot
	this.newRobot = function(id/*, pos*/) {
		var id = robotNum - 1;
		server.serverRobo(controller.roboSet);
		//console.log(server.serverRobo(controller.roboSet));
		//each robot knows the server and get an id
		robo = new Robot(frameControler, server, id /*, pos*/ );
		//server.robot = robo.serverRobo(contoller.roboSet);
    	
		//color = controller.colors[id];
		//push robot into robot list
		allRobots.push(robo);
	};
	
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

	function timer() {
		count--;
		if (server.position.gameSituation == undefined) {
		} else {
			var name = Object.getOwnPropertyNames (server.position.gameSituation)[0];
			//represent the server number of robot
			var size = Object.keys(server.position.gameSituation[name]).length;
			//size triggers the point of robot creation for the gui
			if (size == robotNum) {
				var gameSituation = server.position.gameSituation;
				//check for changes!!!
				for (var key in gameSituation) {
					var robots = gameSituation[key];
					for (var robot in robots) {
						//here check for robot id, changes
						//setRobot if new id new robt()
					}
				}
				//create a new robot
				obj.newRobot();
				robotNum++;
				fillSelect();
				count = 10;
			}
			if (size > 1) {
				clearInterval(counter);
			}
		}
		requestAnimFrame(timer);
		//timer();
	}

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
       // context.fill();
        // actually start drawing
        //x.stroke();
        
        //x.beginPath();
		x.moveTo(0, canvas.height/2);
		x.lineTo(canvas.width, canvas.height/2);
		//x.lineWidth = 1;
		// set drawing style
        x.strokeStyle = "#0f0";
       // context.fill();
        // actually start drawing
        x.stroke();
        x.strokeStyle = "#000"
		//ctx.width = ctx.width;
		/*
		var time = parseInt((new Date().getTime() - start), 10);

		//x.fillText( ((time / (60 * 1000)) % 60) + ':' +  ((time / 1000) % 60) + '<br/>' , 10, 10);
		x.font = "15px Verdana";
		//x.font = "16px Andale Mono";
		x.fillStyle = '#FFFFFF';
		x.fillText('Playtime:  ' + msToTime(time), 15, 30);
		x.fillText('New Robot in:       ' + count, 15, 75);
		x.fillText('Count of Robots:   ' + (robotNum - 1), 15, 120);

		//e = document.getElementById("#robots");
		e = $("#robots").children(":selected").text();
		if (e != '') {
			//selectedItem = e.options[e.selectedIndex].text;

			x.fillText('Selected:     ' + e, 15, 165);
			e = '';
		}
		x.fillStyle = color;
		//console.log(color);
		x.fillText('LAST ROBOT COLOR', 15, 360);
		*/

	}

	construct();
}