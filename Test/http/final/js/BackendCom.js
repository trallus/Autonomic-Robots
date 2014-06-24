function BackendCom ( ) {
	
	var thisObj = this;
    var user;
	var server = "serverRequest";
	
    this.name;
    this.password;
    this.eMail;
    
    this.endGame = function  () {
        serverRequest ( user, server + "/endGame", callback );
	};
    
	this.startGame = function () {
		//Game start via html-document onClick
    }
    
    this.remove = function ( name, password, eMail, callback) {
    	user = {
            name : name,
            password : password,
            eMail : eMail
        };
        serverRequest( user, server + "/remove", callback );
    }
    
    this.logOut = function ( callback ) {
        serverRequest( null, server + "/logOut", callback );
    }
    
    this.logIn = function ( password, eMail, callback) {
    	user = {
            name : "",
            password : password,
            eMail : eMail
        };
        serverRequest( user, server + "/logIn", callback );
    }
    
    this.registration = function ( name, password, eMail, callback ) {
        var user = {
            name : name,
            password : password,
            eMail : eMail
        };
        //console.log(user);
        serverRequest( user, server + "/registration", callback );
    }
    
	
    function serverRequest  ( json, destination, callback ) {
        $.ajax({
            type: "POST",
            url: destination,
            data: JSON.stringify ( json ),
            dataType: "json",
            error: function ( info, b, c ) {
                console.log(info);
                console.log(b);
                console.log(c);
            }
        }).done ( function ( json ) {
        	callback ( json );
        }).fail ( function ( info ) {
            console.log(info);
        });
    }
};
	
	