function swp () {
    var obj = this;
    
    this.position = false;
    function getNextServerFrame () {
    	//dummy robot
    	var robot = {
            weaponPrototype : {
                range : 100,
                rateOfFire : 30,
                damage : 10
            },
            color: controller.colors[0],
            armor : 100,
            enginePower : 100,
            behaviour : "gibts noch nicht"
        };
    	//set first robot
    	$.ajax({
            type: "POST",
            data: JSON.stringify ( robot/*GameController.allRobots*/ ),
            dataType: "json",
            url: "serverRequest/game-setNextRobot"
        }).done ( function ( json ) {
            console.log(json);
        	//join BattleQuery
            $.ajax({
                url: "serverRequest/game-joinBattleQuery"
            }).done ( function ( json ) {
                console.log(json);
                var intervallCounter = 21;
                var intervall = window.setInterval(
                    function () {
                    	//get game situation in 1/sec up to 20x
                        $.ajax({
                            url: "serverRequest/game-getGameSituation"
                        }).done ( function ( json ) {
                        	obj.position = json;
                        	/*
                        	obj.position = {
                        		posX: json.gameSituation.ich[0].position[0],
                        		posY: json.gameSituation.ich[0].position[1],
                        	};
                        	*/
                            //console.log(json.gameSituation.ich[0].position);
                        });
                        intervallCounter--;
                        //end game after 20x
                        if (intervallCounter < 0) {
                            window.clearInterval(intervall);
                            $.ajax({
         					   url: "home.html"
					        }).done ( function ( html ) {
					        	$("#content").html(html).promise().done(function(){
					        		regisButtons();
					        	});
					        	
					        });
                        }
                    }, 1000
                );
            });
        });		
    
    	/*
        $.ajax({
            url: "serverFrameRequest",
            data: "player1",
            error: function ( info, b, c ) {
                console.log(info);
                console.log(b);
                console.log(c);
            }
        }).done ( function ( json ) {
            obj.position = json;
            console.log("Server-Client latenz im ms: " + (json.time - new Date().getTime()));
            getNextServerFrame();
        }).fail ( function ( info ) {
            console.log(info);
        });
        
        $.ajax({
            url: "/",
            error: function ( info, b, c ) {
                console.log(info);
                console.log(b);
                console.log(c);
            }
        }).done ( function ( json ) {
        }).fail ( function ( info ) {
            console.log(info);
        });
        */
    }
    getNextServerFrame();
}