package aoc.day1;

import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

public class DayOne {
    
    private static String leftNumberAsString = "";
    private static String rightNumberAsString = "";

    public static double main(ArrayList<String> inputs){
        double runningTotal = 0;
        for (String calibrationString : inputs) {
            runningTotal += DayOne.findCalibrationValue(calibrationString);
            DayOne.resetTrackers();
        }
        return runningTotal;   
    }

    private static double findCalibrationValue(String calibrationString){
        int leftPointer = 0;
        int rightPointer = calibrationString.length() - 1;
        
        for(int i = 0; i < calibrationString.length(); i++) {
            
            // This seems gross, but its also some silly leetcode type question so whatever
            if(DayOne.leftNumberAsString.length() == 0){
                if(DayOne.isNumber(calibrationString.charAt(leftPointer))){
                    String number = String.valueOf(calibrationString.charAt(leftPointer));
                    DayOne.leftNumberAsString = number;
                } else {
                    leftPointer++;
                }
            }
            
            if(DayOne.rightNumberAsString.length() == 0){
                if(DayOne.isNumber(calibrationString.charAt(rightPointer))){
                    String number = String.valueOf(calibrationString.charAt(rightPointer));
                    DayOne.rightNumberAsString = number;
                } else {
                    rightPointer--;
                }
            }
            
            if(DayOne.leftNumberAsString.length() == 1 && DayOne.rightNumberAsString.length() == 1){
                String combinedVal = DayOne.leftNumberAsString + DayOne.rightNumberAsString; 
                return Integer.parseInt(combinedVal);
            }
        }
        return 0;
    }
    
    private static boolean isNumber(char character) {
        String charAsString = String.valueOf(character);
        return StringUtils.isNumeric(charAsString);

    }

    private static void resetTrackers() {
        DayOne.leftNumberAsString = "";
        DayOne.rightNumberAsString = "";
    }
}
