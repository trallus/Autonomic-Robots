function GUI ( frameControler, server ) {
    var obj = this;
    var canvas;
    var ctx;
    this.context;
    var x;
    var start;
    var count = 30;
    var counter = setInterval(timer, 1000);
    
    
    function construct () {
    	start = new Date();
    	ctx = document.getElementById('info');
    	ctx.width = 200;
    	ctx.height = 400;
    	x = ctx.getContext('2d');
    	
        canvas = document.getElementById('scene');
        canvas.width = 600;
        canvas.height = 400;
        obj.context = canvas.getContext("2d");
        frameControler.addOnNewFrame ( draw );
    };
    
    function timer() {
    	
    	count = count-1;
    	if (count <= 0)
    		{
    		var robo = new Robot(frameControler, server);
    		robo.onclick = function() {
    			x.fillText( 'Stats:  ' + robo.color, 15, 120)
    		}
    		count = 30;
    		timer();
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
        //x.font = "16px Verdana";
        x.fillStyle = '#FFFFFF';
        x.fillText( 'Playtime: ' + msToTime(time), 15, 30);
        x.fillText( 'New Robots:  ' + count, 15, 75)
    }
    
    
    
    construct ();
}