/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jacob.chat.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.image.Image;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 * @author jhoel
 */
public class JavaChat extends Application {
    
    Stage window;
    
    static String username;
    
    
    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Application started successfully");
        window = stage;
        // Set function to be called on window close
        
        try {
            // Get username via a sign in page
            System.out.println("Displaying Sign in window");
            username = SignIn.display();
            
            System.out.println("Retrieving username");
            if(username != null) {
                System.out.println("Retrieved username successfully");
            } else {
                System.out.println("ERROR: Could not retrieve username, closing application");
                throw new IllegalArgumentException("User not signed in");
            }
            
            // Load FXML for sign in page
            Parent root = FXMLLoader.load(getClass().getResource("Chat.fxml"));
            
            // Create Scene based on the FXML
            Scene scene = new Scene(root);
            
            // On window close clean up, and end processes (Specifically for the server listening thread)
            window.setOnCloseRequest(e -> {
                    ChatController.close();
            });
            
            // Try to get application icon, display error and continue processing if it is not available
            System.out.println("Attempting to get application icon");
            try {
                window.getIcons().add(new Image(this.getClass().getResourceAsStream("com/jacob/chat/images/logo.jpg")));
                System.out.println("Application icon successfully loaded");
            } catch(NullPointerException npe) {
                System.out.println("ERROR: Could not load application icon");
            }
            
            // Add scene to the window.
            window.setScene(scene);
            // Show window
            window.show();
        } catch (IllegalArgumentException iae) {
            System.out.println("ERROR: User not signed in");
            
            window.close();
        } catch (Exception e) {
            System.out.println("ERROR: Something went wrong when signing in");
            e.printStackTrace();
            window.close();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Starting Application");
        launch(args);
    }
    
}
