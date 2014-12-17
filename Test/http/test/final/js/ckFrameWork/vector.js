/**
 * Description
 * @method $ckVector
 * @param {} x
 * @param {} y
 * @param {} z
 * @return 
 */
function $ckVector (x, y, z) {
    
    if (!z) z = 0;  // for 2D
    
    var directionXY;
    
    /**
     * Description
     * @method addVector
     * @param {} vektor
     * @return 
     */
    this.addVector = function (vektor) {
        x += vektor.getX();
        y += vektor.getY();
        z += vektor.getZ();
    }
    /**
     * Description
     * @method multipli
     * @param {} factor
     * @return 
     */
    this.multipli = function (factor) {
        x *= factor;
        y *= factor;
        z *= factor;
    }
    
    /**
     * Description
     * @method rotate
     * @param {} winkel
     * @return 
     */
    this.rotate = function (winkel) {
        //weche achse wieviel grad
        var dx = winkel.getX();
        var dy = winkel.getY();
        var dz = winkel.getZ();
        
        var sin, cos, neuX;
        
        log(dx+" "+dy+" "+dz);
        log(x+" "+y+" "+z);
        log("l1 "+this.getLength());
        
        //wird um die x achse gedreht
        if (dx != 0) {
            sin = Math.sin(dx);
            cos = Math.cos(dx);
            var neuZ = z*cos - y*sin;
            y = z*sin + y*cos;
            z = neuZ;
        }
        
        //log("l2 "+this.getLength());
        
        //wird um die y achse gedreht
        if (dy != 0) {
            sin = Math.sin(dy);
            cos = Math.cos(dy);
            neuX = x*cos - z*sin;
            z = x*sin + z*cos;
            x = neuX;
        }
        
        //log("l3 "+this.getLength());
        
        //wird um die z achse gedreht
        if (dz != 0) {
            sin = Math.sin(dz);
            cos = Math.cos(dz);
            neuX = x*cos - y*sin;
            y = y*cos + x*sin;
            x = neuX;
        }
        
        log("l4 "+this.getLength());
    }
    
    /**
     * Description
     * @method getX
     * @return x
     */
    this.getX = function () {
        return x;
    }
    
    /**
     * Description
     * @method getY
     * @return y
     */
    this.getY = function () {
        return y;
    }
    
    /**
     * Description
     * @method getZ
     * @return z
     */
    this.getZ = function () {
        return z;
    }
    
    /**
     * Description
     * @method getLength
     * @return CallExpression
     */
    this.getLength = function () {
        return Math.sqrt(x*x+y*y+z*z);
    }
    
    /**
     * Description
     * @method getCopy
     * @return NewExpression
     */
    this.getCopy = function () {
        return new $ckVector (x,y,z);
    }
    
    // returns the angle on X,Y axis (x=0, y=-Math.PI/2)
    /**
     * Description
     * @method getDirektionXY
     * @return directionXY
     */
    this.getDirektionXY = function () {
        if (x == 0) directionXY = Math.PI/2;
        else directionXY = Math.atan(y/x);
        
        if (x < 0) {
            if (y < 0) {
                directionXY -= Math.PI;
            } else directionXY += Math.PI;
        }
        return (directionXY);
    }
}