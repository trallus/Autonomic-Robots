function FrameControler ( ) {
    var obj = this;
    var lastFrameTime = 0;
    var maxFrameRate = 30;
    var minFrameLength = 1000 / maxFrameRate;
    var onNewFrameFunctions = [];
    var gui;
    var pause = false;
    
    function construct () {
    }
    
    this.setGUI = function ( extGUI ) {
        gui = extGUI;
    };
    
    this.start = function () {
        nextFrame (  );
    };
    
    
    this.pauseButton = function pauseButton () {
        if (pause) {
            pause = false;
            lastFrameTime = new Date().getTime();
        } else {
            pause = true;
        }
    };
    
    function nextFrame () {
        if (pause) {
            setTimeout ( nextFrame , 100);
            return;
        }
        
        var timeSinceLastDraw = ( new Date().getTime() - lastFrameTime ) / 1000;    // sec
        lastFrameTime = new Date().getTime();
        
        // Eigentliche Frame tätigkeit
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
    
    this.addOnNewFrame = function ( func ) {
        onNewFrameFunctions.push ( func );
    };
    
    construct();
}