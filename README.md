# Producer Consumer Synchronized across different servers 
This project was a part of my HW for CS605 course at  RIT. The objective is to run producer, consumer and the storage program on different servers, and synchronize all of the interactions among them. 

### Overall idea: 
Storage server is where all the synchronization is happening. 
Every time a `Producer` or `Consumer` thread connects to `StorageServer`, a new Thread is created and a socket is assigned to it. Synchronization of the threads is maintained using semaphores. See `Semaphore.java`.

### Steps
1. Store your programs in different servers using sftp command etc. 
2. Run the storage server first.
3. Run `Producer` with 
`java Producer <number-of-producers> <number-of-items-this-producer-consumes> <host (fully-qualified-domain)>` 

4. Run `Consumer`with 
`java Consumer <number-of-producers> <number-of-items-this-consumer-consumes> <host (fully-qualified-domain)>`

## Authors

* **Satwik Mishra**
* **Soham Dongargaonkar**

