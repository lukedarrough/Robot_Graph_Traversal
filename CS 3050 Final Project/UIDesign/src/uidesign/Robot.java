/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uidesign;

import java.util.ArrayList;

/**
 *
 * @author jeffreyruffolo
 */
public class Robot {
    int roboID;
    int size;
    int startX, startY;
    int endX, endY;
    Obstacle obs1, obs2;
    
    int lastX, lastY;
    int prevX, prevY;
    
    //constructer for setting up variables
    public Robot(int roboID, int size, int startX, int startY, int endX, int endY, 
            Obstacle obs1, Obstacle obs2){
        this.roboID = roboID;
        this.size = size;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.obs1 = obs1;
        this.obs2 = obs2;
    }
    
    //adds the robot to a given grid
    public boolean create(int board[][]){
        if(is_clear(board, startX, startY)){
            board[startX][startY] = roboID;
        
            lastX = startX;
            lastY = startY;
            
            return true;
        }
        
        System.out.println("Robot create errror: start not clear, " + 
                startX + " , " + startY + " - " + board[startX][startY]);
        return false;        
    }
    
    //main function, used to find the next location on the path
    public boolean move(int board[][], ArrayList<Integer> xRecord, ArrayList<Integer> yRecord){
        //stores possible next locations
        ArrayList<Integer> xList = new ArrayList();
        ArrayList<Integer> yList = new ArrayList();
        
        //add all available locations to a list, then find the optimal one
        available_squares(board, xList, yList);        
        int optimal_square = optimal_square(xList, yList, xRecord, yRecord);
        
        //move the robot to the optimal next location
        if(do_move(board, xList.get(optimal_square), yList.get(optimal_square))){
            //location is stored in a record
            xRecord.add(xList.get(optimal_square));
            yRecord.add(yList.get(optimal_square));
                        
            return true;
        }
        
        return false;
    }
    
    //find all locations adjacent to the robot
    private void available_squares(int board[][], ArrayList<Integer> xList, ArrayList<Integer> yList){
        for(int i = lastX - 1; i <= lastX + 1; i++){
            for(int j = lastY - 1; j <= lastY + 1; j++){
                //if location is clear, add to list
                if(is_clear(board, i, j)){
                    xList.add(i);
                    yList.add(j);
                    
                    continue;
                }
            }
        }
    }
    
    //function used to find optimal location in given list
    private int optimal_square(ArrayList<Integer> xList, ArrayList<Integer> yList,
            ArrayList<Integer> xRecord, ArrayList<Integer> yRecord){
        //counters for finding optimal
        int optimal_square = -1;
        double shortest_dist = size + 1;
        
        int backup_square = 0;
        
        
        //loop to find optimal square using Pythagorean theorem
        for(int i = 0; i < xList.size(); i++){
            double xSquare = Math.pow(endX - xList.get(i), 2);
            double ySquare = Math.pow(endY - yList.get(i), 2);
            
            double dist = Math.sqrt(xSquare + ySquare);
            if(dist < shortest_dist){
                if(xRecord.size() > 1 &&
                        xList.get(i) == xRecord.get(xRecord.size() - 2) &&
                        yList.get(i) == yRecord.get(yRecord.size() - 2)){
                    backup_square = i;
                                    }
                else{
                    optimal_square = i;
                    shortest_dist = dist;
                }
                
            }
            
        }
        
        //mechanism for avoiding repetition of paths
        if(optimal_square == -1){
            if(backup_square == -1){
                //System.out.println("Optimal square error");
            }
            else{
                optimal_square = backup_square;
            }
        }
        
        return optimal_square;
    }
    
    //make the robot move
    private boolean do_move(int board[][], int newX, int newY){
        
        
        board[lastX][lastY] = 0;

        prevX = lastX;
        lastX = newX;
        prevY = lastY;
        lastY = newY;
        board[lastX][lastY] = roboID;
                
        return true;
    }
    
    //check whether given square is available for the robot to move to
    private boolean is_clear(int board[][], int x, int y){
        if(x <= 0 || y <= 0){
            return false;
        }
        if(x > size || y > size){
            return false;
        }
        if(board[x][y] != 0){
            return false;
        }
        if(x == obs1.nextX(false) && y == obs1.nextY(false)){
            //return false;
        }
        if(x == obs2.nextX(false) && y == obs2.nextY(false)){
            //return false;
        }
        
        return true;
    }
    
    //return true if robot has reached the destination
    public boolean is_finished(){
        return lastX == endX && lastY == endY;
    }
    
    
}
