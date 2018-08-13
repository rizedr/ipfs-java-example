package com.clustrex.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.*;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.*;
import io.ipfs.api.*;
import io.ipfs.api.cbor.*;
import io.ipfs.multihash.Multihash;
import io.ipfs.multiaddr.*;

/**
 * Simple IPFS Upload!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {

			String getFilePath = null;
			String IPFSPath = null;
			String saveFilePath = null;

			for (Integer index=0; index < args.length; index++) {
				String [] arrOfStr = args[index].split("=");
				String firstString = arrOfStr[0];        
				switch (firstString) {
        case "uploadFilePath":
            getFilePath = arrOfStr[1];
            break;
        case "ipfsHash":
            IPFSPath = arrOfStr[1];
            break;
        case "downloadFilePath":
            saveFilePath = arrOfStr[1];
            break;
        }
			}

    	InputStream s = App.class.getResourceAsStream("/config.properties");
    	// now can use this input stream as usually, i.e. to load as properties
    	Properties props = new Properties();
			props.load(s);
			// get the property value
			String ipfsHost = props.getProperty("IPFS_HOST");
			Integer apiPort = Integer.valueOf(props.getProperty("IPFS_API_PORT"));
			String apiPath = props.getProperty("IPFS_API_PATH");
			String ipfsProtocol = props.getProperty("IPFS_PROTOCOL");
			String ipfsGatewayPath = props.getProperty("IPFS_GATEWAY_PATH");

	    IPFS ipfs = new IPFS(ipfsHost,apiPort,"/"+apiPath,true);

			if (getFilePath != null) {
    	NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File(getFilePath));
    	List<MerkleNode> addResult = ipfs.add(file);
    	System.out.println("File has been successfully uploaded to ipfs, To view the file "+ ipfsProtocol + "://" + ipfsHost + "/" + ipfsGatewayPath + addResult.get(addResult.size() - 1).hash.toBase58());
			}

    	if ((IPFSPath != null) && (saveFilePath != null)) {
				// Getting the same hash's content back from IPFS and store as output.png
    	Multihash filePointer = Multihash.fromBase58(IPFSPath);
    	byte[] fileContents = ipfs.cat(filePointer);

    	try (FileOutputStream fos = new FileOutputStream(saveFilePath)) {
    		   fos.write(fileContents);
    		   System.out.println("File Successfully saved in your current location");
    		}
			}
    }
}
