function swp () {
    var obj = this;
    
    this.position = false;
    function getNextServerFrame () {
    
        $.ajax({
            url: "serverFrameRequest",
            data: "player1",
            error: function ( info, b, c ) {
                console.log(info);
                console.log(b);
                console.log(c);
            }
        }).done ( function ( json ) {
            obj.position = json;
            console.log("Server-Client latenz im ms: " + (json.time - new Date().getTime()));
            getNextServerFrame();
        }).fail ( function ( info ) {
            console.log(info);
        });
        
        $.ajax({
            url: "/",
            error: function ( info, b, c ) {
                console.log(info);
                console.log(b);
                console.log(c);
            }
        }).done ( function ( json ) {
        }).fail ( function ( info ) {
            console.log(info);
        });
    }
    //getNextServerFrame();
}