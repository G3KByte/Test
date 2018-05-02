/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jacob.chat.client;

import java.net.URL;

import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.*;

import com.jacob.ldap.LDAP;
import com.unboundid.ldap.sdk.*;

/**
 * FXML Controller class
 *
 * @author jhoel
 */
public class ChatSignInController implements Initializable {
    
    @FXML
    private StackPane mainPane;
    
    @FXML
    private TextField txtUsername;
    
    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private Label errorLbl;
    
    private LDAP ldap;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ldap = new LDAP();
    }
    
    @FXML
    private void handleSignInBtn(ActionEvent event) throws Exception {
        errorLbl.setText("");
        
        System.out.println("Attempting to authenticate the user");
        
        if(ldap.authenticate(txtUsername.getText(), txtPassword.getText()) == true) {
            System.out.println("Successfully authenticated as "+ldap.getLoginDn(txtUsername.getText()));
            errorLbl.setText("Successfully authenticated");
            
            System.out.println("Closing Sign In window");
            // Load Node from the action event
            Node source = (Node)event.getSource();
            // Get the primary stage from the node
            Stage window = (Stage) source.getScene().getWindow();
            // Load FXML for sign in page
            //Parent root = FXMLLoader.load(getClass().getResource("Chat.fxml"));
            // Create scene from FXML
            //Scene chatScene = new Scene(root);
            // Load Scene into primary window
            //window.setScene(chatScene);
            
            SignIn.username = txtUsername.getText();
            
            window.close();
        } else {
            errorLbl.setText("Authentication failed, try again");
            System.out.println("WARNING: Could not authenticate user");
        }
    }
}
