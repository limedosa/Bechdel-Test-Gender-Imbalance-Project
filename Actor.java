
/**
 * Represents an Actor, with their gender and their name
 *
 * @author (Ashley, Linda, Agnes)
 * @version (04/16/2023)
 */
public class Actor extends HollywoodNode
{
    // "f" for female, "m" for male, "o" for other?
    // up for change, if we want to make this a bool
    private String gender;
    
    public Actor(String n, String g){
        super(n);
        gender = g;
    }
}
