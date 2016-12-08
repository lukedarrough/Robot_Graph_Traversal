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
public class BoardController {
    int size;
    int board[][];
    
    Robot robot;
    Obstacle obs1;
    Obstacle obs2;
    
    int startX, startY;
    int finX, finY;
    
    ArrayList<Integer> xRecord = new ArrayList();
    ArrayList<Integer> yRecord = new ArrayList();
    
    //constructer establishing object variables
    public BoardController(int size, Robot robot, Obstacle obs1, Obstacle obs2,
            int startX, int startY, int finX, int finY){
        this.size = size;
        this.board = new int[size + 1][size + 1];
        
        //used to keep track of robot and obstacles on the grid
        this.robot = robot;
        this.obs1 = obs1;
        this.obs2 = obs2;
        
        this.startX = startX;
        this.startY = startY;
        this.finX = finX;
        this.finY = finY;
        
        init_board();
    }
    
    //main function, used to move all the pieces on the grid
    public int[][] find_path(){        
        //move each object on the grid
        obs1.move(board);
        obs2.move(board);
        robot.move(board, xRecord, yRecord);

        //called in case obstacles ended up on the same spot
        obs1.remark(board);
        obs2.remark(board);
        //print_board();
        
        
        return board;
    }
    
    //set all spaces on grid to 0, then add objects to the grid
    private void init_board(){
        for(int i = 0; i <= size; i++){
            for(int j = 0; j <= size; j++){
                board[i][j] = 0;
            }
        }
        
        obs1.create(board);
        obs2.create(board);
        robot.create(board);
        
        
    }
    
    //print function used for debugging
    public void print_board(){
        for(int i = 1; i <= size; i++){
            for(int j = 1; j <= size; j++){
                //System.out.print(board[i][j] + "\t");
            }
            
            //System.out.println("\n");
        }
        
    }
    
    public int get_size(){
        return size;
    }
}

