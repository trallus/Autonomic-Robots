function Controller() {
	//Global var's
	var thisObj = this;
	var server = "serverRequest";
	var backendCom = new BackendCom();
	thisObj.name;
	var password;
	var eMail;
	var user;
	var $select;
	//access for changed robot values by user
	thisObj.roboSet = [];
	//control values for upgrade a robot
	var control = 0;
	var upgrade = 5;
	//default robo issue names & values
	var roboName = ["range", "rateOfFire", "damage", "armor", "enginePower", "behavior"];
	var roboVal = [100, 30, 10, 100, 100, "gibts noch nicht"];

	//Get Html Parts
	//
	//... via AJAX
	function getAJAX(destination) {
		$.ajax({
			url : destination
		}).done(function(html) {
			$("#content").html(html).promise().done(function() {
				regisButtons();
			});

		});
	}

	//open debug file - auto.html - in a new window
	this.loadDebug = function() {
		window.open("auto.html");
	};

	//LoadAccountPage
	this.loadAccount = function() {
		getAJAX("account.html");
	};

	//LoadRegistrationPage
	this.loadRegister = function() {
		getAJAX("signup.html");
	};

	//LoadHomePage
	this.loadHome = function() {
		getAJAX("home.html");
	};

	//LoadLoginPage
	this.loadLogin = function() {
		getAJAX("login.html");
	};

	//LogOut
	this.logOut = function() {
		backendCom.logOut(function() {
			thisObj.loadLogin();
		});
	};

	this.endGame = function() {
		backendCom.endGame(name, password, eMail, function() {
			thisObj.loadHome();
		});
	};

	//handle User
	//
	//Start a Game
	this.startGame = function() {

		$.ajax({
			url : "robots.html"
		}).done(function(html) {
			$("#content").html(html).promise().done(function() {
				GameController.main(thisObj);
				document.getElementById("setNext").appendChild(setNext());
				$("#setNext").show();
				regisButtons();
			});
		});
	};
	
	//End a Game
	function endGame() {
		backendCom.endGame(name, password, eMail, function() {
			thisObj.loadHome();
			//$("#overlay").hide();
			$("#setNext").hide();
		});
	}
	
	//Registration
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

	//Change User Settings
	function changeUser() {
		readInputFealds();
		backendCom.changeUser(name, password, eMail, function(json) {
			if (json.userChanged) {
				thisObj.overlay(JSON.stringify(json));
				thisObj.loadHome();
			}
		});
	}

	//Open Account Settings Form
	function account() {
		thisObj.loadAccount();
	}

	//Login
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

	//LogOut
	function logOut() {
		backendCom.logOut(function() {
			thisObj.overlay('You\'ve been logged out!');
			thisObj.loadLogin();
		});
	}

	//Remove an User
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

	//Search User
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
	
	//Robot Stuff
	//
	//set next robot div element builder
	function setNext() {
		$("#btnOverlayOff").hide();
		var div = document.createElement("div");
		div.id = "setRobot";
		div.setAttribute("class", "setRobot");
		var x = document.createElement("LABEL");
		x.innerHTML = "Set next robot via arrow up or down for changes on selected numbers - MAX => +5<br><br>";//" + "<br>" + "
		div.appendChild(x);
		for (var i = 0; i < 6; i++) {
			var e = document.createElement("input");
			e.addEventListener("keydown", function(event) {
				if (event.which == 38) {
					thisObj.upgrade(0,event);
				}
				if (event.which == 40)
				{
					thisObj.upgrade(1,event);
				}
			});
			e.id = roboName[i] + "-setter";
			var n = document.createElement('br');
			e.setAttribute("class","inputRobo");
			e.appendChild(n);
			e.value = roboVal[i];
			e.name = "number";
			e.title = roboName[i];
			e.setAttribute("readonly", "");
			div.appendChild(e);
		}
		var min = document.createElement("p");
		min.id="setRobotControl";
		div.appendChild(min);
		return div;
	}

	//get the values and behavior of the next robot from document inputs
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
	
	//function for setting the first robot via function overlay
	this.setRobot = function() {
		var div = setNext();
		document.getElementById("overlay").appendChild(div);
		document.getElementById("overlay").insertBefore(div, document.getElementById("infoPush"));
		//thisObj.overlay('<div class="button" id="btnStartGame" style="cursor: pointer" onClick="controller.setRoboColor();controller.startGame();controller.overlayOff()">startGame</div></div>');
		var e = document.createElement("div");
		e.setAttribute("class","button");
		e.setAttribute("onClick", "controller.getRobot();controller.overlayOff();controller.startGame();");
		e.innerHTML="startGame";
		e.setAttribute("style","cursor: pointer");
		style="cursor: pointer";
		e.id="btnStartGame";
		document.getElementById("overlay").insertBefore(e, document.getElementById("infoPush"));
		$("#overlay").remove("#infoText");
		$('#content').hide();
		$("#overlay").show();
	};

	//control and check the allowed upgrade
	this.upgrade = function (mode, event) {
		if(upgrade <= 0 && control >= 5 && mode == 0) {
			window.alert('NO CHEATIN!!!');
		}
		else if(mode % 2 == 0) {
			upgrade--;
			event.explicitOriginalTarget.value++;
			control++;
		} else {
			upgrade++;
			event.explicitOriginalTarget.value--;
			control--;
		}
	};

	//UtilityStuff
	//
	//Button Initialization
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

	//InputRead
	function readInputFealds() {
		name = $("#inputUserName").val();
		password = $("#inputUserPass").val();
		eMail = $("#inputUserEMail").val();
	}

	//JSON CHECK for 2 values
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
	
	//open a overlay with a text string called json
	this.overlay = function(json) {
		$(document).ready(function() {
			$('#content').hide();
			var el = $("#overlay").show();
			$('<p id="infoText">INFO!<br><br>' + json + '</p>').insertBefore(".infoPush");
		});
	};

	//close the overlay and remove the tag with id "infoText"
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
		});
	};
};
	
//HTML ACCESS
var controller;

function start() {
	controller = new Controller();
};

function startDebug() {
	controller = new Controller();
	//console.log('nichts');
	var backendCom = new BackendCom();
	backendCom.logIn('', '@', function(json) {

		if (json.logedIn) {
			console.log(json.logedIn);
			controller.valid = true;
			controller.setRobot();

		} else
			controller.overlay('Wrong mail or password!<br>Please try again!!!');
			
	});
};

