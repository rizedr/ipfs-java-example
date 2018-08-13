package io.viant.ipfsclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;

/**
 * Simple IPFS Upload and Retrieval
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
		String getFilePath = null;
		String saveFilePath = null;
		List<MerkleNode> addResult = null;
		
		// parse the arguments from command line
		for (Integer index=0; index < args.length; index++) {
			String [] arrOfStr = args[index].split("=");
			String firstString = arrOfStr[0];        
			switch (firstString) {
				case "uploadFilePath":
					getFilePath = arrOfStr[1];
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
		// get the property values
		String ipfsHost = props.getProperty("IPFS_HOST");
		Integer apiPort = Integer.valueOf(props.getProperty("IPFS_API_PORT"));
		String apiPath = props.getProperty("IPFS_API_PATH");
		String ipfsProtocol = props.getProperty("IPFS_PROTOCOL");
		String ipfsGatewayPath = props.getProperty("IPFS_GATEWAY_PATH");

		String[] splittedPath =  getFilePath.split("/");
		Integer uploadStringLength = splittedPath.length;
		String uploadFileName = splittedPath[uploadStringLength-1 ];

		// Initiate IPFS object
	    IPFS ipfs = new IPFS(ipfsHost, apiPort, "/" + apiPath, true);

		if (getFilePath != null) {
			NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File(getFilePath));
			addResult = ipfs.add(file, true);
			System.out.println("File has been successfully uploaded to ipfs..!");
			System.out.println("View Directory: "+ ipfsProtocol + "://" + ipfsHost + "/" + ipfsGatewayPath + addResult.get(addResult.size() - 1).hash.toBase58());
			System.out.println("View File: "+ ipfsProtocol + "://" + ipfsHost + "/" + ipfsGatewayPath + addResult.get(addResult.size() - 1).hash.toBase58() + "/" + uploadFileName);
		}

    	if (saveFilePath != null) {
			MerkleNode filePart = addResult.get(0);
			byte[] fileContents = ipfs.cat(filePart.hash);

			try (FileOutputStream fos = new FileOutputStream(saveFilePath)) {
				fos.write(fileContents);
				System.out.println("File Successfully saved in " + saveFilePath +" location");
			}
		}
    }
}
