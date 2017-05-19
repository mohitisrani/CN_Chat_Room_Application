import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class Client implements Runnable{
	Socket requestSocket;           //socket connect to the server
	ObjectOutputStream out;         //stream write to the socket
 	ObjectInputStream in;          //stream read from the socket
	String message;                //message send to the server
	String MESSAGE;                //capitalized message read from the server
        int m=0;
	public void Client(){}

	public void run()
	{
		try{
			if(m==0)
			{   
			    m=1;
		    	    requestSocket = new Socket("localhost", 8000);
		    	    System.out.println("Connected to localhost in port 8000");
		    	    out = new ObjectOutputStream(requestSocket.getOutputStream());
		    	    out.flush();
		    	    in = new ObjectInputStream(requestSocket.getInputStream());
		    	    
		    	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			    System.out.printf("Enter Your Name : ");
			    String name=bufferedReader.readLine();
			    sendMessage(name);
			    while(true)
			    {   
			    	message = bufferedReader.readLine();
			    	sendMessage(message);
				System.out.print(String.format("\033[1A"));
			    }
			}
			if(m==1){
			    try{
			    Thread.sleep(1000);
			    }
			    catch(Exception e){}
			    while(true)
			    {
			    	MESSAGE = (String)in.readObject();
			    	System.out.println(MESSAGE);
			    }
			}
		}
		catch (Exception e) {}
		finally{
			try{
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(Exception e){}
		}
	}
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
		}
		catch(Exception e){}
	}
	public static void main(String args[])
	{
		Client client = new Client();
		Thread t1= new Thread(client);
		t1.start();
		Thread t2= new Thread(client);
		t2.start();
	}

}
