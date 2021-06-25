/**
 * @author Abhishek Balasubramanian
 * @date 09.25 .2020
 * @UCID 30054593
 */

// Proxy holds the start-up code for the proxy and code for handling the
//requests.


import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Proxy {
    /** Port for the proxy */
    private static int port;
    /** Socket for client connections */
    private static ServerSocket socket;
    

    /** Create the Proxy object and the socket */
    public static void init(int p) 
    {
    	System.out.println("In Init");
    	
	port = p;
	try {
	    socket = new ServerSocket (port);
	} catch (IOException e) {
	    System.out.println("Error creating socket: " + e);
	    System.exit(-1);
	}
    }

    public static void handle (/*Socket client*/) {
	Socket server = null, client = null;
	HttpRequest request = null;
	HttpResponse response = null;

	/* Process request. If there are any exceptions, then simply
	 * return and end this request. This unfortunately means the
	 * client will hang for a while, until it timeouts. */

	/* Read request- coming in from client, a GET request */
	try {
		System.out.println("Before accept; would block");
		client = socket.accept();
		System.out.println ("after accept");
	    BufferedReader fromClient = new BufferedReader (new InputStreamReader(client.getInputStream()));
	    
	    System.out.println("Before request");
	    
	    request = new HttpRequest (fromClient);
	    System.out.println("Request: " + request.toString());
	} catch (IOException e) {
	    System.out.println("Error reading request from client: " + e);
	    return;
	}
	/* Send request to server */
	try {
	    /* Open socket and write request to socket */
		//System.out.println("Before sending request to webpage");
	    server = new Socket (request.getHost(), request.getPort())/* Fill in */;
	    DataOutputStream toServer = new DataOutputStream (server.getOutputStream());
	    toServer.writeBytes(request.toString());
	    toServer.flush(); /* Fill in */
	} catch (UnknownHostException e) {
	    System.out.println("Unknown host: " + request.getHost());
	    System.out.println(e);
	    return;
	} catch (IOException e) {
	    System.out.println("Error writing request to server: " + e);
	    return;
	}
	/* Read response and forward it to client */
	try {
	    DataInputStream fromServer = new DataInputStream (server.getInputStream())/* Fill in */;
	    response = new HttpResponse(fromServer)/* Fill in */;
	    DataOutputStream toClient = new DataOutputStream (client.getOutputStream())/* Fill in */;
	    System.out.println ("Response before writing to client" + response.toString());
	    //System.out.println("1");
	    toClient.writeBytes(response.toString()); //USE BYTES FOR TOSERVER AS WELL
	    //System.out.println("Body:" + new String(response.body) );
	    toClient.write(response.body);
	    //System.out.println("3");
	    toClient.flush();
	    //System.out.println("4");
	    /* Write response to client. First headers, then body */
	    client.close();
	    server.close();
	    /* Insert object into the cache */
	    /* Fill in (optional exercise only) */
	} catch (IOException e) {
	    System.out.println("Error writing response to client: " + e);
	}
    }


    /** Read command line arguments and start proxy */
    public static void main(String args[]) {
	int myPort = 0;
	
	try {
	    myPort = Integer.parseInt(args[0]);
	} catch (ArrayIndexOutOfBoundsException e) {
	    System.out.println("Need port number as argument");
	    System.exit(-1);
	} catch (NumberFormatException e) {
	    System.out.println("Please give port number as integer.");
	    System.exit(-1);
	}
	
	init(myPort);
	/** Main loop. Listen for incoming connections and spawn a new
	 * thread for handling them */
	Socket client = null;
	
	while (true) {
//	    try {
//		client = new Socket ("198.168.100.1", myPort);
		handle(/*client*/);
//	    } catch (IOException e) {
//		System.out.println("Error reading request from client: " + e);
//		/* Definitely cannot continue processing this request,
//		 * so skip to next iteration of while loop. */
//		//continue;
//	    }
	}

    }
}
