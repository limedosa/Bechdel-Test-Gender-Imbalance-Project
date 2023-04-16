package javafoundations;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;

public class HollyWoodApp {
    
    private Vector<LinkedList<T>> arcs;   // adjacency matrices of arcs
    private Vector<T> vertices;   // values of vertices
    private Graph graph;
    
    public HollyWoodApp(){
        graph = new Graph();
    }
    
    public void addVertex (T vertex){
        // add it to the vertices vector
        vertices.add(vertex);

        //indicate that the new vertex has no arcs to other vertices yet
        arcs.add(new LinkedList<T>());
    }
    
    public boolean isArc (T vertex1, T vertex2){
        try {
            int index = vertices.indexOf(vertex1);
            LinkedList<T> l = arcs.get(index);
            return (l.indexOf(vertex2) != -1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(vertex1 + " vertex does not belong in the graph");
            return false;
        }
    }

    public boolean isEdge(T vertex1, T vertex2) {
        return (isArc(vertex1, vertex2) && isArc(vertex2, vertex1));
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
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("\"")) {
                String[] values = line.split("\",\"");
                if (values.length == 6) {
                    String movie = values[0].replaceAll("\"", "");
                    String actor = values[1].replaceAll("\"", "");
                    String gender = values[5].replaceAll("\"", "");
                    
                    Vertex actorVertex = graph.getVertex(actor);
                    if (actorVertex == null) {
                        actorVertex = new Vertex(actor);
                        actorVertex.addAttribute("gender", gender);
                        graph.addVertex(actorVertex);
                    }
                    
                    Vertex movieVertex = graph.getVertex(movie);
                    if (movieVertex == null) {
                        movieVertex = new Vertex(movie);
                        graph.addVertex(movieVertex);
                    }
                    
                    graph.addEdge(actorVertex, movieVertex);
                }
            }
        }
        reader.close();
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
