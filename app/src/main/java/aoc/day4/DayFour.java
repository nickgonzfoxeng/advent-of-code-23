package aoc.day4;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DayFour {
    private static ArrayList<String> scratchCardWinnerCandidates;
    private static HashSet<String> currentScratchCardWinningNumbers;
    private static HashMap<Integer, Integer> scratchCardWinningCopyTotals = new HashMap<Integer, Integer>(){{
        put(1, 1);
    }};

    public static double main(ArrayList<String> inputs ){
        
        int currentKey = 1;
        DayFour.seedOriginals(inputs.size());
        
        for(String scractchCard : inputs){
            transformInput(scractchCard);
            int winningCopyTotalForCurrentKey = DayFour.scratchCardWinningCopyTotals.get(currentKey);          
            
            for(int i = 0; i < winningCopyTotalForCurrentKey; i++){
                DayFour.calculateWinningTotal(currentKey);
            }
            
            currentKey++;
        }

        return DayFour.scratchCardWinningCopyTotals.values().stream()
        // identity - initial value of the operation trying to reduced this collection of doubles to a number
        // the running subtotal and the next element in the array
        .reduce(0, (totalCopies, copies) -> totalCopies + copies);
    }

    private static void calculateWinningTotal(int currentKey){
        double cardTotal = 0;
        
        for(String candidate : DayFour.scratchCardWinnerCandidates){
            if(candidate.length() > 0 && DayFour.currentScratchCardWinningNumbers.contains(candidate)){
                cardTotal+=1;
            }
        }

        int nextCard = currentKey + 1;
        
        while ( cardTotal > 0 ){            
            int currentCopies = DayFour.scratchCardWinningCopyTotals.get(nextCard);
            
            currentCopies+=1;
           
            DayFour.scratchCardWinningCopyTotals.put(nextCard, currentCopies);
           
            cardTotal --; 
            nextCard ++;
        }
    }

    private static void transformInput(String scratchCard){
        // ex input: Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        String[] splitFromCardId = scratchCard.split(":");
        String[] splitNumbers = splitFromCardId[1].split("\\|");

        DayFour.scratchCardWinnerCandidates = new ArrayList<String>(Arrays.asList(splitNumbers[1].trim().split(" ")));
        DayFour.currentScratchCardWinningNumbers = new HashSet<String>(Arrays.asList(splitNumbers[0].trim().split(" ")));
    }

    private static void seedOriginals( int inputLength ){
        for(int currentCardId = 1; currentCardId <= inputLength; currentCardId++){
            scratchCardWinningCopyTotals.put(currentCardId, 1);
        }
    }
}
