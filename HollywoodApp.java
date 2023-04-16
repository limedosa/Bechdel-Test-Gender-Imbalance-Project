package javafoundations;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;

public class HollyWoodApp {
    private Graph graph;
    public HollyWoodApp(){
        graph = new Graph();
    }
    public void addActor(String name){
        graph.addVertex(name);
    }
    public void addMovie(String name){
        graph.addVertex(name);
    }
    public void addEdge(String actor, String movie){
        graph.addEdge(actor, movie);
    }
    public void readFromFile(String fileName) throws IOException{
        File file = new File(fileName);
        Scanner inputFile = new Scanner(file);
        while(inputFile.hasNext()){
            String line = inputFile.nextLine();
            String[] tokens = line.split(",");
            String actor = tokens[0];
            String movie = tokens[1];
            addActor(actor);
            addMovie(movie);
            addEdge(actor, movie);
        }
        inputFile.close();
    }
    // public static void main(String[] args) throws IOException{
    //     Scanner scan = new Scanner(System.in);
    //     System.out.println("Enter the name of the file to read from: ");
    //     String fileName = scan.nextLine();
    //     File file = new File(fileName);
    //     Scanner inputFile = new Scanner(file);
    //     LinkedList<String> list = new LinkedList<String>();
    //     while(inputFile.hasNext()){
    //         String line = inputFile.nextLine();
    //         list.add(line);
    //     }
    //     inputFile.close();
    //     System.out.println("Enter the name of the file to write to: ");
    //     String fileName2 = scan.nextLine();
    //     PrintWriter outputFile = new PrintWriter(fileName2);
    //     Iterator<String> iter = list.iterator();
    //     while(iter.hasNext()){
    //         String line = iter.next();
    //         outputFile.println(line);
    //     }
    //     outputFile.close();
    //     System.out.println("Done!");
    }
