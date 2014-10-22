function BackendCom ( ) {
	//Global var's
	var thisObj = this;
    var user;
	var server = "serverRequest";
	this.name;
    this.password;
    this.eMail;
    
    //Static Methods
    //
    //EndGame
    this.endGame = function  ( name, password, eMail, callback ) {
    	user = {
            name : name,
            password : password,
            eMail : eMail
        };
        serverRequest ( user, server + "/endGame", callback );
	};
    
	//StartGame
	this.startGame = function ( callback ) {
		callback();
    }
    
	//Remove User
    this.remove = function ( name, password, eMail, callback) {
    	user = {
            name : name,
            password : password,
            eMail : eMail
        };
        serverRequest( user, server + "/remove", callback );
    }
    
    //LogOut
    this.logOut = function ( callback ) {
        serverRequest( null, server + "/logOut", callback );
    }
    
    //Login
    this.logIn = function ( password, eMail, callback) {
    	user = {
            name : "",
            password : password,
            eMail : eMail
        };
        serverRequest( user, server + "/logIn", callback );
    }
    
    //Registration
    this.registration = function ( name, password, eMail, callback ) {
        var user = {
            name : name,
            password : password,
            eMail : eMail
        };
        serverRequest( user, server + "/registration", callback );
    }
    
    //SearchUser
    this.searchUser = function ( name, callback ) {
        var user = {
            name : name,
            password : "",
            eMail : ""
        };
        
        serverRequest( user, server + "/searchUser", callback );
    }
    
    //ChangeUser
    this.changeUser = function ( name, password, eMail, callback ) {
        var user = {
            name : name,
            password : password,
            eMail : eMail
        };
        
        serverRequest( user, server + "/changeUser", callback );
    }
    
    //Method
    //
	//ServerCommunication
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
        }).done (
    		function (json) {
    			if (json.failure) {
    				window.alert(json.failure)
    			}
    			callback (json);
    		}).fail ( function ( info ) {
            console.log(info);
        });
    }
};	