/*
* ManualMode Class
* Make a Manual Mode Class that helps preform the key strokes
* the user puts in.
*/
package textkarel;

import java.util.Scanner;



/**
 *
 * @author Gonzo
 */
public class ManualMode {
    Board manualBoard;
    
    /**
     * Constructor
     * @param board created in main function  
     */
    public ManualMode(Board board){
        manualBoard = board;
    }
    
    
    /**
     * Prompts the user for input, and sends it to Action function.  
     */
    public void Start(){
        Scanner in = new Scanner(System.in);
        
        while((manualBoard.isManualMode()) && (!manualBoard.checkCompleted() && (!manualBoard.userQuit())) ){
            System.out.print("Enter move: ");
            String key = in.nextLine();
            if(!key.isEmpty()){
                action(key.charAt(0));
            }
            
            
        }
        
    }
    
    /**
     * Send character to board to preform corresponding action 
     * @param key Character read in 
     */
    public void action(char key) {
            
            
            //turn face of player 90 degrees to the left
            if (key == 'a') {  
                manualBoard.keyPressed('a');
            } 
            
            //turn face of player 90 degrees to the right
            else if (key == 'd') {  
                manualBoard.keyPressed('d');
            } 
            
            //checks if player can move to next spot and if allowed moved player to next spot
            else if (key == 'w') {
                manualBoard.keyPressed('w');
            } 
            
            //if player wants to set down a gem
            else if (key == 's') {
                manualBoard.keyPressed('s');
            } 
            
            //if the player wants to pick up a gem
            else if(key == 'e'){
               manualBoard.keyPressed('e');
            } 
            
            //restart the level
            else if (key == 'r') {
                manualBoard.keyPressed('r');
            }
            
            //switch to programming mode
            else if (key == 'p'){
                manualBoard.keyPressed('p');
            }
            
            //quit the program
            else if (key == 'q'){
                manualBoard.keyPressed('q');
            }
            
    }
    
      
}
