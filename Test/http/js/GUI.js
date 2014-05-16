function GUI ( frameControler ) {
    var obj = this;
    var canvas;
    this.context;
    
    function construct () {
        canvas = document.getElementById('scene');
        canvas.width = 600;
        canvas.height = 400;
        obj.context = canvas.getContext("2d");
        frameControler.addOnNewFrame ( draw );
    };
    
    function draw () {
        canvas.width = canvas.width;
    }
    
    construct ();
}