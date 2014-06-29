function CollisionControler ( physicObjects ) {
    
    var aircraftRegister = physicObjects;
    
    this.onFrame = function onFrame (context, time) {
        collisionDetection(time);
    };

    function collisionDetection ( time ) {
                    
        for (var i=0; i<aircraftRegister.length; i++) {
            
            var pos1 = aircraftRegister[i].getPosition();
            var rad1 = aircraftRegister[i].getDimension();
            
            for (var k=i+1; k<aircraftRegister.length; k++) {
                
                var pos2 = aircraftRegister[k].getPosition();
                var rad2 = aircraftRegister[k].getDimension();
            
                var a = pos1[0]-pos2[0];
                var b = pos1[1]-pos2[1];
                
                var distance = Math.sqrt (a*a + b*b);
                
                if (distance < rad1+rad2) {
                    
                    // aircraftRegister[i].collisionWith(aircraftRegister[k]);
                    // aircraftRegister[k].collisionWith(aircraftRegister[i]);

                    // wich distance have the aircrafts in the (close) future (.01 sec)
                    var futureTime = .01; //sec
                    
                    var mV1 = aircraftRegister[i].getMoveVector();
                    var mV2 = aircraftRegister[k].getMoveVector();
                    
                    var pos1a = new Array(pos1[0]+mV1[0]*futureTime, pos1[1]+mV1[1]*futureTime);
                    var pos2a = new Array(pos2[0]+mV2[0]*futureTime, pos2[1]+mV2[1]*futureTime);
            
                    a = pos1a[0]-pos2a[0];
                    b = pos1a[1]-pos2a[1];

                    var futureDistance = Math.sqrt (a*a + b*b);
                    
                    if (futureDistance > distance) return;  // zusammen geschoben aber schon collidiert
                    
                    // real rollision detected
                    
                    var kV = new Array (pos2[0]-pos1[0], pos2[1]-pos1[1]); // Vektor von mittelpunkt zu Mittelpunkt (Stoßvektor, Kollisionsvektor kV)
                    var tV = new Array (kV[1], -kV[0]);    // Vekrot entlang der Tangente zwischen den Scheiben (Tangentenvektor tV) tv = kV "+ 90°"
                    
//                    log("pos1 "+pos1);
//                    log("pos2 "+pos2);
//                    log("speed1 "+aircraftRegister[i].getSpeed());
//                    log("speed1 "+aircraftRegister[k].getSpeed());
//                    log("mV1 "+mV1);
//                    log("mV2 "+mV2);
//                    log("kV "+kV);
//                    log("tV "+tV);
                    
                    // aufteilung der Bewegungsvektoren (mV(x)) auf kV und tV
                    var matrix = new Array (
                        new Array(kV[0], tV[0], mV1[0]),
                        new Array(kV[1], tV[1], mV1[1])
                    );
                    matrix = new $ckMatrix(matrix);
                    var ab = matrix.getResult();
                    mV1 = new Array(new Array (kV[0]*ab[0], kV[1]*ab[0]), new Array (tV[0]*ab[1], tV[1]*ab[1]));
                    // auch für mV2
                    matrix = new Array (
                        new Array(kV[0], tV[0], mV2[0]),
                        new Array(kV[1], tV[1], mV2[1])
                    );
                        
                    matrix = new $ckMatrix(matrix);
                    ab = matrix.getResult();
                    mV2 = new Array(new Array (kV[0]*ab[0], kV[1]*ab[0]), new Array (tV[0]*ab[1], tV[1]*ab[1]));
                    
//                    log("mV1 "+mV1);
//                    log("mV2 "+mV2);
                    
                    // War für ohne Masse berücksitigung gut
                    // tauschen der Kollisionsteile (auf eindimensionalen Stoß reduziert :)
//                    var tmp = new Array (mV1[0][0], mV1[0][1]);
//                    mV1[0] = new Array (mV2[0][0], mV2[0][1]);
//                    mV2[0] = tmp;
//                    
                    // berücksicheigen der masse
                    // Achtung: hier noch nur Kollisionsanteile
                    var m1 = aircraftRegister[i].getMass();
                    var m2 = aircraftRegister[k].getMass();
                    var sp1 = 2 * (m1*mV1[0][0]+m2*mV2[0][0]) / (m1+m2) // 2 * geschwindigkeit des gemeinsamen schwerpunktes
                    var sp2 = 2 * (m1*mV1[0][1]+m2*mV2[0][1]) / (m1+m2) // 2 * geschwindigkeit des gemeinsamen schwerpunktes

                    mV1[0] = new Array (sp1-mV1[0][0], sp2-mV1[0][1]);
                    mV2[0] = new Array (sp1-mV2[0][0], sp2-mV2[0][1]);
                    
                    // addieren der aufgeteilten Bewegungsvektoren zu einem Vektor
                    mV1 = new $ckVector (mV1[0][0]+mV1[1][0], mV1[0][1]+mV1[1][1]);
                    mV2 = new $ckVector (mV2[0][0]+mV2[1][0], mV2[0][1]+mV2[1][1]);

                    // neue Richtiung setzen
                    aircraftRegister[i].setDirection(mV1.getDirektionXY());
                    aircraftRegister[k].setDirection(mV2.getDirektionXY());
                    
                    // neue Geschwinigkeit setzen
                    aircraftRegister[i].setSpeed(mV1.getLength());
                    aircraftRegister[k].setSpeed(mV2.getLength());
                    // schieb die objecte ein stück aus einander damit sie nicht zusammen kleben bleiben
                    pos1[0] -= kV[0]*time;
                    pos1[1] -= kV[1]*time;
                    pos2[0] += kV[0]*time;
                    pos2[1] += kV[1]*time;
                    aircraftRegister[i].setPosition(pos1);
                    aircraftRegister[k].setPosition(pos2);
//                    log("speed1 "+mV1.getLength());
//                    log("speed1 "+mV2.getLength());
//                    //test=false;
//                    log ("coll");
                }
            }
        }
    }
}