package aoc.day1;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

public class DayOne {
    
    private static String leftNumberAsString = "";
    private static String rightNumberAsString = "";
    private static boolean isNumberFoundLeft = false; 
    private static boolean isNumberFoundRight = false;

    private static HashMap<String, Integer> numbers = new HashMap<String, Integer>() {{
        put("one", 1);
        put("two", 2);
        put("three", 3);
        put("four", 4);
        put("five", 5);
        put("six", 6);
        put("seven", 7);
        put("eight", 8);
        put("nine", 9);
    }};

    public static double main(ArrayList<String> inputs){
        int runningTotal = 0;
        for (String calibrationString : inputs) {
            DayOne.findNumbers(calibrationString);
            runningTotal += Integer.parseInt((DayOne.leftNumberAsString + DayOne.rightNumberAsString));
            DayOne.resetTrackers();
        }
        return runningTotal;   
    }

    private static void findNumbers(String calibrationString){
        int leftPointer = 0;
        int rightPointer = calibrationString.length() - 1;
        
        for(int i = 0; i < calibrationString.length(); i++) {
            if(!isNumberFoundLeft){
                determineNumber(calibrationString, true, leftPointer);
            }

            if(!isNumberFoundRight){
                determineNumber(calibrationString, false, rightPointer);
            }
            
            if(DayOne.isNumberFoundLeft && DayOne.isNumberFoundRight){
                return;
            }
            leftPointer+=1; 
            rightPointer--;
        }
    }
    
    private static void determineNumber(String calibrationString, boolean isLeft, int currentIndex) {
        // if its an actual number just return
        if(StringUtils.isNumeric(String.valueOf(calibrationString.charAt(currentIndex)))){
            if(isLeft){
                DayOne.leftNumberAsString = String.valueOf(calibrationString.charAt(currentIndex));
                DayOne.isNumberFoundLeft = true;      
            } else {
               DayOne.rightNumberAsString = String.valueOf(calibrationString.charAt(currentIndex)); 
               DayOne.isNumberFoundRight = true;
            }
            return;
        }

        if(isLeft){
            determineStrings(calibrationString.substring(currentIndex, calibrationString.length()), true);
        } else {
            determineStrings(calibrationString.substring(currentIndex,  calibrationString.length()), false);
        }
    }

    private static void determineStrings(String calibrationString, boolean isLeft){
        int numberVal = 0;
        for(String numberKey: DayOne.numbers.keySet()){
            if(calibrationString.startsWith(numberKey)){
                numberVal = DayOne.numbers.get(numberKey);
                break;
            }
        }
        if(numberVal == 0){
            return;
        }

        if(isLeft){
            DayOne.leftNumberAsString = String.valueOf(numberVal);
            DayOne.isNumberFoundLeft = true;
        } else {
            DayOne.rightNumberAsString = String.valueOf(numberVal);
            DayOne.isNumberFoundRight = true;
        }
    }

    private static void resetTrackers() {
        DayOne.leftNumberAsString = "";
        DayOne.rightNumberAsString = "";
        DayOne.isNumberFoundLeft = false; 
        DayOne.isNumberFoundRight = false;
    }
}
