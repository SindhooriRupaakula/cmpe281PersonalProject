package database;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.xml.bind.DatatypeConverter;
import java.util.StringTokenizer;

public class ImplementStorage {
    public static SM sm;
    public ImplementStorage() 
    {
        sm = new SMFactory().getInstance();
    }


    public void store(String key, String value) {
        try {
        	System.out.println("Doing store");
        		Record rec = new Record(value.length());
                rec.setBytes(value.getBytes());
            	SM.OID oid = (SM.OID) sm.store(rec);
                String obj = DatatypeConverter.printHexBinary(oid.toBytes());
                writeToKeyObjFile(key, obj);
                System.out.println("Store Successful");
        } catch(Exception e) {
        		System.out.println("Store Unsuccessful");
        		System.out.println( e ) ;
                e.printStackTrace() ;
        }
    }
    
    public String fetch(String key) {
    	System.out.println("Fetching Value");
    		try {
    			SM.Record fetch = null;
    			String obj = readFromKeyObjFile(key);
    			if(!obj.equals("")) {
    				byte[] recId = DatatypeConverter.parseHexBinary(obj);
        			OID oid = new OID(recId);
        			fetch = sm.fetch(oid);
        			String decoded = new String(fetch.getBytes(0, 0), "UTF-8");
        			decoded = decoded.replace("\n}", "");
        	        System.out.println("Fetch Successful!");
        	        return decoded;
    			}
    			else {
        			System.out.println("Fetch Unsuccessful");
        			return "No Value. Please check key.";
    			}
    		} catch(Exception e) {
            System.out.println( e );
            e.printStackTrace();
            return "Fetch Unsuccessful";
    		}
    		
    }

    public boolean update(String key, String value) {
		boolean flag = false;
		try {
		SM.Record fetch = null;
		OID oid = null;
		String obj = readFromKeyObjFile(key);
	    System.out.println("Fetched -------> " + obj);
		if(!obj.equals("")) {
		             byte[] recId = DatatypeConverter.parseHexBinary(obj);
			        oid = new OID(recId);
				    Record updateRec = new Record(value.length());
				    updateRec.setBytes(value.getBytes());
					SM.OID newoid = (SM.OID) sm.update(oid, updateRec);
					String hexUpdatedObjectId = DatatypeConverter.printHexBinary(newoid.toBytes());
					updateKeyObjFile(key, hexUpdatedObjectId);
					System.out.println("Update Successful");
				    flag = true;
		}
		else {
			System.out.println("Fetch Unsuccessful");
			flag = false;
		}
		}catch(Exception e) {
		         flag = false;
		         System.out.println("Update Unsuccessful");
				 System.out.println( e );
				 e.printStackTrace();
		}
			return flag;
    }
    
    public void delete(String key) {
		try {
			SM.Record fetch = null;
			OID oid = null;
			String obj = readFromKeyObjFile(key);
			System.out.println("Record fetched is " + obj);
			if(!obj.equals("")) {
				byte[] recId = DatatypeConverter.parseHexBinary(obj);
				oid = new OID(recId);
				sm.delete(oid);
				deleteFromKeyObjFile(key);
                System.out.println("Successfully deleted record");
			}
			else {
				System.out.println("Unable to delete record");
			}
		}catch(Exception e) {
    			System.out.println("Unable to delete record");
            System.out.println( e );
            e.printStackTrace();
    		} 
    }

    public void deleteFromKeyObjFile(String key) throws IOException {
		String fLine = null;
		File f = new File("KeyObj.txt");
		File tempf = new File("KeyObj.temp");
		if(!tempf.exists()) {
			tempf.createNewFile();
		}

	    FileWriter fw = new FileWriter(tempf.getAbsoluteFile());
	    BufferedWriter bw = new BufferedWriter(fw);
	    FileReader fr = new FileReader(f);
	    BufferedReader br = new BufferedReader(fr);
    		while((fLine = br.readLine())!=null) {
    			StringTokenizer str = new StringTokenizer(fLine, " ");
    			String thiskey = str.nextToken();
    			if(!thiskey.equals(key)) {
    				bw.write(fLine);
    				bw.write("\n");
    			}
    		}
    		bw.close();
    		br.close();
    		f.delete();
    		tempf.renameTo(f);    		
    }
 
    public void updateKeyObjFile(String key, String newObjectId) throws IOException {
		String fLine = null;
		File f = new File("KeyObj.txt");
		File tempf = new File("KeyObj.temp");
		if(!tempf.exists()) {
            System.out.println("Creating KeyObj.temp file.");
			tempf.createNewFile();
		}

	    FileWriter fw = new FileWriter(tempf.getAbsoluteFile());
	    BufferedWriter bw = new BufferedWriter(fw);
	    FileReader fr = new FileReader(f);
	    BufferedReader br = new BufferedReader(fr);
    		while((fLine = br.readLine())!=null) {
    			
                StringTokenizer str = new StringTokenizer(fLine, " ");
    			String thiskey = str.nextToken();
    			if(thiskey.equals(key)) {
    				String newData = fLine.replaceFirst(".*", key + " " + newObjectId);
    				bw.write(newData);
    				bw.write("\n");
    			}
    			else {
    				bw.write(fLine);
    				bw.write("\n");
    			}
    		}

    		bw.close();
    		br.close();
    		f.delete();
    		tempf.renameTo(f);
    }

    public String readFromKeyObjFile(String key) throws IOException {
    		File directory = new File(".");
			File in = new File("KeyObj.txt");
    		FileInputStream fInputStream = new FileInputStream(in);
    		BufferedReader br = new BufferedReader(new InputStreamReader(fInputStream));
    		
    		String line = null;
    		while((line = br.readLine())!=null) {
    			StringTokenizer str = new StringTokenizer(line, " ");
    			String thiskey = str.nextToken();
    			if(thiskey.equals(key)) {
        			return str.nextToken();    				
    			}
    		}
    		return "";
    }

    public void writeToKeyObjFile(String key, String objectId) throws IOException{
    		try {
    			String fLine = null;
				File f = new File("KeyObj.txt");
        		if(!f.exists()) {
        			System.out.println("Creating KeyObj.txt file.");
				    f.createNewFile();
        		} else{

                    System.out.println("File exists");
                }

            FileWriter fw = new FileWriter(f.getAbsoluteFile(),true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            if(br.readLine()!=null) {
            		pw.println("");
                br.close();
            }

			pw.print(key + " "+ objectId);
			System.out.println("Successful file write.");            			
			pw.close();

    		} catch(IOException io) {
    			System.out.println("IO Exception");
    			io.printStackTrace();
    		}
    }

    public static class OID implements SM.OID {
        private byte[] key;
        private int intKey;
        private int keyType = 0;
        public OID(byte[] key) {
          this.key = key;
        }

        public OID(int key) {
            this.key = Util.generateGUID() ;
        }
        public String getKey() {
            return Util.toHexString(this.key) ;
        }
        public byte[] toBytes() {

            return this.key ;

        }
    }

}

