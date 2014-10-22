var GameController = {
    
    main : function () {

        var frameControler = new FrameControler();
        var server = new swp();
        var gui = new GUI ( frameControler, server );
        
        frameControler.setGUI ( gui );
        frameControler.start();
        
        //new Robot ( frameControler, server );
        gui.newRobot();
        
        $("body").keydown(function(event) {
            if(event.keyCode == 80){
                frameControler.pauseButton();
            }
        });
        
    }
};

