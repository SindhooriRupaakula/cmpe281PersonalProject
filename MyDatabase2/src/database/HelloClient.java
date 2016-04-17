package database;

// REF:  http://restlet.com/technical-resources/restlet-framework/guide/2.3/editions/jse/overview

import org.restlet.resource.ClientResource;

/**
 * Creates and launches a HTTP client invoking the server listening on port
 * 8080, and writing the response entity on the console.
 */
public class HelloClient {


    	private static String service_url = "http://localhost:8081/restlet/service" ;

    	public static void main(String[] args) throws Exception {
        ClientResource helloClientresource = new ClientResource( service_url ); 
		helloClientresource.get().write(System.out);
		System.out.println("") ;
    	}
}

