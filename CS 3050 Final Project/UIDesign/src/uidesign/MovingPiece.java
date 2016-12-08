/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uidesign;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author Jeremy
 */
public class MovingPiece extends StackPane{
    int type;
    double radius;
    
    private Circle circle;
    // type 1 --> Obstacle 1
    // type 2 --> obstacle 2
    // type 3 --> Robot
    
    public MovingPiece(double radius, int type){
        this.type = type;
        this.radius = radius;
        radius = radius/2;
        
        if(type == 1){
            circle = new Circle();
            setPrefWidth(radius);
            setPrefHeight(radius);
            circle.setFill(Color.BLACK);
            getChildren().add(circle);
        }
        if(type == 2){
            circle = new Circle();
            circle.setFill(Color.BLACK);    
        }
        if(type == 3){
            circle = new Circle();
            circle.setFill(Color.WHITE);
        }
    }
}
