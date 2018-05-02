/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jacob.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.scene.control.*;

/**
 *
 * @author jhoel
 */
public class Client implements Runnable {
    public BufferedReader in;
    public PrintWriter out;
    
    // Thread for listening to server messages
    private static Thread srvrListener;
    // Information for connecting to the server via a socket connection
    public static String serverIP = "192.168.14.172";
    public static int port = 15658;
    
    private String username;
    private TextArea chatTxtArea;
    
    private Socket socket;
    
    public Client(String username, TextArea chatTxtArea, TextArea usersTxtArea) {
        this.username = username;
        this.chatTxtArea = chatTxtArea;
        
        // Create a runnable Interface to hand to an interface
        Runnable serverListener = () -> { 
            
        };
        
        // Create the thread
        srvrListener = new Thread(this);
        // Start the thread
        srvrListener.start();
    }
    
    public void sendMsg(String Msg) {
        out.println(Msg);
    }
    
    @Override
    public void run() {
        try {
            System.out.println("Establishing Connection to chat server");
            // Make connection and initialize in/out streams
            socket = new Socket(serverIP, port);
            System.out.println("Connection to chat server initialized on "+serverIP+":"+port);
            
            this.in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);

            // Process all messages from server, according to the protocol.
            while (true) {
                String line = in.readLine();


                if (line.startsWith("SUBMITNAME")) {
                    out.println(this.username);
                } else if (line.startsWith("NAMEACCEPTED")) {
                    System.out.println("Server confirmed initialization, and connection has been established");
                } else if (line.startsWith("MESSAGE")) {
                    chatTxtArea.appendText(line.substring(8) + "\n");
                } else if (line.startsWith("BROADCAST")) {
                    chatTxtArea.appendText( "Message From Server: " + line.substring(10) + "\n" );
                }
            }
        } catch(IOException e) {
            System.out.println("ERROR: Could not communicate with chat server "+serverIP+":"+port);
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        socket.close();
    }
}
