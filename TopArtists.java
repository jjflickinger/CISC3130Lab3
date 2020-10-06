import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class TopArtists {

    //Read file into 1D array and return the array
    public static String[] readFile(String fileName, int length, int linesToIgnore) {
        /*Put each line as an element in a 1D array,
        excluding the first few lines as specified by linesToIgnore*/
        String[] a = new String[length];
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            for (int i = 0; i < linesToIgnore; i++) {
                scanner.nextLine();
            }
            int i = 0;
            while (scanner.hasNextLine() && i < length) {
                a[i] = scanner.nextLine();
                i++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
        return a;
    }
    
    //Extract the artist name from a line of text in the csv file
    public static String getArtistName(String line) {
        String[] words = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        return words[2];
    }
    
    //Add artist to correct position in sorted linked list
    public static void addArtist(LinkedList<Artist> list, Artist a) {
            //If the list is empty just add the artist at the head of the list
            //This conditional is only true once
            if (list.isEmpty()) {
                list.add(a);
            //Insert the artist in the right place
            } else if ( list.get(0).name.compareToIgnoreCase(a.name) > 0) {
                list.add(0, a);
            } else if ( list.get(list.size() - 1).name.compareToIgnoreCase(a.name) < 0) {
                list.add(list.size(), a);
            } else {
                int i = 0;
                while (list.get(i).name.compareToIgnoreCase(a.name) <= 0) {
                    if (list.get(i).name.compareToIgnoreCase(a.name) == 0) {
                        return;
                    }
                    i++;
                }
                list.add(i, a);
            }
    }
    
    //Given an array of lines of text, create a linked list of the artist names
    public static LinkedList<Artist> createList(String[] lines) {
        
        LinkedList<Artist> topStreamingArtists = new LinkedList<Artist>();
        
        for (String line : lines) {
            String name = getArtistName(line);
            //Remove quotation marks in names
            name = name.replace("\"", "");
            Artist a = new Artist(name);
            /*if (topStreamingArtists.contains(a)) {
                break;
            } else {
                topStreamingArtists.add(a);
            }*/
            addArtist(topStreamingArtists, a);
        }
        return topStreamingArtists;
    }
    
    //Create an empty file with its name as the argument
    public static File createFile(String name) {
        try{
            File file = new File(name);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
            return file;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }
    
    public static void writeToFile(LinkedList<Artist> artists, File file) {
        try {
            FileWriter writer = new FileWriter(file.getName());
            //for each artist name
            for (int i = 0; i < artists.size(); i++) {
                writer.write(artists.get(i).name);
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        /*Spotify Charts always has Top 200 with same file name
        so the program is hard-coded with these two parameters
        but the only hard-coding is here in main in case it needs to be changed*/
        String fileNameRead = "regional-us-weekly-latest.csv";
        int length = 200;
        //First 2 lines do not contain data
        int linesToIgnore = 2;
        //Create array of lines in the file except the first 2
        String[] lines = readFile(fileNameRead, length, linesToIgnore);
        //Create sorted linked list of artists
        LinkedList<Artist> topStreamingArtists = createList(lines);
        //Create an empty file called Artists-WeekOf2020-10-01.txt
        String fileNameWrite = "Artists-WeekOf2020-10-01.txt";
        File file = createFile(fileNameWrite);
        //Write the top artists to the file
        writeToFile(topStreamingArtists, file);
    }
}
