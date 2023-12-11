package aoc.day5;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class DayFive {

    
    public static double main(ArrayList<String> inputs){
        Method[] methods = DayFive.class.getMethods();
        System.out.println("before method print");
        //get sublist for the maps
        DayFiveMapGenerator.prepareMaps(new ArrayList<String>(inputs.subList(2, inputs.size())));
        // DayFive.prepareMaps(inputs);

        return 0;
    }

    
}
