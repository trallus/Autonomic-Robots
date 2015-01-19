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
	/**
    *to hold maxPoints to ajust roboter settings
    * @type number
    * @memberof Controller
    */
	var maxPoints = 0;;
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
	var roboVal = [100, 30, 10, 100, 100/*, "gibts noch nicht"*/];
	/**
    *array for default behavior configuration
    * @type Array
    * @memberof Controller
    */
	var roboBeh = ["Angriff", "Verteidigung", "Schwarm"];
	//Get html parts method
	/**
	 * get html parts via AJAX
	 * @param {String} destination - specifies the path URL
	 */
	function getAJAX(destination, callback) {
		//$.ajaxSetup({cache: true});
		$.ajax({
			url : destination
		}).done(function(html) {
			$("#content").html(html).promise().done(function() {
				regisButtons();
				if(callback) callback();
			});
			//$.ajaxSetup({cache: false});
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
		getAJAX("home.html", function () {
			thisObj.getBehavior();
			$("#nameSpace").append('<p id=\"helloName\">... Hey ' + thisObj.name + '!');
		});
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
	 * user logout
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
	this.endGame = function (score, win) {
		backendCom.endGame(thisObj.name, score, eMail, function() {
			if(win == true) s = "V I C T O R Y";
			else if(win == false) s = "D E F E A T";
			else s ="D R A W";
			thisObj.overlay(s);
			
			var e = document.createElement("div");
			e.setAttribute("class", "button");
			e.setAttribute("onClick", "controller.overlayOff();");
			e.innerHTML = "home";
			e.setAttribute("style", "cursor: pointer; margin-left: 100px;margin-top: 60px;");
			style = "cursor: pointer";
			e.id = "home";
			$("#overlay p").append(e);

			thisObj.loadHome();
		});
	};
	//Set next robot
	/**
	 * set next robot
	 */
	this.setNextRobot = function (robot, callback) {
		backendCom.setNext(robot, callback);
	};
	//Get behaviors
	/**
	 * get behaviors
	 */
	this.getBehavior = function () {
		var r = [];
		backendCom.getBehavior(function (json) {
			if (json.behaviours) {
				r = [];
				for(var keys in json.behaviours) {
					r[keys] = json.behaviours[keys];
				}
			}
			roboBeh = r;
		});
	}
	//Join batlle query
    /**
     * join battle query
     */
	this.joinBattleQuery = function (callback) {
		backendCom.joinBattleQ(callback);
	};
	
	this.setBehave = function (callback) {
		var b = {
			robotID : $("#selectedRobot").attr("value"),
		    behaviour : $("#new-behavior").val()
		}
		backendCom.setBehave(b, callback);
	}
	//Start a game
	/**
	 * starts a Game
	 */
	this.startGame = function() {
		var robot = thisObj.getRobot();
		getAJAX("robots.html", function() {
			
			//$.ajaxSetup({cache: false});
			
			GameController.mainGC(thisObj, robot);
			
			document.getElementById("setNext").appendChild(setNext());
			plusMinusButton ();
			
			var e = document.createElement("div");
			e.setAttribute("class", "button");
			e.setAttribute("onClick", "controller.setNextRobot(controller.getRobot(), function () {});");
			e.innerHTML = "confirm";
			e.setAttribute("style", "cursor: pointer");
			style = "cursor: pointer";
			e.id = "confirmRobot";
			
			$("#setNext").append(e);
			$("#setNext").show();
		});
	};

	//Registration
	/**
	 * registration
	 */
	function registration() {
		readInputFealds();
		if (thisObj.name.trim() == '') {
			thisObj.overlay('Your name can\'t be empty');
		} else {
			if (eMail.indexOf('@') < 0) {
				thisObj.overlay('This is no valid eMail');
			} else {
				backendCom.registration(thisObj.name, password, eMail, function(json) {

					if (json.registered) {
						var text = 'Successfully registered... <br>Welcome ' + thisObj.name + '!!!<br>';
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
		backendCom.changeUser(thisObj.name, password, eMail, function(json) {
			if (json.userChanged) { 
				var s = '';
				if (thisObj.name) s += "new name: " + thisObj.name + "<br>";
				if (eMail) s += "new email: " + eMail + "<br>";
				if (password) s += "password: " + "<br>successfully changed";
				thisObj.overlay(s);
				thisObj.loadHome();
			}
			if (json.failure) thisObj.overlay(json.failure); 
			
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
				thisObj.name = json.username;
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
			name : thisObj.name,
			password : password,
			eMail : eMail
		};
		backendCom.remove(thisObj.name, password, eMail, function(json) {
			if (json.removed) {
				thisObj.overlay('User ' + thisObj.name + ' has been removed');
				thisObj.loadRegister();
			} else {
				thisObj.overlay('There is no User: ' + thisObj.name + '\n... Please try again...\nType in name, eMail and password');
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
	//Robot Stuff
	//Set next robot HTML-element
	/**
	 * set next robot div element builder
	 * @return div - returns a HTML-DIV containing default set up of a robot
	 */
	function setNext() {
		$("#btnOverlayOff").hide();
		
		var div = document.createElement("div");
		div.id = "setNext";
		div.setAttribute("class", "setRobot");
		
		var x = document.createElement("LABEL");
		x.innerHTML = "Set next robot<br><br>";
		div.appendChild(x);
		
		for (var i = 0; i < 5; i++) {
			var n = document.createElement('br');
			
			var hug = document.createElement("div");
			hug.setAttribute("class", "roboTableSet")
			hug.appendChild(n);
			
			var z = document.createElement("div");
			hug.innerHTML = roboName[i] + '';
			z.setAttribute("style", "text-align: left;float: none;width:90px;")
			
			var m = document.createElement("div");
			m.innerHTML = "-";
			m.id = roboName[i] + "-min";
			m.value=roboName[i];
			m.setAttribute("style", "float: right;text-align:  center;width: 20px;cursor: pointer;")
			
			var e = document.createElement("input");
			e.id = roboName[i] + "-setter";
			e.setAttribute("class", "inputRobo");
			if(i<5){
				e.setAttribute("type", "number");
				e.setAttribute("disabled", "disabled");
				e.setAttribute("style", "text-align: center;float: right;");
			}else{
				e.setAttribute("type", "text");
			}
			if (Object.keys(thisObj.roboSet).length >0) e.value = thisObj.roboSet[i];
			else e.value = "0";
			e.title = roboName[i];
			
			var p = document.createElement("div");
			p.innerHTML = "+";
			p.id = roboName[i] + "-add";
			p.value=roboName[i];
			p.setAttribute("style", "float: right;text-align:  center;width: 20px;cursor: pointer;")
			hug.appendChild(p);
			hug.appendChild(e);
			hug.appendChild(m);
			div.appendChild(hug);
		}
		
		var hug = document.createElement("div");
		hug.setAttribute("class", "roboTableSet")
		
		var n = document.createElement('br');
		
		var b = document.createElement("div");
		hug.innerHTML = "behavior";
		b.setAttribute("style", "text-align:  left;width:85px;height:18px;")
		
		var s = document.createElement("select");
		s.id=roboName[5] + "-setter";
		s.setAttribute("style", "text-align:  left;float: right;width: 90px;")
		var r = roboBeh;
		
		for (var key in r) {
			var e = document.createElement("option");
			e.value = r[key];
			e.text = r[key];
			s.appendChild(e);
		}
		hug.appendChild(s);
		div.appendChild(hug);
		return div;
	}

	//Get current robot values
	/**
	 * get the values and behavior of the next robot from document inputs
	 */
	this.getRobot = function() {
		 var roboter = {
		 range : $("#range-setter").val(),
		 rateOfFire : $("#rateOfFire-setter").val(),
		 damage : $("#damage-setter").val(),
		 armor : $("#armor-setter").val(),
		 enginePower : $("#enginePower-setter").val(),
		 behaviour : $("#behavior-setter").val()
		 };
		 
		thisObj.roboSet[0] = $("#range-setter").val();
		thisObj.roboSet[1] = $("#rateOfFire-setter").val();
		thisObj.roboSet[2] = $("#damage-setter").val();
		thisObj.roboSet[3] = $("#armor-setter").val();
		thisObj.roboSet[4] = $("#enginePower-setter").val();
		thisObj.roboSet[5] = $("#behavior-setter").val();
		
		return roboter;
	};
	//Create a SetRobot-DIV-element for the overlay
	/**
	 * function for setting the first robot via function overlay
	 */
	this.setRobot = function() {
		$("#overlay p").empty();
		var div = setNext();
		document.getElementById("overlay").appendChild(div);
		document.getElementById("overlay").insertBefore(div, document.getElementById("infoPush"));
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
		
		plusMinusButton ();
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
		$("#btnEndGame").click(thisObj.endGame);
		$("#btnAccount").click(account);
		$("#btnChangeUser").click(changeUser);
		$("#btnSearchUser").click(searchUser);
	};
	//Input read
	/**
	 * input fields read
	 */
	function readInputFealds() {
		thisObj.name = $("#inputUserName").val();
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
			$("#infoText").remove();
			$("#btnOverlayOff").show();
			$("#btnStartGame").remove();
			$("#setRobot").remove();
			$("#overlay p").remove();
			$("#setNext").remove();
			$('#users').remove();
		});
	};
	//Plus minus functionality for setting a robot
	/**
	 * creates a click-listener for the setRobot-div (HTML), handle logic for max clicks
	 */
	function plusMinusButton () {
		$('#setNext').click(function (event) {
			if (maxPoints >= 10 && event.originalEvent.target.id.indexOf("add")>0) {
				window.alert("You already added 10 points!");
			} else if (event.originalEvent.target.id.indexOf("min")>0 && document.getElementById(event.originalEvent.target.value + "-setter").value == 0) {
				window.alert("No negative numbers allowed!");
			} else if(event.originalEvent.target.id.indexOf("add")>0) {
				maxPoints++;
				document.getElementById(event.originalEvent.target.value + "-setter").value++;
			} else if(event.originalEvent.target.id.indexOf("min")>0) {
				maxPoints--;
				document.getElementById(event.originalEvent.target.value + "-setter").value--;
			}
		});
	}
};
//Start
/**
 * default start for whole application
 */
//Global var controller
/**
*to create user related controller-object
* @type Controller
*/
var controller;
function start() {
	controller = new Controller();
}
//utility.js
function selectBehave () {
	var s = document.createElement("select");
	s.id= "new-behavior";
	$("#behavior-setter option").each(function(){
		var x = document.createElement("option");
		x.value = this.value;
		x.text = this.value;
		s.appendChild(x);
	});
	return s;
}