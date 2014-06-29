function Robot ( frameControler, server ) {
    var posX = 575;
    var posY = 375;
    var speed = 0; // pix / sec
    var direction = Math.PI;
    var destinationX = 100;
    var destinationY = 100;
    var acceleration = 10; // pix / (sec*sec)
    //var color = getColor();
    var color = getColor();
    this.getRobotColor = color;
    var radius = 10;
    var moveVector = new Array(0, 0);
    var mass = 1; // Gewicht des Robot
    
    function construct () {
        frameControler.addOnNewFrame ( onFrame );
    }
    
    
    this.getDimension = function () { return radius; };
    this.getPosition = function () {
        var ret = [posX, posY];
        return ret;
    };
    this.getMoveVector = function () { return moveVector; };
    this.getMass = function () { return mass; };
    
    this.setDirection = function ( d ) { direction = d; };
    this.setSpeed = function ( s ) {
        speed = s;
    };
    this.setPosition = function ( p ) {
        posX = p[0];
        posY = p[1];
    };
    
    function onFrame ( context, timeSinceLastDraw ) {

        if (server.position) {
            destinationX = server.position.posX;
            destinationY = server.position.posY;
        }
        
        calcPosition ( timeSinceLastDraw );
        draw ( context );
    }
    
    function getColor() {
    	var letters = "0123456789ABCDEF".split("");
    	var hold = "#";
    	for ( var i = 0; i < 6; i++) {
    		hold += letters[Math.floor(Math.random() * 16)];
    	}
    	return hold;	
    }
    
    
    function draw ( context ) {
        
        context.beginPath();
        context.arc( posX, posY, radius, 0, 2 * Math.PI, false);
        
        // set drawing style
        context.lineWidth = 2;
        context.strokeStyle =  color;
        
        // actually start drawing
        context.stroke(); 
        
        
        // draw destination
        context.beginPath();
        context.arc( destinationX, destinationY, 2, 0, 2 * Math.PI, false);
        
        // set drawing style
        context.lineWidth = 2;
        context.strokeStyle = "#00FF00";;
        
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