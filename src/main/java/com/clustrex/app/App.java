package com.clustrex.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multiaddr.MultiAddress;
import io.ipfs.multihash.Multihash;

/**
 * Simple IPFS Upload!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	
    	IPFS ipfs = new IPFS(new MultiAddress("/ip4/127.0.0.1/tcp/5001"));
    	
    	// replace your file name
    	NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File("C:\\Users\\Murugesh\\Downloads\\bfitlogo.png"));
    	List<MerkleNode> addResult = ipfs.add(file);
    	
    	
    	//Open your browser and copy past this url  
    	System.out.println("http://localhost:8080/ipfs/" + addResult.get(addResult.size() - 1).hash.toBase58());
    	
    	// Getting the same hash's content back from IPFS and store as output.png
    	Multihash filePointer = Multihash.fromBase58(addResult.get(addResult.size() - 1).hash.toBase58());
    	byte[] fileContents = ipfs.cat(filePointer);

    	try (FileOutputStream fos = new FileOutputStream("C:\\\\Users\\\\Murugesh\\\\Downloads\\\\ooutput.png")) {
    		   fos.write(fileContents);
    		}
    	
    }
}
