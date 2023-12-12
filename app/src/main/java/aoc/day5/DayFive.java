package aoc.day5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class DayFive {

    private static HashMap<String, ArrayList<String>> almanacEntries = new HashMap<>();
    private static HashSet<String> keys = new HashSet<>() {{
        add("seed-to-soil map:");
        add("soil-to-fertilizer map:");
        add("fertilizer-to-water map:");
        add("water-to-light map:");
        add("light-to-temperature map:");
        add("temperature-to-humidity map:");
        add("humidity-to-location map:");
    }}; 

    
    public static String main(ArrayList<String> inputs){
        ArrayList<Double> seeds = getSeeds(inputs.get(0));
        List<String> almanacEntriesSublist = inputs.subList(2, inputs.size());
        DayFive.prepareMaps(new ArrayList<String>(almanacEntriesSublist));
        double lowestLocation = 999999999;

        for(double seed : seeds){
            //Seed 79, soil 81, fertilizer 81, water 81, light 74, temperature 78, humidity 78, location 82.
            double soil = determineAlmanacVal(almanacEntries.get("seed-to-soil map:"), seed);
            double fert = determineAlmanacVal(almanacEntries.get("soil-to-fertilizer map:"), soil);
            double water = determineAlmanacVal(almanacEntries.get("fertilizer-to-water map:"), fert);
            double light = determineAlmanacVal(almanacEntries.get("water-to-light map:"), water);
            double temp = determineAlmanacVal(almanacEntries.get("light-to-temperature map:"), light);
            double humidity = determineAlmanacVal(almanacEntries.get("temperature-to-humidity map:"), temp);
            double locationCandidate = determineAlmanacVal(almanacEntries.get("humidity-to-location map:"), humidity);
            lowestLocation = Math.min(locationCandidate, lowestLocation);
  
        }
        return new BigDecimal(lowestLocation).toPlainString();
    }

    private static double determineAlmanacVal(ArrayList<String> almanacEntries, double almanacValue){
        
        for(String entry : almanacEntries){
            ArrayList<Double> splitNumbers = new ArrayList<>(Arrays.asList(entry.split(" ")).stream().mapToDouble(
                (String num) -> Double.parseDouble(num)).boxed().collect(Collectors.toList()));
            if(almanacValue >= splitNumbers.get(1) && almanacValue <= (splitNumbers.get(1) + splitNumbers.get(2))) {
                return ((almanacValue - splitNumbers.get(1)) + splitNumbers.get(0)); 
            }
        }
        return almanacValue;
    }

    private static void prepareMaps(ArrayList<String> inputs){
        ArrayList<String> currentKeyRows = new ArrayList<String>();
        String currentKey = "";
        for(String input : inputs){
            // get a func ref for the given input if it's a key
            if(DayFive.keys.contains(input)){
                currentKey = input;
                continue;
            }

            if(input.length() > 0){
                currentKeyRows.add(input);      
            } else {
                DayFive.almanacEntries.put(currentKey, currentKeyRows);
                currentKeyRows = new ArrayList<String>();
            }
        }

        // hacky but there's no empty line at the end of the file so calling here for the last map:
        DayFive.almanacEntries.put(currentKey, currentKeyRows);
    }

    private static ArrayList<Double> getSeeds(String seedLine) {
        // lol get fucked
        return new ArrayList<Double>( Arrays.asList(seedLine.split(":")[1].trim().split(" ")).stream().mapToDouble((String num) -> Double.parseDouble(num)).boxed().collect(Collectors.toList()));

    }
}
