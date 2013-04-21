/* Wall class
*  Simple wall class. Very easy to implement because it does not move.
*/
package textkarel;

/**
 *
 * @author Gonzo
 */
public class Wall extends Actor {
    /**
     * Constructor
     * @param x Starting position on x axis
     * @param y Starting position on y axis
     */
    public Wall(int x, int y) {
        super(x, y);       
        this.setChar('#');
    }
}
