# Server_and_Client
This java project shows a basic Server-Client architecture built using Sockets and Multithreading

## Purpose
* Can be used as a starting-point for more complex servers such as
  * Messaging server
  * IOT server - [actual (partial) implementation](https://github.com/chrisBas/server_and_client/tree/iot_server)
  * HTTP server
  
## Features
* Multithreaded
  * The Server contains 2 threads; ne for listening for new connections, and one for managing existing connections
* Extendible Protocol
  * The Server takes in a parameter of a ProtocolHandler.  Any implementation can be created to satisfy the need of different types servers.
  * The current protocol is an EchoProtocolHandler which returns "echo: <msg>"
  * The current implementation works on a single request and single response; this would need to change for certain implementations where a user could send x requests before receiving any responses or visa-versa

## How to Use
* clone the github repo
```bash
git clone https://github.com/chrisBas/server_and_client.git
```
1. Run in Eclipse
	* Open and run the file com.cbasile.server_and_client.Main.java
2. Run with Maven
	* Open the command line and run the following:
	```bash
	mvn clean compile assembly:single
	java -jar target/server_and_client-0.0.1-SNAPSHOT-jar-with-dependencies.jar
	```
    * Separetely, connect a client which can be done from the command line using the 'nc' command as such
    ```bash
        nc localhost 9500
    ```