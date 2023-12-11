package aoc.day3;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import com.google.common.base.Optional;

public class DayThreeAttemptThree {
    private static int runningTotal= 0;
    private static int matrixColumnLength = 0;
    private static int matrixHeight = 0;
    private static String recentlyFoundNumber = "";
    private static boolean isValidEnginePartFoundForCurrentNumber = false; 
    private static ArrayList<ArrayList<String>> matrix = new ArrayList<>();
    private static ArrayList<ImmutablePair<Integer, Integer>> coordinateDeltas = new ArrayList<ImmutablePair<Integer, Integer>>();

    public static int main(ArrayList<String> inputs){
        int currentRowIndex = 0;
        initCoords();
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

            // DayThreeAttemptThree.scanRowForValidParts(currentRowIndex);
            DayThreeAttemptThree.scanRowForGears(currentRowIndex);
            currentRowIndex++;
        }

        return DayThreeAttemptThree.runningTotal;
    }

    private static void scanRowForGears(int currentRowIndex){
        ArrayList<String> row = DayThreeAttemptThree.matrix.get(currentRowIndex);
        int currentColumnIndex = 0; 
        for(String column : row){

            if(column.equals("*")){
                double gearValue = DayThreeAttemptThree.validateGear(currentRowIndex, currentColumnIndex);
                DayThreeAttemptThree.runningTotal += gearValue;
            }
            currentColumnIndex++; 
        }
    }

    private static void initCoords(){
        // look down
        coordinateDeltas.add(new ImmutablePair<Integer,Integer>(1, 0));
        // look up
        coordinateDeltas.add(new ImmutablePair<Integer,Integer>(-1, 0));
        // look right
        coordinateDeltas.add(new ImmutablePair<Integer,Integer>(0, 1));
        // look left
        coordinateDeltas.add(new ImmutablePair<Integer,Integer>(0, -1));
        // diaganolly down to the right
        coordinateDeltas.add(new ImmutablePair<Integer,Integer>(1, 1));
        // diaganolly diaganolly to the left
        coordinateDeltas.add(new ImmutablePair<Integer,Integer>(1, -1));
        // diaganolly up to the left 
        coordinateDeltas.add(new ImmutablePair<Integer,Integer>(-1, -1));
        // diaganolly up to the right
        coordinateDeltas.add(new ImmutablePair<Integer,Integer>(-1, 1));
    }

    private static void scanRowForValidParts(int currentRowIndex){

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

    private static double validateGear(int currRow, int currCol){
        String firstGearValue = "";
        for(ImmutablePair<Integer, Integer> coordDelta : DayThreeAttemptThree.coordinateDeltas){
            
            ImmutablePair<Integer, Integer> adjacentCoordinates = new ImmutablePair<Integer,Integer>(coordDelta.getLeft() + currRow, coordDelta.getRight() + currCol);
            Optional<String> numberChar = Optional.fromNullable(DayThreeAttemptThree.matrix.get(adjacentCoordinates.getLeft()).get(adjacentCoordinates.getRight()));
            
            if(numberChar.isPresent() && StringUtils.isNumeric(numberChar.get())){
                String gearValue = parseGearValue(adjacentCoordinates, numberChar.get());
                if(firstGearValue.length() > 0 && !firstGearValue.equals(gearValue)){
                    return (Double.parseDouble(firstGearValue) * Double.parseDouble(gearValue));
                } else {
                    firstGearValue = gearValue;
                }
            }  
        }
        // return 0 to the running total if it wasn't a a valid gear
        return 0;
    }


    private static String parseGearValue(ImmutablePair<Integer, Integer> adjacentCoordinates, String startingNumberChar){
            String currentNumberString = startingNumberChar;
            // setup pointers for the found numeric character to scan left and right simultaneously
            // right number in pair represents the current found adjacent numbers column
            int leftPointer = adjacentCoordinates.getRight() - 1;
            int rightPointer = adjacentCoordinates.getRight() + 1;
            ArrayList<String> charRow = DayThreeAttemptThree.matrix.get(adjacentCoordinates.getLeft());
            boolean isFullNumberFound = false;

            while(!isFullNumberFound) {

                if(leftPointer >= 0 && StringUtils.isNumeric(charRow.get(leftPointer))){
                    currentNumberString = charRow.get(leftPointer) + currentNumberString;
                    leftPointer-=1;
                }

                if(rightPointer <= DayThreeAttemptThree.matrixColumnLength && StringUtils.isNumeric(charRow.get(rightPointer))){
                        currentNumberString =  currentNumberString + charRow.get(rightPointer);
                        rightPointer+=1;
                }
                
                // if the first char in row was a number and pointer is now -1 signfiying EOL OR the char is not a number 
                boolean leftTermination = leftPointer < 0 || !StringUtils.isNumeric(charRow.get(leftPointer));
                // if the last char was a number and pointer is now == rowlength signfiying EOL OR the char is not a number 
                boolean rightTermination = rightPointer > DayThreeAttemptThree.matrixColumnLength || !StringUtils.isNumeric(charRow.get(rightPointer));
                
                if(leftTermination && rightTermination){
                    isFullNumberFound = true;
                }
            }
            return currentNumberString;
        }

    // handle running total for part search
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
