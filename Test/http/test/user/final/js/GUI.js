function GUI(frameControler, controllerName) {
	var obj = this;
	var canvas, physic, ctx, x;
	var count = 1;
	var mult = 0;
	var robotNum = 1;
	this.context;
	var myBots = [];
	var enemyBots = [];
	var allRobots = [];
	var RIP = [];
	//default values
	var start = new Date();
	var	canvas = document.getElementById('scene');

	//Construct
	//setting up the default attributes
	//add physics and gui function draw to framecontrollerNameName
	function construct() {
		canvas.width = 600;
		canvas.height = 600;
		obj.context = canvas.getContext("2d");
		frameControler.addOnNewFrame(draw);
		//physic = new CollisionControler(allRobots);
		//frameControler.addOnNewFrame(physic.onFrame);
	};

	//get actual robot number
	this.getRobotNum = function () {
		return robotNum;
	};
	//setter for robot position
	this.setBot = function (i, position) {
		if(allRobots[i]){
			allRobots[i].setDestination( position);
		}
	};
	//create a new robot
	this.newRobot = function(color) {
		console.log(color);
		var c = 1;
		if(controllerName == color) {
			c = 0;
		}
		for(var i = 0; i<=1; i++){
			
			var robo = new Robot(frameControler, c%2, robotNum );
			//increase number of robots
			robotNum++;
			//push robot into my robot list
			allRobots.push(robo);
			c++;
		}
	};

	//Gui Utility
	//
	//mouse on canvas detection (using robot.hitTest)
	//correct top/left navi bar offset with final variables (without var declaration)
	var html = document.body.parentNode;
	htmlTop = html.offsetTop;
	htmlLeft = html.offsetLeft;
	//correct mouse position to the canavs
	stylePaddingLeft = parseInt(document.defaultView.getComputedStyle(canvas, null)['paddingLeft'], 10) || 0;
	stylePaddingTop = parseInt(document.defaultView.getComputedStyle(canvas, null)['paddingTop'], 10) || 0;
	styleBorderLeft = parseInt(document.defaultView.getComputedStyle(canvas, null)['borderLeftWidth'], 10) || 0;
	styleBorderTop = parseInt(document.defaultView.getComputedStyle(canvas, null)['borderTopWidth'], 10) || 0;
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
	 	//var allRobots = getAllRobots();
		var pt = getMouse(e, canvas);
		for (var i = 0, l = allRobots.length; i < l; i++) {
			if (allRobots[i].hitTest(pt.x, pt.y)) {
				//allRobots[i].color = "#ffffff";
				console.log("AUSGEWAEHLTER ROBOT: " + (i+1));
				console.log(pt);
			}
		}
	};
	//END mouse detection
	
	//Utility
	//convert system ms to hh:minmin:secsec
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

	//drawing the content
	function draw() {
		
		mult += 0.05;
		if(mult == 2){ mult=0;}
		canvas.width = canvas.width;
		canvas.height = canvas.height;
		x = canvas.getContext("2d");
		
		x.beginPath();
		x.fillStyle = '#2F2F2F';
		x.arc( canvas.width/2, canvas.height/2, 300, 0, 2 * Math.PI, true);
		x.fill();
		
		x.beginPath();
		x.lineWidth = 600;
		x.arc(canvas.width/2, canvas.height/2,300,mult*Math.PI,(1.5+mult)*Math.PI, true) ;
		var grd=x.createRadialGradient(canvas.width/2, canvas.height/2, 600, canvas.width/2, canvas.height/2,20 );
		grd.addColorStop(0,"#0f0");
		grd.addColorStop(1,"#2f2f2f");
		x.strokeStyle = grd;
		/*
		x.moveTo(canvas.width/2, canvas.height/2);
		x.lineTo(canvas.width * Math.PI,canvas.height);
		x.lineTo(canvas.width * Math.PI, (canvas.height-100) * Math.PI);
		x.lineTo(canvas.width/2, canvas.height/2);
		*/
		
		x.stroke();
		
		
		x.beginPath();
		x.lineWidth = 1;
		x.strokeStyle = "#0d0";
		for(var i = 0; i <= 10; i++){
			x.arc( canvas.width/2, canvas.height/2, 30*i/*(59*i+i)*/, 0, 2 * Math.PI, false);
		}
		x.stroke();
		
		x.beginPath();
		x.strokeStyle = "#0f0";
		x.moveTo(canvas.width/2, 0);
		x.lineTo(canvas.width/2, canvas.height);
		x.lineWidth = 1;
		
		// set drawing style
        
		x.moveTo(0, canvas.height/2);
		x.lineTo(canvas.width, canvas.height/2);
        
        // actually start drawing
        x.stroke();
        x.strokeStyle = "#000";
		
		var time = parseInt((new Date().getTime() - start), 10);

		x.font = "13px Verdana";
		x.fillStyle = '#FFFFFF';
		x.fillText('Playtime:  ' + msToTime(time), 467, 10);
	}
	construct();
}