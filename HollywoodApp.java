import javafoundations.AdjListsGraph;
import java.util.Hashtable;
import java.util.Vector;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.*;
import java.util.*;

/**
 * HollywoodApp.java
 * 
 * This class generates an undirected bipartite graph based
 * on a txt file of movie casts and creates a new
 * Bechdel-like value for each movie that captures whether
 * the movie has over 48% women in its cast. It also enables
 * the user to save the graph to a tgf file, get a String
 * representation of the graph, and get a list of the
 * movies that have over 48% women in its cast.
 * 
 * People we consulted with:
 * We went to Angel Cooper's office hours, emailed
 * the CS230 instructors and got replied by Takis Metaxas
 * and Smaranda Sandu. Thank you so much for your
 * instructions and guidance!
 * 
 * @author (Ashley (as126), Linda (ld104), Agnes (yq100))
 * @version (04/16/2023)
 */
public class HollywoodApp {
    private AdjListsGraph<String> hollywoodRelationalGraph;

    /*
     * Keeps track of actors' gender. Key: name of actor;
     * value: gender of actor
     */
    private Hashtable<String, String> actorToGender;

    /*
     * Keeps track of the cast of each movie. Key: name of
     * movie; value: a LinkedList of the names of all actors
     * in the movie
     */
    private Hashtable<String, LinkedList<String>> actorsInMovie;

    /**
     * The constructor for HollywoodApp initializes the instance
     * variables: the adjacency list hollywoodRelationalGraph and
     * hastables actorToGender and actorsInMovie.
     */
    public HollywoodApp() {
        hollywoodRelationalGraph = new AdjListsGraph<String>();
        actorToGender = new Hashtable<String, String>();
        actorsInMovie = new Hashtable<String, LinkedList<String>>();
    }

    /**
     * This method creates an undirected graph with vertices
     * representing movie names and actor names from a txt
     * file of movie casts using an adjacency list. The edges
     * reflect the relationship that an actor played in
     * the movie. It also keeps track of the genders of
     * the actors.
     * 
     * @param fileName a String that denotes the name of the
     *                 txt file to read from
     */
    public void createGraphFromFile(String fileName) {
        try { // to read from bechdal test file
              // Set comma, newline, carriage return character as delimiters
            Scanner scanner = new Scanner(new File(fileName))
                    .useDelimiter("\\,|\\n|\\r");

            scanner.nextLine(); // throw away first line
            while (scanner.hasNext()) {
                String movie = scanner.next().replaceAll("\"", "");
                hollywoodRelationalGraph.addVertex(movie);

                String actor = scanner.next().replaceAll("\"", "");

                /*
                 * Skip CHARACTER_NAME, TYPE, BILLING by
                 * overwriting temp
                 */
                for (int i = 0; i < 3; i++) {
                    String temp = scanner.next();
                }
                String gender = scanner.next().replaceAll("\"", "");
                hollywoodRelationalGraph.addVertex(actor);

                hollywoodRelationalGraph.addEdge(movie, actor);

                // Assuming actors have the same gender in different movies
                actorToGender.put(actor, gender);

                if (!actorsInMovie.containsKey(movie)) {
                    actorsInMovie.put(movie, new LinkedList<String>());
                }
                actorsInMovie.get(movie).add(actor);
            }
            scanner.close();
        } catch (IOException ex) {
            System.out.println(" ***(T)ERROR*** The file was not "
                    + "found: " + ex);
        }
    }

    /**
     * This method saves the graph into a .tgf file that can
     * be opened in yED by using the saveTGF() method
     * of the AdjListsGraph class.
     * 
     * @param outFileName a String that denotes the name of
     *                    the output file
     */
    public void saveIntoTGF(String outFileName) {
        hollywoodRelationalGraph.saveTGF(outFileName);
    }

    /**
     * This method returns a String representation of the
     * graph by using the toString() method of the
     * AdjListsGraph class.
     * 
     * @return a String representation of the graph
     */
    public String toString() {
        return hollywoodRelationalGraph.toString();
    }

    /**
     * This method writes the result of the new Bechdel
     * Test into a text file. It includes a list of
     * the names of the movies that have over 48% of
     * women in the cast and another list of those that
     * do not as well as their Bechdel Value.
     * 
     * @param fName a String that denotes the name of
     *              the output file
     */
    public void bechdelTestingToFile(String fName) {
        try {
            PrintWriter writer = new PrintWriter(new File(fName));
            writer.println(findPassedTest());

            writer.close();
        } catch (IOException ex) {
            System.out.println("***ERROR***" + fName + " could "
                    + "not be written: " + ex);
        }
    }

    /**
     * This is a helper method that returns a String
     * representation of whether the movies have over 48%
     * of women in the cast and calculates their Bechdel
     * Value.
     * 
     * @return a report of whether the movies have over 48%
     *         of women in the cast
     */
    private String findPassedTest() {
        String passedTest = "Movies that have over 48% "
                + "women in the cast:\n";
        String didNotPass = "Movies that do NOT have over "
                + "48% women in the cast:\n";

        // Keeps track of the number of female actors
        int femaleCount;

        for (String m : actorsInMovie.keySet()) {
            femaleCount = 0;
            for (int j = 0; j < actorsInMovie.get(m).size(); j++) {
                if (actorToGender.get(actorsInMovie.get(m)
                        .get(j)).equals("Female")) {
                    femaleCount++;
                }
            }

            /*
             * actorsInMovie.get(m).size() gives the total
             * number of actors in the cast, including
             * genders female, male and unknown.
             */
            double bechdelValue = (double) femaleCount
                    / actorsInMovie.get(m).size();

            /*
             * We calculated the percentage of female actors
             * in the movie using
             * (number of female actors)/(number of male
             * actors + number of unknown actors)
             * because we think the project is trying to find
             * whether there are over 48% women in the cast.
             * Ultimately, the lack of female representation
             * on screen is an integral layer of the gender
             * imbalance in the movie industry that we need
             * to take account of. Therefore, if a character
             * is not identifiably female, it contributes to
             * the absence of female representation in movies.
             * For this reason, we believe actors with gender
             * "unknown" should be included in the denominator
             * while calculating the percentage of female
             * actors in movies.
             */
            if (bechdelValue >= 0.48) {
                passedTest += "\t" + m + " - Bechdel Value "
                        + bechdelValue + "\n";
            } else {
                didNotPass += "\t" + m + " - Bechdel Value "
                        + bechdelValue + "\n";
            }
        }
        return (passedTest + "\n" + didNotPass);
    }

