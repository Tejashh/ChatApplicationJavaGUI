//
// CS342-Project5
// Networked_Server file
// Discription: Networked_Server file with creates the server with ip address and port numbers with will
// allow different conncetion with multiple clients to send key Also does dycrition
//

// Different libraries

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Networked_Server extends JFrame {

 // GUI items
 JButton ssButton;
 JLabel machineInfo;
 JLabel portInfo;
 JTextArea history;
 JTextArea clientList;
 private boolean running;

 // Network Items
 boolean serverContinue;
 ServerSocket serverSocket;
 Vector<PrintWriter> outStreamList;
 
 ArrayList<String>names = new ArrayList<String>();
 // set up GUI
 public Networked_Server() {
  super("Echo Server");

  // set up the shared outStreamList
  outStreamList = new Vector<PrintWriter>();

  // get content pane and set its layout
  Container container = getContentPane();
  container.setLayout(new FlowLayout());

  // create buttons
  running = false;
  ssButton = new JButton("Start Listening");
  ssButton.addActionListener(e -> doButton(e));
  container.add(ssButton);

  String machineAddress = null;
  try {
   InetAddress addr = InetAddress.getLocalHost();
   machineAddress = addr.getHostAddress();
  } catch (UnknownHostException e) {
   machineAddress = "127.0.0.1";
  }
  machineInfo = new JLabel(machineAddress);
  container.add(machineInfo);
  portInfo = new JLabel(" Not Listening ");
  container.add(portInfo);

  history = new JTextArea(10, 40);
  history.setEditable(false);
  history.insert("SERVER'S MESSAGE BOX" + "\n", 0);

  container.add(new JScrollPane(history));
  
  clientList = new JTextArea(10,20 );
  clientList.setEditable(false);
  clientList.insert("CLIENT'S LIST" + "\n", 0);
  container.add(new JScrollPane(clientList));

  setSize(800, 270);
  setVisible(true);

 } // end CountDown constructor

 public static void main(String args[]) {
  Networked_Server application = new Networked_Server();
  application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 }

 // handle button event
 public void doButton(ActionEvent event) {
  if (running == false) {
   new ConnectionThread(this);
  } else {
   serverContinue = false;
   ssButton.setText("Server Started");
   portInfo.setText("Sever is Turned off ");
  }
 }

} // end class EchoServer4

class ConnectionThread extends Thread {
 Networked_Server gui;

 public ConnectionThread(Networked_Server es3) {
  gui = es3;
  start();
 }

 public void run() {
  gui.serverContinue = true;

  try {
   gui.serverSocket = new ServerSocket(0);
   gui.portInfo.setText("Listening on Port: " + gui.serverSocket.getLocalPort());
   System.out.println("Connection Socket Created");
   try {
    while (gui.serverContinue) {
     System.out.println("Waiting for Connection");
     gui.ssButton.setText("Stop Listening");
     new CommunicationThread(gui.serverSocket.accept(), gui, gui.outStreamList);
    }
   } catch (IOException e) {
    System.err.println("Accept failed.");
    System.exit(1);
   }
  } catch (IOException e) {
   System.err.println("Could not listen on port: 10008.");
   System.exit(1);
  } finally {
   try {
    gui.serverSocket.close();
   } catch (IOException e) {
    System.err.println("Could not close port: 10008.");
    System.exit(1);
   }
  }
 }
}

class CommunicationThread extends Thread {
 // private boolean serverContinue = true;
 private Socket clientSocket;
 private Networked_Server gui;
 private Vector<PrintWriter> outStreamList;
 
 
 // prompt the user to enter their name
 

 public CommunicationThread(Socket clientSoc, Networked_Server ec3, Vector<PrintWriter> oSL) {
  clientSocket = clientSoc;
  gui = ec3;
  outStreamList = oSL;
  //gui.history.insert("A new client named: " + " just came online." + " with public key of ( "+ e + ", " + n + " )\n" , 0);
  
  //gui.clientList.insert(name, 1);
  //ClientNames.add(name);

  start();
 }

