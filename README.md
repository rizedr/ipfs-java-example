# Viant - Java IPFS

Viant offers a private IPFS service as a file storage solution. This IPFS service is disconnected from public IPFS and, thus, it's private from other nodes. More information about IPFS can be found_here_ (https://ipfs.io/docs/getting-started/).

In this Java-IPFS example, we have used the java-ipfs-api (https://github.com/ipfs/java-ipfs-api) library to establish a secure connection with the Private IPFS instances of Viant. Please make sure you are using v1.2.1. 

## Pre-requisites

To connect to Viant IPFS in Java we need SSL authentication, below steps helps to get and set the certificate for the domain.

1. Download SSL from https://www.openssl.org (https://www.openssl.org/) if SSL is not in your system
2. Move to the SSL location in CLI tool and connect to viant server using the command

     openssl s_client -connect {HOST_NAME}:443 -servername {HOST_NAME} > {SSL_FILE_NAME_TO_BE_SAVED}

    *Example:*
    ```
    openssl s_client -connect develop.supplychain.consensys.net:443 -servername develop.supplychain.consensys.net > develop.supplychain.consensys.net_ssl_cert.pem
    ```
3. Add the created pem file to your keystore using the command

    keytool -import -keystore cacerts -alias devviant -file {GENERATED_PEM_FILE_LOCATION_}

    *Example:*
    ```
    keytool -import -keystore cacerts -alias devviant -file "C:\OpenSSL-Win64\bin\develop.supplychain.consensys.net_ssl_cert.pem"
    ```


## Example Repository Location: ## 

    [Java-IPFS](https://github.com/ConsenSys/viant-IPFS-Java)

## Java-IPFS Environment Variables ##

IPFS_PROTOCOL=https

IPFS_HOST=develop.supplychain.consensys.net // Any Viant Instance

IPFS_API_PORT=443

IPFS_API_PATH=ipfs/api/v0/

IPFS_GATEWAY_PORT=443

IPFS_GATEWAY_PATH=gateway/ipfs/

IPFS_DIRECTORY_PATH=supplychain

IPFS_MAX_FILE=20000


## IPFS API endpoint ##
*-* ${IPFS_PROTOCOL}://${IPFS_HOST}:${IPFS_API_PORT}/${IPFS_API_PATH}
## IPFS Gateway endpoint ##
*-* ${IPFS_PROTOCOL}://${IPFS_HOST}:${IPFS_GATEWAY_PORT}/${IPFS_GATEWAY_PATH}

## Execution Steps ##

1. To install all dependency jars and compile the code

    *mvn clean install*
2. To take the compiled code and package it in JAR format.

    *mvn clean package*


**Commands**

- *Upload and Read a file in IPFS*_

``` java
java -classpath {JAR_LOCATION_PATH} io.viant.ipfsclient.App -Djavax.net.ssl.trustStore={SSL_LOCATION} -Djavax.net.ssl.trustStorePassword={SSL_PASSWORD} uploadFilePath={FILE_LOCATION_PATH} downloadFilePath={FILE_DOWNLOAD_LOCATION_PATH}
```

```java
/**
 * Uploads the file by reading the path of the input file from User in CLI to IPFS
 * and returns the ipfs hash of the file.
 * 
 * @param ipfs               - IPFS Instance connected to our Instance 
 * @param getFilePath        - Path of the file which is going to be uploaded
 * @return                   - The List of MerkleNode which contains the FilePart,
 *                             Hash of the file, etc.
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
```

```java
/**
 * Reads the File using the MerkleNode which contains the hash of the File
 * and stores the file in the Local system at the Location specified by user
 *
 * @param ipfs            - IPFS Instance connected to our Instance
 * @param filePart        - Filepart is a single MerkleNode from the resultant of upload it contains the hash of the file and it's details
 * @param saveFilePath    - Location to store the retrieved file from IPFS
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
```


**Example:**

```java
java -classpath target\ipfs-java-example-1.0-SNAPSHOT.jar io.viant.ipfsclient.App -Djavax.net.ssl.trustStore="C:\Users\Murugesh\Desktop\Projects\java-ipfs-api\cacerts" -Djavax.net.ssl.trustStorePassword="password123" uploadFilePath="C:/Users/Clustrex4/Downloads/111.png" downloadFilePath="C:/Users/Clustrex4/Desktop/111.png"
```

**Output:**

```
File has been successfully uploaded to IPFS in develop.supplychain.consensys.net...!

---------------------------------------------------------------------------------------------------------

View Directory: https://develop.supplychain.consensys.net/gateway/ipfs/QmeQFuyvqequETGy8Z5ExNHpyqkySzJVm2NYetQ6UAwDk2

---------------------------------------------------------------------------------------------------------

View File: https://develop.supplychain.consensys.net/gateway/ipfs/QmeQFuyvqequETGy8Z5ExNHpyqkySzJVm2NYetQ6UAwDk2/player.png

---------------------------------------------------------------------------------------------------------

File Successfully saved in Location: C:/Users/Clustrex4/Desktop/111.png
```
