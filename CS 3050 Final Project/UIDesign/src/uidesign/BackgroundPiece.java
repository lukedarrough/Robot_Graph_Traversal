/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uidesign;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Jeremy
 */
public class BackgroundPiece extends StackPane{
    
    public Rectangle rectangle;
    
            
    double size;
    
    // this creates the board by creating rectangles based on the number given
    // from the file
    public BackgroundPiece(double size, double x, double y){
        rectangle = new Rectangle(size, size);
        setPrefWidth(size);
        setPrefHeight(size);
        rectangle.setFill(Color.LIGHTGRAY);
        rectangle.setStroke(Color.BLACK);
        getChildren().add(rectangle);
        this.setTranslateX(x);
        this.setTranslateY(y);
    }
}
