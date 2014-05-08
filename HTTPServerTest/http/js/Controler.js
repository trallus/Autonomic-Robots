var Controler = {
    
    main : function () {

        var frameControler = new FrameControler();
        var gui = new GUI ( frameControler );
        
        frameControler.setGUI ( gui );
        frameControler.start();
        var server = new swp();
        
        new Robot ( frameControler, server );
        
        
        $("body").keydown(function(event) {
            if(event.keyCode == 80){
                frameControler.pauseButton();
            }
        });
    }
};