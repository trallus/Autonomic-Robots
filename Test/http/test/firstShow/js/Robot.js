function Robot ( frameControler, server ) {
    var posX = 100;
    var posY = 100;
    var speed = 0; // pix / sec
    var direction = Math.PI;
    var destinationX = 100;
    var destinationY = 100;
    var acceleration = 10; // pix / (sec*sec)
    
    function construct () {
        frameControler.addOnNewFrame ( onFrame );
    }
    
    function onFrame ( context, timeSinceLastDraw ) {
        if (server.position) {
            destinationX = server.position.posX;
            destinationY = server.position.posY;
        }
        
        calcPosition ( timeSinceLastDraw );
        draw ( context );
    }
    
    function draw ( context ) {

        var radius = 10;
        
        context.beginPath();
        context.arc( posX, posY, radius, 0, 2 * Math.PI, false);
        
        // set drawing style
        context.lineWidth = 2;
        context.strokeStyle = "#000000";;
        
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