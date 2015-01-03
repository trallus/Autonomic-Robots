/**
 * BackendCom - creates communication between client and server
 * @class BackendCom
 * @project Automatic Robots
 * @author Florian Krüllke
 * @version 0.1
 */

function BackendCom ( ) {
	//Global variables
	/**
    *to create a static methods
    * @type this
    * @memberof BackendCom
    */
	var thisObj = this;
	/**
    *to hold user-object
    * @type object
    * @memberof BackendCom
    */
    var user;
    /**
     *name space for build up URLs 
     * @type String
     * @memberof BackendCom
     */
	var server = "serverRequest";
	/**
    *name of the user
    * @type String
    * @memberof BackendCom
    */
	this.name;
	/**
    *password of the user
    * @type String
    * @memberof BackendCom
    */
    this.password;
    /**
     *email of the user
     * @type String
     * @memberof BackendCom
     */
    this.eMail;
    
    //Static Methods
    //to create interaction with other classes, mainly Controller.js
    /**
     * end the current game
     * @param {String} name - name of the user
     * @param {String} password - password of the user
     * @param {String} eMail - email of the user
     * @param {Function} callback - callback function
     */
    this.endGame = function  ( name, password, eMail, callback ) {
    	user = {
            name : name,
            password : password,
            eMail : eMail
        };
        serverRequest ( user, server + "/endGame", callback );
	};
    
	//Start a game
	/**
	 * starts a game
	 * @param {Function} callback - callback function
	 */
	this.startGame = function ( callback ) {
		callback();
    };
    
	//Remove the user
    /** 
     * remove the user
     * @param {String} name - name of the user
     * @param {String} password - password of the user
     * @param {String} eMail - email of the user
     * @param {Function} callback - callback function
     */
    this.remove = function ( name, password, eMail, callback) {
    	user = {
            name : name,
            password : password,
            eMail : eMail
        };
        serverRequest( user, server + "/remove", callback );
    };
    
    //LogOut
    /**
     * logout the user
     * @param {Function} callback - callback function
     */
    this.logOut = function ( callback ) {
        serverRequest( null, server + "/logOut", callback );
    };
    
    //Login
    /**
     * login the user
     * @param {String} password - password of the user
     * @param {String} eMail - email of the user
     * @param {Function} callback - callback function
     */
    this.logIn = function ( password, eMail, callback) {
    	user = {
            name : "",
            password : password,
            eMail : eMail
        };
        serverRequest( user, server + "/logIn", callback );
    };
    
    //Registration
    /**
     * registration for an user
     * @param {String} name - name of the user
     * @param {String} password - password of the user
     * @param {String} eMail - email of the user
     * @param {Function} callback - callback function
     */
    this.registration = function ( name, password, eMail, callback ) {
        var user = {
            name : name,
            password : password,
            eMail : eMail
        };
        serverRequest( user, server + "/registration", callback );
    };
    
    //Search for other online users
    /**
     * search for another online user
     * @param {String} name - name of the user
     * @param {Function} callback - callback function
     */
    this.searchUser = function ( name, callback ) {
        var user = {
            name : name,
            password : "",
            eMail : ""
        };
        
        serverRequest( user, server + "/searchUser", callback );
    };
    
    //Change user informations
    /**
     * change user informations
     * @param {String} name - name of the user
     * @param {String} password - password of the user
     * @param {String} eMail - email of the user
     * @param {Function} callback - callback function
     */
    this.changeUser = function ( name, password, eMail, callback ) {
        var user = {
            name : name,
            password : password,
            eMail : eMail
        };
        
        //serverRequest( user, server + "/changeUser", callback );
        serverRequest( user, server + "/changeUser", function () {
        	controller.overlay('Changed into:<br>' + name + '<br>' + password + '<br>'  + eMail);
        });
    };
    
    //Set next robot
    /**
     * set next robot
     * @param {JSON} robot - robot data send to server
     * @param {Function} callback - callback function
     */
    this.setNext = function (robot, callback) {
    	serverRequest( robot, server + "/game-setNextRobot", callback );
    } 
    
    //Join batlle query
    /**
     * join battle query
     * @param {Function} callback - callback function
     */
    this.joinBattleQ = function ( callback ) {
    	serverRequest( undefined, server + "/game-joinBattleQuery", callback );  
    }
    
    //Set robot behavior
    /**
     * set robot behavior
     * @param {JSON} b - robot behavior data send to server
     * @param {Function} callback - callback function
     */
    this.setBehave = function (b, callback) {
    	serverRequest( b, server + "/game-setBehaviour", callback );
    }
    
    //Get behaviors
    /**
     * get behaviors
     */
    this.getBehavior = function (callback) {
    	serverRequest(undefined, server + "/game-getBehaviours", callback);
    }

    
    //inner server request method
    /**
     * inner-server communication method
     * @param {JSON} json - data send to server
     * @param {String} destination - specifies the URL
     * @param {Function} callback - callback function
     */
    function serverRequest  ( json, destination, callback ) {
        $.ajax({
            type: "POST",
            url: destination,
            data: JSON.stringify ( json ),
            dataType: "json",
            //log errors via script console
            /**
             * Description for any not loaded - not found content
             * @param {JSON} info - contains failure informations
             * @param {Object} b - for further error events
             * @param {Object} c - for further error events
             */
            error: function ( info, b, c ) {
                console.log(info);
                console.log(b);
                console.log(c);
            }
        }).done (
    		function (json) {
    			if (json.logedIn == false && json.registered == false) {
                    thisObj.overlay(json.failure);
    			}
    			callback (json);
    		}).fail ( function ( info ) {
            console.log(info);
        });
    }
};	