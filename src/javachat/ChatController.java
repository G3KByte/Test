/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javachat;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.util.HashSet;

import java.io.IOException;

import javachat.*;

/**
 *
 * @author jhoel
 */
public class ChatController implements Initializable {
    
    @FXML
    private TextField userTxt;
    @FXML
    private PasswordField passTxt;
    @FXML
    private TextArea chatTxtArea;
    @FXML
    private TextArea usersTxtArea;
    @FXML
    private TextField chatMsgTxt;
    
    private Client client;
    
    private static Thread thread;
    @FXML
    private Button signOnBtn;
    
    @FXML
    private void handleSignOn(ActionEvent event) {
        System.out.println("You clicked me!");
        
        client = new Client(userTxt.getText(), chatTxtArea);
        
        Runnable serverListener = () -> { 
            try {
                client.run();
            } catch(IOException e) {
                e.printStackTrace();
            }
        };
        
        thread = new Thread(serverListener);
        
        thread.start();
    }
    
    @FXML
    private void handleEnterKey(KeyEvent e) {
        if(e.getCode() == KeyCode.ENTER) {
            String msg = chatMsgTxt.getText();
            chatMsgTxt.setText("");
            client.sendMsg(msg);
        }
    }
    
    @FXML
    private void handleSendMsg(ActionEvent event) {
        String msg = chatMsgTxt.getText();
        chatMsgTxt.setText("");
        client.sendMsg(msg);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    
    public static void closeProgram() {
        thread.stop();
    }
}
