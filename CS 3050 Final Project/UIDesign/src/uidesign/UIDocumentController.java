/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uidesign;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Jeremy
 */
public class UIDocumentController implements Initializable {
    
    @FXML
    private AnchorPane background;
    
    @FXML
    private Pane view;
    
    @FXML
    private Button start;
    
    @FXML
    private Button next;
    
    @FXML
    private Label label;
    
    @FXML
    private TextArea test;
    
    @FXML
    private Label currLabel;
    
    @FXML
    private Label lastLabel;
    
    String path, pathAdd;
    private Stage stage;
    Integer size = 0;
    BackgroundPiece board[][];
    double viewSize;
    boolean fileLoaded = false, firstMove = false, started = false;
    double pos = viewSize/size;
    
    BoardController bc;
    
    // Call this function to first initialize the ui
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewSize = view.getPrefHeight();
        background.getChildren().remove(next);
    }  
    
    // if the user clicks the open menu tab then open a file chooser to allow
    // the user to select a file they want to open. Then if they provide a file
    // then parse the file and check if the file is formatted correctly.
    // if formated correctly then initialize the board and place all the 
    // obstacles and robot
    @FXML
    private void handleOpen(){
        //System.out.println("I AM OPENING STUFF!");
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null){
            try{
                bc = handleParseGetline(file);
                fileLoaded = true;
                firstMove = true;
                initializeBoard(bc.get_size());
            }catch (Exception ex){
                System.out.println(ex.toString());
            }
        }
    }
    
    // if there was a file selected then this handles the placing of the objects
    // and changes all the variables to display the previous position, the current
    // position. This function also handles the coloring of the start and ending 
    // positions
    @FXML
    private void handleStart(){
        if(!fileLoaded){
            label.setText("ERROR: File Not Loaded");
            return;
        }
        background.getChildren().remove(start);
        background.getChildren().add(next);
        label.setText("");
        path = "(" + bc.robot.startX + "," + bc.robot.startY + ")";
        //path += ("-->" + path);
        //test.setText(path);
        lastLabel.setText(path);
        currLabel.setText(path);
        if(started){
            label.setText("ERROR: Already Started");
            return;
        }
        else{
            view.getChildren().clear();
            for(int i = 1; i <= size; i++){
                //System.out.println(view.getChildren().toString());
                for(int j = 1;j <= size;j++){
                    BackgroundPiece bgPiece = new BackgroundPiece(pos, (double)(i - 1) * pos, (double)(j - 1) * pos);
                    view.getChildren().add(bgPiece);
                    board[j][i] = bgPiece;
                }
            }
            // Mark Start
            int startX = bc.startX;
            int startY = bc.startY;
            // Mark Finish
            int finX = bc.finX;
            int finY = bc.finY;
            
            // Add Start
            board[startX][startY].rectangle.setFill(Color.GREEN);
            
            // Add End
            board[finX][finY].rectangle.setFill(Color.RED);
            
            // When Robot hits the Finish
            if(bc.robot.lastX == bc.finX && bc.robot.lastY == bc.finY){
                label.setText("DONE!");
                //System.out.println("DONE!");
                return;
            }
            
            // Mark Robot
            int robotStartX = bc.robot.startX;
            int robotStartY = bc.robot.startY;
            // Mark Obstacle 1
            int obstacle1StartX = bc.obs1.startX;
            int obstacle1StartY = bc.obs1.startY;
            // Mark Obstacle 2
            int obstacle2StartX = bc.obs2.startX;
            int obstacle2StartY = bc.obs2.startY;

            // Add Robot
            Circle robot = new Circle(pos/3, Color.ORANGE);
            robot.setTranslateX(((double)(robotStartY - 1) * pos) + pos/2);
            robot.setTranslateY(((double)(robotStartX - 1) * pos) + pos/2);
            view.getChildren().add(robot);

            // Add Obstacle 1
            Circle obstacle1 = new Circle(pos/3, Color.BLACK);
            obstacle1.setTranslateX(((double)(obstacle1StartY - 1) * pos) + pos/2);
            obstacle1.setTranslateY(((double)(obstacle1StartX - 1) * pos) + pos/2);
            view.getChildren().add(obstacle1);

            // Add Obstacle 2
            Circle obstacle2 = new Circle(pos/3, Color.AQUA);
            obstacle2.setTranslateX(((double)(obstacle2StartY - 1) * pos) + pos/2);
            obstacle2.setTranslateY(((double)(obstacle2StartX - 1) * pos) + pos/2);
            view.getChildren().add(obstacle2);


            started = true;
        }
    }
    
    // If there is a file, then this function handles the stepping of the robot 
    // and the movement of the obstacles. This also updates the previous and 
    // current position. This happens by completely destroying the board
    // and recreating it with new variables given based on the movement of the
    // robot and obstacles
    @FXML
    private void handleNextStep(){
        label.setText("");
        if(!fileLoaded){
            label.setText("ERROR: File Not Loaded");
            return;
        }
        else{
            
            if(!started){
                label.setText("Error: Must Start");
                return;
            }
            
            // setting a path
            path = "(" + bc.robot.lastX + "," + bc.robot.lastY + ")";
            pathAdd = "(" + bc.robot.prevX + "," + bc.robot.prevY + ")" + "-->" + path;
            
            view.getChildren().clear();
            for(int i = 1; i <= size; i++){
                //System.out.println(view.getChildren().toString());
                for(int j = 1;j <= size;j++){
                    BackgroundPiece bgPiece = new BackgroundPiece(pos, (double)(i - 1) * pos, (double)(j - 1) * pos);
                    view.getChildren().add(bgPiece);
                    board[j][i] = bgPiece;
                }
            }
            // Mark Start
            int startX = bc.startX;
            int startY = bc.startY;
            // Mark Finish
            int finX = bc.finX;
            int finY = bc.finY;

            // Add Start
            board[startX][startY].rectangle.setFill(Color.GREEN);

            // Add End
            board[finX][finY].rectangle.setFill(Color.RED);

            // to show the path the robot took
            if(bc.robot.lastX != bc.robot.endX && bc.robot.lastY != bc.robot.endY){
                //test.setText(pathAdd);
                lastLabel.setText(pathAdd);
                currLabel.setText(path);
            }

            // When Robot hits the Finish
            if(bc.robot.lastX == bc.finX && bc.robot.lastY == bc.finY){
                label.setText("DONE!");
                //test.setText(pathAdd);
                lastLabel.setText(pathAdd);
                currLabel.setText("Finished");
                fileLoaded = false;
                started = false;
                background.getChildren().add(start);
                background.getChildren().remove(next);
                return;
            }
            // Mark Robot
            int robotX = bc.robot.lastX;
            int robotY = bc.robot.lastY;
            // Mark Obstacle 1
            int obstacle1X = bc.obs1.lastX;
            int obstacle1Y = bc.obs1.lastY;
            // Mark Obstacle 2
            int obstacle2X = bc.obs2.lastX;
            int obstacle2Y = bc.obs2.lastY;

            // Add Robot
            Circle robot = new Circle(pos/3, Color.ORANGE);
            robot.setTranslateX(((double)(robotY - 1) * pos) + pos/2);
            robot.setTranslateY(((double)(robotX - 1) * pos) + pos/2);
            view.getChildren().add(robot);

            // Add Obstacle 1
            Circle obstacle1 = new Circle(pos/3, Color.BLACK);
            obstacle1.setTranslateX(((double)(obstacle1Y - 1) * pos) + pos/2);
            obstacle1.setTranslateY(((double)(obstacle1X - 1) * pos) + pos/2);
            view.getChildren().add(obstacle1);

            // Add Obstacle 2
            Circle obstacle2 = new Circle(pos/3, Color.AQUA);
            obstacle2.setTranslateX(((double)(obstacle2Y - 1) * pos) + pos/2);
            obstacle2.setTranslateY(((double)(obstacle2X - 1) * pos) + pos/2);
            view.getChildren().add(obstacle2);

            // Next Step
            bc.find_path();

            //after the first move
            firstMove = true;

        }
    }
    
    // this parses the file and checks if it is formated correctly. If there is 
    // an error then catch the exception
    private BoardController handleParseGetline(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        
        String line = null;
        
        Integer size = null;
        Integer startX = null, startY = null;
        Integer endX = null, endY = null;
        Integer obj1startX = null, obj1startY = null, obj1speed = null;
        Integer obj2startX = null, obj2startY = null, obj2speed = null;
        Integer obj1dirX = null, obj1dirY = null;
        Integer obj2dirX = null, obj2dirY = null;
        
        //first line
        line = br.readLine();
        for (char c : line.toCharArray()) {
            if(!Character.isDigit(c)) {
                throw new IOException();
            }
        }
        
        //get size of room from line
        size = Integer.parseInt(line);
        
        //second line
        line = br.readLine();
        
        // String to be scanned to find the pattern.
        String pattern;
        pattern = "\\((\\d+),(\\d+)\\)";
        
        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(line);
        if(matcher.find()) {
            startX = Integer.parseInt(matcher.group(1));
            startY = Integer.parseInt(matcher.group(2));
        } else {
            throw new IOException();
        }
        
        //third line
        line = br.readLine();
        matcher = r.matcher(line);
        if(matcher.find()) {
            endX = Integer.parseInt(matcher.group(1));
            endY = Integer.parseInt(matcher.group(2));
        } else {
            throw new IOException();
        }
        
        //fourth line
        line = br.readLine();
        matcher = r.matcher(line);
        if(matcher.find()) {
            obj1startX = Integer.parseInt(matcher.group(1));
            obj1startY = Integer.parseInt(matcher.group(2));
        } else {
            throw new IOException();
        }      
        
        //fifth line
        line = br.readLine();
        for (char c : line.toCharArray()) {
            if(!Character.isDigit(c)) {
                throw new IOException();
            }
        }
        
        obj1speed = Integer.parseInt(line);
        
        //sixth line
        //String to be scanned to find the pattern.
        line = br.readLine();
        String pattern2;
        pattern2 = "\\((\\+?)(-?)(\\d),(\\+?)(-?)(\\d)\\)";
        
        Pattern q = Pattern.compile(pattern2);
        matcher = q.matcher(line);
        
        if(matcher.find()) {
            //x value
            if(matcher.group(3).equals("0")) {
                if(!matcher.group(1).isEmpty() && !matcher.group(2).isEmpty()) {
                    throw new IOException();
                }
                obj1dirX = Integer.parseInt(matcher.group(3));
            } 
            if(matcher.group(1).equals("+")) {
                if(!matcher.group(2).isEmpty() || !matcher.group(3).equals("1")) {  
                    throw new IOException();
                } 
                obj1dirX = Integer.parseInt(matcher.group(3));
            }
            if(matcher.group(2).equals("-")) {
                if(!matcher.group(1).isEmpty() || !matcher.group(3).equals("1")) {  
                    throw new IOException();
                } 
                obj1dirX = -1 * Integer.parseInt(matcher.group(3));                
            }
            //y value
            if(matcher.group(6).equals("0")) {
                if(!matcher.group(4).isEmpty() || !matcher.group(5).isEmpty()) {
                    throw new IOException();
                }
                obj1dirY = Integer.parseInt(matcher.group(6));
            } 
            if(matcher.group(4).equals("+")) {
                if(!matcher.group(5).isEmpty() || !matcher.group(6).equals("1")) {  
                    throw new IOException();
                } 
                obj1dirY = Integer.parseInt(matcher.group(6));
            }
            if(matcher.group(5).equals("-")) {
                if(!matcher.group(4).isEmpty() || !matcher.group(6).equals("1")) {  
                    throw new IOException();
                } 
                obj1dirY = -1 * Integer.parseInt(matcher.group(6));                
            }
        } else {
            throw new IOException();
        } 
        
        //seventh line
        line = br.readLine();
        matcher = r.matcher(line);
        
        if(matcher.find()) {
            obj2startX = Integer.parseInt(matcher.group(1));
            obj2startY = Integer.parseInt(matcher.group(2));
        } else {
            throw new IOException();
        } 
        
        //eigth line
        line = br.readLine();
        for (char c : line.toCharArray()) {
            if(!Character.isDigit(c)) {
                throw new IOException();
            }
        }
        
        obj2speed = Integer.parseInt(line);
        
        //ninth line
        line = br.readLine();
        matcher = q.matcher(line);
        
        if(matcher.find()) {
            //x value
            if(matcher.group(3).equals("0")) {
                if(!matcher.group(1).isEmpty() && !matcher.group(2).isEmpty()) {
                    throw new IOException();
                }
                obj2dirX = Integer.parseInt(matcher.group(3));                
            } 
            if(matcher.group(1).equals("+")) {
                if(!matcher.group(2).isEmpty() || !matcher.group(3).equals("1")) {  
                    throw new IOException();
                } 
                obj2dirX = Integer.parseInt(matcher.group(3));
            }
            if(matcher.group(2).equals("-")) {
                if(!matcher.group(1).isEmpty() || !matcher.group(3).equals("1")) {  
                    throw new IOException();
                } 
                obj2dirX = -1 * Integer.parseInt(matcher.group(3));                
            }
            //y value
            if(matcher.group(6).equals("0")) {
                if(!matcher.group(4).isEmpty() || !matcher.group(5).isEmpty()) {
                    throw new IOException();
                }
                obj2dirY = Integer.parseInt(matcher.group(6));                
            } 
            if(matcher.group(4).equals("+")) {
                if(!matcher.group(5).isEmpty() || !matcher.group(6).equals("1")) {  
                    throw new IOException();
                } 
                obj2dirY = Integer.parseInt(matcher.group(6));
            }
            if(matcher.group(5).equals("-")) {
                if(!matcher.group(4).isEmpty() || !matcher.group(6).equals("1")) {  
                    throw new IOException();
                } 
                obj2dirY = -1 * Integer.parseInt(matcher.group(6));                
            }
        } else {
            throw new IOException();
        }
        
        /*
        System.out.println("Size = " + size);
        System.out.println("Start X = " + startX + " and start y = " + startY);
        System.out.println("End X = " + endX + " and end y = " + endY);
        System.out.println("Obj 1 start X = " + obj1startX + " and y = " + obj1startY);
        System.out.println("Obj 1 speed = " + obj1speed);
        System.out.println("Obj 2 start X = " + obj2startX + " and y = " + obj2startY);
        System.out.println("Obj 2 speed = " + obj2speed);
        System.out.println("Obj 1 dir X = " + obj1dirX + " and y = " + obj1dirY);
        System.out.println("Obj 2 dir X = " + obj2dirX + " and y = " + obj2dirY);
        */
        
        Obstacle obs1 = new Obstacle(3, size, obj1startX, obj1startY, obj1speed, obj1dirX, obj1dirY);
        Obstacle obs2 = new Obstacle(4, size, obj2startX, obj2startY, obj2speed, obj2dirX, obj2dirY);
        Robot robot = new Robot(5, size, startX, startY, endX, endY, obs1, obs2);
        BoardController bc = new BoardController(size, robot, obs1, obs2,
                startX, startY, endX, endY);
        
        
        br.close();
        
        return bc;
    }
    
    // this initializes the board by creating a double array and displays it in
    // the view
    private void initializeBoard(Integer size){
        this.size = size;
        board = new BackgroundPiece[size + 1][size + 1];

        pos = (double)viewSize/size;
        
        for(int i = 1; i <= size; i++){
            //System.out.println(view.getChildren().toString());
            for(int j = 1;j <= size;j++){
                BackgroundPiece bgPiece = new BackgroundPiece(pos, (double)(i - 1) * pos, (double)(j - 1) * pos);
                view.getChildren().add(bgPiece);
                board[j][i] = bgPiece;
            }
        }        
    }
}
