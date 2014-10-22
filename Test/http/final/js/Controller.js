function Controller () {
	//Global var's
	var thisObj = this;
    var server = "serverRequest";
    var backendCom = new BackendCom ();
    var name;
    var password;
    var eMail;
    var user;
    
    //Get Html Parts
    //
    //... via AJAX
    function getAJAX ( destination ) {
    	$.ajax({
            url: destination
        }).done ( function ( html ) {
        	$("#content").html(html).promise().done(function(){
        		regisButtons();
        	});
        	
        });
    }
    
    //LoadAccountPage
    this.loadAccount = function () {
    	getAJAX ( "account.html" );
    }
    
    //LoadRegistrationPage
    this.loadRegister = function () {
    	getAJAX ( "signup.html" );
    }
    
    //LoadHomePage
    this.loadHome = function () {
    	getAJAX ( "home.html" );
    }
    
    //LoadLoginPage
    this.loadLogin = function () {
    	getAJAX ( "login.html" );
    }
    
    //LogOut
    this.logOut = function () {
    	backendCom.logOut( function () {
    		thisObj.loadLogin();
    	});
    }
    
    //handle User
    //
    //Registration
    function registration () {
        readInputFealds();
        if ( name.trim() == '' ) {
        	window.alert('Your name can\'t be empty');
        }
        else {
        	if ( eMail.indexOf('@') < 0) {
        	window.alert('This is no valid eMail');
        	}
        	else {
        		backendCom.registration ( name, password, eMail, function ( json ) {

        			if ( json.logedIn ) {
        				window.alert('Successfully registered...\n\nWelcome ' + name + '!!!\n\n');
        				logIn();
            	
        			} else {
        				console.log(json);
        			}
        		});
        	}
        }
    }
    
    this.overlay = function () {
        window.alert("sdfsdf");
        $("body").hide();
        $.ajax( {
            url: "signup.html"
        }).done ( function () {
            
        });
    }
    
    //Change User Settings
    function changeUser() {
    	readInputFealds();
    	backendCom.changeUser( name, password, eMail, function ( json ) {
    		if (json.userChanged) {
    			window.alert('User settings changed');
    			thisObj.loadHome();
    		}
                
        });
    }
    
    //Open Account Settings Form
    function account () {
    	thisObj.loadAccount();
    }
    
    //Login
    function logIn () {
    	readInputFealds();
    	backendCom.logIn ( password, eMail, function ( json ) {
    		
    		if ( json.logedIn )  {
    			thisObj.valid = true;
    			thisObj.loadHome();
    			
    		} 
    	});
    }
    
    //Start a Game
    this.startGame = function () {
        
        $.ajax({
            url: "serverRequest/game/joinBattleQuery"
        }).done ( function ( json ) {
        	console.log(json);
        });
        
        /*
    	$.ajax({
            url: "robots.html"
        }).done ( function ( html ) {
        	$("body").html(html).promise().done(function(){
        		GameController.main();
        		regisButtons();
        	});
        });
        */
    }
    
    //End a Game
    function endGame () {
    	backendCom.endGame( name, password, eMail, function () { thisObj.loadHome(); } );
    }
    
    //LogOut
    function logOut () {
    	backendCom.logOut( function () { 
    		window.alert('You\'ve been logged out!\n')
    		thisObj.loadLogin(); });
    }
    
    //Remove an User
    function remove () {
		//window.alert( 'User: ' + name + ' has been removed' );
    	//window.alert('For remove an user: \nType in name, eMail and password of the user')
    	readInputFealds();
        user = {
            name : name,
            password : password,
            eMail : eMail
        };
        backendCom.remove(name, password, eMail, function( json ) {
        	if ( json.removed ) {
        		window.alert( 'User ' + name + ' has been removed' );
        		thisObj.loadRegister();
        	} else {
        		window.alert( 'There is no User: ' + name + '\n... Please try again...\nType in name, eMail and password' );
        	}
        } );
    }
    
    //Search User
    function searchUser () {
    	$('select').empty();
    	backendCom.searchUser ( $("#inputUserName").val(), function ( json ) {
            var searchResult = json.searchResult;
            $select = $('#Users');
        	for ( var i = 0; i < searchResult.length; i++) {
        		$select.append($('<option />', { value: (i+1), text: searchResult[i] }));
        	}
        });
    }
    
    
    //UtilityStuff
    //
    //Button Initialization
    regisButtons = function () {

        $("#btnRegister").click( registration );
        $("#btnLogIn").click( logIn );
        $("#btnLogOut").click( logOut );
        $("#btnRemove").click( remove );
        $("#btnEndGame").click( endGame );
        $("#btnAccount").click( account );
        $("#btnChangeUser").click( changeUser );
        $("#btnSearchUser").click( searchUser );
    };
    
    //InputRead
    function readInputFealds () {
        name = $("#inputUserName").val();
        password = $("#inputUserPass").val();
        eMail = $("#inputUserEMail").val();
    }
    
    
    
    //JSON CHECK for 2 values
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

//HTML ACCESS
var controller;

function start () {
	controller = new Controller ();
};


	 
