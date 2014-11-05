var GameController = {

    main : function (controller) {
		
        var frameControler = new FrameControler();
        var server = new swp(controller);
        //server.serverRobo(controller.roboSet);
        var gui = new GUI ( frameControler, server, controller );
        
        
        
        frameControler.setGUI ( gui );
        frameControler.start();

        $("body").keydown(function(event) {
            if(event.keyCode == 80){
                frameControler.pauseButton();
                gui.on = false;
            }
            
        });
        
    }
};

