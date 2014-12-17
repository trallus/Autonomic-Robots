/**
 * GUI - sets up the canvas drawing part of the GUI
 * @class GUI
 * @author Florian Krüllke
 * @version 0.1
 * @param {FrameControler} frameControler - controls frame activity
 * @param {String} controllerName - name of the user
 */

function GUI(frameControler, controllerName) {
	var obj = this, canvas, physic, x, mult = 0, robotNum = 1, allRobots = [], RIP = [],canvas = document.getElementById('scene');
	this.context;
	valid = false;

	//Construct
	//
	/**
	 * setting up the default attributes/add physics and gui function draw to framecontrollerNameName
	 */
	function construct() {
		canvas.width = 600;
		canvas.height = 600;
		obj.context = canvas.getContext("2d");
		frameControler.addOnNewFrame(draw);
		physic = new CollisionControler(allRobots);
		frameControler.addOnNewFrame(physic.onFrame);
	};

	//Get current count of robots
	/**
	 * get actual robot number
	 * @return {Number} robotNum - count of robots
	 */
	this.getRobotNum = function () {
		return robotNum;
	};
	
	//Change position of a robot
	/**
	 * set robot position
	 * @param {Number} i - robot id
	 * @param {Array} position - array with position values
	 */
	this.setBot = function (i, position) {
		size = Object.keys(allRobots).length;
		for(var j = 0; j < size; j++) {
			r = allRobots[j];
			if(r && r.getId() == i+1) r.setDestination(position);
		}
	};
	//Create a new robot
	/**
	 * create a new robot
	 * @param {String} name - user name (for displaying right color of an frinedly or enemy robot)
	 */
	this.newRobot = function(name) {
		//on first start -> drawing play time via valid=true, set start time
		c = 1;
		if(controllerName == name) {c = 0;}
		for(var i = 0; i<=1; i++){
			robo = new Robot(frameControler, c%2, robotNum );
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
	
	/**
	 * return a set of x & y coordinates
	 * @param {Event} e - mouse event
	 * @param {Canvas} canvas - game canvas element
	 * @return Object - containing current mouse position (x,y)
	 */
	function getMouse(e, canvas) {
		var element = canvas, offsetX = 0, offsetY = 0;

		// Compute the total offset. It's possible to cache this if you want
		if (element.offsetParent !== undefined) {
			do {
				offsetX += element.offsetLeft;
				offsetY += element.offsetTop;
			} while ((element = element.offsetParent));
		}
		var resultOffsetX = offsetX;
		// Add padding and border style widths to offset
		// Also add the <html> offsets in case there's a position:fixed bar (like the stumbleupon bar)
		// This part is not strictly necessary, it depends on your styling
		resultOffsetX += stylePaddingLeft + styleBorderLeft + htmlLeft;
		offsetY += stylePaddingTop + styleBorderTop + htmlTop;

		//return a simple javascript object with x and y defined
		return {
			x : e.pageX - resultOffsetX,
			y : e.pageY - offsetY
		};
	};
	//Mouse click on robot detection
	/**
	 * detect mouse over robot
	 * @param {event} e - mouse event
	 */
	canvas.onmousedown = function(e) {
		pt = getMouse(e, canvas);
		for (var i = 0, l = allRobots.length; i < l; i++) {
			if (allRobots[i].hitTest(pt.x, pt.y)) {
				console.log("AUSGEWAEHLTER ROBOT: " + (i+1));
				console.log(pt);
			}
		}
	};
	//END mouse detection
	
	//Utility
	/**
	 * convert system ms to (hh:minmin:secsec)
	 * @param {Date} s - date time in ms
	 * @return {String} - format (hh:mm:ss)
	 */
	function msToTime(s) {
		/**
		 * adds a 0 for one literal numbers (09 for 9)
		 * @param {Number} n - number
		 * @return {String} - formated number
		 */
		function addZ(n) {
			return (n < 10 ? '0' : '') + n;
		}
		ms = s % 1000;
		s = (s - ms) / 1000;
		secs = s % 60;
		s = (s - secs) / 60;
		mins = s % 60;
		hrs = (s - mins) / 60;

		return addZ(hrs) + ':' + addZ(mins) + ':' + addZ(secs);
	}
	
	//Set start time
	/**
	 * set start time for a new game, start drawing in draw()
	 */
	this.setStart = function () {
		valid = true;
		start = new Date();
	};

	//Draw
	/**
	 * drawing the GUI context
	 */
	function draw() {
		
		mult += 0.03;
		if(mult == 2){ mult=0;}
		canvas.width = canvas.width;
		canvas.height = canvas.height;
		x = canvas.getContext("2d");
		
		x.beginPath();
		x.fillStyle = '#2F2F2F';
		x.arc( canvas.width/2, canvas.height/2, 300, 0, 2 * Math.PI, true);
		x.fill();
		
		//draw map radar when there is a gameSituation, valid set by obj.newRobot()
		if(valid){
			x.beginPath();
			x.strokeStyle = "#000";
			x.lineWidth = 600;
			
			x.arc(canvas.width/2, canvas.height/2,300,mult*Math.PI,(1.5+mult)*Math.PI, true) ;
			grd=x.createRadialGradient(canvas.width/2, canvas.height/2, 1500, canvas.width/2, canvas.height/2,30 );
			grd.addColorStop(0,"#0f0");
			grd.addColorStop(1,"#2f2f2f");
			x.strokeStyle = grd;
			x.stroke();
			time = parseInt((new Date().getTime() - start), 10);
			x.beginPath();
			x.font = "13px Verdana";
			x.fillStyle = '#FFFFFF';
			x.fillText('Playtime:  ' + msToTime(time), 467, 10);
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
			x.moveTo(0, canvas.height/2);
			x.lineTo(canvas.width, canvas.height/2);
			
			
        	
		}else{
			x.beginPath();
			x.font = "13px Verdana";
			x.fillStyle = '#FFFFFF';
			x.fillText('WAITING FOR OTHER PLAYER', 200, 300);
		}
        // actually start drawing
        x.stroke();
	}
	construct();
}