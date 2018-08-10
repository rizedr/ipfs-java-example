package com.clustrex.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Properties;

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
    	
    	InputStream s = App.class.getResourceAsStream("/config.properties");

    	//now can use this input stream as usually, i.e. to load as properties
    	Properties props = new Properties();
    	
		props.load(s);
    	
		System.out.println(props.getProperty("IPFS_HOST"));
		
    	IPFS ipfs = new IPFS("develop.supplychain.consensys.net",443,"/ipfs/api/v0/",true);
    	System.out.println(ipfs.host+ipfs.port);
    	
    	
    	NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File("C:\\Users\\Murugesh\\Downloads\\bfitlogo.png"));
    	List<MerkleNode> addResult = ipfs.add(file);
    	
    	
    	//Open your browser and copy past this url  
    	System.out.println("http://localhost:8080/ipfs/" + addResult.get(addResult.size() - 1).hash.toBase58());
    	
    }
}
