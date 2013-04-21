/*
 * Home class
 */
package textkarel;

/**
 *
 * @author Gonzo
 */
public class Home extends Actor {
    private int GemCounter;
    
    /**
     * 
     * @param x Starting position on x axis
     * @param y Starting position on y axis
     */
     public Home(int x, int y) {
        super(x, y);       
        this.setChar('H');
        GemCounter=0;
    }
    
     /**
      * Increase number of gems on home by one
      */
     public void CountInc(){
         
         GemCounter++;
     }
     
     /**
      * Decrease number of gems on home by one
      */
     public void CountDec(){
         
         GemCounter--;
     }
     
     /**
      * 
      * @return Number of gems on home  
      */
     public int CountGet(){
         
         return GemCounter;
     }
}
