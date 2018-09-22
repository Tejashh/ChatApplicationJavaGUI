//
// CS342-Project5
// Client_info file
// Discription: Client Info file which does getter-setter work for Client.
//



// DIfferent libraries
import java.net.*;
import java.util.Random;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

//Client info class
public class Client_info {

    // valiable which should be use for get set
	public String Client_Name;
	public long eVal;
	public long nVal;
	
    // getName method with the return of Clients name
	public String getName() {
		return  this.Client_Name;
	}//end of the getName method
	
    // get_eVal method with the return of eVal
	public long get_eVal() {
		return this.eVal;
	}//end ofthe get_eVal method
	
    // get_nVal method with the return of nVal
	public long get_nVal() {
		return this.nVal;
	}//end of the get_nVal method
	
    //
    //setName method with takes ClientName string as parameter and set it
    //to the Client_Name public string
    //
	public void setName(String ClientName) {
		Client_Name = ClientName;
	}//end of the set Name method
	
    
    //
    //set_eVal method with takes e long as parameter and set it
    //to the eVal public long
    //
	public void set_eVal(long e) {
		eVal = e ;
	}// end of the set_eVal method
	
    //
    //set_nVal method with takes n long as parameter and set it
    //to the nVal public long
    //
	public void set_nVal(long n) {
		nVal = n;
	}//end of the set_nVal method
	
	
}//end of the Client_info class
