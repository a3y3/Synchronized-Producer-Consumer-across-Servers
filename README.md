# Producer Consumer Synchronized across different servers 
This project was a part of my HW for CS605 course at  RIT. The objective is to run producer, consumer and the storage program on different servers, and synchronize all of the interactions among them. 

### Overall idea: Storage server is where all the synchronization is happening. 
Every time a producer or consumer thread pings the storage, a new thread is created in the server side a socket is assigned to it. Synchronization of the threads is maintained using semaphores on server-side implementation. 

### Steps
1. Store your programs in different servers using sftp command etc. 
2. Run the storage server first.
3. Now you can run both producer and consumers.

## Authors

* **Satwik Mishra**
* **Soham Dongargaonkar**

