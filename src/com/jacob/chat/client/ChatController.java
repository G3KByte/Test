/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jacob.chat.client;

import javafx.scene.paint.Color;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.stage.Stage;

import java.util.HashSet;

import java.io.IOException;

import java.io.File;
import javafx.geometry.Insets;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;

import javafx.scene.paint.Paint;

import java.util.List;
/**
 *
 * @author jhoel
 */
public class ChatController implements Initializable {
    private static Client client;
    
    @FXML
    private BorderPane rootPane;
    @FXML
    private TextArea chatTxtArea;
    @FXML
    private TextArea usersTxtArea;
    @FXML
    private TextField chatMsgTxt;
    @FXML
    private Button sendMsgBtn;
    
    @FXML
    private void handleEnterKey(KeyEvent e) {
        if(e.getCode() == KeyCode.ENTER) {
            this.sendMsgBtn.fire();
        }
    }
    
    @FXML
    private void handleSendMsg(ActionEvent event) {
        // Check to ensure user entered text
        if(!chatMsgTxt.getText().equals("")) {
            String msg = chatMsgTxt.getText();
            chatMsgTxt.setText("");
            client.sendMsg(msg);
        } else {
            System.out.println("ERROR: No message entered");
        }
    }
    
    @FXML
    private void handleDragOver(DragEvent event) {
        /* data is dragged over the target */
        /* accept it only if it is not dragged from the same node 
         * and if it has a string data */
        if (event.getGestureSource() != rootPane &&
                event.getDragboard().hasFiles()) {
            /* allow for both copying and moving, whatever user chooses */
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        
        event.consume();
    }
    
    @FXML
    private void handleDrop(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
            
        //System.out.println("Content: "+db.hasContent());
        System.out.println("Files: "+db.hasFiles());
        System.out.println("Html: "+db.hasHtml());
        System.out.println("Image: "+db.hasImage());
        System.out.println("Rtf: "+db.hasRtf());
        System.out.println("String: "+db.hasString());
        System.out.println("Url: "+db.hasUrl());

        //System.out.println(db.getFiles().get(0).getPath());

        if (db.hasFiles()) {
            List<File> files = db.getFiles();
            
            for(File f : files) {
                System.out.println("Dropped: " + f.getPath());
            }
            
            success = true;
        }

        event.setDropCompleted(success);
        event.consume();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        client = new Client(JavaChat.username, chatTxtArea, usersTxtArea);
    }
    
    @Override
    protected void finalize() throws Throwable {
        client.finalize();
    }
    
    public static void close() {
        try {
            client.finalize();
        } catch(Throwable e) {
            System.out.println("Error Closing Chat");
        }
    }
}
