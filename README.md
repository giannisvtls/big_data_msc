
# Big Data exercises!

A repository created to serve as a collection of exercises written in Java for the Big Data Analysis class for the MsC Web and Data Science AUTH

## Exercise 2
### Prerequisites

 - Java >= 8, Created using Java17

### Description

#### Overview

This program simulates a healthcare scenario where two concurrent threads manage the flow of ICU cases in a hospital environment. It consists of two main components: a **Disease** thread that generates new ICU cases and a **Hospital** thread that recovers cases and frees up ICU beds. The threads interact with shared resources (ICU bed occupancy) using a locking mechanism to avoid data inconsistencies.

#### Functionality

The **Disease** thread generates new cases at random intervals, constrained by the maximum number of cases (`MAX_NEW_CASES`). If ICU capacity (`MAX_ICU_CAPACITY`) is exceeded, the overflow cases are rejected. The **Hospital** thread recovers patients at random intervals, freeing up beds as specified by `MAX_RECOVERIES`. The total recovered and rejected cases are logged for final reporting.

#### Synchronization and Configuration

A `ReentrantLock` and `Condition` are used to coordinate access to shared variables, ensuring thread-safe updates on ICU bed occupancy. Both the `Disease` and `Hospital` threads are configured to sleep between iterations, emulating real-time case management intervals. Configurable parameters such as ICU capacity, iteration count, and thread sleep intervals can be set via environment variables or default values.

### How to run:
1. Clone the project
2. Optionally set the environment variables for the following values

| ENV VAR | DEFAULT VALUE |
|--|--|
| DISEASE_THREAD_SLEEP_TIME_MS | 3000 |
|HOSPITAL_THREAD_SLEEP_TIME_MS|5000|
|ITERATIONS|20|
|MAX_ICU_CAPACITY|20|
|MAX_NEW_CASES|10|
|MAX_RECOVERIES|5|

3. Run `Main.java`

## Exercise 4
### Prerequisites

 - Java >= 8, Created using Java17

### Description

#### Overview 
This project simulates a simple distributed storage system with three main components: `ProducerClient`, `ConsumerClient`, and `StorageServer`.

1.  **ProducerClient**: Simulates a producer that randomly connects to a storage server to "ADD" data. Each request sends a random amount of data to a randomly selected port. Requests are sent at intervals of 1 to 10 seconds.
    
3.  **ConsumerClient**: Simulates a consumer that connects to a server to "REMOVE" data. Similar to the producer, it sends random removal requests at random intervals between 1 and 10 seconds.
    
4.  **StorageServer**: Manages storage, with predefined maximum and minimum capacity. It runs separate threads to handle both producer and consumer requests. When a client request exceeds storage limits, the server rejects it with an error message. Each server instance handles multiple clients simultaneously using synchronized methods to ensure thread safety.

#### Functionality 

- Both `ProducerClient` and `ConsumerClient` generate random data amounts to add or remove from the storage server, simulating dynamic data flow in a distributed system.
- Producers and consumers connect to random ports, allowing multiple storage servers to handle requests concurrently. 
- The `StorageServer` uses synchronized methods to prevent data inconsistencies when handling multiple requests. This ensures that concurrent operations do not violate storage limits. 
- If a producer’s "ADD" operation would exceed maximum storage, or if a consumer’s "REMOVE" operation would result in storage below the minimum, the request is denied with an appropriate message.

### How to run:
1. Clone the project
2. Run `StorageServer.java` which will create 3 servers for Producers on ports 8881, 8882, 8883 and 3 servers for Consumers on ports 9991, 9992,9993
3. Run one or more instances of `ProducerClient.java` 
4. Run one or more instances of `ConsumerClient.java` 
