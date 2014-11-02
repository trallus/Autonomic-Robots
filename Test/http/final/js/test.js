var stage;
//var canvas = window.document.getElementById("robot");
//var body = $("html");
var canvas = $("robot");
var circle;

//var h = window.innerHeight;
//var w = window.innerWidth;

var w = 800;
var h = 500;
var robot;
var robotNum;
var server;
var allRobots;
var colors = [ "#f00", "#0f0", "#00f"];


//console.log( w + "     " + h );

function startGame () {
	server = new swp();
}

function init() {
	allRobots = [];
	robotNum = 1;
	stage = new createjs.Stage("robot");
    line = new createjs.Shape();
	line.graphics.moveTo((w/2),0).setStrokeStyle(1).beginStroke("#00ff00").lineTo((w/2),(h));
    stage.addChild(line);
    line = new createjs.Shape();
    line.graphics.moveTo(0, h/2).setStrokeStyle(1).beginStroke("#00ff00").lineTo(w,h/2);
	stage.addChild(line);
	
	

/*
stage.x = w/2;
stage.h = h/2;


    circle = new createjs.Shape();

circle.graphics.beginFill('red').drawCircle(0,0,5);
circle.x = circle.y = 5;
circle.snapToPixel = true;
stage.addChild(circle);

createjs.Ticker.addEventListener('tick', handleTick);
createjs.Ticker.setFPS(40);
*/
//newRobot();
createjs.Ticker.addEventListener('tick', handleTick);
stage.update();
}

function newRobot() {
	circle = new createjs.Shape();
	circle.graphics.beginFill(colors[robotNum]).drawCircle(0,0,5);
	circle.x = circle.y = 5;
	//circle.snapToPixel = true;
	//circle.cache(-circle.x,-circle.y, circle.x*2, circle.y*2);
	
	allRobots.push(circle);
	stage.addChild(circle);
	stage.update();
}
  

function moveTo() {
		var count = 0;
		var l = stage.getNumChildren() - 1;
		//console.log(l);
		l = robotNum-1;
		var name = Object.getOwnPropertyNames (server.position.gameSituation)[0];
		if (server.position.gameSituation[name][count] != undefined) {

            for(var i = 0; i < l; i++) {
                var robo = stage.getChildAt(i);
				//console.log(server.position.gameSituation[name][count].position[0]);
				var y = Math.floor(server.position.gameSituation[name][count].position[0]);
				var x = Math.floor(server.position.gameSituation[name][count].position[1]);
				createjs.Tween.get(robo, {loop: false}).to({x: x, y: y}, 10);
				//robo.x = 100;//Math.floor(server.position.gameSituation[name][count].position[0]);
				//robo.y = 200;//Math.floor(server.position.gameSituation[name][count].position[1]);
				count++;
				//console.log(robo.x + ' --- ' + robo.y);
				
			
		} 
		} else {return;}
	
	
	stage.update();
}





function handleTick(e) { 
//console.log(!server);
	if (server) {
		if(server.position.gameSituation) {
		var name = Object.getOwnPropertyNames (server.position.gameSituation)[0];
    	var size = Object.keys(server.position.gameSituation[name]).length;
		//console.log(size);
    	if (size == robotNum) {
    	/*
    		var gameSituation = server.position.gameSituation;
    		//check for changes!!!
    		for (var key in gameSituation) {
    			var robots = gameSituation[key];
    			for (var robot in robots){
    				//here check for robot id, changes
    				//setRobot if new id new robt()
    			}
    		}
    		*/
    		//console.log(size + '   ' + name );
    		newRobot();
    		
    		robotNum++;
    		
    		}
    	moveTo();
    	}
	}
	
    
    

	/*
if(circle) {

  if (circle.x < (stage.canvas.width-20) && circle.y <= 20) {
    circle.x++;
  } else if (circle.x >= (stage.canvas.width-20) && circle.y < (stage.canvas.height-20)) {
    circle.y++;
  } else if (circle.y >= (stage.canvas.height-20) && circle.x > 20) {
    circle.x--;
  } else if (circle.x <= 20) {
    circle.y--;
  }
  
  createjs.Tween.get(circle, {loop: true}).to({x:400, y: 200}, 7000).to({x:0}, 5000).to({y:0}, 5000);
  
  */
 
 
  
 //circle.x += 3;
  
  //stage.update(e);
}



/*
window.onresize = function() {
  stage.canvas.width = $(window).width();
  stage.canvas.height = $(window).height();
};
stage.canvas.width = $(window).width();
stage.canvas.height = $(window).height();
*/
