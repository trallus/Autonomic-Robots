function Controller () {
	
	var thisObj = this;
    var server = "serverRequest";
    var backendCom = new BackendCom ();
    
    var name;
    var password;
    var eMail;
    var user;
    
    //
    //Get Html Parts
    //
    
    function getAJAX ( destination ) {
    	$.ajax({
            url: destination
        }).done ( function ( html ) {
        	$("body").html(html).promise().done(function(){
        		regisButtons();
        	});
        });
    }
    
    this.loadRegister = function () {
    	getAJAX ( "register.html" );
    }
    
    this.loadHome = function () {
    	getAJAX ( "home.html" );
    }

    this.loadLogin = function () {
    	getAJAX ( "login.html" );
    }

    this.logOut = function () {
    	backendCom.logOut( function () {
    		thisObj.loadLogin();
    	});
    }
    
    this.startGame = function () {
    	$.ajax({
            url: "gorobo.html"
        }).done ( function ( html ) {
        	$("body").html(html).promise().done(function(){
        		GameController.main();
        		regisButtons();
        	});
        });	
    }
    
    function endGame () {
        //backendCom.endGame( function () { thisObj.loadHome(); } );
    	thisObj.loadHome();
    }
    
    //
    //handle User
    //
    
    function remove () {
        user = {
            name : name,
            password : password,
            eMail : eMail
        };
        backendCom.remove(name, password, eMail, function() { thisObj.loadRegister(); } );
    }
    
    function logOut () {
    	backendCom.logOut( function () { thisObj.loadLogin(); });
    }
    
    function logIn () {
    	readInputFealds();
    	backendCom.logIn ( password, eMail, function ( json ) {
    		if ( check( json, "logedIn", true ) ) {
    			thisObj.valid = true;
    			thisObj.loadHome();
    			
    		} else if ( check( json, "logedIn", false ) ) {
    			window.alert("Wrong Mail or Password");
    			thisObj.loadRegister();
			}
    	});
    }

    function registration () {
        readInputFealds();
        backendCom.registration ( name, password, eMail, function ( json ) {

        	if ( check( json, "registered", true ) ) {
            	logIn();
            	
        	} else {
        		window.alert("This eMail is already chosen.");
        	}
			//console.log(json);
        });
    }

    regisButtons = function () {

        $("#btnRegister").click( registration );
        $("#btnLogIn").click( logIn );
        $("#btnLogOut").click( logOut );
        $("#btnRemove").click( remove );
        $("#btnEndGame").click( endGame );
    };
    
    //UtilityStuff
    
    function readInputFealds () {
        name = $("#inputUserName").val();
        password = $("#inputUserPass").val();
        eMail = $("#inputUserEMail").val();
    }
    
	function check ( json,value1, value2) {
	    for (key in json) {
	        if (typeof (json[key]) === "object") {
	            return checkForValue(json[key], value1, value2);
	            
	        } else if ( json[key] === value2 && (JSON.stringify(json).indexOf(value1) > 0 ) ) {
	            return true;
	        }
	    }
	    return false;
	}
    
};

var controller;

function start () {
	controller = new Controller ();
};