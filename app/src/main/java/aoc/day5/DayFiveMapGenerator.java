package aoc.day5;

import java.util.stream.Collectors;
import java.util.stream.IntStream;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Consumer;

public class DayFiveMapGenerator {
    
    public static HashMap<Integer, Integer> seedToSoilMap = new HashMap<>();
    public static HashMap<Integer, Integer> soilToFertMap = new HashMap<>();
    public static HashMap<Integer, Integer> fertToWaterMap = new HashMap<>();
    public static HashMap<Integer, Integer> waterToLightMap = new HashMap<>();
    public static HashMap<Integer, Integer> lightToTempMap = new HashMap<>();
    public static HashMap<Integer, Integer> tempToHumidityMap = new HashMap<>();
    public static HashMap<Integer, Integer> humidityToLocMap = new HashMap<>();
    
    private static HashMap<String, Consumer<ArrayList<String>>> inputKeys = new HashMap<String, Consumer<ArrayList<String>>>(){{
        put("seed-to-soil map:", (inputs) -> DayFiveMapGenerator.prepareSeedToSoilMap(inputs));
        put("soil-to-fertilizer map:", (inputs) -> DayFiveMapGenerator.prepareSoilToFertilizerMap(inputs));
        put("fertilizer-to-water map:", (inputs) -> DayFiveMapGenerator.prepareFertelizerToWaterMap(inputs));
        put("water-to-light map:", (inputs) -> DayFiveMapGenerator.prepareWaterToLightMap(inputs));
        put("light-to-temperature map:", (inputs) -> DayFiveMapGenerator.prepareLightToTempMap(inputs));
        put("temperature-to-humidity map:", (inputs) -> DayFiveMapGenerator.prepareTempToHumidityMap(inputs));
        put("humidity-to-location map:", (inputs) -> DayFiveMapGenerator.prepareHumidityToLocationMap(inputs));
        
    }};
    
    public static void prepareMaps(ArrayList<String> inputs){
        ArrayList<String> nextFunctionInputs = new ArrayList<String>();
        String currentKey = "";
        for(String input : inputs){
            // get a func ref for the given input if it's a key
            if(DayFiveMapGenerator.inputKeys.containsKey(input)){
                currentKey = input;
                continue;
            }

            if(input.length() > 0){
                nextFunctionInputs.add(input);      
            } else {
                Consumer<ArrayList<String>> mapFunc = inputKeys.get(currentKey);
                mapFunc.accept(nextFunctionInputs);
                nextFunctionInputs = new ArrayList<String>();
            }
        }
    }

    protected static void prepareSoilToFertilizerMap(ArrayList<String> inputs) {
        for(String input : inputs){
            ArrayList<String> rangeInputs = new ArrayList<String>(Arrays.asList(input.split(" ")));
            HashMap<Integer, Integer> rangeMap = prepareRanges(rangeInputs);
            soilToFertMap.putAll(rangeMap);
        }
    }

    protected static void prepareHumidityToLocationMap(ArrayList<String> inputs) {
        for(String input : inputs){
            ArrayList<String> rangeInputs = new ArrayList<String>(Arrays.asList(input.split(" ")));
            HashMap<Integer, Integer> rangeMap = prepareRanges(rangeInputs);
            humidityToLocMap.putAll(rangeMap);
        }    
    }

    protected static void prepareTempToHumidityMap(ArrayList<String> inputs) {
        for(String input : inputs){
            ArrayList<String> rangeInputs = new ArrayList<String>(Arrays.asList(input.split(" ")));
            HashMap<Integer, Integer> rangeMap = prepareRanges(rangeInputs);
            tempToHumidityMap.putAll(rangeMap);
        }    
    }

    protected static void prepareLightToTempMap(ArrayList<String> inputs) {
        for(String input : inputs){
            ArrayList<String> rangeInputs = new ArrayList<String>(Arrays.asList(input.split(" ")));
            HashMap<Integer, Integer> rangeMap = prepareRanges(rangeInputs);
            lightToTempMap.putAll(rangeMap);
        }  
    }

    protected static void prepareWaterToLightMap(ArrayList<String> inputs) {
         for(String input : inputs){
            ArrayList<String> rangeInputs = new ArrayList<String>(Arrays.asList(input.split(" ")));
            HashMap<Integer, Integer> rangeMap = prepareRanges(rangeInputs);
            waterToLightMap.putAll(rangeMap);
        }   
    }

    protected static void prepareFertelizerToWaterMap(ArrayList<String> inputs) {
        for(String input : inputs){
            ArrayList<String> rangeInputs = new ArrayList<String>(Arrays.asList(input.split(" ")));
            HashMap<Integer, Integer> rangeMap = prepareRanges(rangeInputs);
            fertToWaterMap.putAll(rangeMap);
        }

    }

    protected static void prepareSeedToSoilMap(ArrayList<String> inputs){
        for(String input : inputs){
            ArrayList<String> rangeInputs = new ArrayList<String>(Arrays.asList(input.split(" ")));
            HashMap<Integer, Integer> rangeMap = prepareRanges(rangeInputs);
            seedToSoilMap.putAll(rangeMap);
        }
    }
    

    private static HashMap<Integer, Integer> prepareRanges(ArrayList<String> ranges){
        HashMap<Integer, Integer> rangeMap = new HashMap<Integer, Integer>();
        
        // hah lol wtf do half these words even mean.
        // boxed = dumb word for all the values of the stream
        // IntStream = if using flatmaptoint, it needs to return a "stream" of the single value you're trying to convert, like wtf guy
        ArrayList<Integer> splitNumbers = new ArrayList<>(ranges.stream().mapToInt((String num) -> Integer.parseInt(num)).boxed().collect(Collectors.toList()));
        
        // get the range
        int range = splitNumbers.get(2);
        
        // generate the ranges.
        ArrayList<Integer> firstColumnRanges = new ArrayList<>(IntStream.range(splitNumbers.get(0), splitNumbers.get(0) + range).boxed().collect(Collectors.toList()));
        ArrayList<Integer> secondColumnRanges = new ArrayList<>(IntStream.range(splitNumbers.get(1), splitNumbers.get(1) + range).boxed().collect(Collectors.toList()));

        for(int i = 0; i < range; i++) {
            rangeMap.put(firstColumnRanges.get(i), secondColumnRanges.get(i));
        }

        return rangeMap;
    }
}