 public void run() {
  System.out.println("New Communication Thread Started");

  try {
   PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
   outStreamList.add(out);

   BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

   String inputLine;
   
   String inputLineClient = in.readLine();
   
   // get the user info and parse it and display it on the history bar.
   String[] clientInfo = inputLineClient.split("/");
   gui.clientList.insert(clientInfo[0] + ": Key: ("+ clientInfo[1] + "," + clientInfo[2] + ")\n", 0);
   gui.history.insert(clientInfo[0] + " joined the chatroom with a key of ("+ clientInfo[1] + "," + clientInfo[2] + ")\n", 0);   //display the gui info
   
   //add names to the online list
   gui.names.add(clientInfo[0]);
   
   //sanitiy check
   System.out.println(inputLineClient);
   
   //while the the input line is null
   while ((inputLine = in.readLine()) != null) {
    System.out.println(inputLine);
    
    //Dipslay the the encryptes message to the server 
    gui.history.insert(clientInfo[0] + ": " + inputLine + "\n", 0);
    
    
    //start decrypting the the text
    String[] encryptedNumbers = inputLine.split(",");
    long[] Todecrypt = new long[encryptedNumbers.length];
    
    //put the encrypted bigInteger to the a array
    for(int i = 0; i< encryptedNumbers.length; i++) {
     
     Todecrypt[i] = Long.parseLong(encryptedNumbers[i]);
    }//end of forr
    
    
    //make an array list to input the asscii values of the encrypted 
    //message to a array list 
    ArrayList<Long> decrptedMssg = new ArrayList<Long>();
    
    for(int i = 0; i < Todecrypt.length ; i++) {
     
     long val1 = Todecrypt[i] / (long)(Math.pow(128, 3)); //put the last value of the decypted ascci value to the array list
     decrptedMssg.add(val1) ;   //108. ....
     
     long difference2 = Todecrypt[i] - (val1 * (long)Math.pow(128, 3));  //second last char assccii value
     long val2 = (difference2) / (long)Math.pow(128, 2);
     decrptedMssg.add(val2);
     
     long difference3 = difference2 - (val2 * (long)Math.pow(128, 2));
     long val3 = (difference3) / (long)Math.pow(128, 1);
     decrptedMssg.add(val3);
     
     long val4 = difference3 - (val3* 128);
     decrptedMssg.add(val4);
 
    }// end of for loop
    
    
    //as the array list is in the reverse order you need to make it straight and then send to the clients.
    ArrayList<Long> finaldec = new ArrayList<Long>();

    for (int i = 0; i < decrptedMssg.size(); i = i + 4) {
     
     for (int j = i+3; j >= i ; j--) {

      finaldec.add(decrptedMssg.get(j));    
     }
    } 
    
    
    System.out.println(finaldec);
    
    String decryptedText = "";
    
    for(int i = 0; i< finaldec.size(); i++) {
     
     long l = finaldec.get(i); // Take charactors from the String
     char c = (char)l; 
     
     decryptedText += c;
    }
    
    System.out.println(decryptedText);

    
    // Loop through the outStreamList and send to all "active" streams
    // out.println(inputLine);
    for (PrintWriter out1 : outStreamList) {
     System.out.println("Sending Message");
     
     out1.println(clientInfo[0] + ": " + decryptedText);
    }
    
    if ( decryptedText.equals("Show online.")){
      
      String display = "";
      
      for (String n : gui.names){
            display += n + "\n"; 
      }
      System.out.println(display);
       JOptionPane.showMessageDialog(null,
          "Online users:\n" + display, null,
          JOptionPane.PLAIN_MESSAGE);
    }
    
    if (decryptedText.equals("Bye.")) {
      
      gui.names.remove(clientInfo[0]);
        break;
    }
    
    if (decryptedText.equals("End Server."))
     gui.serverContinue = false;
    
   }

   outStreamList.remove(out);
   out.close();
   in.close();
   clientSocket.close();
  } catch (IOException e) {
   System.err.println("Problem with Communication Server");
   // System.exit(1);
  }
 }
}