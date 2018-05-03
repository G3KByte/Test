/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jacob.chat.client;

import javafx.fxml.FXMLLoader;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.io.IOException;
import javafx.scene.image.Image;

/**
 *
 * @author jhoel
 */
public class SignIn {
    private static Stage window;
    
    static String username;
    
    public static String display() throws IOException {
        System.out.println("Building user sign in page");
        // Load FXML for sign in page
        Parent root = FXMLLoader.load(SignIn.class.getClassLoader().getResource("com/jacob/chat/client/ChatSignIn.fxml"));
        
        System.out.println("Loading user sign in page into GUI");
        window = new Stage();
        
        // Create Scene
        Scene scene = new Scene(root);
        System.out.println("User sign in page successfully loaded");
        
        window.setOnCloseRequest(e -> closeWindow());
        
        // Try to get application icon, display error and continue processing if it is not available
        System.out.println("Attempting to get application icon");
        try {
            window.getIcons().add(new Image(SignIn.class.getClassLoader().getResourceAsStream("com/jacob/chat/images/globe_logo.png")));
            System.out.println("Application icon successfully loaded");
        } catch(NullPointerException npe) {
            System.out.println("ERROR: Could not load application icon");
        }
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("RCN Chat - Sign In");
        System.out.println("Displaying Sign in window");
        window.setScene(scene);
        window.showAndWait();
        
        System.out.println("Sign in window closed");
        return username;
    }
    
    public static void closeWindow() {
        
    }
}
