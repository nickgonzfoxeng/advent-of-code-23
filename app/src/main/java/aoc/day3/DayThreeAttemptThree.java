package aoc.day3;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

public class DayThreeAttemptThree {
    private static int runningTotal= 0;
    private static int matrixColumnLength = 0;
    private static int matrixHeight = 0;
    private static String recentlyFoundNumber = "";
    private static boolean isValidEnginePartFoundForCurrentNumber = false; 
    private static ArrayList<ArrayList<String>> matrix = new ArrayList<>();

    public static int main(ArrayList<String> inputs){
        int currentRowIndex = 0;
        DayThreeAttemptThree.matrixHeight = inputs.size() - 1;
     
        for (String matrixRow: inputs) {
            // convert to array to treat as matrix row
            ArrayList<String> row = new ArrayList<String>(Arrays.asList(matrixRow.trim().split(""))); 
               DayThreeAttemptThree.matrix.add(row);
        }
        
        for (ArrayList<String> row: DayThreeAttemptThree.matrix) {

            // initial row set
            if(DayThreeAttemptThree.matrixColumnLength == 0){
                DayThreeAttemptThree.matrixColumnLength = row.size() - 1;
            }

            DayThreeAttemptThree.scanRow(currentRowIndex);
            currentRowIndex++;
        }

        return DayThreeAttemptThree.runningTotal;
    }

    private static void scanRow(int currentRowIndex){

        ArrayList<String> row = DayThreeAttemptThree.matrix.get(currentRowIndex);
        int currentColumnIndex = 0; 
        for(String column : row){
            // if it's a number, determine if it's located next to a symbold
            if(StringUtils.isNumeric(column)){
                if(!isValidEnginePartFoundForCurrentNumber){
                    DayThreeAttemptThree.isValidEnginePartFoundForCurrentNumber = validateStandardCoords(currentRowIndex, currentColumnIndex);
                }
                DayThreeAttemptThree.recentlyFoundNumber = recentlyFoundNumber + column;
            } else {
                // if it's not a number, check if the previous number had a symbol next to it, and add it to the total if it did.
                DayThreeAttemptThree.handleRunningTotal();
            }
            // regardles if its a number symbol or ., increase the column index to keep moving along the matrix.
            currentColumnIndex++; 
        }
        // hande found number at the end of the row
        DayThreeAttemptThree.handleRunningTotal();
    }

    private static void handleRunningTotal() {
                if(isValidEnginePartFoundForCurrentNumber){
                    DayThreeAttemptThree.runningTotal += Integer.parseInt(recentlyFoundNumber);
                }
                // Always reset the search result values to default
                DayThreeAttemptThree.recentlyFoundNumber = "";
                DayThreeAttemptThree.isValidEnginePartFoundForCurrentNumber = false;
    }

    // generate coordinates for up right left and below
    private static boolean validateStandardCoords(int row, int column){
            if(column > 0){
                if(!DayThreeAttemptThree.matrix.get(row).get(column - 1).equals(".") 
                    && !StringUtils.isNumeric(DayThreeAttemptThree.matrix.get(row).get(column - 1))){
                    // symbol found directly left
                    return true;
            }
        }

            if(column < DayThreeAttemptThree.matrixColumnLength){
                if(!StringUtils.isNumeric(DayThreeAttemptThree.matrix.get(row).get(column + 1)) 
            && !DayThreeAttemptThree.matrix.get(row).get(column + 1).equals(".")) {
                // symbol found directly right
                return true;
            }

         
            }

            if(row > 0){
                if(!StringUtils.isNumeric(DayThreeAttemptThree.matrix.get(row - 1).get(column)) 
                && !DayThreeAttemptThree.matrix.get(row - 1).get(column).equals(".")) {
                // symbol found directly above
                return true;
            }}

            if(row < DayThreeAttemptThree.matrixHeight){
                if(!StringUtils.isNumeric(DayThreeAttemptThree.matrix.get(row + 1).get(column)) 
                    && !DayThreeAttemptThree.matrix.get(row + 1).get(column).equals(".")) {
                    // symbol found below
                    return true;
                }
            }


        return validateDiagonals(row, column);
    }

    private static boolean validateDiagonals(int row, int column){

        if(column > 0 && row < DayThreeAttemptThree.matrixHeight){
            if(!StringUtils.isNumeric(DayThreeAttemptThree.matrix.get(row + 1).get(column - 1)) 
                 && !DayThreeAttemptThree.matrix.get(row + 1).get(column - 1).equals(".")){
                //symbol found diagonal left below
                 return true;
            }
        }

        if(column < DayThreeAttemptThree.matrixColumnLength && row < DayThreeAttemptThree.matrixHeight){
            if(!StringUtils.isNumeric(DayThreeAttemptThree.matrix.get(row + 1).get(column + 1)) 
                && !DayThreeAttemptThree.matrix.get(row + 1).get(column + 1).equals(".")) {
                // symbol found diagonal right below
                return true;
        }}
      
        if(row > 0 && column > 0){
            if(
                !StringUtils.isNumeric(DayThreeAttemptThree.matrix.get(row - 1).get(column - 1)) 
                && !DayThreeAttemptThree.matrix.get(row - 1).get(column - 1).equals(".")
            ) {
                // symbol found diagonal left above
                return true;
        }}

        if(row > 0 && column < DayThreeAttemptThree.matrixColumnLength){
            if( !StringUtils.isNumeric(DayThreeAttemptThree.matrix.get(row - 1).get(column + 1)) 
           && !DayThreeAttemptThree.matrix.get(row - 1).get(column + 1).equals(".")) {
            // symbol found diagonal right above
            return true;
            }
        }

        return false;
    }
}
