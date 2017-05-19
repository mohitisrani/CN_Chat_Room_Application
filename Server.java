import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.util.regex.*;
public class Server {

	private static final int sPort = 8000;   //The server will be listening on this port number

	public static void main(String[] args) throws Exception {
		System.out.println("The server is running."); 
        	ServerSocket listener = new ServerSocket(sPort);
		int clientNum = 1;
        	try {
            		while(true) {
                		Handler h=new Handler(listener.accept(),clientNum);
				Thread t1=new Thread(h);
				t1.start();
				Thread t2=new Thread(h);
				t2.start();
				System.out.println("Client "  + clientNum + " is connected!");
				clientNum++;
            			}
        	} finally {
            		listener.close();
        	} 
 
    	}

private static class Handler implements Runnable {
	private String message;    //message received from the client
	private static String MESSAGE,TO;    //message to be sent to the client
	private Socket connection;
	private ObjectInputStream in;	//stream read from the socket
	private ObjectOutputStream out;    //stream write to the socket
	private int no;		//The index number of the client
        private int m=0;
	private int n=0;
	private int h=0;
	private String name;
	private static String except="none0";
	private static boolean signalled=false;
	private boolean me;
	private String[] color={"\033[34m","\033[32m","\033[36m","\033[35m","\033[33m","\033[31m"};
	private String color0="\033[0m";
	public Handler(Socket connection, int no) {
    		this.connection = connection;
    		this.no = no;
	}

        public void run() {
 		try{     
		    
			if(n==0){n=1;
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			m=1;
			}
			try
			{
			if(m==1 && n==1){
			        h=1;
				name=(((String)in.readObject()).replaceAll("\\s+","")).toUpperCase();
				System.out.println("Client "+Integer.toString(no)+" : "+name);
				MESSAGE=name+" has now joined the chat";TO="ALL";
				signalled=true;
				sendMessage("Welcome "+name);
				while(true)
				{
					message = (String)in.readObject();
					System.out.println(message);
					System.out.println("Receive message: " + message + " from client " + no);
					String[] parts = (message+":ERROR!! Use Proper Syntax.").split(":");
					MESSAGE=parts[1];
					TO=(parts[0].replaceAll("\\s+","")).toUpperCase();
					if(MESSAGE.equals("ERROR!! Use Proper Syntax.")){
					MESSAGE="\033[31m"+MESSAGE+"\033[0m";
					TO=name;
					}
					except="none0";
					if(TO.matches("^*-.*")){
					except=TO.split("-")[1];
					TO="ALL";
					}
					String name0=String.format("%1$-" +7 + "s", name);;
					MESSAGE=color[no%6]+"["+name0+"] : "+color0+MESSAGE;
					me=true;
					signalled=true;
				}
			}
			if(h==0){
			        try{
				   Thread.sleep(1000);
				}catch(Exception e){}
			        while(true){
				        try{
					    Thread.sleep(500);
					}catch(Exception e){}
				        if(signalled){
					try{
					    Thread.sleep(500);
					}catch(Exception e){}
					signalled=false;
					if(((TO.equals("ALL")) || (TO.equals(name)) || me) && (!except.equals(name)|| me)){
					    sendMessage(MESSAGE);
					    me=false;
				        }
					}}
			}
			}
			catch(ClassNotFoundException classnot){
					System.err.println("Data received in unknown format");
				}
		}
		catch(Exception e){}
		finally{
			try{
			if(n==1){
			n=2;
				in.close();
				out.close();
				connection.close();
			}}
			catch(IOException ioException){
				System.out.println("Disconnect with Client " + no);
			}
		}
	}

	public void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("Send message: " + msg + " to Client " + no);
		}
		catch(Exception e){}
	}

    }

}
