/**
 * Description
 * @method $ckMatrix
 * @param {} matrixArray
 * @return 
 */
function $ckMatrix (matrixArray) {
    //zum lösen von lineatren Gleichungssysthemen nach dem Muster siehe $ckMatrixTest
    
    checkSort(matrixArray);
    
    /**
     * Description
     * @method getResult
     * @return CallExpression
     */
    this.getResult = function () {
        
        var result = calculate(matrixArray);
        
        var tempArray = matrixArray[0];
        
        return multiplyOut(result, tempArray);
    }
    
    /**
     * Description
     * @method calculate
     * @param {} inputMA
     * @return 
     */
    function calculate (inputMA) {
        //lösung Lineares Gleich.Sys. jede Stufe eine Gleichung weniger
        var localMA = new Array(inputMA.length-1);
        var tempArray;
        //erstelle nächstes LGS
        for (var i=0; i < localMA.length; i++) {
            tempArray = new Array(inputMA.length);
            //Erster Wert eine Zeile wird mint dem ersten Wert der abzuziehenden Zeile Multipliziert. So wird die erste spalte immer 0
            for (var j=0; j < inputMA[0].length-1; j++) {
                tempArray[j] = inputMA[i+1][0] * inputMA[0][j+1];
                tempArray[j] -= inputMA[0][0] * inputMA[i+1][j+1];
            }
            //neue Gleichung (Reihe) schreiben
            localMA[i] = tempArray;
        }
        
        //beim rückwerts einsetzen darf der neu zu bestimmente wert nicht 0 mal vorkommen
        //ges x  0x+4y=5  geg y=3 ist nicht für x lösbar deshalb sortieren
        checkSort(localMA);
        tempArray = localMA[0];
        
        var result;
        
        if (localMA.length > 1) {
            result = calculate(localMA);
            
            return multiplyOut(result, tempArray);
            
        }else{
            result = new Array(1);
            result[0] = tempArray[1] / tempArray[0];
            
            return result;
        }
        
    }
    
    /**
     * Description
     * @method multiplyOut
     * @param {} result
     * @param {} tempArray
     * @return result
     */
    function multiplyOut(result, tempArray) {
        
        for (var i=0; i < result.length; i++) {
            tempArray[i+1] *= result[i];
            tempArray[tempArray.length-1] -= tempArray[i+1];
        }

        result.unshift(tempArray[tempArray.length-1] / tempArray[0]);
        
        return result;
    }
    
    /**
     * Description
     * @method checkSort
     * @param {} array
     * @return 
     */
    function checkSort(array){
        //ist der erste wert in der ersten reihe 0 muß umsortiert werden
        if (array[0][0] == 0) {
            for (var i=0; i < array.length; i++){
                if(array[i][0] != 0){
                    var temp = array[0];
                    array[0] = array[i];
                    array[i] = temp;
                    return;
                }
            }
        }
    }
}