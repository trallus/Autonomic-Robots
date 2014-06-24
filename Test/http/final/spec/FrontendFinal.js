describe("BackendCom Test", function () {
	
	var b;
	var c;
	var server = "serverRequest";
	var register = server + "/registration";
	var login = server + "/logIn";
	
	var name = "name";
	var password = "password";
	var eMail = "eMail1";
	
	var user1 = {
	        name : name,
	        password : password,
	        eMail : eMail
	    };
	
	var user2 = user1;
    
    it("should be able to sign up a new user", function() {
    	b = new BackendCom();
    	var json = {};
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
    
    it("should be unable to sign up a user with wrong password", function() {
    	b = new BackendCom();
    	var json = {};
        runs(function() {
        	b.registration ( name, password, eMail, function () {});
        	b.logOut( function () {});
        	b.logIn(password, eMail, function ( json1 ) {
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

    it("should be unable to to start a game cause there is no game yet", function() {
    	b = new BackendCom();
    	var valid = false;
        runs(function() {
        	b.startGame( function () { valid = true; });
        }, 750);

        waitsFor(function() {
            return valid;
        }, " --> leere Methode! FEHLER ERWUENSCHT!", 1500);

        runs(function() {
            expect(valid).toEqual(false);
        });
    });
});