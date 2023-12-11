/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package aoc;

import java.util.ArrayList;

import aoc.day1.DayOne;
import aoc.day2.DayTwo;
import aoc.day3.DayThreeAttemptThree;
import aoc.utility.filereader.FileReader;


public class App {
    public static void main(String[] args) {
        App.runDayThree();
    }

    public static void runDayOne(){
        ArrayList<String> inputs = FileReader.readInputFile("app/src/main/java/aoc/day1/input.txt");
        System.out.println(DayOne.main(inputs));
    }

    public static void runDayTwo(){
        ArrayList<String> inputs = FileReader.readInputFile("app/src/main/java/aoc/day2/input.txt");
        System.out.println(DayTwo.main(inputs));
    }

    public static void runDayThree(){
        ArrayList<String> inputs = FileReader.readInputFile("app/src/main/java/aoc/day3/input.txt");
        System.out.println(DayThreeAttemptThree.main(inputs));
    }


}
