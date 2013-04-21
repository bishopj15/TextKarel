/*
 * Main class for project
 * Starts board in manual mode by default
 */
package textkarel;

import java.util.Scanner;

/**
 *
 * 
 * @author Gonzo
 */
public class TextKarel {

    /**
     * Main function of the program.States the goals for the user.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
 
        boolean userquit = false;
        
        System.out.println("\nThe goal of Karel is to collect gems, and drop them off without crashing.");
        System.out.println("Please enter your file name of the level: ");
        System.out.println("(type in maps/grid1.txt for a default map)");
        
        Scanner in = new Scanner(System.in);
        
   
        String fileName = in.nextLine();
        
        Board board = new Board(fileName);

        
        //while user has not entered a 'q'
        while(!userquit){
            
            
            if(board.isManualMode()){
                
                ManualMode manualmode = new ManualMode(board);
                manualmode.Start();

                    userquit = board.userQuit();

                    //promt for and start new level when completed
                    if(board.checkCompleted()){
                        System.out.println("Enter the file name of the level");
                        fileName = in.nextLine();
                        board = new Board(fileName);
                    }
                }
            
            //user has switched to programming mode
            else{
                System.out.println("Enter the file name of your program");
                String programFile = in.nextLine();
                
                ProgrammerMode programmerMode = new ProgrammerMode(programFile, board);
                programmerMode.Start();

                //promt for and start new level when completed
                if(board.checkCompleted()){
                        System.out.println("Enter the file name of the level");
                        fileName = in.nextLine();
                        board = new Board(fileName);
                }
            }
              
        }//end while
        
    }//end main
    
}