    /**
     * Returns a linked list of movies in which the given actor appears.
     * 
     * @param actorName the name of the actor to search for
     * @return a linked list of movies in which the given actor appears
     */
    public LinkedList<String> moviesOfActor(String actorName) {
        LinkedList<String> movies = new LinkedList<String>();
        Set<String> movieNames = actorsInMovie.keySet();
        for (String movieName : movieNames) {
            LinkedList<String> actors = actorsInMovie.get(movieName);
            if (actors.contains(actorName)) {
                movies.add(movieName);
            }
        }
        return movies;
    }

    /**
     * Returns a linked list of actors in the given movie.
     * 
     * @param movieName the name of the movie to search for
     * @return a linked list of actors in the given movie, or an empty list if the
     *         movie is not found
     */
    public LinkedList<String> actorsInMovie(String movieName) {
        if (actorsInMovie.containsKey(movieName)) {
            return actorsInMovie.get(movieName);
        } else {
            return new LinkedList<String>();
        }
    }

    /**
     * 
     * Finds the degree of separation between two actors using a BFS algorithm on a
     * given file of movies and actors.
     * 
     * @param actor1   the name of the first actor
     * 
     * @param actor2   the name of the second actor
     * 
     * @param fileName the name of the file containing the list of movies and actors
     * 
     * @return the degree of separation between the two actors, or -1 if they are
     *         not connected
     * 
     * @throws IOException if there is an error reading the file
     */
    public static int findDegreeOfSeparation(String actor1, String actor2, String fileName) throws IOException {
        // Create maps and sets to store the movie to actors and actor to separation
        // relationships
        Map<String, Set<String>> movieToActors = new HashMap<>();
        Map<String, Integer> actorToSeparation = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        // Read the file and create movie to actors relationships

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split(",");
            if (tokens.length != 6) {
                continue; // skip line if it doesn't have 6 parts separated by commas bc of data structure
            }
            String movie = tokens[0].replaceAll("\"", "");
            String actor = tokens[1].replaceAll("\"", "");
            Set<String> movieActors = movieToActors.computeIfAbsent(movie, k -> new HashSet<>());
            movieActors.add(actor);
            if (!actorToSeparation.containsKey(actor)) {
                actorToSeparation.put(actor, Integer.MAX_VALUE);
            }
        }
        br.close();

        // BFS to find degree of separation
        actorToSeparation.put(actor1, 0);
        queue.offer(actor1);
        visited.add(actor1); // Marking the starting actor as visited
        while (!queue.isEmpty()) {
            String currActor = queue.poll();
            int currSeparation = actorToSeparation.get(currActor);

            Set<String> currMovies = new HashSet<>();
            // Find movies that contain the current actor
            for (String movie : movieToActors.keySet()) {
                Set<String> actorsInMovie = movieToActors.get(movie);
                if (actorsInMovie.contains(currActor)) {
                    currMovies.add(movie);
                }
            }

            // Find actors in the movies and add them to the queue
            for (String movie : currMovies) {
                Set<String> actorsInMovie = movieToActors.get(movie);
                for (String actor : actorsInMovie) {
                    if (!visited.contains(actor)) {
                        actorToSeparation.put(actor, currSeparation + 1);
                        visited.add(actor);
                        queue.offer(actor);
                    }
                }
            }
            // Return degree of separation if current actor is second actor
            if (currActor.equals(actor2)) {
                return currSeparation - 1;
            }
        }

        return -1; // return this if actors aren't connected
    }

    /**
     * The main() method provides basic testing for the
     * methods above.
     * 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // HollywoodApp ha = new HollywoodApp();
        // ha.createGraphFromFile("data/nextBechdel_castGender.txt");
        // System.out.println(ha);
        // System.out.println(ha.actorToGender);
        // ha.saveIntoTGF("out1.tgf");
        // System.out.println(ha.findPassedTest());
        // ha.bechdelTestingToFile("bechdelProject_testing.txt");
        // HollywoodApp app = new HollywoodApp();
        // app.createGraphFromFile("bechdelProject_testing.txt");
        // LinkedList<String> movies = app.moviesOfActor("Jennifer Lawrence");
        // System.out.println(movies);
        HollywoodApp app = new HollywoodApp();
        app.createGraphFromFile("bechdelProject_testing.txt");
        String actor1 = "Megan Fox";
        String actor2 = "Tyler Perry";
        int degreeOfSeparation = HollywoodApp.findDegreeOfSeparation(actor1, actor2,
        "bechdelProject_testing.txt");
        System.out.println("Degree of separation between " + actor1 + " and " +
        actor2 + ": " + degreeOfSeparation);

        actor1 = "Nick Arapoglou";
        actor2 = "Tyler Perry";
        degreeOfSeparation = HollywoodApp.findDegreeOfSeparation(actor1, actor2,
        "bechdelProject_testing.txt");
        System.out.println("Degree of separation between " + actor1 + " and " +
        actor2 + ": " + degreeOfSeparation);


    }

}