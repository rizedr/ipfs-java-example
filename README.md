# Simple IPFS JAVA Example

#### Install IPFS

https://ipfs.io/docs/install/

#### IPFS getting started

https://ipfs.io/docs/getting-started/

#### Running Example

- mvn clean install

- mvn clean package 

- To connect to Viant IPFS in Java we need SSL authentication, below steps helps to get and set the certificate for the domain.

1. Download SSL from https://www.openssl.org if SSL is not in your system.
2. Move to the SSL location in CLI tool and connect to viant server using the command _openssl s_client -connect develop.supplychain.consensys.net:443 -servername develop.supplychain.consensys.net > develop.supplychain.consensys.net_ssl_cert.pem_
3. Add the created pem file to your keystore using the command _keytool -import -keystore cacerts -alias devviant -file "C:\OpenSSL-Win64\bin\develop.supplychain.consensys.net_ssl_cert.pem"

- Upload and Download File

  java -classpath target\ipfs-java-example-1.0-SNAPSHOT.jar com.clustrex.app.App -Djavax.net.ssl.trustStore="SSL_LOCATION" -Djavax.net.ssl.trustStorePassword="SSL_PASSWORD" uploadFilePath="FILE_LOCATION_PATH" downloadFilePath="FILE_DOWNLOAD_LOCATION_PATH"

  **Example**
>			java -classpath target\ipfs-java-example-1.0-SNAPSHOT.jar com.clustrex.app.App -Djavax.net.ssl.trustStore="C:\Users\Murugesh\Desktop\Projects\java-ipfs-api\cacerts" -Djavax.net.ssl.trustStorePassword="password123" uploadFilePath="C:/Users/Clustrex4/Downloads/2.png"
downloadFilePath="C:/Users/Clustrex4/Downloads/2.png"
