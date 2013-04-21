/*
* Board class
* This class is considered the intermedate of the whole program.
* It connects the actors with the other actors on the board.
* this makes for low coupling.
*/
package textkarel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Gonzo
 */
public class Board {
    private final int LEFT_COLLISION = 1;
    private final int RIGHT_COLLISION = 2;
    private final int TOP_COLLISION = 3;
    private final int BOTTOM_COLLISION = 4;
    private final int SPACE = 1;
    private final int TURN_LEFT=-1;
    private final int TURN_RIGHT=1;
    
    private ArrayList walls = new ArrayList();
    private ArrayList gems = new ArrayList();
    private Player karel;
    private Home home;
    private boolean completed;
    private boolean crashed;
    private String level;
    char[][] worldArray;
    private int boardHeight;
    private int boardWidth;
    private String fileName;
    private boolean manualMode;
    private boolean userquit;
    
    /**
     * Constructor
     * @param file File name of the level
     */
    public Board(String file) {
        completed = false;
        manualMode = true;
        userquit = false;
        crashed = false;
        setFileName(file);
        loadLevel();
        initWorld();
        printWorld();
    }
    
    /**
     * Sets the file name of the level to be loaded
     * @param fileName File name of the level  
     */
    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    
    /**
     * Reset the board to it's initial state
     * @param ManualRestart True if user selects key to restart level
     */
    public void restartLevel(boolean ManualRestart){
        
        //empty ArrayList of created actors
        walls.clear();
        gems.clear();
        
        //recreate and initialize all actors that will be on the board
        initWorld();
        
        //reset completed variable
        if(completed){
            completed = false;
        }
       
        
        printWorld();
        
        //if ManualRestart is false the user crashed; else the user manually restarted the level
        if(ManualRestart==false){
            crashed = true;
            System.out.println("Karel Crashed!! Try again.");
            
        }
    }
    
    /**
     * Reads in level data from from file and stores in string called level
     */
    public void loadLevel(){
       int temp = 0;
       String tempString;
       
        try{
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            while ((tempString = br.readLine()) != null) {
                level += (tempString + "\n");
                temp++;
                if(temp == 1){
                    boardWidth = tempString.length();
                }
            }
            
            boardHeight = temp;
            worldArray = new char[boardHeight][boardWidth];
        }catch(IOException e){
            System.out.println("File name invalid. Enter new file name.");
            Scanner in = new Scanner(System.in);
            fileName = in.nextLine();
            loadLevel();
        }
    }
    
    /**
     * Create and initialize all actor that will be a part of the board
     * Blank spaces aren't considered actors thus aren't stored
     * For easy printing to the console actors and blank space are saved to a 2d character array 
     */
    public void initWorld(){ 
        
        Wall wall;
        Gems gem;
        
        
        int x = 0;
        int y = 0;

        for (int i = 0; i < level.length(); i++) {
            char item = level.charAt(i);
            
            //if new line adjust coordinates 
            if (item == '\n') {
                y++;
                x=0;
            } 
            //creates wall and adds to list of walls on the board
            else if (item == '#') {
                wall = new Wall(x, y);
                walls.add(wall);
                worldArray[y][x] = '#';
                x++;
            } 
            //creates gems and adds to list of gems
            else if (item == 'G') {
                gem = new Gems(x, y);
                gems.add(gem);
                worldArray[y][x] = 'G';
                x++;
            }
            //create home
            else if (item == 'H') {
                home = new Home(x, y);
                worldArray[y][x] = 'H';
                x++;
            }
            //create the player
            else if (item == '@') {
                karel = new Player(x, y);
                worldArray[y][x] = '@';
                x++;
            } 
            //add blanks to the world array for easy printing to the console
            else if (item == ' ') {
                worldArray[y][x] = ' ';
                x++;
            }   
        }
        
    }
    
