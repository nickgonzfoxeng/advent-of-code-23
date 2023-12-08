package aoc.utility.filereader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {
        
    public static ArrayList<String> readInputFile(String filePath) {
        ArrayList<String> inputs = new ArrayList<String>();
        
        try {
            File file = new File(filePath);
            Scanner myReader = new Scanner(file);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            inputs.add(data);
        }
        
        myReader.close();
        
    } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return inputs;
    }
}