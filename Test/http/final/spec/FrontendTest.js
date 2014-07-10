describe("BackendCom Test", function () {
	
	//Global Var's
	var b = new BackendCom();
	var json;
	var valid;
        var timeout = 10000;
	
	//Testuser
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
    	json = {};
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
    	json = {};
        runs(function() {
        	b.remove ( name, password, eMail, function ( json1 ) {
                    console.log(json1);
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
    
    //Test-Case4
    it("should NOT be able to start a game cause there is NO game yet", function() {
    	valid = false;
        runs(function() {
        	b.startGame( function () { valid = true; });
        }, timeout);

        waitsFor(function() {
            return valid;
        }, " --> kein callback --> leere Methode in BackendCom.startGame() ! FEHLER ERWUENSCHT !", 1500);

        runs(function() {
            expect(valid).toEqual(true);
        });
    });
});