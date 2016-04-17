package database;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Delete;
import org.restlet.resource.ServerResource;
import org.restlet.representation.Representation;
import org.restlet.*;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;

public class MyDBResource extends ServerResource {
	//typeFlag = 0 => master
	protected static boolean typeFlag = false;	 
	ImplementStorage isg = new ImplementStorage() ;

	@Get ("json")
	public String getFunction() throws JSONException {
		
		InetAddress ip;
		JSONObject jsonObj = new JSONObject();
		String request = getQuery().getValues("key");
		String result = null;

		try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
                return e.getMessage();
        }

		String value = isg.fetch(request);
		result = "Restlet Response : " + value;
		System.out.println("Value for the key "+request+ " is "+value);
		jsonObj.put(request,value);
		jsonObj.put("IP",ip);
		JSONArray jarr = new JSONArray();
		jarr.put(jsonObj);
		System.out.println("JARRAY"+jarr);
		String ret = jarr.toString();
		System.out.println("returnString "+ret);
		
		return ret;
		
	}

	@Post
	public String postFunction(Representation entity) throws ResourceException, IOException {
		if(typeFlag) {
			System.out.println("POST request handled by master.");
			return "POST request handled by master.";
		}
		String s = null;
	    if (entity.getMediaType().isCompatible(MediaType.APPLICATION_JSON)) {
	    	s = entity.getText();
	    	System.out.println("------------> Save Successful."+s);
	    }     
	    System.out.println(entity.getText()); 
		String[] data = s.split(":");
		data[0] = data[0].replaceAll("[^a-zA-Z]","");
		isg.store(data[0], data[1]);
		
		return s;
	}
	
	@Put
	public String putFunction(Representation entity) throws IOException {
		if(typeFlag) {
			System.out.println("POST request handled by master.");
			return "POST request handled by master.";
		}
		String value = entity.getText();
		System.out.println("Value "+value);
		String key = getQuery().getValues("newValue");
		System.out.println("key "+key);
		boolean b = isg.update(key, value);
		if(b == true) 
			return "Saved "+value+" :"+key;
		else return "Unable to update";		
	}

	@Delete
	public void deleteFunction(Representation entity) {
		if(typeFlag) {
			System.out.println("DELETE request handled by master.");
			return;
		}
		
		String req;
		Form f = new Form(entity);
		req = getQuery().getValues("key");
        System.out.println("The request is "+req);
		isg.delete(req);
		
	}
}
