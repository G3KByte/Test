/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javachat;

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
public class Client {
    public BufferedReader in;
    public PrintWriter out;
    
    public String serverIP = "192.168.14.172";
    private String name;
    private TextArea chatAreaTxt;
    
    public Client(String name, TextArea chatAreaTxt) {
        this.name = name;
        this.chatAreaTxt = chatAreaTxt;
    }
    
    public void sendMsg(String Msg) {
        out.println(Msg);
    }
    
    private String getName() {
        return this.name;
    }
    
    public void run() throws IOException {
        // Make connection and initialize streams
        Socket socket = new Socket("192.168.14.172", 9005);
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
        // Process all messages from server, according to the protocol.
        while (true) {
            String line = in.readLine();
			
			
            if (line.startsWith("SUBMITNAME")) {
                out.println(getName());
            } else if (line.startsWith("NAMEACCEPTED")) {
                System.out.println("Successfully Connected");
            } else if (line.startsWith("MESSAGE")) {
                chatAreaTxt.appendText(line.substring(8) + "\n");
            } else if (line.startsWith("BROADCAST")) {
                chatAreaTxt.appendText( "Message From Server: " + line.substring(10) + "\n" );
            }
        }
    }
}
