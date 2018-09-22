//
////
// CS342-Project5
// Networked_Client.java
// Discription: Networked_Client file which sends key to server ALSO does encryption.
//

//Different libraries
import java.net.*;
import java.util.Random;
import java.io.*;
import java.math.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class Networked_Client extends JFrame implements ActionListener {
 // GUI items
 JButton sendButton;
 JButton connectButton;
 JButton manualButton;
 JButton autoButton;
 JTextField machineInfo;
 JTextField portInfo;
 JTextField message;
 JTextArea history;
 JTextArea ClientList;

 // Network Items
 boolean connected;
 Socket echoSocket;
 PrintWriter out;
 BufferedReader in;

 // Client GUI Items
 private JMenu fileMenu;
 private JMenu helpMenu;

 // set up GUI
 public static long v1;
 public static long v2;
 public static BigInteger b1;
 public static BigInteger b2;
 
 public static int connectNum;
 public static int primeFlag = 0;

 JFileChooser fc = new JFileChooser();
 public BigInteger n;
 public BigInteger e;
 public BigInteger phi;
 public BigInteger k = BigInteger.valueOf(2);
 private BigInteger d;

 public static Client_info clientInfo = new Client_info();

 public Networked_Client() {
  super("Echo Client");

  JMenuBar bar = new JMenuBar();
  setJMenuBar(bar);
  fileMenu = new JMenu("File");

  JMenuItem aboutItem = new JMenuItem("About");
  aboutItem.addActionListener(

    new ActionListener() {
     @Override
     public void actionPerformed(ActionEvent event) {
      JOptionPane.showMessageDialog(null,
        " CS 342: Project5 - Networked Chat\n Authors: Foram Patel\n   Harshil Patel\n Tejash Contractor",
        null, JOptionPane.PLAIN_MESSAGE);
     }
    });
  fileMenu.add(aboutItem);

  JMenuItem exit = new JMenuItem("Exit"); // Exit JMenuItem
  exit.setMnemonic('X');
  fileMenu.add(exit); // exit menuOptions to sudoku

  exit.addActionListener(new ActionListener() {
   // exit buttons actionPerformed
   public void actionPerformed(ActionEvent e) {
    System.exit(0);
   }
  }); // end of actionListner

  bar.add(fileMenu);
  
   //HelpMenu JMenu
  helpMenu = new JMenu("Help");

 //SendMess JMenuItem
  JMenuItem sendMess = new JMenuItem("App usage steps");
  sendMess.addActionListener(new ActionListener() {
   // exit buttons actionPerformed
   public void actionPerformed(ActionEvent e) {
    JOptionPane.showMessageDialog(null,
          "How to Connect: start listening on server with will give Server Address and port number\n" +
           "Run Networked_Client multiple times to get multiple users\n" +
           "Add Server Address and Server Port to each users and You need to add Prime Numbers first and Click on Connect\n" +
           "Typing Show online. will show who is currently online\n " +
          "Typing Bye. will end the Chat for that person by breaking communication between server\n" +
           "Press the disconnect button to disconect",
          "How to Use the APP", JOptionPane.PLAIN_MESSAGE);
   }
  }); // end of actionListner

  helpMenu.add(sendMess);

  // EncrypDecryp JMenuItem
  JMenuItem encrypDecryp = new JMenuItem("Safety of the Messages");
  encrypDecryp.addActionListener(new ActionListener() {
   // exit buttons actionPerformed
   public void actionPerformed(ActionEvent e) {
    JOptionPane.showMessageDialog(null,
          "We are using RSA encryption decryption with two keys for the protection towards sending messages",
          "Safety", JOptionPane.PLAIN_MESSAGE);
   }
  }); // end of actionListner

  helpMenu.add(encrypDecryp);
  bar.add(helpMenu);

  // get content pane and set its layout
  Container container = getContentPane();
  container.setLayout(new BorderLayout());

  // set up the North panel
  JPanel upperPanel = new JPanel();
  upperPanel.setLayout(new GridLayout(4, 1));
  container.add(upperPanel, BorderLayout.NORTH);

  // create buttons
  connected = false;

  upperPanel.add(new JLabel("Server Address: ", JLabel.CENTER));
  machineInfo = new JTextField("192.168.2.11");
  upperPanel.add(machineInfo);

  upperPanel.add(new JLabel("Server Port: ", JLabel.CENTER));
  portInfo = new JTextField("");
  upperPanel.add(portInfo);

  manualButton = new JButton("Add Prime Numbers Manually");
  manualButton.addActionListener(

    new ActionListener() {

     @Override
     public void actionPerformed(ActionEvent event) {
      // TODO Auto-generated method stub
      JTextField prime1 = new JTextField();
      JTextField prime2 = new JTextField();

      Object[] text = { "Enter your first prime number(p > 999999): ", prime1,
        "Enter your second prime number(q > 999999): ", prime2 };
      int ok = JOptionPane.showConfirmDialog(Networked_Client.this, text, "Enter your values: ",
        JOptionPane.OK_CANCEL_OPTION);
      Boolean flag;
      Boolean flag2;

      if (ok == JOptionPane.OK_OPTION) {

       // store teh value of prime1 and prime2 into val1 and val2
       String val1 = prime1.getText();
       String val2 = prime2.getText();

       // **************************************************************CHNAGE FROM
       // LONG TO BIG**************************************************
       // Convert String to long from val1 to v1 and val2 to v2
       v1 = Long.parseLong(val1);
       v2 = Long.parseLong(val2);

       // convert to big integer

       b1 = new BigInteger(val1);
       b2 = new BigInteger(val2);

       System.out.println("Big Integer are: " + b1 + "," + b2);

       // check is the numbers are prime or not
       flag = b1.isProbablePrime(1);
       flag2 = b2.isProbablePrime(7);

       System.out.println("Is prime: " + b1 + ":" + flag + ", " + b2 + ":" + flag2);

       // when second prime input is not prime number
       if (flag == true && flag2 == false) {
        JOptionPane.showMessageDialog(Networked_Client.this,
          "Second input is not a Prime number. Please Try Again!!", "Error",
          JOptionPane.PLAIN_MESSAGE);
       }
       // when first prime input is not prime number
       else if (flag == false && flag2 == true) {
        JOptionPane.showMessageDialog(Networked_Client.this,
          " First input is not a Prime number. Please Try Again!!", "Error",
          JOptionPane.PLAIN_MESSAGE);
       }
       // when both numbers are not prime
       else if (flag == false && flag2 == false) {
        JOptionPane.showMessageDialog(Networked_Client.this,
          "Both numbers are not Prime numbers. Please Try Again!!", "Error",
          JOptionPane.PLAIN_MESSAGE);
       } else {
        if (flag == true && flag2 == true) {
         // to do:
         performCalc();
         primeFlag = 1;
        }
       }
      }
     }
    });
  upperPanel.add(manualButton);

  autoButton = new JButton("Add Prime Numbers Automatically");
  autoButton.addActionListener(

    new ActionListener() {
     @Override
     public void actionPerformed(ActionEvent e) {
      // TODO Auto-generated method stub
      try {

       funcFile.openFile(fc);

       if ((!b1.equals(BigInteger.ZERO) ) && (!b2.equals(BigInteger.ZERO))) {
        JOptionPane.showMessageDialog(null,
          "The two prime numbers picked for you are:\n 1)" + b1 + " 2)" + b2, null,
          JOptionPane.PLAIN_MESSAGE);
          primeFlag = 1;
          performCalc();
       }
      } catch (FileNotFoundException e1) {
       // TODO Auto-generated catch block
       e1.printStackTrace();
      }
     }
    });
  upperPanel.add(autoButton);

  upperPanel.add(new JLabel("Server Connection ", JLabel.CENTER));
  connectButton = new JButton("Connect");
  connectButton.addActionListener(this);
  upperPanel.add(connectButton);
  
  JPanel lowerPanel = new JPanel();
  lowerPanel.setLayout(new GridLayout(3, 1));
  container.add(lowerPanel, BorderLayout.SOUTH);

  lowerPanel.add(new JLabel("Message: ( To see who's online type 'Show online.' without the quotes. :-) )", JLabel.LEFT));
  message = new JTextField("");
  message.addActionListener(this);
  lowerPanel.add(message);

  sendButton = new JButton("Send Message");
  sendButton.addActionListener(this);
  sendButton.setEnabled(false);
  lowerPanel.add(sendButton);

  history = new JTextArea(10, 40);
  history.setEditable(false);
  container.add(new JScrollPane(history), BorderLayout.CENTER);

  setSize(600, 500);
  setVisible(true);

 } // end CountDown constructor

 public static void main(String args[]) {
  Networked_Client application = new Networked_Client();
  application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 }

 // http://www.baeldung.com/java-generate-random-long-float-integer-double

 public static BigInteger randomGenerator(BigInteger n) {
  // BigInteger max = n;
  // BigInteger min = BigInteger.ONE;
  // BigInteger generatedLong = min.add(((max.subtract(min))));
  BigInteger generatedLong = new BigInteger(n.bitLength(), new Random());
  return generatedLong;
 }

 // handle button event
 public void actionPerformed(ActionEvent event) {

  if (connected && (event.getSource() == sendButton || event.getSource() == message)) {
   doSendMessage();
  } else if (event.getSource() == connectButton) {
   
    if ( primeFlag == 1){ 
     
     connectNum++;
    if ( connectNum == 1){
     String name = JOptionPane.showInputDialog("Please enter your name?");
   
    System.out.println(name);
     String eVal = "" + e;
     String nVal = "" + n;

    String client = name + "/" + eVal + "/" + nVal + "/";
    doManageConnection();
    out.println(client);
    }
    else if (connectNum == 2){
        out.println("Bye.");
       System.exit(0);
    }
  }
    else{
        JOptionPane.showMessageDialog(null,
          "You first need to add the prime numbers from the above options." , null,
          JOptionPane.PLAIN_MESSAGE);
    }
  }
 }

 // doSendMessage method
 public void doSendMessage() {
  try {

   long sum2 = 0; // initialize sum2 as long

   String ToEncrypt = message.getText(); // String to be Encrypted

   // if the length of the string is less than 4 and adjust the size accordingly.
   int length = ToEncrypt.length(); // get the length of the string
   int remainder = length % 4; // get by how much is the string short.
   if (length % 4 != 0) { // if the string is not a multile of 4

    int add = 4 - remainder; // add the requirement to make the string length such that
    // it is a multiple of four
    if (add == 1) {
     ToEncrypt = ToEncrypt + " ";
    }
    if (add == 2) {
     ToEncrypt = ToEncrypt + "  ";
    }
    if (add == 3) {
     ToEncrypt = ToEncrypt + "   ";
    }

   } // end of if

   ArrayList<Long> EncryptedNumber = new ArrayList<Long>();

   // start encryting the string
   for (int i = 0; i < ToEncrypt.length(); i = i + 4) {
    int k = 0;
    sum2 = 0;
    for (int j = i; j < i + 4; j++) {

     char ch = ToEncrypt.charAt(j); // Take charactors from the String

     sum2 = sum2 + (long) (ch * (Math.pow(128, k))); // Adding the encrypted chunk to sum2
     k++; // increment k
    }

    EncryptedNumber.add(sum2);

   } // end of encrypting a block.

   String encryptedText = new String();
   for (long l : EncryptedNumber) {

    encryptedText += Long.toString(l) + ",";

   }

   out.println(encryptedText);

   // history.insert ("From Server: " + in.readLine() + "\n" , 0);
  } catch (Exception e) {
   history.insert("Error in processing message ", 0);
  }
 }

 public void performCalc() {
  n = b1.multiply(b2); // calculating value on n

  System.out.println("n: " + n);

  // Do-while loop to calculate the random Value of e
  // which is less then value of n and odd number which is equally divisible
  // by n and e
  do {
   e = randomGenerator(n);
  } while (!n.mod(e).equals(BigInteger.ZERO) && !e.mod(new BigInteger("2")).equals(BigInteger.ZERO));

  System.out.println("e: " + e);

  // Calculate the value of phi
  phi = (b1.subtract(BigInteger.ONE)).multiply(b1.subtract(BigInteger.ONE));
  System.out.println("phi: " + phi);

  // Calculate the value of d
  d = ((k.multiply(phi)).add(BigInteger.valueOf(1))).divide(e);
  System.out.println("d: " + d);

 }

 public void doManageConnection() {
  if (connected == false) {
   String machineName = null;
   int portNum = -1;
   try {
    machineName = machineInfo.getText();
    portNum = Integer.parseInt(portInfo.getText());
    echoSocket = new Socket(machineName, portNum);
    out = new PrintWriter(echoSocket.getOutputStream(), true);
    in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

    // start a new thread to read from the socket
    new CommunicationReadThread(in, this);

    sendButton.setEnabled(true);
    connected = true;
    connectButton.setText("Disconnect from Server");
   } catch (NumberFormatException e) {
    history.insert("Server Port must be an integer\n", 0);
   } catch (UnknownHostException e) {
    history.insert("Don't know about host: " + machineName, 0);
   } catch (IOException e) {
    history.insert("Couldn't get I/O for " + "the connection to: " + machineName, 0);
   }

  } else {
   try {
    out.close();
    in.close();
    echoSocket.close();
    sendButton.setEnabled(false);
    connected = false;
    connectButton.setText("Connect to Server");
   } catch (IOException e) {
    history.insert("Error in closing down Socket ", 0);
   }
  }

 }

} // end class EchoServer3

// Class to handle socket reads
// THis class is NOT written as a nested class, but perhaps it should
class CommunicationReadThread extends Thread {
 // private Socket clientSocket;
 private Networked_Client gui;
 private BufferedReader in;

 public CommunicationReadThread(BufferedReader inparam, Networked_Client ec3) {
  in = inparam;
  gui = ec3;
  start();
  gui.history.insert("Communicating with Port\n", 0);

 }

 public void run() {

  try {

   String inputLine;

   while ((inputLine = in.readLine()) != null) {
    // history.insert ("From Server: " + in.readLine() + "\n" , 0);
    
    System.out.println(inputLine);
    gui.history.insert(inputLine + "\n", 0);

    if (inputLine.equals("Bye."))
     break;

   }

   in.close();
   // clientSocket.close();
  } catch (IOException e) {
   System.err.println("Problem with Client Read");
   // System.exit(1);
  }
 }
}
