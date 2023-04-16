
/**
 * Write a description of class HollywoodApp here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class HollywoodApp
{

    private Vector<LinkedList<T>> arcs;   // adjacency matrices of arcs
    private Vector<T> vertices;   // values of vertices

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
}
