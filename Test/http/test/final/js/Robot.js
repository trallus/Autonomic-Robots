/**
 * Robot - sets up a robot Object
 * @class Robot
 * @author Florian Krüllke
 * @version 0.1
 * @param {FrameControler} frameControler - controls frame activity
 * @param {Number} color - controls color to separate friendly and enemy bots
 * @param {Number} id - unique robot id
 */

function Robot ( frameControler, color, id) {
    var thisObj = this, health = 100;
    var posX;
    var posY;
    var direction = Math.PI;
    var destinationX; 
    var destinationY; 
    var weapon;
    var acceleration = 10; // pix / (sec*sec)
    var moveVector = new Array(0, 0);
    var speed = 0; // pix / sec
    radius = 10;
    mass = 1;
    canvaswidth = 600;
    canvasheight = 600;
    colors = ["#0f0", "red"];
    startPosition();
    
    //Start Position
    /**
     * give every player his start position
     */
    function startPosition () {
    	if(id%2 == 1){
    		posX=0+ canvaswidth/2;
    		posY=100+ canvasheight/2;
    	}else{
    		posX=14+ canvaswidth/2;
    		posY=-98+ canvasheight/2;
    	}
    }
    
    //Construct
    /**
     * add onFrame (draw, check for updates) to frameController
     */
    function construct () {
        frameControler.addOnNewFrame ( onFrame );
    }
    
   	//Get dimension
    /**
     * get dimension
     * @return BinaryExpression - radius / 2
     */
    thisObj.getDimension = function () { return radius*.5; };
    
    //Get Position
    /**
     * get position
     * @return {Array} ret - position array
     */
    thisObj.getPosition = function () {
        ret = [posX, posY];
        return ret;
    };
    
    //Get move vector
    /**
     * get move vector
     * @return {Number} moveVector
     */
    thisObj.getMoveVector = function () { return moveVector; };
    
    //Get mass
    /**
     * get mass
     * @return {Number} mass
     */
    thisObj.getMass = function () { return mass; };
    
    //Set direction
    /**
     * set direction
     * @param {Number} d - direction
     */
    thisObj.setDirection = function ( d ) { direction = d; };
    
    //Set speed
    /**
     * set speed
     * @param {Number} s speed
     */
    thisObj.setSpeed = function ( s ) { speed = s; };
    
    //Set position
    /**
     * set position
     * @param {Array} p - position array
     */
    thisObj.setPosition = function ( p ) {
        posX = p[0];
        posY = p[1];
    };
    
    //Set destination
    /**
     * set destination
     * @param {Array} p - position array
     */
    thisObj.setDestination = function ( p ) {
        destinationX = p[0]+ canvaswidth/2;
        destinationY = p[1]+ canvasheight/2;
    };
    
    //Set current health
    /**
     * set health
     * @param {Number} damage - damage to subtract from health
     */
    function setHealth (damage) { health = health-damage;}
    
    //Set behavior
    /**
     * set an specified behavior
     * @param {JSON} behave - new robot behave
     */
    this.setBehavior = function(behave) {
    	//switch case for behavior like animations or health state
    };
    
    //Get id
    /**
     * get id
     * @return {Number} id - robot id
     */
    thisObj.getId = function () { return id; };
    
    //Hit test
     /**
      * HIT TEST - checks for clicked canvas position
      * @param {Number} x - x position
      * @param {Number} y - y position
      * @return {boolean} true or false
      */
     thisObj.hitTest = function (x, y) {
        dx = x - posX;
        dy = y - posY;
        return dx * dx + dy * dy <= radius/2 * radius;
    };

    //On frame
    /**
     * onframe - global ticker for drawing methods
     * @param {getContext("2d")} context - to draw context
     * @param {Number} timeSinceLastDraw - time since last draw
     */
    function onFrame ( context, timeSinceLastDraw ) {
        calcPosition ( timeSinceLastDraw );
        draw ( context );
    }
    
    //Draw
    /**
     * drawing method
     * @param {getContext("2d")} context - to draw context
     */
    function draw ( context ) {
    	//draw info
    	context.beginPath();
    	context.fillStyle = '#000000';
    	context.fillRect(posX-16,posY-24,32,15);
    	context.fillStyle = '#0F0';
    	context.fillRect(posX-15,posY-23,Math.floor(health*0.3),13);
    	//draw robot
    	context.font = "10px Verdana";
        context.fillStyle = '#FFFFFF';
        context.fillText( '#' + id, posX-7, posY-13);
        context.arc( posX, posY, radius/2, 0, 2 * Math.PI, false);
        //set drawing style
        context.lineWidth = 1;
        context.strokeStyle =  "#000";
        context.fillStyle= colors[color];
        context.fill();
        context.stroke(); 
        //draw destination
        context.beginPath();
        context.arc( destinationX, destinationY, 2, 0, 2 * Math.PI, false);
        //actually start drawing
        context.stroke();    
    }
    
    //Physics
    /**
     * create a physical behavior on changed destinations
     * @param {Number} timeSinceLastDraw - time since last draw
     */
    function calcPosition ( timeSinceLastDraw ) {
    
        //angle between direction and pos, destination
        //treeangle abc: tan @ = a/b
        a = destinationY - posY;
        b = destinationX - posX;
        alpha = Math.atan(a/b);
        
        if ( b < 0 ) {
            if ( a > 0 ) alpha += Math.PI;
            else alpha -= Math.PI;
        }
        angleDiff = alpha - direction;
        behind = false; //if the destination behind the robot
        
        // values frim -2PI - 2PI
        if (angleDiff > Math.PI) {
            angleDiff -= 2*Math.PI;
            behind = true;
        } else if (angleDiff < -Math.PI) {
            angleDiff += 2*Math.PI;
            behind = true;
        }
        // distance
        distanceToDestination = Math.sqrt(a*a + b*b);
        if (angleDiff < 0) direction -= 1 * timeSinceLastDraw;
        else direction += 1 * timeSinceLastDraw;
        if (direction > Math.PI) direction -= 2*Math.PI;
        if (direction < -Math.PI) direction += 2*Math.PI;
        
        // position
        speedX = Math.cos ( direction ) * speed;
       	speedY = Math.sin ( direction ) * speed;
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