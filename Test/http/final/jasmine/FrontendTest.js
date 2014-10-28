describe("BackendCom/ServerCom Test", function () {
	
	//Global Var's
	var b = new BackendCom();
    var timeout = 10000;
	
	//Test user
	var name = "name";
	var password = "password";
	var eMail = "eMail1";
	
    //Test-Case1
    it("should be able to sign up a new user", function() {
    	var json = new Object();
        runs(function() {
        	b.registration ( name, password, eMail, function ( json1 ) {
        		json = json1;
        		
        	} );
        }, timeout);

        waitsFor(function() {
            return json.registered;
        }, "JSON registered should be set", timeout);

        runs(function() {
            expect(json.registered).toEqual(true);
        });
    });
    
    //Test-Case2
    it("should be able to login a user", function() {
    	var json = {};
        runs(function() {
        	b.logOut( function () {});
        	b.logIn ( password, eMail, function ( json1 ) {
        		json = json1;
        		
        	} );
        }, timeout);

        waitsFor(function() {
            return json.logedIn;
        }, "JSON logedIn should be set", timeout);

        runs(function() {
            expect(json.logedIn).toEqual(true);
        });
    });
    
    //Test-Case3
    it("should be able to remove a user", function() {
    	var json = {};
        runs(function() {
        	b.remove ( name, password, eMail, function ( json1 ) {
                    //console.log(json1);
        		json = json1;
        	});	
        	
        }, 500);

        waitsFor(function() {
            return json.removed;
        }, "valid should be set", timeout);

        runs(function() {
            expect(json.removed).toEqual(true);
        });
    });
    /*
    //Test-Case4
    it("should be able to start a game", function() {
    	var valid;
        runs(function() {
        	b.startGame( function () { valid = true; });
        }, timeout);

        waitsFor(function() {
            return valid;
        }, "valid should be set", 1500);

        runs(function() {
            expect(valid).toEqual(true);
        });
    });
    *//*
    //Test-Case5
    it("should be able to search for other online players", function() {
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
        
        // search
        runs(function() {
            b.searchUser ( searchName, function ( json ) {
                searchResult = json.searchResult;
            });
        }, 500);

        waitsFor(function() {
            return searchResult;
        }, "valid should be set", timeout);

        runs(function() {
            for (var i in expectetSearchUserNames) {
                var tmp = expectetSearchUserNames[i];
                var position = searchResult.indexOf(tmp);
                expect(position).toBeGreaterThan(-1);
                //remove the found name
                searchResult.splice(position, 1);
            }
            
            // be sure that not to many names
            expect(searchResult.length).toBe(0);
            
            // cleanUp
            for (var i in testUserNames) {
                b.remove ( testUserNames[i], password, eMail+i, function ( json ) {
                	// remove has been tested above
                });
            }
        });
    });
    /*
    //Test-Case6
    it("should be able to change name, eMail, password of an user", function() {
    	var json = new Object();
        runs(function() {
            b.registration ( name, password, eMail, function ( json1 ) {
                json = json1;
            } );
        }, timeout);

        waitsFor(function() {
            return json.registered;
        }, "JSON registered should be set", timeout);
        
        runs(function() {
            b.logIn ( password, eMail, function ( json1 ) {
                json = json1;
            } );
        }, timeout);

        waitsFor(function() {
            return json.logedIn;
        }, "JSON logedIn should be set", timeout);
        
        var newName = "newName";
        var newEmail = "newEmail";
        var newPassword = "newPassword";
        
        runs(function() {
            //b.logOut(function ( ) {});
            b.changeUser ( newName, newPassword, newEmail, function ( json1 ) {
                json = json1;
            } );
        }, timeout);

        waitsFor(function() {
            return json.userChanged;
        }, "JSON registered should be set", timeout);
        
        runs(function() {
            b.logOut(function ( ) {});
            b.logIn ( newPassword, newEmail, function ( json1 ) {
                json = json1;
            } );
        }, timeout);

        waitsFor(function() {
            return json.logedIn;
        }, "JSON logedIn should be set", timeout);

        runs(function() {
            expect(json.logedIn).toEqual(true);
        });
        
        runs(function() {
            b.remove ( newPassword, newPassword, newEmail, function ( json ) {
                // remove has been tested above
            });
        });
    });
    */
 });