function Robot ( frameControler, color, id) {
    var thisObj = this;
    var health = 100;
    var posX = 0;
    var posY = 100;
    var speed = 0; // pix / sec
    var direction = Math.PI;
    var random = Math.random()*3;
    var destinationX = 100*random;
    var destinationY = 100;
    var acceleration = 10; // pix / (sec*sec)
    var radius = 10;
    var moveVector = new Array(0, 0);
    var mass = 1; // Gewicht des Robot
    var canvaswidth = 600;
    var canvasheight = 600;
    var colors = ["#0f0", "red"];
    startPosition();
    
    function startPosition () {
    	if(id%2 == 1){
    		posX=0+ canvaswidth/2;
    		posY=100+ canvasheight/2;
    	}else{
    		posX=0+ canvaswidth/2;
    		posY=-100+ canvasheight/2;
    	}
    	
    }
    
    function construct () {
        frameControler.addOnNewFrame ( onFrame );
    }
    
   
    thisObj.getDimension = function () { return radius; };
    thisObj.getPosition = function () {
        var ret = [posX, posY];
        return ret;
    };
    thisObj.getMoveVector = function () { return moveVector; };
    thisObj.getMass = function () { return mass; };
    
    thisObj.setDirection = function ( d ) { direction = d; };
    thisObj.setSpeed = function ( s ) {
        speed = s;
    };
    thisObj.setPosition = function ( p ) {
        posX = p[0];
        posY = p[1];
    };
    thisObj.setDestination = function ( p ) {
        destinationX = p[0]+ canvaswidth/2;
        destinationY = p[1]+ canvasheight/2;
    };
    thisObj.getId = function () {
        return id;
    };
    
    //HIT TEST
     thisObj.hitTest = function (x, y) {
        var dx = x - posX;
        var dy = y - posY;
        return dx * dx + dy * dy <= radius/2 * radius;
    };

	
   
    
    //onframe
    function onFrame ( context, timeSinceLastDraw ) {
        calcPosition ( timeSinceLastDraw );
        draw ( context );
    }
    
    
    function getColor() {
    	var hold = "#";
    	for ( var i = 0; i < 6; i++) {
    		hold += letters[Math.floor(Math.random() * 16)];
    	}
    	return hold;	
    }
    
    
    function draw ( context ) {
    	//context.translate(canvaswidth/2,canvasheight/2);
    	context.beginPath();
    	context.fillStyle = '#000000';
    	context.fillRect(posX-16,posY-24,32,15);
    	//context.beginPath();
    	context.fillStyle = '#0F0';
    	context.fillRect(posX-15,posY-23,Math.floor(health*0.3),13);
    	
    	context.font = "10px Verdana";
        //context.font = "10px Andale Mono";
        context.fillStyle = '#FFFFFF';
        context.fillText( '#' + id, posX-7, posY-13);
        //x.fillText( 'New Robot in:       ' + count, 15, 75);
        //x.fillText( 'Count of Robots:   ' + (robotNum-1), 15, 120);
    
    
       
       // context.beginPath();
        context.arc( posX, posY, radius/2, 0, 2 * Math.PI, false);
        
        // set drawing style
        context.lineWidth = 1;
        context.strokeStyle =  "#000";
        //context.fillStyle= "#0f0";
        context.fillStyle= colors[color];
        context.fill();
        // actually start drawing
        context.stroke(); 
        
        
        // draw destination
        context.beginPath();
        context.arc( destinationX, destinationY, 2, 0, 2 * Math.PI, false);
        
        // set drawing style
        //context.lineWidth = 2;
        //(context.strokeStyle = "#00FF00";;
        
        // actually start drawing
        context.stroke();    
    }
    
    function calcPosition ( timeSinceLastDraw ) {
        //angle between direction and pos, destination
        //treeangle abc: tan @ = a/b
        
        var a = destinationY - posY;
        var b = destinationX - posX;
        var alpha = Math.atan(a/b);
        
        if ( b < 0 ) {
            if ( a > 0 ) alpha += Math.PI;
            else alpha -= Math.PI;
        }
        
        var angleDiff = alpha - direction;
        behind = false; //if the destination behind the aircraft
        
        // values frim -2PI - 2PI
        if (angleDiff > Math.PI) {
            angleDiff -= 2*Math.PI;
            behind = true;
        }
        else if (angleDiff < -Math.PI) {
            angleDiff += 2*Math.PI;
            behind = true;
        }
        
        // distance
        
        var distanceToDestination = Math.sqrt(a*a + b*b);
        
        if (angleDiff < 0) direction -= 1 * timeSinceLastDraw;
        else direction += 1 * timeSinceLastDraw;
        
        if (direction > Math.PI) direction -= 2*Math.PI;
        if (direction < -Math.PI) direction += 2*Math.PI;
        
        // position
        var speedX = Math.cos ( direction ) * speed;
        var speedY = Math.sin ( direction ) * speed;
        moveVector = [speedX, speedY];
        posX += ( speedX * timeSinceLastDraw );
        posY += ( speedY * timeSinceLastDraw );
        
        if ( distanceToDestination < speed) speed -= acceleration * timeSinceLastDraw;
        else if ( Math.abs(angleDiff) < 1 ) speed += acceleration * timeSinceLastDraw;
        
        
        if (false) {
            console.log("####################################");
            console.log(a);
            console.log(b);
            console.log(angleDiff);
            console.log(direction);
        }
    }
    
    construct ();
}