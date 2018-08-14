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
	private static String ipfsHost;
	private static Integer apiPort;
	private static String apiPath;
	private static String ipfsProtocol;
	private static String ipfsGatewayPath;
	
	
	/**
	 * Updates the Member variables of the class by reading the config
	 * properties from the Resources  
	 */
	public static void initiate(){
		try {
			InputStream s = App.class.getResourceAsStream("/config.properties");
	    // now can use this input stream as usually,
			// i.e. to load as properties
	    Properties props = new Properties();
			props.load(s);
			App.ipfsHost = props.getProperty("IPFS_HOST");
			App.apiPort = Integer.valueOf(props.getProperty("IPFS_API_PORT"));
			App.apiPath = props.getProperty("IPFS_API_PATH");
			App.ipfsProtocol = props.getProperty("IPFS_PROTOCOL");
			App.ipfsGatewayPath = props.getProperty("IPFS_GATEWAY_PATH");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}
	
	
	/**
	 * Uploads the file by reading the path of the input file from User in CLI to IPFS
	 * and returns the ipfs hash of the file.
	 * 
	 * @param ipfs				- IPFS Instance connected to our Instance 
	 * @param getFilePath		- Path of the file which is going to be uploaded
	 * @return					- The List of MerkleNode which contains the FilePart,
	 * 							  Hash of the file, etc.
	 */
	private static List<MerkleNode> uploadToIpfs(IPFS ipfs, String getFilePath) {
		List<MerkleNode> addResult = null;
		try {
			if (getFilePath == null) {
				throw new Exception("File Path to Upload is not passed as arguments.");
			}
			NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File(getFilePath));
			addResult = ipfs.add(file, true);
			String[] splittedPath =  getFilePath.split("/");
			Integer uploadStringLength = splittedPath.length;
			String uploadFileName = splittedPath[uploadStringLength-1 ];
			System.out.println("\n\nFile has been successfully uploaded to IPFS in "+ ipfsHost +"...!");
			System.out.println("\n---------------------------------------------------------------------------------------------------------\n");
			System.out.println("View Directory: "+ ipfsProtocol + "://" + ipfsHost + "/" + ipfsGatewayPath + addResult.get(addResult.size() - 1).hash.toBase58());
			System.out.println("\n---------------------------------------------------------------------------------------------------------\n");
			System.out.println("View File: "+ ipfsProtocol + "://" + ipfsHost + "/" + ipfsGatewayPath + addResult.get(addResult.size() - 1).hash.toBase58() + "/" + uploadFileName);
			System.out.println("\n---------------------------------------------------------------------------------------------------------\n");
		} catch(Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return addResult;
	}
	
	/**
	 * Reads the File using the MerkleNode which contains the hash of the File
	 * and stores the file in the Local system at the Location specified by user
	 *
	 * @param ipfs			- IPFS Instance connected to our Instance
	 * @param filePart		- Filepart is a single MerkleNode from the resultant of upload it contains the hash of the file and it's details
	 * @param saveFilePath	- Location to store the retrieved file from IPFS
	 */
	private static void readFromIpfs(IPFS ipfs, MerkleNode filePart, String saveFilePath) {
		try {
			if (saveFilePath == null) {
				throw new Exception("Location to Save the File not passed as arguments.");
			}
			byte[] fileContents = ipfs.cat(filePart.hash);
			FileOutputStream fos = new FileOutputStream(saveFilePath);
			fos.write(fileContents);
			System.out.println("File Successfully saved in Location: " + saveFilePath + "\n\n");
			fos.close();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}
	
    public static void main(String[] args) throws IOException {
			// Initiate the process by reading the config properties
			initiate();
			String getFilePath = null;
			String saveFilePath = null;
			List<MerkleNode> addResult = null;

			// parse the arguments from command line
			// Read the Arguments from command line here
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

			// Initiate IPFS object
			IPFS ipfs = new IPFS(ipfsHost, apiPort, "/" + apiPath, true);  
			addResult = uploadToIpfs(ipfs, getFilePath);
			// Get the Filepart from the uploaded result
			// pass it to read from ipfs location
			MerkleNode filePart = addResult.get(0);
			readFromIpfs(ipfs, filePart, saveFilePath);
		}
}