    /**
     * Builds world(updates world array) according to actor's stored coordinates
     */
    public void buildWorld(){
        ArrayList world = new ArrayList();
        world.addAll(walls);
        world.addAll(gems);
        world.add(home);
        world.add(karel);

        for (int i = 0; i < world.size(); i++) {
            Actor item = (Actor) world.get(i);
            
            if((item instanceof Gems)){
                Gems tempGem = (Gems) world.get(i);
                if((tempGem.Gempickup() == false) && (tempGem.getOnHome() == false)){
                    worldArray[item.y()][item.x()] = item.getChar();
                }
            }
            else{
                worldArray[item.y()][item.x()] = item.getChar();
            }
            
        }
    }
    
    /**
     * Prints world to console. Also prints data useful to user (gems on home, etc.) 
     */
    public void printWorld(){
        pause();
        
        int n=0;
        buildWorld();
        
        //space buffer from other printouts
        for(n=0;n<15;n++)
        {
            System.out.println();
        }
        n=0;
        
        //print to console if game is not completed 
        if(completed == false){
           for(int i = 0; i < boardHeight; i++){
               for(int j = 0; j < boardWidth; j++){
                   System.out.print(worldArray[i][j]);
               }
               System.out.println();
           }
           
            //display number of gems
            n=karel.ReturnGemCount();
            System.out.print("Number of Gems on Karel: ");
            System.out.println(n);

            //display direction the player is facing
            n=karel.ReturnDirection();
            System.out.print("Direction facing: ");

            switch(n)
            {
                case 1:
                    System.out.println("North");
                    break;

                case 2:
                    System.out.println("East");
                    break;

                case 3:
                    System.out.println("South");
                    break;

                case 4:
                    System.out.println("West");
                    break;
            }//end switch

            //display number of gems on home 
            n=home.CountGet();
            System.out.print("Number of Gems on Home: ");
            System.out.println(n);
            System.out.println();
            System.out.println("Control's of the Game:");
            System.out.println("w=move   a=change-direction left   d=change-direction right");
            System.out.println("e=Pickup   s=Drop   p=Switch to programmer mode");
            System.out.println("q=quit");
        }
        else{
            System.out.println("Level Complete");
            
        }
        
       
    }
    
