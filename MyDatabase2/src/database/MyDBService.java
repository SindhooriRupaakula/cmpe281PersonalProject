package database;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

public class MyDBService extends Application {

@Override
public synchronized Restlet createInboundRoot() {

// Create a router Restlet that routes each call to a
Router router = new Router(getContext());
router.attach("/service", MyDBResource.class);
router.attach("/service/{request}", MyDBResource.class);
return router;
}

public static void main(String[] args) throws Exception {  
        //get nodetype as argument.
        String nodeType = args[0];
        System.out.println(nodeType);
        
        if (nodeType.equals("slave")) 
            MyDBResource.typeFlag = true;
        else if (nodeType.equals("master"));
        else {
            System.out.println("Invalid argument: " + nodeType);
            return;
        }
        
        // Create a new Component.  
        Component component = new Component();  

        // Add a new HTTP server listening on port 8081.  
        component.getServers().add(Protocol.HTTP, 8081);  

        // Attach the sample application.  
        component.getDefaultHost().attach("/restlet", new MyDBService());  

        // Start the component.  
        component.start();  
    }

}

