describe("BackendCom Test", function () {
	
	//Global Var's
	var b = new BackendCom();
	var json;
	var valid;
	
	//Destinations
	var server = "serverRequest";
	var register = server + "/registration";
	var login = server + "/logIn";
	
	//Testuser
	var name = "name";
	var password = "password";
	var eMail = "eMail1";
	
    //Test-Case1
    it("should be able to sign up a new user", function() {
    	json = {};
        runs(function() {
        	b.registration ( name, password, eMail, function ( json1 ) {
        		json = json1;
        		
        	} );
        }, 500);

        waitsFor(function() {
            return json.registered;
        }, "JSON registered should be set", 750);

        runs(function() {
            expect(json.registered).toEqual(true);
        });
    });
    
    //Test-Case2
    it("should be able to login a user", function() {
    	json = {};
        runs(function() {
        	b.logOut( function () {});
        	b.logIn ( password, eMail, function ( json1 ) {
        		json = json1;
        		
        	} );
        }, 500);

        waitsFor(function() {
            return json.logedIn;
        }, "JSON logedIn should be set", 750);

        runs(function() {
            expect(json.logedIn).toEqual(true);
        });
    });
    
    //Test-Case3
    it("should be able to remove a user", function() {
    	valid = false;
        runs(function() {
        	b.remove ( name, password, eMail, function () {
        		valid = true;
        	});	
        	
        }, 500);

        waitsFor(function() {
            return valid;
        }, "valid should be set", 750);

        runs(function() {
            expect(valid).toEqual(true);
        });
    });
    
    //Test-Case4
    it("should NOT be able to start a game cause there is NO game yet", function() {
    	valid = false;
        runs(function() {
        	b.startGame( function () { valid = true; });
        }, 750);

        waitsFor(function() {
            return valid;
        }, " --> kein callback --> leere Methode in BackendCom.startGame() ! FEHLER ERWUENSCHT !", 1500);

        runs(function() {
            expect(valid).toEqual(true);
        });
    });
});