/**
 * FrameController - controls main frame activity
 * @class FrameControler
 * @author Christian Köditz
 * @version 0.1
 */

function FrameControler ( ) {
    var obj = this;
    var lastFrameTime = 0;
    var maxFrameRate = 30;
    var minFrameLength = 1000 / maxFrameRate;
    var onNewFrameFunctions = [];
    var gui;
    var pause = false;
    
    /**
     * set a GUI to render on frame
     * @param {} extGUI
     */
    this.setGUI = function ( extGUI ) {
        gui = extGUI;
    };
    
    /**
     * start rendering frames
     */
    this.start = function () {
        nextFrame (  );
    };
    
    /**
     * set up a pause functionality
     */
    this.pauseButton = function pauseButton () {
        if (pause) {
            pause = false;
            lastFrameTime = new Date().getTime();
        } else {
            pause = true;
        }
    };
    
    /**
     * calculates next frame
     */
    function nextFrame () {
        
        if (pause) {
            setTimeout ( nextFrame , 100);
            return;
        }
        
        var timeSinceLastDraw = ( new Date().getTime() - lastFrameTime ) / 1000;    // sec
        lastFrameTime = new Date().getTime();
        
        // Eigentliche Frame-tätigkeit
        for (var i in onNewFrameFunctions) {
            onNewFrameFunctions[i]( gui.context, timeSinceLastDraw );
        }
        
        // Sicherstellen das die maximale Framerate nicht überschritten wird
        var actFrameTime = new Date().getTime();
        var timeDiff = actFrameTime - lastFrameTime;
        
        if (timeDiff < minFrameLength) {
            setTimeout ( nextFrame , minFrameLength - timeDiff);
            return;
        }
       nextFrame(); 
    }
    
    //Request Animation Frame
    /**
     * Request Animation Frame - setting request animation frame for different browsers
     * @method requestAnimFrame
     * @param {Function} callback - callback function
     */
    window.requestAnimFrame = (function(){
        return  window.requestAnimationFrame       || 
                window.webkitRequestAnimationFrame || 
                window.mozRequestAnimationFrame    || 
                window.oRequestAnimationFrame      || 
                window.msRequestAnimationFrame     || 
                function(/* function */ callback, /* DOMElement */ element){
                  window.setTimeout(callback, 1000 / 60);
                };
      })();
  
    //requestAnimFrame(nextFrame);
    //Add context on new frame
    /**
     * possibility to add a context to frameController
     * @param {Function} function - function to execute on frame
     */
    this.addOnNewFrame = function ( func ) {
        onNewFrameFunctions.push ( func );
    };
}