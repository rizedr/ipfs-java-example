# Simple IPFS JAVA Example

#### Install IPFS

https://ipfs.io/docs/install/

#### IPFS getting started

https://ipfs.io/docs/getting-started/

#### Running Example

mvn clean install

mvn clean package 

java -classpath target\ipfs-java-example-1.0-SNAPSHOT.jar com.clustrex.app.App uploadFilePath="XXX" ipfsHash="YYY" downloadFilePath="ZZZ"
