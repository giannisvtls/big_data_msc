# Exercise 3
## How to run:
Simply compile the two files on your command line:

    javac HashServer.java

    javac HashClient.java

Then, run the HashServer with an available port number of your liking as input,
eg:

    java HashServer 9003

While the server is running, on a different command line, run the HashClient, providing the IP and the port number,
eg:

    java HashClient 127.0.0.1 9003

The available commands for the client are the following:

* Insertion
  *     1,key,value
*  Deletion
    *     2, key 
* Search
    *     3, key
* Exit
    *     0,0 
