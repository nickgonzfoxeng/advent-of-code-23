package aoc.day2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class DayTwo {
    // 12 red cubes, 13 green cubes, and 14 blue cubes
    private static final HashMap<String, Integer> ELF_INPUT = new HashMap<String, Integer>() {{
        put("red", 12);
        put("green", 13);
        put("blue", 14);
    }};

    public static double main(ArrayList<String> inputs){
        double gamesIdsRunningTotal = 0;
        for (String gameRecord : inputs) {
            String[] splitGameIdFromRecord = gameRecord.split(":");
            int gameId = Integer.parseInt(splitGameIdFromRecord[0].replace("Game ", ""));
            
            if(DayTwo.isGamePossible(splitGameIdFromRecord[1])){
                gamesIdsRunningTotal += gameId;
            }
        }
        return gamesIdsRunningTotal;
    }

    // 4 green, 5 blue, 2 red; 1 red, 14 blue, 6 green; 1 green, 14 red, 5 blue; 18 red, 16 blue; 15 blue, 8 red, 18 green; 1 green, 18 red, 6 blue
    private static boolean isGamePossible(String gameRecord){
        
        // split the game record into individual records [[1 green, 4 blue], [1 blue, 2 green, 1 red], [1 red, 1 green, 2 blue], [1 green, 1 red]]
        String[] getGameRoundsAsArray = gameRecord.split(";");
        boolean possible = true;
        for (String round : getGameRoundsAsArray) {
            HashMap<String, Integer> roundCubeCount = DayTwo.prepareCubeRecordHashMap(round);

            for (String color : DayTwo.ELF_INPUT.keySet()) {
                int cubeCountFromElfInput = DayTwo.ELF_INPUT.get(color);   
                Optional<Integer> colorCubeCount = Optional.ofNullable(roundCubeCount.get(color));
                if(colorCubeCount.isPresent() && colorCubeCount.get() > cubeCountFromElfInput){
                    possible = false;
                    break;
                }
            }

            if(!possible){
                break;
            }
        }
        return possible;
    }

    
    private static HashMap<String, Integer> prepareCubeRecordHashMap(String roundRecord){

        HashMap<String, Integer> gameRecordMap = new HashMap<String, Integer>();
       
        // split the game records into individual cube counts [[1 green], [4 blue], [1 blue], [2 green], [1 red]]
        for (String cubeCount : roundRecord.split(",")) {
            
            // split individual record to a cube color and count ["1", "green"]
            String[] cubeRecordSplit = cubeCount.trim().split(" ");
            gameRecordMap.put(cubeRecordSplit[1], Integer.parseInt(cubeRecordSplit[0]));
        }
        
        return gameRecordMap;
    }
}
