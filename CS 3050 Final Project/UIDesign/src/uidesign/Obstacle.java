/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uidesign;

/**
 *
 * @author jeffreyruffolo
 */
public class Obstacle {
    int obsID;
    int size;
    int startX, startY;
    int speed;
    int dirX, dirY;
    
    
    int lastX, lastY;
    
    //constructer for setting up object variables
    public Obstacle(int obsID, int size, int startX, int startY, int speed, int dirX, int dirY){
        this.obsID = obsID;
        this.size = size;
        this.startX = startX;
        this.startY = startY;
        
        if(speed == size){
            this.speed = 0;
            this.dirX = dirX;
            this.dirY = dirY;
        }
        else if(speed > size){
            this.speed = speed - size;
            this.dirX = -1 * dirX;
            this.dirY = -1 * dirY;
        }
        else{
            this.speed = speed;
            this.dirX = dirX;
            this.dirY = dirY;
        }
    }
    
    //add the obstacle to the grid
    public void create(int board[][]){
        board[startX][startY] = obsID;
        
        lastX = startX;
        lastY = startY;
    }
    
    //main function, move the obstacle to its next location
    public boolean move(int board[][]){
        board[lastX][lastY] = 0;

        lastX = nextX(true);
        lastY = nextY(true);
        
        board[lastX][lastY] = obsID;
        
        return true;
    }
    
    //find the next X based on direction and speed, boolean determines whether to make lasting changes
    public int nextX(boolean real){
        int xDest = speed * dirX + lastX;
        int nextX;
        
        //handle reflections off edges
        if(xDest > size || xDest <= 0){
            nextX = xDest - size > 0 ? 2 * size - xDest : (1 - xDest) + 1;
            if(real) dirX = -1 * dirX;
        }
        else{
            nextX = xDest;
        }
        
        return nextX;
    }
    
    //find the next Y based on direction and speed, boolean determines whether to make lasting changes
    public int nextY(boolean real){
        int yDest = speed * dirY + lastY;
        int nextY;
        
        //handle reflections off edges
        if(yDest > size || yDest <= 0){
            nextY = yDest - size > 0 ? 2 * size - yDest : (1 - yDest) + 1;
            if(real) dirY = -1 * dirY;
        }
        else{
            nextY = yDest;
        }
        
        return nextY;
    }
    
    //add the obstacle back to grid if it was overwritten
    public void remark(int board[][]){
        board[lastX][lastY] = obsID;
    }
}
