package aoc.day5;

import java.math.BigDecimal;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Range;


public class DayFive {

    private static HashMap<String, ArrayList<ArrayList<Double>>> almanacEntries = new HashMap<>();
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
        ArrayList<SimpleImmutableEntry<Double, Double>> seedRangePairs = DayFive.getSeedRangePairs(seeds);

        // grab the lines below the blank line after seeds
        List<String> almanacEntriesSublist = inputs.subList(2, inputs.size());
        DayFive.prepareMaps(new ArrayList<String>(almanacEntriesSublist));

        double lowestLocation = Double.MAX_VALUE;
        
        for(SimpleImmutableEntry<Double, Double> seedRange: seedRangePairs){
            //Seed 79, soil 81, fertilizer 81, water 81, light 74, temperature 78, humidity 78, location 82.
            HashSet<SimpleImmutableEntry<Double, Double>> seedRangeArray = new HashSet<>() {{
                add(seedRange);
            }}; 
            HashSet<SimpleImmutableEntry<Double, Double>> soil = determineAlmanacVal(almanacEntries.get("seed-to-soil map:"), seedRangeArray);
            HashSet<SimpleImmutableEntry<Double, Double>> fert = determineAlmanacVal(almanacEntries.get("soil-to-fertilizer map:"), soil);
            HashSet<SimpleImmutableEntry<Double, Double>> water = determineAlmanacVal(almanacEntries.get("fertilizer-to-water map:"), fert);
            HashSet<SimpleImmutableEntry<Double, Double>>light = determineAlmanacVal(almanacEntries.get("water-to-light map:"), water);
            HashSet<SimpleImmutableEntry<Double, Double>> temp = determineAlmanacVal(almanacEntries.get("light-to-temperature map:"), light);
            HashSet<SimpleImmutableEntry<Double, Double>> humidity = determineAlmanacVal(almanacEntries.get("temperature-to-humidity map:"), temp);
            HashSet<SimpleImmutableEntry<Double, Double>> locationCandidate = determineAlmanacVal(almanacEntries.get("humidity-to-location map:"), humidity);
            HashSet<Double> someLocations = new HashSet<Double>();
            for(SimpleImmutableEntry<Double, Double> location : locationCandidate) {
                someLocations.add(location.getKey());
            }
            lowestLocation = Math.min(lowestLocation, DayFive.handleLowestLocation(locationCandidate));
        }

        return new BigDecimal(lowestLocation).toPlainString();
    }

    private static HashSet<SimpleImmutableEntry<Double, Double>> determineAlmanacVal(ArrayList<ArrayList<Double>> almanacEntries, HashSet<SimpleImmutableEntry<Double, Double>> startRange){
        

        HashSet<SimpleImmutableEntry<Double, Double>> retSet = new HashSet<>();

        for (SimpleImmutableEntry<Double, Double> sourceValRange : startRange){
            SimpleImmutableEntry<Double, Double>  currentRange = sourceValRange;
            HashSet<SimpleImmutableEntry<Double, Double>> innerRetSet = new HashSet<>();
            for(ArrayList<Double> entry : almanacEntries){
                double destinationCandidate = entry.get(0);
                double sourceValCandidate = entry.get(1);
                double rangeForCandidate = entry.get(2);

                // // first get value upper bound
                double currentSourceUpperBound = currentRange.getKey() + currentRange.getValue();
                double candidateSourceUpperBound = rangeForCandidate + sourceValCandidate;
                double destinationRange = currentRange.getValue();

                double currentSourceLower = currentRange.getKey();
                final Range<Double> nextRange = Range.of(sourceValCandidate, candidateSourceUpperBound);
                final Range<Double> currRange = Range.of(currentSourceLower, currentSourceUpperBound);
                Range<Double> intersection = null;
                try{
                    intersection = currRange.intersectionWith(nextRange);
                } catch(Exception e) {
                    continue;
                }
                
                // if the lower bound is present, store like normal
                if(intersection.contains(currentSourceLower)){
                    destinationCandidate = destinationCandidate + (currentRange.getKey() - sourceValCandidate);
                } else {
                    // fix the invalid range and split it to its own range plus the valid range
                    if((currentSourceLower + rangeForCandidate) > destinationRange){
                        double destDelta = sourceValCandidate - currentSourceLower;
                        SimpleImmutableEntry<Double, Double> nextEntry = new SimpleImmutableEntry<Double, Double> (destinationCandidate, destDelta);
                        retSet.add(nextEntry);
                        destinationRange = destinationRange - destDelta;
                        destinationCandidate = destinationCandidate + destDelta;
                    }
                }

                SimpleImmutableEntry<Double, Double> nextEntry = new SimpleImmutableEntry<Double, Double> (destinationCandidate, destinationRange);
                innerRetSet.add(nextEntry);
            }
            if(innerRetSet.size() == 0){
                retSet.add(currentRange);
            } else {
                retSet.addAll(innerRetSet);
            }
        }
        System.out.println(retSet);
        return retSet;
    }

    private static void prepareMaps(ArrayList<String> inputs){
        ArrayList<ArrayList<Double>> currentKeyRows = new ArrayList<ArrayList<Double>> ();
        String currentKey = "";
        for(String input : inputs){
            if(DayFive.keys.contains(input)){
                currentKey = input;
                continue;
            }

            if(input.length() > 0){
                currentKeyRows.add(new ArrayList<Double>( Arrays.asList(input.trim().split(" ")).stream().mapToDouble((String num) -> 
                    Double.parseDouble(num)).boxed().collect(Collectors.toList())));      
            } else {
                DayFive.almanacEntries.put(currentKey, currentKeyRows);
                currentKeyRows = new  ArrayList<ArrayList<Double>>();
            }
        }

        // hacky but there's no empty line at the end of the file so calling here for the last map:
        DayFive.almanacEntries.put(currentKey, currentKeyRows);
    }

    private static ArrayList<SimpleImmutableEntry<Double, Double>> getSeedRangePairs(ArrayList<Double> seeds){
        ArrayList<SimpleImmutableEntry<Double, Double>> seedRangePairs = new ArrayList<>();
        int cap = seeds.size() - 1;
        int counter = 0; 
        while(counter < cap){
            SimpleImmutableEntry<Double, Double> pair = new SimpleImmutableEntry<Double, Double>(seeds.get(counter), seeds.get(counter+1));
            seedRangePairs.add(pair);
            // Jump to the next pair instead of going to the range
            counter+=2;
        }

        return seedRangePairs;
    }
    
    private static double handleLowestLocation(HashSet<SimpleImmutableEntry<Double, Double>> locations) {
        double lowestLoc = Double.MAX_VALUE;
        for (SimpleImmutableEntry<Double, Double> location : locations){
            lowestLoc = Math.min(lowestLoc, location.getKey());
        }
        return lowestLoc;
    }

    private static ArrayList<Double> getSeeds(String seedLine) {
        // lol get fucked
        return new ArrayList<Double>( Arrays.asList(seedLine.split(":")[1].trim().split(" ")).stream().mapToDouble((String num) -> Double.parseDouble(num)).boxed().collect(Collectors.toList()));

    }
}
