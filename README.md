
# Big Data exercises!

A repository created to serve as a collection of exercises written in Java for the Big Data Analysis class for the MsC Web and Data Science AUTH

## Exercise 1

### Prerequisites

- Java >= 8, tested with Java 17

### Description

#### Overview

This program performs matrix-vector multiplication in parallel using multiple threads. The matrix \( A \) and vector \( v \) are generated with random integer values between 0 and 10.

The program divides the work among multiple threads, where each thread is responsible for computing a specific subset of rows in the matrix \( A \). Each thread multiplies its assigned rows by the vector \( v \) and stores the results in a shared result array. The number of threads can be varied to observe the impact on computation time.

#### Functionality

- The program ensures that both the matrix size (`n`) and the number of threads are powers of 2.
- The program initializes a matrix \( A \) with dimensions \( n \times m \) and a vector \( v \) with dimensions \( m \times 1 \), where values are randomly generated between 0 and 10.
- The program distributes the rows of the matrix across the specified number of threads. Each thread multiplies its assigned rows by the vector and stores the result in the corresponding entries of the result array.
- Execution time is measured for each specified number of threads, allowing users to observe the efficiency and impact of multithreading on matrix-vector multiplication.

### How to run:
1. Clone the project.
2. Optionally, modify the matrix dimensions by configuring the following environment variables.
 
| ENV VAR | DEFAULT VALUE |
|--|--|
| ROWS | 1024 |
|COLUMNS|4000|

3. Run `MatrixVectorMultiplicationHandler.java`.

### Time Measurements for 1024 rows and 4000 columns

| NUMBER OF THREADS | TIME IN MILLISECONDS |
|--|--|
| 1 | 10.1702 |
| 2 | 1.4055 |
| 3 | 1.215 |
| 4 | 1.0919 |

## Exercise 2
### Prerequisites

 - Java >= 8, Created using Java17

### Description

#### Overview

This program simulates a healthcare scenario where two concurrent threads manage the flow of ICU cases in a hospital environment. It consists of two main components: a **Disease** thread that generates new ICU cases and a **Hospital** thread that recovers cases and frees up ICU beds. The threads interact with shared resources (ICU bed occupancy) using a locking mechanism to avoid data inconsistencies.

#### Functionality

The **Disease** thread generates new cases at random intervals, constrained by the maximum number of cases (`MAX_NEW_CASES`). If ICU capacity (`MAX_ICU_CAPACITY`) is exceeded, the overflow cases are rejected. The **Hospital** thread recovers patients at random intervals, freeing up beds as specified by `MAX_RECOVERIES`. The total recovered and rejected cases are logged for final reporting.

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

## Exercise 3

### Prerequisites

- Java >= 8, tested with Java 17

### Description

#### Overview

This program implements a basic server-client architecture for managing a hash table over a network connection. The **HashServer** acts as a centralized server to store key-value pairs in a hash table, while the **HashClient** allows clients to connect to the server and perform various operations on the hash table, including insertion, deletion, and search. Communication between the client and server occurs over TCP sockets, where each command is transmitted as a comma-separated string.

#### Functionality

- **HashServer**:
  - Initializes a hash table with a default size of \(2^{20}\) and listens on a specified port for client connections.
  - Handles the following operations from connected clients:
    - **Insert (1,key,value)**: Inserts or updates a value associated with a key.
    - **Delete (2,key)**: Removes the specified key and associated value from the hash table.
    - **Search (3,key)**: Retrieves the value associated with the specified key.
  - The server continuously listens for new client connections, and each client is handled sequentially.
  - The server outputs messages about connected clients and logs errors if a connection issue occurs.

- **HashClient**:
  - Connects to the **HashServer** using a specified IP address and port.
  - Provides a command-line interface where users can issue commands to insert, delete, or search for values in the hash table:
    - **1,key,value**: Insert a key-value pair.
    - **2,key**: Delete a key-value pair.
    - **3,key**: Search for the value associated with a key.
    - **0,0**: Exit the client program.
  - Receives and displays responses from the server, indicating the success of each operation or showing retrieved values for search operations.

### How to run:
1. Clone the project.
2. Optionally, set the environment variable for the port in **both** `HashServer` and `HashClient`

| ENV VAR | DEFAULT VALUE |
|--|--|
| SERVER_PORT | 9003 |

4. Run `HashServer.java`


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
