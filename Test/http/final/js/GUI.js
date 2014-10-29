function GUI ( frameControler, server ) {
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
   
    
    
    function construct () {
    	start = new Date();
    	ctx = document.getElementById('info');
    	ctx.width = 200;
    	ctx.height = 400;
    	x = ctx.getContext('2d');
    
        canvas = document.getElementById('scene');
        canvas.width = 600;
        canvas.height = 423;
        obj.context = canvas.getContext("2d");
        //fillSelect();
        
        frameControler.addOnNewFrame ( draw );
        
        physic = new CollisionControler ( allRobots );
        frameControler.addOnNewFrame ( physic.onFrame );
    };
    
    this.newRobot = function () {
    	var id = robotNum-1;
    	robo = new Robot ( frameControler, server, id );
    	color = controller.colors[id];
    	
        allRobots.push(robo);
    };
    
    
    function fillSelect() {
    	var data = [ {'1':'Robot ' + (robotNum-1)} ];
    	$select = $('#robots');
		$.each(data, function(i, val){
			$select.append($('<option />', { value: (i), text: val[i+1] }));
		});
		
    };
    
    function timer() {
    /*
    	count = count-1;
    	if (count == 0)
    		{
    		obj.newRobot();
    		robotNum++;
    		fillSelect();
    		console.log(server.position.gameSituation.ich);
    		
    		count = 13;
    		timer();
    		}
    		*/
    		count--;
    		//console.log(Object.getOwnPropertyNames ( server.position.gameSituation)[0]);
    if(server.position.gameSituation == undefined){
    }
    else{
    	var name = Object.getOwnPropertyNames ( server.position.gameSituation)[0];
    	var size = Object.keys(server.position.gameSituation[name]).length;
		//console.log(size);
    	if (size == robotNum) {
    		obj.newRobot();
    		robotNum++;
    		fillSelect();
    		count=10;
    		if(size < 2) { 
    			timer();
    		} else { 
    			clearInterval(counter);		
    			}
    		}
    	}
    }
    
    
    
    function msToTime(s) {

  	  function addZ(n) {
  	    return (n<10? '0':'') + n;
  	  }

  	  var ms = s % 1000;
  	  s = (s - ms) / 1000;
  	  var secs = s % 60;
  	  s = (s - secs) / 60;
  	  var mins = s % 60;
  	  var hrs = (s - mins) / 60;

  	  return addZ(hrs) + ':' + addZ(mins) + ':' + addZ(secs);//'.' + ms;
  	}
    
    function draw () {
        canvas.width = canvas.width;
        ctx.width = ctx.width;
        var time = parseInt((new Date().getTime() - start), 10);
       
        //x.fillText( ((time / (60 * 1000)) % 60) + ':' +  ((time / 1000) % 60) + '<br/>' , 10, 10);
        x.font = "15px Verdana";
        //x.font = "16px Andale Mono";
        x.fillStyle = '#FFFFFF';
        x.fillText( 'Playtime:  ' + msToTime(time), 15, 30);
        x.fillText( 'New Robot in:       ' + count, 15, 75);
        x.fillText( 'Count of Robots:   ' + (robotNum-1), 15, 120);
        
        //e = document.getElementById("#robots");
        e = $("#robots").children(":selected").text();
        if(e != ''){
		//selectedItem = e.options[e.selectedIndex].text;
		
		x.fillText( 'Selected:     ' + e, 15, 165);
		e = '';
		}
		x.fillStyle = color;
		//console.log(color);
		x.fillText( 'LAST ROBOT COLOR', 15, 360);
        
    }
    
    construct ();
}