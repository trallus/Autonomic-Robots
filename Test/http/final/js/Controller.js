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
    
    this.endGame = function () {
    	backendCom.endGame( name, password, eMail, function () { thisObj.loadHome(); } );
    }
    
    //handle User
    //
    //Registration
    function registration () {
        readInputFealds();
        if ( name.trim() == '' ) {
        	thisObj.overlay('Your name can\'t be empty');
        }
        else {
        	if ( eMail.indexOf('@') < 0) {
        	thisObj.overlay('This is no valid eMail');
        	}
        	else {
        		backendCom.registration ( name, password, eMail, function ( json ) {

        			if ( json.registered ) {
        				var text = 'Successfully registered... <br>Welcome ' + name + '!!!<br>';
                        thisObj.overlay(text)
        				logIn();
            	
        			} else {
        				console.log(json);
        			}
        		});
        	}
        }
    }
    
    //open a overlay with a text string called json
    this.overlay = function ( json ) {
        $(document).ready(function () {
            $('#content').hide();
            var el = $("#overlay").show();
            $( '<p id="infoText">INFO!!!<br><br>' + json + '</p>' ).insertBefore( ".infoPush" );
        });
    }

    //close the overlay and remove the tag with id "infoText"
    this.overlayOff = function () {
        $(document).ready(function () {
            $("#content").show();
            var el = $("#overlay").hide();
            //el.remove('#infoText');
            $( "#infoText" ).remove();
        });
    }
    
    //Change User Settings
    function changeUser() {
    	readInputFealds();
    	backendCom.changeUser( name, password, eMail, function ( json ) {
    		if (json.userChanged) {
    			thisObj.overlay(JSON.stringify(json));
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
    			
    		} else thisObj.overlay('Wrong mail or password!<br>Please try again!!!');
    	});
    }
    
    //Start a Game
    this.startGame = function () {
        /*
        var robot = {
            weaponPrototype : {
                range : 100,
                rateOfFire : 30,
                damage : 10
            },
            armor : 100,
            enginePower : 100,
            behaviour : "gibts noch nicht"
        };
        
        $.ajax({
            type: "POST",
            data: JSON.stringify ( robot ),
            dataType: "json",
            url: "serverRequest/game-setNextRobot"
        }).done ( function ( json ) {
            console.log(json);
        
            $.ajax({
                url: "serverRequest/game-joinBattleQuery"
            }).done ( function ( json ) {
                console.log(json);
                var intervallCounter = 20;
                var intervall = window.setInterval(
                    function () {
                        $.ajax({
                            url: "serverRequest/game-getGameSituation"
                        }).done ( function ( json ) {
                            console.log(json);
                        });
                        intervallCounter--;
                        if (intervallCounter < 0) {
                            window.clearInterval(intervall);
                        }
                    }, 1000
                );
            });
        });
        /*
        $.ajax({
            url: "serverRequest/game/joinBattleQuery"
        }).done ( function ( json ) {
        	console.log(json);
        }).fail ( function ( info ) {
            console.log(info);
        });
        */
        
    	$.ajax({
            url: "robots.html"
        }).done ( function ( html ) {
        	$("#content").html(html).promise().done(function(){
        		GameController.main();
        		regisButtons();
        	});
        });
        
    }
    
    //End a Game
    function endGame () {
    	backendCom.endGame( name, password, eMail, function () { thisObj.loadHome(); } );
    }
    
    //LogOut
    function logOut () {
    	backendCom.logOut( function () { 
    		thisObj.overlay('You\'ve been logged out!')
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
        		thisObj.overlay('User ' + name + ' has been removed' );
        		thisObj.loadRegister();
        	} else {
        		thisObj.overlay('There is no User: ' + name + '\n... Please try again...\nType in name, eMail and password' );
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
        $("#btnEndGame").click( endGame /*function () {thisObj.loadHome(); }*/);
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


	 
