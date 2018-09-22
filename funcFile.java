//
// CS342-Project5
// funcFIle file
// Discription: FuncFile Class to get TWO prime numbers automatically from file
//

// DIfferent libraries
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.Scanner;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigInteger;

// funcFile class
public class funcFile {

 // initialize fileVals array as BigIntegers
 public static BigInteger fileVals[] = new BigInteger[10];

 //OpenFile void function with FileNotFoundExeption
 public static void openFile(JFileChooser fc) throws FileNotFoundException {
  fc = new JFileChooser();
  int value = fc.showOpenDialog(null);
     
     
  //if value is approved
  if (value == JFileChooser.APPROVE_OPTION) {
   File selectedFile = fc.getSelectedFile();
   @SuppressWarnings("resource")
   Scanner input = new Scanner(selectedFile);
   int i = 0;
   while (input.hasNext()) {
    String nextLine = input.nextLine();
    fileVals[i] = new BigInteger(nextLine);
    i++;
   }//end of the while loop
  }//end of the if statement
     
     
  int rand1 = 0 + (int) (Math.random() * 9);
  int rand2 = 0 + (int) (Math.random() * 9);

  //for loop o generate two random prime number from file
  for (int k = 0; k < 10; k++) {
   if (rand1 == k) {
    Networked_Client.b1 = fileVals[k];
   }
   if (rand2 == k) {
    Networked_Client.b2 = fileVals[k];
   }

  } //end of the for loop

 }//end of the void method
}//end of the funcFile Class
