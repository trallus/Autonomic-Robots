/**
 * Controller - main user/client side functionality
 * @class Controller
 * @project Automatic Robots
 * @author Florian Krüllke
 * @version 0.1
 */

function Controller() {
	//Global var's
	/**
    *to create a static methods
    * @type this
    * @memberof Controller
    */
	var thisObj = this;
	/**
    *name space for build up URLs 
    * @type String
    * @memberof Controller
    */
	var server = "serverRequest";
	/**
    *new BackendCom-Object for server communication
    * @type BackendCom
    * @memberof Controller
    */
	var backendCom = new BackendCom();
	//- User
	/**
    *static name space for user name
    * @type String
    * @memberof Controller
    */
	thisObj.name;
	/**
    *password of the user
    * @type String
    * @memberof Controller
    */
	var password;
	/**
     * email of the user
     * @type String
     * @memberof Controller
     */
	var eMail;
	/**
    *to hold user-object
    * @type object
    * @memberof Controller
    */
	var user;
	//- access for changed robot values by user
	/**
    * array to hold all robot-objects, static access
    * @type Array
    * @memberof Controller
    */
	thisObj.roboSet = [];
	//- default robo issue names & values
	/**
    * array for default name configuration
    * @type Array
    * @memberof Controller
    */
	var roboName = ["range", "rateOfFire", "damage", "armor", "enginePower", "behavior"];
	/**
    *array for default values configuration
    * @type Array
    * @memberof Controller
    */
	var roboVal = [100, 30, 10, 100, 100, "gibts noch nicht"];
	
	//Get html parts method
	/**
	 * get html parts via AJAX
	 * @param {String} destination - specifies the path URL
	 */
	function getAJAX(destination) {
		$.ajax({
			url : destination
		}).done(function(html) {
			$("#content").html(html).promise().done(function() {
				regisButtons();
			});

		});
	}
	//Outer methods
	//Load debug-mode
	/**
	 * open debug file - auto.html - in a new window
	 */
	this.loadDebug = function() {
		window.open("auto.html");
	}; 
	//Load account settings
	/**
	 * load account-settings-page
	 */
	this.loadAccount = function() {
		getAJAX("account.html");
	};
	//Registration
	/**
	 * load sign-up-page
	 */
	this.loadRegister = function() {
		getAJAX("signup.html");
	};
	//Load home-page
	/**
	 * load home-page
	 */
	this.loadHome = function() {
		getAJAX("home.html");
	};
	//Load login-page
	/**
	 * load login-page
	 */
	this.loadLogin = function() {
		getAJAX("login.html");
	};
	//load logout-page
	/**
	 * user loguut
	 */
	this.logOut = function() {
		backendCom.logOut(function() {
			thisObj.loadLogin();
		});
	};
	//End a game
	/**
	 * end a game
	 */
	this.endGame = function() {
		backendCom.endGame(name, password, eMail, function() {
			thisObj.loadHome();
		});
	};
	//Start a game
	/**
	 * start a Game
	 */
	this.startGame = function() {
		
		if($("#users :selected")){
			var e = $("#users :selected").text();
			console.log(e);
			thisObj.name = e;
		}
		if (thisObj.name) {
			backendCom.logIn("", thisObj.name, function(json) {

				if (json.logedIn) {
					$.ajax({
						url : "robots.html"
					}).done(function(html) {
						$("#content").html(html).promise().done(function() {
							GameController.mainGC(thisObj);
							document.getElementById("setNext").appendChild(setNext());
							$("#setNext").show();
							
							regisButtons();
						});
					});
				} else
					thisObj.overlay('Wrong mail or password!<br>Please try again!!!');
			});
			e = '';
		}

	};
	//Inner methods
	//End a game
	/**
	 * end a Game
	 */
	function endGame() {
		backendCom.endGame(name, password, eMail, function() {
			thisObj.loadHome();
			//$("#overlay").hide();
			$("#setNext").hide();
		});
	}

	//Registration
	/**
	 * registration
	 */
	function registration() {
		readInputFealds();
		if (name.trim() == '') {
			thisObj.overlay('Your name can\'t be empty');
		} else {
			if (eMail.indexOf('@') < 0) {
				thisObj.overlay('This is no valid eMail');
			} else {
				backendCom.registration(name, password, eMail, function(json) {

					if (json.registered) {
						var text = 'Successfully registered... <br>Welcome ' + name + '!!!<br>';
						thisObj.overlay(text);
						logIn();

					} else {
						console.log(json);
					}
				});
			}
		}
	}

	//Change user settings
	/**
	 * change user settings via readInputFields()
	 */
	function changeUser() {
		readInputFealds();
		backendCom.changeUser(name, password, eMail, function(json) {
			if (json.userChanged) {
				thisObj.overlay(JSON.stringify(json));
				thisObj.loadHome();
			}
		});
	}

	//Open account
	/**
	 * open account settings page
	 */
	function account() {
		thisObj.loadAccount();
	}

	//Login
	/**
	 * login
	 */
	function logIn() {
		readInputFealds();
		backendCom.logIn(password, eMail, function(json) {

			if (json.logedIn) {
				thisObj.valid = true;
				thisObj.loadHome();

			} else
				thisObj.overlay('Wrong mail or password!<br>Please try again!!!');
		});
	}

	//Logout
	/**
	 * logOut
	 */
	function logOut() {
		backendCom.logOut(function() {
			thisObj.overlay('You\'ve been logged out!');
			thisObj.loadLogin();
		});
	}

	//Remove the user
	/**
	 * remove an User
	 */
	function remove() {
		readInputFealds();
		user = {
			name : name,
			password : password,
			eMail : eMail
		};
		backendCom.remove(name, password, eMail, function(json) {
			if (json.removed) {
				thisObj.overlay('User ' + name + ' has been removed');
				thisObj.loadRegister();
			} else {
				thisObj.overlay('There is no User: ' + name + '\n... Please try again...\nType in name, eMail and password');
			}
		});
	}

	//Search for other online users
	/**
	 * search for other online users
	 */
	function searchUser() {
		$('select').empty();
		backendCom.searchUser($("#inputUserName").val(), function(json) {
			var searchResult = json.searchResult;
			$select = $('#Users');
			for (var i = 0; i < searchResult.length; i++) {
				$select.append($('<option />', {
					value : (i + 1),
					text : searchResult[i]
				}));
			}
		});
	}

	//Debug load player feature
	/**
	 * load dummy players for default users select
	 * @param {JSON} users - default dummy users
	 */
	this.loadPlayer = function(users) {
		var e = document.createElement("select");
		e.setAttribute("name", "users");
		e.setAttribute("style", "cursor: pointer");
		e.id = "users";
		document.getElementById("overlay").insertBefore(e, document.getElementById("infoPush"));
		//$("#overlay").remove("#infoText");
		//$('#content').hide();
		//$("#overlay").show();
		var $select = $('#users');
		for (var key in users) {
			$select.append($('<option />', {
				value : (key),
				text : users[key] + "@"
			}));
		}
		var index = navigator.userAgent;
			if (index.indexOf("Chrome/") > -1) {
			    $("#users").val("1");
			}else if (index.indexOf("Safari/") > -1) {
			    $("#users").val("2");
			}
	};

	//Robot Stuff
	//Set next robot HTML-element
	/**
	 * set next robot div element builder
	 * @return div - returns a HTML-DIV containing default set up of a robot
	 */
	function setNext() {
		$("#btnOverlayOff").hide();
		var div = document.createElement("div");
		div.id = "setRobot";
		div.setAttribute("class", "setRobot");
		var x = document.createElement("LABEL");
		x.innerHTML = "Set next robot via arrow up or down for changes on selected numbers - MAX => +5<br><br>";
		//" + "<br>" + "
		div.appendChild(x);
		for (var i = 0; i < 6; i++) {
			var e = document.createElement("input");
			e.id = roboName[i] + "-setter";
			var n = document.createElement('br');
			e.setAttribute("class", "inputRobo");
			if(i<5){
				e.setAttribute("type", "number");
			}else{
				e.setAttribute("type", "text");
			}
			e.appendChild(n);
			e.value = roboVal[i];
			e.name = "number";
			e.title = roboName[i];
			div.appendChild(e);
		}
		var min = document.createElement("p");
		min.id = "setRobotControl";
		div.appendChild(min);
		return div;
	}

	//Get current robot values
	/**
	 * get the values and behavior of the next robot from document inputs
	 */
	this.getRobot = function() {
		/*
		 thisObj.roboSet = {
		 weaponPrototype : {
		 range : $("#range").val(),
		 rateOfFire : $("#rateOfFire").val(),
		 damage : $("#damage").val()
		 },
		 armor : $("#armor").val(),
		 enginePower : $("#enginePower").val(),
		 behaviour : $("#behavior").val()
		 };
		 */
		thisObj.roboSet[0] = $("#range-setter").val();
		thisObj.roboSet[1] = $("#rateOfFire-setter").val();
		thisObj.roboSet[2] = $("#damage-setter").val();
		thisObj.roboSet[3] = $("#armor-setter").val();
		thisObj.roboSet[4] = $("#enginePower-setter").val();
		thisObj.roboSet[5] = $("#behavior-setter").val();
		//console.log(thisObj.roboSet);
		//return thisObj.roboSet;
	};

	//Create a SetRobot-DIV-element for the overlay
	/**
	 * function for setting the first robot via function overlay
	 */
	this.setRobot = function() {
		var div = setNext();
		document.getElementById("overlay").appendChild(div);
		document.getElementById("overlay").insertBefore(div, document.getElementById("infoPush"));
		//thisObj.overlay('<div class="button" id="btnStartGame" style="cursor: pointer" onClick="controller.setRoboColor();controller.startGame();controller.overlayOff()">startGame</div></div>');
		var e = document.createElement("div");
		e.setAttribute("class", "button");
		e.setAttribute("onClick", "controller.getRobot();controller.startGame();controller.overlayOff();");
		e.innerHTML = "startGame";
		e.setAttribute("style", "cursor: pointer");
		style = "cursor: pointer";
		e.id = "btnStartGame";
		document.getElementById("overlay").insertBefore(e, document.getElementById("infoPush"));
		$("#overlay").remove("#infoText");
		$('#content').hide();
		$("#overlay").show();
  			
  			
	};
	
	//UtilityStuff
	//
	//Button initialization
	/**
	 * button initialization
	 */
	regisButtons = function() {

		$("#btnRegister").click(registration);
		$("#btnLogIn").click(logIn);
		$("#btnLogOut").click(logOut);
		$("#btnRemove").click(remove);
		$("#btnEndGame").click(endGame);
		$("#btnAccount").click(account);
		$("#btnChangeUser").click(changeUser);
		$("#btnSearchUser").click(searchUser);
	};

	//Input read
	/**
	 * input fields read
	 */
	function readInputFealds() {
		name = $("#inputUserName").val();
		password = $("#inputUserPass").val();
		eMail = $("#inputUserEMail").val();
	}

	//Check JSON for two values
	/**
	 * Description JSON CHECK for 2 values
	 * @param {JSON} json - JSON to search through
	 * @param {VALUE} value1 - any value to look for
	 * @param {VALUE} value2 - any value to look for
	 * @return boolean - true or false for success or not
	 */
	function check(json, value1, value2) {
		for (key in json) {
			if ( typeof (json[key]) === "object") {
				return checkForValue(json[key], value1, value2);

			} else if (json[key] === value2 && (JSON.stringify(json).indexOf(value1) > 0 )) {
				return true;
			}
		}
		return false;
	}

	//Overlay opener
	/**
	 * open a overlay with a text string called info
	 * @param {String} info - text string displayed in the overlay
	 */
	this.overlay = function(info) {
		$(document).ready(function() {
			$('#content').hide();
			var el = $("#overlay").show();
			$('<p id="infoText">INFO!<br><br>' + info + '</p>').insertBefore(".infoPush");
		});
	};

	//Overlay closer
	/**
	 * close the overlay and remove the tag with id "infoText"
	 */
	this.overlayOff = function() {
		$(document).ready(function() {
			$("#content").show();
			var el = $("#overlay").hide();
			//el.remove('#infoText');
			$("#infoText").remove();
			$("#btnOverlayOff").show();
			$("#btnStartGame").remove();
			$("#setRobot").remove();
			$("#overlay p").remove();
			$("#setNext").remove();
			$('#users').remove();
		});
	};
};

	//DEBUG FEATURES
	/**
	 * DEBUG FEATURES
	 */
	var controller;
	/**
	 * registers a bunch of auto-users for development
	 */
	function regist() {
		var users = ["a", "b", "c", "d", "e", "f", "g", "h"];
		var controller;
	
		var backendCom = new BackendCom();
		for (var i in users) {
			backendCom.registration(users[i] + '@', "", users[i] + '@', function(json) {
				//console.log(json);
	
			});
		}
	};
	
	/**
	 * initialization method for creating a new instance of class Controller
	 */
	function start() {
		controller = new Controller();
	};
	
	/**
	 * initialization method for debugging mode, creates a bunch of dummy users
	 */
	function startDebug() {
		
		//regist();
		var users = ["a", "b", "c", "d", "e", "f", "g", "h"];
		controller = new Controller();
		controller.setRobot();
		controller.loadPlayer(users);
	};
	