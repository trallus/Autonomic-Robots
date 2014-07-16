describe("BackendCom/ServerCom Test", function () {
	
	//Global Var's
	var b = new BackendCom();
    var timeout = 10000;
	
	//Test user attributes
	var name = "name";
	var password = "password";
	var eMail = "eMail1";
	
    //Test-Case1
    it("Test-Case1 - should be able to sign up a new user", function() {
    	var json = new Object();
        runs(function() {
        	//Registration for a new user
        	b.registration ( name, password, eMail, function ( json1 ) {
        		json = json1;
        		
        	} );
        }, timeout);
        //waits for responding JSON
        waitsFor(function() {
            return json.registered;
        }, "JSON registered should be set", timeout);
        //Check for valid registration
        runs(function() {
            expect(json.registered).toEqual(true);
        });
    });
    
    //Test-Case2
    it("Test-Case2 - should be able to login a user", function() {
    	var json = {};
        runs(function() {
        	//LogOut user from Test-Case1
        	b.logOut( function () {});
        	//Login this user again
        	b.logIn ( password, eMail, function ( json1 ) {
        		json = json1;
        		
        	} );
        }, timeout);
      //waits for responding JSON
        waitsFor(function() {
            return json.logedIn;
        }, "JSON logedIn should be set", timeout);
      //Check for valid login
        runs(function() {
            expect(json.logedIn).toEqual(true);
        });
    });
    
    //Test-Case3
    it("Test-Case3 - should be able to remove a user", function() {
    	var json = {};
        runs(function() {
        	//removes user from Test-Cas1 & 2
        	b.remove ( name, password, eMail, function ( json1 ) {
        		json = json1;
        	});	
        	
        }, 500);

        waitsFor(function() {
            return json.removed;
        }, "valid should be set", timeout);
      //waits for responding JSON
        runs(function() {
        	//Check for valid removed issue in JSON
            expect(json.removed).toEqual(true);
        });
    });
    
    //Test-Case4
    it("Test-Case4 - should be able to start a game", function() {
    	var valid;
        runs(function() {
        	//Starts a game
        	b.startGame( function () { valid = true; });
        }, timeout);

        waitsFor(function() {
            return valid;
        }, "valid should be set", 1500);
      //waits for responding valid
        runs(function() {
        	//Check valid
            expect(valid).toEqual(true);
        });
    });
    
    //Test-Case5
    it("Test-Case5 - should be able to search for other online players", function() {
    	//sets up a list of new users and a list of expected search result
        var registerResult = new Array();
        var searchName = "mic";
        var searchResult;
        var testUserNames = [
            "micha",
            "müller",
            "hinz",
            "mict",
            "mic",
            "test",
            "bla",
            "michaela"
        ];
        var expectetSearchUserNames = [
            "micha",
            "mict",
            "mic",
            "michaela"
        ];
        
        // sign up new users
        for (var i in testUserNames) {
            b.registration ( testUserNames[i], password, eMail+i, function ( json ) {
                registerResult.push(json);
            });
        }
        // wait for registrations
        waitsFor(function() {
            return registerResult[testUserNames.length - 1];
        }, "valid should be set", timeout);
        
        // search for the user 'mic'
        runs(function() {
            b.searchUser ( searchName, function ( json ) {
                searchResult = json.searchResult;
            });
        }, 500);
        //wait for searchResult
        waitsFor(function() {
            return searchResult;
        }, "searchResult should be set", timeout);
        //Compare searchResult with expected searchResult
        runs(function() {
            for (var i in expectetSearchUserNames) {
                var tmp = expectetSearchUserNames[i];
                var position = searchResult.indexOf(tmp);
                expect(position).toBeGreaterThan(-1);
                //remove the found name
                searchResult.splice(position, 1);
            }
            
            // be sure that there are no names left
            expect(searchResult.length).toBe(0);
            
            // cleanUp the DB
            for (var i in testUserNames) {
                b.remove ( testUserNames[i], password, eMail+i, function ( json ) {
                	// remove has been tested above
                });
            }
        });
    });
    
    //Test-Case6
    it("Test-Case6 - should be able to change name, eMail, password of an user", function() {
    	var json = new Object();
        runs(function() {
        	//new user registration
            b.registration ( name, password, eMail, function ( json1 ) {
                json = json1;
            } );
        }, timeout);
        //check for valid registration
        waitsFor(function() {
            return json.registered;
        }, "JSON registered should be set", timeout);
        //check for valid login
        runs(function() {
        	//LogOut
        	b.logOut(function ( ) {});
        	//Login
            b.logIn ( password, eMail, function ( json1 ) {
                json = json1;
            } );
        }, timeout);
        waitsFor(function() {
            return json.logedIn;
        }, "JSON logedIn should be set", timeout);
        //set up new user attributes
        var newName = "newName";
        var newEmail = "newEmail";
        var newPassword = "newPassword";
        
        //ChangeUser
        runs(function() {
            //changes the attributes of the user
            b.changeUser ( newName, newPassword, newEmail, function ( json1 ) {
                json = json1;
            } );
        }, timeout);
        //waits for userChanged to be set
        waitsFor(function() {
            return json.userChanged;
        }, "JSON userChanged should be set", timeout);
        //check for login with new attributes
        runs(function() {
            b.logOut(function ( ) {});
            b.logIn ( newPassword, newEmail, function ( json1 ) {
                json = json1;
            } );
        }, timeout);
        //waits for valid login
        waitsFor(function() {
            return json.logedIn;
        }, "JSON logedIn should be set", timeout);

        runs(function() {
            expect(json.logedIn).toEqual(true);
        });
        //remove this user
        runs(function() {
            b.remove ( newPassword, newPassword, newEmail, function ( json ) {
                // remove has been tested above
            });
        });
    });
});