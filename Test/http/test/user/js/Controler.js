var c = new Controler();
c.remove(name, password);

function Controler () {
    var server = "serverRequest";
    
    var name;
    var password;
    var eMail;
    
    function main () {

        $("#btnRegister").click( registration );
        $("#btnLogIn").click( logIn );
        $("#btnLogOut").click( logOut );
        $("#btnRemove").click( remove );

    };
    
    this.remove = function ( name, password ) {
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
        
        serverRequest ( user, destination );
    }
    
    function registration () {
        readInputFealds();
        
        var destination = server + "/registration";
        
        var user = {
            name : name,
            password : password,
            eMail : eMail
        };
        
        serverRequest ( user, destination );
    }
    
    function readInputFealds () {
        name = $("#inputUserName").val();
        password = $("#inputUserPass").val();
        eMail = $("#inputUserEMail").val();
    }
    
    function serverRequest ( json, destination, callback ) {
    
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
            console.log(json);
        }).fail ( function ( info ) {
            console.log(info);
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