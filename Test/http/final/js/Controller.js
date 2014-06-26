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
    	getAJAX ( "signup.html" );
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
    
    //
    //handle User
    //
    
    function registration () {
        readInputFealds();
        backendCom.registration ( name, password, eMail, function ( json ) {

        	if ( json.registered ) {
        		window.alert('Successfully registered...\n\nWelcome ' + name + '!!!\n\n');
            	logIn();
            	
        	} else {
        		window.alert("This eMail is already chosen");
        	}
        });
    }
    
    function logIn () {
    	readInputFealds();
    	backendCom.logIn ( password, eMail, function ( json ) {
    		
    		if ( json.logedIn )  {
    			thisObj.valid = true;
    			thisObj.loadHome();
    			
    		} else {
    			window.alert("Wrong Mail or Password");
			}
    	});
    }
    
    this.startGame = function () {
    	$.ajax({
            url: "robots.html"
        }).done ( function ( html ) {
        	$("body").html(html).promise().done(function(){
        		GameController.main();
        		regisButtons();
        	});
        });	
    }
    
    function endGame () {
    	backendCom.endGame( name, password, eMail, function () { thisObj.loadHome(); } );
    }
    
    function logOut () {
    	backendCom.logOut( function () { 
    		window.alert('You\'ve been logged out!\n')
    		thisObj.loadLogin(); });
    }
    
    function remove () {
    	readInputFealds();
        user = {
            name : name,
            password : password,
            eMail : eMail
        };
        backendCom.remove(name, password, eMail, function( json ) {
        	
        	if ( json.removed ) {
        		window.alert( 'User: ' + name + '\n...has been removed' );
        		thisObj.loadRegister();
        	} else {
        		window.alert( 'There is no User: ' + name + '\n... Please try again' );
        	}
        } );
    }
    
    //UtilityStuff
    
    regisButtons = function () {

        $("#btnRegister").click( registration );
        $("#btnLogIn").click( logIn );
        $("#btnLogOut").click( logOut );
        $("#btnRemove").click( remove );
        $("#btnEndGame").click( endGame );
    };
    
    function readInputFealds () {
        name = $("#inputUserName").val();
        password = $("#inputUserPass").val();
        eMail = $("#inputUserEMail").val();
    }
    
	function check ( json, value1, value2) {
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