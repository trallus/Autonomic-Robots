/**
 * GUI - sets up the canvas drawing part of the GUI
 * @class GUI
 * @author Florian Krüllke
 * @version 0.1
 * @param {FrameControler} frameControler - controls frame activity
 * @param {String} controllerName - name of the user
 */

function GUI(frameControler, controllerName) {
	var obj = this; 
	var x;
	var mult = 0; 
	var robotNum = 1; 
	var allRobots = [];
	var allShots = [];
	var RIP = [];
	var canvas = document.getElementById('scene');
	this.context;
	valid = false;
	var test;
	var myScore = 0;
	var eScore = 0;
	var score = [];
	//Construct
	//
	/**
	 * setting up the default attributes/add physics and gui function draw to frameController
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
	//Set settings of a robot
	/**
	 * set settings of a robot
	 * @param {Number} i - robot id
	 * @param {Array} position - array with position values
	 * @param {Number} hp - current health of a robot
	 * @param {Array} shot - array with position values
	 */
	this.setBot = function (i, position, hp, shot) {
		var hold;
		var s = [];
		size = Object.keys(allRobots).length;
		for(var j = 0; j < size; j++) {
			r = allRobots[j];
			if(r && r.setHealth() > 0 && r.getId() == i+1) {
				r.setHealth(hp);
				r.setDestination(position);
				s[0] = r.getPosition();
				s[2] = r.getColor(i);
				if (hp == 0) {
					score.push(r.getUser());
					checkScore();
					allRobots.splice(j,1);
				}
			}
		}
		if (shot > -1) {
			for(var j = 0; j < size; j++) {
				r = allRobots[j];
				if (r && s[0] && r.setHealth() > 0 && r.getId() == shot+1) {
					s[1] = r.getPosition();
					//console.log(i + ' on ' + shot);
					//new Shot(frameControler, s);
					
					new shotGun(s);
				}
			}
		}
	};
	
	//Check score
	/**
	 * for updating actual score vars myScore & eScore
	 */
	function checkScore () {
		var i= 0,j = 0;
		for (var keys in score) {
			str = score[keys] + '';
			if(str.indexOf("you") >= 0) j++;
			else if(str.indexOf("enemy") >= 0) i++;
		}
		myScore = i;
		eScore = j;
	}
	//Get current score
	/**
	 * get current score
	 * @return {Array} score - array with game data (killed robots)
	 */
	this.getScore = function () {
		var h = [];
		h[0] = myScore;
		h[1] = eScore;
		return h;
	}
	//Get winner
	/**
	 * get winner
	 * @return {Boolean} boolean - true for victory, false for defeat, null for draw
	 */
	this.getWinner = function () {
		if (myScore<eScore) return false;
		else if( myScore == eScore) return null;
		else return true;
	}
	//Set a shot from a robot to another robot
	/**
	 * set shot position array
	 * @param {Array} shot - array with position values
	 */
	this.setShot = function () {
		s = shot;
	};
	//Create a new robot
	/**
	 * create a new robot
	 * @param {String} name - user name (for displaying right color of an friendly or enemy robot)
	 */
	this.newRobot = function(i,n,p) {
		//on first start -> drawing play time via valid=true, set start time
		c = 0;
		if(controllerName == n) {c = 1;}
		for (var k = 0; k<2; k++) {
			var robo = new Robot(frameControler, c%2, robotNum, n, p );
			//increase number of robots
			//console.log(robotNum);
			i++;
			robotNum++;
			c++;
			//push robot into my robot list
			allRobots.push(robo);
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
	var timer;
	this.clear = function () {
		window.clearInterval(timer);
		allRobots = [];
		delete obj;
	};
	canvas.onmousedown = function(e) {
		pt = getMouse(e, canvas);
		var j;
		for (var i = 0, l = allRobots.length; i < l; i++) {
			if (allRobots[i].hitTest(pt.x-300, pt.y-300)) {
				window.clearInterval(timer);
				var r = allRobots[i];
				if (r.getUser()) {
					try {
						document.getElementById("selectedRobot").value=i;
						selectedRobot("choosen robot:<br> "+ r.getUser()+"r robot" +" #"+ (r.getId()-1) + '\n<br><p id=\"roboHP\">'+ 'HP: ' + r.setHealth() + '<!p><br>', (r.getId()-1));
						timer = window.setInterval(function() {
							if (r.setHealth() && document.getElementById("roboHP")) document.getElementById("roboHP").innerHTML= 'HP: ' +  r.setHealth() + '';
							else if (document.getElementById("roboHP")) document.getElementById("roboHP").innerHTML= 'HP: ' + '0';
						}, 750);
					}
					catch (e) {}
					return;
				}
			}
		}
	};
	//Selected on robot detection for HTML
	/**
	 * selected robot to push in HTML Data
	 * @param {String} info - sets a String into HMTL Data
	 * @param {Number} id - to create an unique id for related HTML tag 
	 */
	function selectedRobot (info, id) {
		if(info.indexOf("enemy")>-1) return;
		else {
			$("#selectedRobot").empty();
			
			var x = document.createElement("div");
			x.innerHTML = "X";
			x.id=id;
			$("#selectedRobot").append(x);
			$("#selectedRobot").append(info);
			$("#selectedRobot").append(selectBehave());
			
			var e = document.createElement("div");
			e.setAttribute("class", "button");
			e.setAttribute("onClick", "controller.setBehave(function () {})");
			e.innerHTML = "change";
			e.setAttribute("style", "cursor: pointer");
			e.id = "setBehave";
			$("#selectedRobot").append(e);
			$("#selectedRobot").show();
			$("#selectedRobot div").on("click", function () {
				$("#selectedRobot").hide();
				window.clearInterval(timer);
			});
		}
	}
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
		//to increase other values over time
		mult += 0.03;
		if(mult == 2){ mult=0;}
		canvas.width = canvas.width;
		canvas.height = canvas.height;
		x = canvas.getContext("2d");
		//draw a round map
		x.beginPath();
		x.fillStyle = '#2F2F2F';
		x.arc( canvas.width/2, canvas.height/2, 300, 0, 2 * Math.PI, true);
		x.fill();
		
		//draw map radar when there is a gameSituation, valid set by obj.newRobot()
		if(valid){
			time = parseInt((new Date().getTime() - start), 10);
			x.beginPath();
			x.font = "13px Verdana";
			x.fillStyle='LimeGreen';
			x.fillText('commander '+ controllerName, 75, 10);
			x.fillText("you " + myScore, 465, 10);
			x.fillStyle = '#FFFFFF';
			x.fillText(msToTime(time) + " | ", 0, 10);
			x.fillText(' | ', 510, 10);
			x.fillStyle='red';
			x.fillText("enemy " + eScore, 535, 10);
			x.lineWidth = 1;
			x.strokeStyle = "#0d0";
			for(var i = 0; i <= 10; i++){
				x.beginPath();
				
				x.arc( canvas.width/2, canvas.height/2, 30*i/*(59*i+i)*/, 0, 2 * Math.PI, false);
				x.stroke();
			}
			x.beginPath();
			x.strokeStyle = "#0f0";
			x.moveTo(canvas.width/2, 0);
			x.lineTo(canvas.width/2, canvas.height);
			x.lineWidth = 1;
			x.moveTo(0, canvas.height/2);
			x.lineTo(canvas.width, canvas.height/2);
			x.stroke();
			
			x.translate(300.5,300.5);
			
			x.beginPath();
    		var grd=x.createLinearGradient(22,67,0,-17);
			grd.addColorStop(1,"rgba(0, 255, 0, 0)");
			grd.addColorStop(0,"rgba(0, 255, 0, 0.5)");

    		x.fillStyle=grd;
    		x.rotate(((mult*19)%360)*Math.PI/180);
    		x.arc(0,0,301,0,1.9*Math.PI,true);
        	x.lineTo(0.5,0.5);
        	x.fill();
        	
        	x.rotate(((-mult*19)%360)*Math.PI/180);
        	x.translate(0.5,0.5);
        	
		}else{
			x.font = "13px Verdana";
			x.fillStyle = '#FFFFFF';
			x.fillText('WAITING FOR OTHER PLAYER', 200, 300);
		}
	}
	construct();
}
function shotGun(s) {
	var canvas = document.getElementById('scene');
	for (var i = 0; i < 10000;i++) {
		context = canvas.getContext("2d");
		context.beginPath();
		context.lineWidth = 2.5;
		context.strokeStyle = s[2];
		context.lineCap = "round";
		context.moveTo(s[0][0],s[0][1]);
		context.lineTo(s[1][0],s[1][1]);
		context.stroke();
	}
}