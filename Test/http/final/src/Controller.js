function Controller () {
    var server = "serverRequest";
    
    var name;
    var password;
    var eMail;
    var valid = false;
    
    function main () {

        $("#btnRegister").click( registration );
        $("#btnLogIn").click( logIn );
        $("#btnLogOut").click( logOut );
        $("#btnRemove").click( remove );
        $("#btnStartGame").click( startGame );

    };
    
    function startGame () {
        
        var destination = server + "/login";
        
        var user = {
            name : name,
            password : password,
            eMail : eMail
        };
        
        serverRequest ( user, destination, function() { window.location = "gorobo.html";} );
    }
    
    function remove () {
        readInputFealds();
        
        var destination = server + "/remove";
        
        var user = {
            name : name,
            password : password,
            eMail : eMail
        };
        
        serverRequest ( user, destination );
    }
    
    function logOut () {
        var destination = server + "/logOut";
        
        serverRequest ( null, destination );
    }
    
    function logIn () {
        readInputFealds();
        
        var destination = server + "/logIn";
        
        var user = {
            name : name,
            password : password,
            eMail : eMail
        };
        
        serverRequest ( user, destination, function() { window.location = "home.html"; } );
    }
    
    function registration () {
        readInputFealds();
        
        var destination = server + "/registration";
        
        var user = {
            name : name,
            password : password,
            eMail : eMail
        };
        
        serverRequest ( user, destination, function() { } );
        
    }
    
    function readInputFealds () {
        name = $("#inputUserName").val();
        password = $("#inputUserPass").val();
        eMail = $("#inputUserEMail").val();
    }
    
    this.serverRequest = function ( json, destination, callback ) {
    	
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
            console.log( json );
            callback();
        }).fail ( function ( info ) {
            console.log(info);
            callback();
        });
        
    }
    
    main ();
};


function TrackPoint () {
    this.latitude = parseInt(Math.random()*100000000000);
    this.longitude = parseInt(Math.random()*100000000000);
    this.altitude = parseInt(Math.random()*100000000000);
    this.timestamp = parseInt(Math.random()*100000000000);
    this.isCheckPoint = false;
}