    /**
     * Checks if the actor being passed to it will crash into a wall when moved
     * @param actor Actor being checked for collision 
     * @param type Numeric representation of the type of collision to test
     * @return True if a crash will happen
     */
    private boolean checkWallCollision(Actor actor, int type) {

        if (type == LEFT_COLLISION) {

            for (int i = 0; i < walls.size(); i++) {
                Wall wall = (Wall) walls.get(i);
                if (actor.isLeftCollision(wall)) {
                    return true;
                }
            }
            return false;

        } else if (type == RIGHT_COLLISION) {

            for (int i = 0; i < walls.size(); i++) {
                Wall wall = (Wall) walls.get(i);
                if (actor.isRightCollision(wall)) {
                    return true;
                }
            }
            return false;

        } else if (type == TOP_COLLISION) {

            for (int i = 0; i < walls.size(); i++) {
                Wall wall = (Wall) walls.get(i);
                if (actor.isTopCollision(wall)) {
                    return true;
                }
            }
            return false;

        } else if (type == BOTTOM_COLLISION) {

            for (int i = 0; i < walls.size(); i++) {
                Wall wall = (Wall) walls.get(i);
                if (actor.isBottomCollision(wall)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    
    /**
     * Interprets the key pressed when in manual mode 
     * @param key Character key entered on the keyboard
     */
     public void keyPressed(char key) {
            
         //dont do anything if the level is complete 
            if (completed) {
                return;
            }
            
            //turn face of player 90 degrees to the left
            if (key == 'a') {
                
                karel.SetDir(TURN_LEFT);
            } 
            //turn face of player 90 degrees to the right
            else if (key == 'd') {
                
                karel.SetDir(TURN_RIGHT);

            } 
            
            //checks if player can move to next spot and if allowed moved player to next spot
            else if (key == 'w') {
                karelGO();
            } 
            
            //if player wants to set down a gem
            else if (key == 's') {  
                karelPUT();
            } 
            
            //if the player wants to pick up a gem
            else if(key == 'e'){
                karelGET();
            } 
            
            //restart the level
            else if (key == 'r') {
                restartLevel(true);
            }
            
            else if (key == 'p'){
                manualMode = false;
                restartLevel(true);
                
            }
            
            else if (key == 'q'){
                userquit = true;
            }
            
            else if (key == 'm'){
                manualMode = true;
                restartLevel(true);
            }

            printWorld();
        }
     
     /**
     * Adjusts carried gem positions to the position of the player
     * @param x How many spaces on the x axis to move 
     * @param y How many spaces on the y axis to move
     */
     private void movePickedUpGems(int x, int y){
         for(int i=0; i<gems.size(); i++){
             Gems gem = (Gems) gems.get(i);
             if(gem.Gempickup() == true){
                 gem.move(x, y);
             }
         }
     }
     
     /**
      * Sets completed variable to true is all gems have been moved to home
      */
     public void isCompleted(){
         if(home.CountGet() == gems.size()){
             completed = true;
         }
         else{
             completed = false;
         }
     }
     
     /**
      * Returns mode the board is in
      * @return True if board is in manual mode
      */
     public boolean isManualMode(){
         return manualMode;
     }
     
     /**
      * Returns state of application  
      * @return True if user inputs 'q' to quit the application 
      */
     public boolean userQuit(){
         return userquit;
     }
     
     /**
      * Returns state the game is in
      * @return True if all gems have been picked up and place on home
      */
     public boolean checkCompleted(){
         return completed;
     }
     
     /**
      * Move player forward if move is accepted
      */
     public void karelGO(){
         int temp=0;
                temp=karel.ReturnDirection();    //player's current direction
                
                switch(temp)
                {
                    case 1://trying to move north
                        if (checkWallCollision(karel, TOP_COLLISION)) {
                            restartLevel(false);
                            return;
                        }
                        else{  
                            worldArray[karel.y()][karel.x()]=' ';
                            karel.move(0,-SPACE);
                            movePickedUpGems(0, -SPACE);
                            isCompleted();
                            break;
                        }
                        
                    case 2://trying to move east
                        if(checkWallCollision(karel, RIGHT_COLLISION)) {
                            restartLevel(false);
                            return;
                        }
                        else{
                            worldArray[karel.y()][karel.x()]=' ';
                            karel.move(SPACE,0);
                            movePickedUpGems(SPACE, 0);
                            isCompleted();
                            break;
                       }
                        
                    case 3://trying to move south
                        if (checkWallCollision(karel, BOTTOM_COLLISION)) {
                            restartLevel(false);
                            return;
                        }
                        else{
                            worldArray[karel.y()][karel.x()]=' ';
                            karel.move(0,SPACE);
                            movePickedUpGems(0, SPACE);
                            isCompleted();
                            break;
                       }
                    case 4://trying to move west
                        if (checkWallCollision(karel, LEFT_COLLISION)) {
                            restartLevel(false);
                            return;
                        }
                        else{
                            worldArray[karel.y()][karel.x()]=' ';
                            karel.move(-SPACE,0);
                            movePickedUpGems(-SPACE, 0);
                            isCompleted();
                            break;
                        }
                    default:
                        break;
                        
                }//end switch
     }
     
     /**
      * Places gem if player is carrying one
      */
     public void karelPUT(){
         int x, y;

                //players current position
                x = karel.x();
                y = karel.y();
                
                //find a gem that is being carried
                //if no gem is being carried nothing happens
                for(int i=0; i<gems.size(); i++){
                    Gems gem = (Gems) gems.get(i);
                    
                    //if there is a gem to put down updates all associated data necessary
                    if((gem.x()== x) && (gem.y()==y)){
                        if(gem.Gempickup() == true){
                            gem.Setpickup(false);
                            karel.DecGems();
                            worldArray[y][x] = gem.getChar();
                            System.out.println("You have dropped off a gem!");
                            
                            if((gem.x() == home.x()) && (gem.y() == home.y())){
                                gem.setOnHome(true);
                                home.CountInc();
                            }//end inner most if
                            break;
                        }//end middle if
                    }//end outer if  
                }//end for
              
              //test to see if the level has been completed
              isCompleted();
     }
     
     /**
      * Grab gem if spot has a gem
      */
     public void karelGET(){
         int x, y;

                x = karel.x();
                y = karel.y();
                
                //find a gem that is on the current location of the player
                //if no gem is on the location nothing happens
                for(int i=0; i<gems.size(); i++){     
                    Gems gem = (Gems) gems.get(i);
                    
                    //if there is a gem to pick up updates all associated data necessary
                    if((gem.x()== x) && (gem.y()==y)){
                        if(gem.Gempickup() == false){
                            gem.Setpickup(true);
                            karel.IncrementGems();
                            worldArray[y][x] = ' ';
                            System.out.println("You have picked up a gem!");
                            
                            if((gem.x() == home.x()) && (gem.y() == home.y())){
                                gem.setOnHome(false);
                                home.CountDec();
                            }//end inner most if
                            break;
                        }//end middle if       
                    }//end outer if  
                }//end for
     }
     
     /**
      * Checks if there is a wall directly in front of the player
      * @return True if the is a wall directly in front of the player
      */
     public boolean wallSensor(){
         
                int temp=karel.ReturnDirection();    //player's current direction
                
                switch(temp)
                {
                    case 1://trying to move north
                        if (checkWallCollision(karel, TOP_COLLISION)) {
                            return true;
                        }
                        else{  
                            return false;
                        }
                        
                    case 2://trying to move east
                        if(checkWallCollision(karel, RIGHT_COLLISION)) {
                            return true;
                        }
                        else{
                            return false;
                       }
                        
                    case 3://trying to move south
                        if (checkWallCollision(karel, BOTTOM_COLLISION)) {
                            return true;
                        }
                        else{
                            return false;
                       }
                    case 4://trying to move west
                        if (checkWallCollision(karel, LEFT_COLLISION)) {
                            return true;
                        }
                        else{
                            return false;
                        }
                    default:
                        return true;
                        
                }//end switch
     }
     
     /**
      * Checks if the player is on a gem
      * @return True if the player is on a space with a gem
      */
     public boolean gemSensor(){
         int x, y;
         boolean state = false;

                x = karel.x();
                y = karel.y();
                
                //find a gem that is on the current location of the player
                //if no gem is on the location nothing happens
                for(int i=0; i<gems.size(); i++){     
                    Gems gem = (Gems) gems.get(i);
                    
                    //if there is a gem to pick up updates all associated data necessary
                    if((gem.x()== x) && (gem.y()==y)){
                        if(gem.Gempickup() == false){
                            state = true;
                            
                        }//end middle if       
                    }//end outer if  
                }//end for
                
                return state;
     }
     
     public boolean emptySensor(){
         if(karel.ReturnGemCount() == 0){
             return true;
         }
         else{
             return false;
         }
     }
     
     public boolean northSensor(){
         if(karel.ReturnDirection() == 1){
             return true;
         }
         else{
             return false;
         }
     }
     
     public boolean homeSensor(){
         if( (karel.x() == home.x()) && (karel.y() == home.y())){
             return true;
         }
         else{
             return false;
         }
     }
     
     public boolean ReturnCrashState(){
         return crashed;
     }
     
     private void pause(){
         try{
             Thread.sleep(400);
             
         }
         catch(InterruptedException e){
             e.printStackTrace();
         }
         
     }
}